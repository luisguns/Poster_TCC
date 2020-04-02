package com.tcc.luis.poster.empresa.ui.minha_empresa.ui;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tcc.luis.poster.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AboutCompanyTabFragment extends Fragment {


    public AboutCompanyTabFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_about_company_tab, container, false);
    }

    @Override
    public void onStart() {


        super.onStart();
    }
}
