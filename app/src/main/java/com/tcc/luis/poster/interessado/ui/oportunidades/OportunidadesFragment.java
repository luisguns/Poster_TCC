package com.tcc.luis.poster.interessado.ui.oportunidades;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.tcc.luis.poster.R;
import com.tcc.luis.poster.adapter.OportunidadesAdapter;
import com.tcc.luis.poster.helpers.Constantes;
import com.tcc.luis.poster.model.OportunidadeDeEmprego;

import java.util.ArrayList;
import java.util.List;

public class OportunidadesFragment extends Fragment {

    private RecyclerView mRvOportunidades;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        mRvOportunidades = v.findViewById(R.id.oportunidades_interessado_rv);
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        LinearLayoutManager llm = new LinearLayoutManager(getActivity().getApplicationContext());
        mRvOportunidades.setLayoutManager(llm);
        getOportunidades();
        return v;
    }

    private void getOportunidades() {
        firebaseFirestore.collection(Constantes.TABELA_DATABASE_OPORTUNIDADE_EMPREGO).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            List<OportunidadeDeEmprego> oportunidades = new ArrayList<>();
                            List<DocumentSnapshot> documents = task.getResult().getDocuments();
                            for (DocumentSnapshot document: documents
                                 ) {
                                OportunidadeDeEmprego oportunidadeDeEmprego = document.toObject(OportunidadeDeEmprego.class);
                                oportunidadeDeEmprego.setKey(document.getId());
                                oportunidades.add(oportunidadeDeEmprego);
                            }

                            loadRecyclerView(oportunidades);
                        }
                    }
                });
    }

    private void loadRecyclerView(List<OportunidadeDeEmprego> oportunidades) {
        OportunidadesAdapter adapter = new OportunidadesAdapter(oportunidades,getActivity(), false);
        mRvOportunidades.setAdapter(adapter);
    }
}