package com.tcc.luis.poster.interessado.ui.meu_perfil.ui.experiencia;

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
import com.tcc.luis.poster.adapter.ExperienciaProfissionalAdapter;
import com.tcc.luis.poster.helpers.Constantes;
import com.tcc.luis.poster.model.Competencia;
import com.tcc.luis.poster.model.Curriculo;
import com.tcc.luis.poster.model.ExperienciaProfissional;

import java.util.List;

public class ExperienciaProfissionalActivity extends AppCompatActivity {
    private RecyclerView mRvExperiencias;
    private FloatingActionButton mFabNovaExperiencia;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experiencia_profissional);
        setTitle("Experiencia profissional");
        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRvExperiencias = findViewById(R.id.experiencia_profissional_rv);
        mFabNovaExperiencia = findViewById(R.id.experiencia_profissional_fab);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        mRvExperiencias.setLayoutManager(llm);

        mFabNovaExperiencia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ExperienciaProfissionalActivity.this, NovaExperienciaProfissionalActivity.class));
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
                    List<ExperienciaProfissional> experienciasProfissionais = curriculo.getExperienciasProfissionais();
                    if(experienciasProfissionais != null){
                        loadRecyclerVIew(experienciasProfissionais);
                    }
                }
            }
        });
    }

    private void loadRecyclerVIew(List<ExperienciaProfissional> experienciasProfissionais) {
        ExperienciaProfissionalAdapter adapter = new ExperienciaProfissionalAdapter(experienciasProfissionais, ExperienciaProfissionalActivity.this,true);
        mRvExperiencias.setAdapter(adapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
