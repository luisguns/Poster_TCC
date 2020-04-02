package com.tcc.luis.poster.interessado.ui.meu_perfil.ui.formacao;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tcc.luis.poster.R;
import com.tcc.luis.poster.adapter.FormacoesAcademicasAdapter;
import com.tcc.luis.poster.helpers.Constantes;
import com.tcc.luis.poster.model.Curriculo;
import com.tcc.luis.poster.model.FormacaoAcademica;

import java.util.List;

public class FormacoesAcademicaActivity extends AppCompatActivity {
    private RecyclerView mRvFormacoes;
    private FloatingActionButton mFabAddFormcao;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formacoes_academica);
        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        mRvFormacoes = findViewById(R.id.recycler_view_formacoes_academicas);
        mFabAddFormcao = findViewById(R.id.formacao_academica_fab_add);
        setTitle("Formações academicas");

        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        mRvFormacoes.setLayoutManager(llm);


        mFabAddFormcao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FormacoesAcademicaActivity.this, NovaFormacaoAcademicaActivity.class));
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        findFormationFromFirebase();
    }

    private void findFormationFromFirebase() {
        firebaseFirestore.collection(Constantes.TABELA_DATABASE_CURRICULO).document(firebaseAuth.getCurrentUser().getUid())
        .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    Curriculo curriculo = task.getResult().toObject(Curriculo.class);
                    List<FormacaoAcademica> formacoesAcademicas = curriculo.getFormacoesAcademicas();
                    if(formacoesAcademicas != null){
                        loadRecyclerVIew(formacoesAcademicas);
                    }

                }
            }
        });
    }

    private void loadRecyclerVIew(List<FormacaoAcademica> formacoesAcademicas) {
        FormacoesAcademicasAdapter adapter = new FormacoesAcademicasAdapter(formacoesAcademicas,FormacoesAcademicaActivity.this, true);
        mRvFormacoes.setAdapter(adapter);
    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
