package com.tcc.luis.poster.interessado.ui.meu_perfil.ui.competencias;

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
import com.tcc.luis.poster.adapter.CompetenciasAdapter;
import com.tcc.luis.poster.adapter.FormacoesAcademicasAdapter;
import com.tcc.luis.poster.helpers.Constantes;
import com.tcc.luis.poster.interessado.ui.meu_perfil.ui.formacao.FormacoesAcademicaActivity;
import com.tcc.luis.poster.model.Competencia;
import com.tcc.luis.poster.model.Curriculo;
import com.tcc.luis.poster.model.FormacaoAcademica;

import java.util.List;

public class CompetenciasActivity extends AppCompatActivity {

    private FloatingActionButton mFabNovaCompetencia;
    private RecyclerView mRvCompetencias;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_competencias);
        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Competencias");

        mFabNovaCompetencia = findViewById(R.id.competencias_fab);
        mRvCompetencias = findViewById(R.id.competencias_rv);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        mRvCompetencias.setLayoutManager(llm);


        mFabNovaCompetencia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CompetenciasActivity.this, NovaCompetenciaActivity.class));
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
                    List<Competencia> competencias = curriculo.getCompetencias();
                    if(competencias != null){
                        loadRecyclerVIew(competencias);
                    }

                }
            }
        });
    }


    private void loadRecyclerVIew(List<Competencia> competencias) {
        CompetenciasAdapter adapter = new CompetenciasAdapter(competencias, CompetenciasActivity.this, true);
        mRvCompetencias.setAdapter(adapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
