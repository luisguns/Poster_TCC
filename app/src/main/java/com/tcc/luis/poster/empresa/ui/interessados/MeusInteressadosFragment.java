package com.tcc.luis.poster.empresa.ui.interessados;

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
import com.tcc.luis.poster.adapter.InteressadosAdapter;
import com.tcc.luis.poster.adapter.OportunidadesAdapter;
import com.tcc.luis.poster.helpers.Constantes;
import com.tcc.luis.poster.model.Interessado;

import java.util.ArrayList;
import java.util.List;

public class MeusInteressadosFragment extends Fragment {

    private RecyclerView mRvInteressados;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_meus_interessados, container, false);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        LinearLayoutManager llm = new LinearLayoutManager(getActivity().getApplicationContext());

        mRvInteressados = v.findViewById(R.id.meus_interessados_rv);
        mRvInteressados.setLayoutManager(llm);
        getInteressadosApi();

        return v;
    }

    private void getInteressadosApi() {
        firebaseFirestore.collection(Constantes.TABELA_DATABASE_INTERESSADOS)
                .whereEqualTo("idEmpresa", firebaseAuth.getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            List<Interessado> interessados = new ArrayList<>();
                            List<DocumentSnapshot> documents = task.getResult().getDocuments();

                            for (DocumentSnapshot document: documents
                                 ) {
                                Interessado interessado = document.toObject(Interessado.class);
                                interessados.add(interessado);
                            }

                            loadRecycler(interessados);
                        }
                    }
                });
    }

    private void loadRecycler(List<Interessado> interessados) {
        InteressadosAdapter adapter = new InteressadosAdapter(interessados,getActivity());
        mRvInteressados.setAdapter(adapter);
    }
}