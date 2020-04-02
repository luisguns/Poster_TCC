package com.tcc.luis.poster.empresa;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tcc.luis.poster.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class InfomacoesDaEmpresaFragment extends Fragment {

    public InfomacoesDaEmpresaFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_infomacoes_da_empresa, container, false);
    }

}
