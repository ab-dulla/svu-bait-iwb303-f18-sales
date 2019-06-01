package org.svuonline.f18sales.salesmen.management;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import org.svuonline.f18sales.R;
import org.svuonline.f18sales.data.DatabaseHelper;
import org.svuonline.f18sales.model.Region;
import org.svuonline.f18sales.model.Salesman;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import static android.text.TextUtils.isEmpty;

public class ModifySalesmanFragment extends Fragment {

    private static final String TAG = ModifySalesmanFragment.class.getName();
    private static final int GALLERY_REQUEST_CODE = 406;
    DatabaseHelper dbHelper;

    private Spinner spinnerSalesmen;
    private ImageView imageView;
    private FloatingActionButton fabUploadImage;
    private EditText editTextSalesmanId;
    private EditText editTextFullName;
    private Spinner spinnerRegions;
    private EditText editTextHiringDate;
    private Button buttonModifySalesman;

    public ModifySalesmanFragment() {
    }

    public static ModifySalesmanFragment newInstance() {
        return new ModifySalesmanFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_salesman, container, false);

        dbHelper = new DatabaseHelper(getContext());

        initElements(v);
        setImageUploadOnClickListener(v);
        fillRegionsSpinnerWithData();
        fillSalesmenSpinnerWithData();

        setModifySalesmanButtonOnClickListener();
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
        spinnerSalesmen = v.findViewById(R.id.spinner_modify_salesmen);
        imageView = v.findViewById(R.id.image);
        fabUploadImage = v.findViewById(R.id.fab_upload_image);
        editTextSalesmanId = v.findViewById(R.id.editText_salesman_id);
        editTextFullName = v.findViewById(R.id.editText_full_name);
        spinnerRegions = v.findViewById(R.id.spinner_region);
        editTextHiringDate = v.findViewById(R.id.editText_hiring_date);
        buttonModifySalesman = v.findViewById(R.id.button_add_salesman);
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

    private void fillSalesmenSpinnerWithData() {
        ArrayList<Salesman> salesmenList = dbHelper.getAllSalesmen();
        if (salesmenList.size() > 0) {
            System.out.println("ooooooooooooo " + salesmenList.get(0).toString() + salesmenList.size());
            SalesmenSpinnerArrayAdapter salesmenAdapter = new SalesmenSpinnerArrayAdapter(getContext(), salesmenList);
            System.out.println( "is null " + spinnerSalesmen == null);
            spinnerSalesmen.setAdapter(salesmenAdapter);
        }
    }

    private void setModifySalesmanButtonOnClickListener() {
        buttonModifySalesman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValid()) {
                    boolean modified = modifySalesman();
                    if (modified) {
                        showMessage("Success", "Salesman was successfully modified in the database.");
                    } else {
                        showMessage("Failure", "Failed modifying salesman in the database.");
                    }
                } else {
                    Toast.makeText(getContext(), "Please fill all fields correctly including the image!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private boolean isValid() {
        // regions has a default value (always valid)
        // Hiring date editText is disabled --> its value will not change (always valid)
        // editTextSalesmanId has android:inputType="number" --> no need to do an extra check for integer
        return imageView.getDrawable() != null &&
                !isEmpty(editTextSalesmanId.getText().toString()) &&
                !isEmpty(editTextFullName.getText().toString());
    }

    private boolean modifySalesman() {
        // `id` and `newId` are used in the sql UPDATE command
        Integer id = ((Salesman) spinnerSalesmen.getSelectedItem()).getId();
        Integer newId = Integer.valueOf(editTextSalesmanId.getText().toString());
        String fullName = editTextFullName.getText().toString();
        Region region = (Region) spinnerRegions.getSelectedItem();
        String hiringDate = editTextHiringDate.getText().toString();
        byte[] image = parseImage();
        Salesman salesman = new Salesman(id, fullName, region.getId(), hiringDate, image, newId);

        return dbHelper.updateSalesman(salesman) != -1;
    }

    private byte[] parseImage() {
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() / 3, bitmap.getHeight() / 3, true);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 75, baos);
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
