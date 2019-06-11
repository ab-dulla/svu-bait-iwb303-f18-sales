package org.svuonline.f18sales.salesmen.management;


import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import org.svuonline.f18sales.R;
import org.svuonline.f18sales.data.DatabaseHelper;
import org.svuonline.f18sales.model.Salesman;

public class DeleteSalesmanFragment extends Fragment {
    private DatabaseHelper dbHelper;
    private Spinner spinnerSalesmen;
    private Button buttonDeleteSalesman;

    public DeleteSalesmanFragment() {
    }

    public static DeleteSalesmanFragment newInstance() {
        return new DeleteSalesmanFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_delete_salesman, container, false);

        dbHelper = new DatabaseHelper(getContext());

        initElements(v);
        setDeleteButtonOnClickListener();
        return v;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            fillSalesmenSpinnerWithData();
        }
    }

    private void initElements(View v) {
        spinnerSalesmen = v.findViewById(R.id.spinner_salesmen);
        buttonDeleteSalesman = v.findViewById(R.id.button_delete_salesman);
    }

    private void fillSalesmenSpinnerWithData() {
        boolean dbNotYetReady = dbHelper == null;
        if (dbNotYetReady) {
            Toast.makeText(getContext(), "Database is not ready yet, please try to re-visit this page after 3 seconds.", Toast.LENGTH_LONG).show();
            return;
        }
        SalesmenSpinnerArrayAdapter salesmenAdapter = new SalesmenSpinnerArrayAdapter(getContext(), dbHelper.getAllSalesmen());
        spinnerSalesmen.setAdapter(salesmenAdapter);
    }

    private void setDeleteButtonOnClickListener() {
        buttonDeleteSalesman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (spinnerSalesmen.getCount() == 0) {
                        Toast.makeText(getContext(), "There is no salesman to delete", Toast.LENGTH_LONG).show();
                        return;
                    }
                    Salesman selectedSalesman = (Salesman) spinnerSalesmen.getSelectedItem();
                    boolean deleted = dbHelper.deleteSalesman(selectedSalesman) != -1;
                    if (deleted) {
                        showMessage("Success", "Salesman is deleted.");
                        fillSalesmenSpinnerWithData();
                    } else {
                        showMessage("Error", "Error deleting salesman.");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    showMessage("Error", "Unexpected error occurred while trying to delete salesman.");
                }
            }
        });

    }

    private void showMessage(String title, String Message) {
        new AlertDialog.Builder(getContext())
                .setCancelable(true)
                .setTitle(title)
                .setMessage(Message)
                .show();
    }
}
