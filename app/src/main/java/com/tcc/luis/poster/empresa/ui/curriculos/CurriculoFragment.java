package com.tcc.luis.poster.empresa.ui.curriculos;

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
import com.tcc.luis.poster.adapter.CurriculosAdapter;
import com.tcc.luis.poster.helpers.Constantes;
import com.tcc.luis.poster.model.Curriculo;
import com.tcc.luis.poster.model.Usuario;

import java.util.ArrayList;
import java.util.List;

public class CurriculoFragment extends Fragment {

    private RecyclerView rvCurriculos;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_curriculos_company, container, false);

        rvCurriculos = root.findViewById(R.id.curriculos_company_rv);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity().getApplicationContext());
        rvCurriculos.setLayoutManager(llm);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        pegarCurriculosDoApi();


        return root;
    }

    private void pegarCurriculosDoApi() {

        firebaseFirestore
                .collection(Constantes.TABELA_DATABASE_USUARIO)
                .whereEqualTo("companyRepresentative",false)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        List<Usuario> usuarios = new ArrayList<>();
                        List<DocumentSnapshot> documents = task.getResult().getDocuments();
                        for (DocumentSnapshot doc : documents
                        ) {
                            Usuario usuario = doc.toObject(Usuario.class);
                            usuarios.add(usuario);
                        }

                        carregarRvComUsuarios(usuarios);
                    }
                });

    }

    private void carregarRvComUsuarios(List<Usuario> usuarios) {

        CurriculosAdapter adapter = new CurriculosAdapter(usuarios,getActivity());
        rvCurriculos.setAdapter(adapter);
    }
}