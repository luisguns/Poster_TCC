package com.tcc.luis.poster.empresa.ui.minha_empresa.ui;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.tcc.luis.poster.R;
import com.tcc.luis.poster.adapter.OportunidadesAdapter;
import com.tcc.luis.poster.helpers.Constantes;
import com.tcc.luis.poster.model.OportunidadeDeEmprego;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CompanyJobsTabFragment extends Fragment {

    private RecyclerView mRvJobs;
    private FirebaseFirestore firebaseFirestore;

    public CompanyJobsTabFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_company_jobs_tab, container, false);
        initViews(v);
        getJobsFromFirestore();
        return v;
    }

    private void getJobsFromFirestore() {
        firebaseFirestore
                .collection(Constantes.TABELA_DATABASE_OPORTUNIDADE_EMPREGO)
                .whereEqualTo("empresaUid", FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            Log.d("myTagJobs", "onComplete: " + "SUCESS");
                            List<OportunidadeDeEmprego> oportunidadeDeEmpregos = task.getResult().toObjects(OportunidadeDeEmprego.class);
                            loadRecyclerView(oportunidadeDeEmpregos);
                        } else {
                            Log.d("myTagJobs", "onComplete: " + "ERRO");
                        }
                    }
                });
    }

    private void loadRecyclerView(List<OportunidadeDeEmprego> oportunidadeDeEmpregos) {
        OportunidadesAdapter adapter = new OportunidadesAdapter(oportunidadeDeEmpregos,getActivity(), true);
        mRvJobs.setAdapter(adapter);
    }

    private void initViews(View v) {
        mRvJobs = v.findViewById(R.id.company_jobs_tab_rv_jobs);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        mRvJobs.setLayoutManager(llm);
        firebaseFirestore = FirebaseFirestore.getInstance();

    }


}
