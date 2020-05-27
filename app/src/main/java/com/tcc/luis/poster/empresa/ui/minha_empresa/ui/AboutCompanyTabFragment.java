package com.tcc.luis.poster.empresa.ui.minha_empresa.ui;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tcc.luis.poster.R;
import com.tcc.luis.poster.helpers.Constantes;
import com.tcc.luis.poster.model.Empresa;

/**
 * A simple {@link Fragment} subclass.
 */
public class AboutCompanyTabFragment extends Fragment {

    private TextView mDescricao;
    private TextView mSite;
    private TextView mSetor;
    private TextView mTelefone;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    public AboutCompanyTabFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_about_company_tab, container, false);

        mDescricao = v.findViewById(R.id.about_company_text_descrcicao);
        mSite = v.findViewById(R.id.about_company_tab_site);
        mSetor = v.findViewById(R.id.about_company_tab_setor);
        mTelefone = v.findViewById(R.id.about_company_tab_telefone);
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore.collection(Constantes.TABELA_DATABASE_EMPRESA).document(firebaseAuth.getCurrentUser().getUid())
        .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    Empresa empresa = task.getResult().toObject(Empresa.class);
                    loadUi(empresa);
                } else {
                    Toast.makeText(getActivity(), "Ocorreu um erro", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return v;
    }

    private void loadUi(Empresa empresa) {
        mDescricao.setText(empresa.getDescricao());
        mSetor.setText(empresa.getSetor());
        mSite.setText(empresa.getSite());
        mTelefone.setText(empresa.getTelefone());

    }

    @Override
    public void onStart() {
        super.onStart();
    }
}
