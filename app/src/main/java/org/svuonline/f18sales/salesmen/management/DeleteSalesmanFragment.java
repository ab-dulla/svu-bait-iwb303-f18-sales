package org.svuonline.f18sales.salesmen.management;


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
import org.svuonline.f18sales.data.Utilities;
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
        spinnerSalesmen = v.findViewById(R.id.spinner_salesmen);
        buttonDeleteSalesman = v.findViewById(R.id.button_delete_salesman);
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

    private void fillSalesmenSpinnerWithData() {
        // a race condition
        boolean dbNotYetReady = dbHelper == null;
        if (dbNotYetReady) {
            Toast.makeText(getContext(), "قاعدة البيانات ليست جاهزة بعد، الرجاء معاودة زيارة الصفحة بعد 3 ثوان", Toast.LENGTH_LONG).show();
            return;
        }
        // fill spinner
        SalesmenSpinnerArrayAdapter salesmenAdapter = new SalesmenSpinnerArrayAdapter(getContext(), dbHelper.getAllSalesmen());
        spinnerSalesmen.setAdapter(salesmenAdapter);
    }

    private void setDeleteButtonOnClickListener() {
        // delete the selected salesman
        buttonDeleteSalesman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (spinnerSalesmen.getCount() == 0) {
                        Toast.makeText(getContext(), "لا يوجد أي مندوب مبيعات ليتم حذفه", Toast.LENGTH_LONG).show();
                        return;
                    }
                    Salesman selectedSalesman = (Salesman) spinnerSalesmen.getSelectedItem();
                    boolean deleted = dbHelper.deleteSalesman(selectedSalesman) != -1;
                    if (deleted) {
                        Utilities.showMessage(getContext(), "نجاح", "تمت حذف مندوب المبيعات بنجاح");
                        fillSalesmenSpinnerWithData();
                    } else {
                        Utilities.showMessage(getContext(), "فشل", "فشل حذف مندوب المبيعات");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Utilities.showMessage(getContext(), "فشل", "حدث خطأ غير متوقع خلال محاولة حذف المندوب");
                }
            }
        });
    }
}
