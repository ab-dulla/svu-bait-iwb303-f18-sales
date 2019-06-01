package org.svuonline.f18sales.salesmen.management;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import org.svuonline.f18sales.R;
import org.svuonline.f18sales.data.DatabaseHelper;
import org.svuonline.f18sales.model.Region;
import org.svuonline.f18sales.model.Salesman;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static android.text.TextUtils.isEmpty;

public class AddSalesmanFragment extends Fragment {

    private static final String TAG = AddSalesmanFragment.class.getName();
    private static final int GALLERY_REQUEST_CODE = 406;
    private static final String DATE_FORMAT = "MM/dd/yyyy";
    private static final Locale LOCAL_DATE_FORMAT = Locale.US;

    private ImageView imageView;
    private FloatingActionButton fabUploadImage;
    private EditText editTextFullName;
    private Spinner spinnerRegions;
    private EditText editTextHiringDate;
    private Button buttonAddSalesman;

    DatabaseHelper dbHelper;

    public AddSalesmanFragment() {
    }

    public static AddSalesmanFragment newInstance() {
        return new AddSalesmanFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_salesman, container, false);

        dbHelper = new DatabaseHelper(getContext());

        initElements(v);
        setImageUploadOnClickListener(v);
        fillRegionsSpinnerWithData();
        initHiringDateCalender();

        setAddSalesmanButtonOnClickListener();
        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result code is RESULT_OK only if the user selects an Image
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GALLERY_REQUEST_CODE) {
                //data.getData returns the content URI for the selected Image
                Uri selectedImage = data.getData();
                imageView.setImageURI(selectedImage);
            }
        }
    }

    private void initElements(View v) {
        imageView = v.findViewById(R.id.image);
        fabUploadImage = v.findViewById(R.id.fab_upload_image);
        editTextFullName = v.findViewById(R.id.editText_full_name);
        spinnerRegions = v.findViewById(R.id.spinner_region);
        editTextHiringDate = v.findViewById(R.id.editText_hiring_date);
        buttonAddSalesman = v.findViewById(R.id.button_add_salesman);
    }

    private void setImageUploadOnClickListener(View v) {
        fabUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // selectImageFromGallery
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                String[] mimeTypes = {"image/jpeg", "image/png"};
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
                startActivityForResult(intent, GALLERY_REQUEST_CODE);
            }
        });
    }

    private void fillRegionsSpinnerWithData() {
        ArrayList<Region> regionsList = dbHelper.getAllRegions();
        RegionSpinnerArrayAdapter regionsAdapter = new RegionSpinnerArrayAdapter(getContext(), regionsList);
        spinnerRegions.setAdapter(regionsAdapter);
    }

    private void initHiringDateCalender() {
        final Calendar calendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                // update calender
                SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, LOCAL_DATE_FORMAT);
                editTextHiringDate.setText(sdf.format(calendar.getTime()));
            }
        };

        editTextHiringDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(), date, calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void setAddSalesmanButtonOnClickListener() {
        buttonAddSalesman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValid()) {
                    boolean added = addSalesman();
                    if (added) {
                        showMessage("Success", "Salesman was successfully added to the database.");
                    } else {
                        showMessage("Failure", "Failed adding a new Salesman to the database.");
                    }
                } else {
                    Toast.makeText(getContext(), "Please fill all fields correctly including the image!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private boolean isValid() {
        // regions is always valid (it has a default value).
        return imageView.getDrawable() != null &&
                !isEmpty(editTextFullName.getText().toString()) &&
                !isEmpty(editTextHiringDate.getText().toString());
    }

    private boolean addSalesman() {
        String fullName = editTextFullName.getText().toString();
        Region region = (Region) spinnerRegions.getSelectedItem();
        String hiringDate = editTextHiringDate.getText().toString();
        byte[] image = parseImage();
        Salesman salesman = new Salesman(fullName, region.getId(), hiringDate, image);

        return dbHelper.insertSalesman(salesman) != -1;
    }

    private Date parseDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, LOCAL_DATE_FORMAT);
        try {
            return dateFormat.parse(editTextHiringDate.getText().toString());
        } catch (ParseException e) {
            Log.e(TAG, "Failed to parse hiring date.", e);
            return null;
        }
    }

    private byte[] parseImage() {
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        return baos.toByteArray();
    }

    private void showMessage(String title, String Message) {
        new AlertDialog.Builder(getContext())
                .setCancelable(true)
                .setTitle(title)
                .setMessage(Message)
                .show();
    }
}
