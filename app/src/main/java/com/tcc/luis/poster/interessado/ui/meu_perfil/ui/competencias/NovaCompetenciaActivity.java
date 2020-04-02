package com.tcc.luis.poster.interessado.ui.meu_perfil.ui.competencias;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tcc.luis.poster.R;
import com.tcc.luis.poster.helpers.Constantes;
import com.tcc.luis.poster.helpers.SnackBarHelper;
import com.tcc.luis.poster.model.Competencia;

public class NovaCompetenciaActivity extends AppCompatActivity {

    private TextInputLayout mInpCompetencia;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private ConstraintLayout mLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nova_competencia);
        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mInpCompetencia = findViewById(R.id.nova_competencia_txt_competencia);
        mLayout = findViewById(R.id.layout_activity_nova_oportunidade);
        setTitle("Nova competencia");


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();


    }

    private void saveCompetencia() {
        String competenciaTitle = mInpCompetencia.getEditText().getText().toString();
        DocumentReference documentSnapshotTask = firebaseFirestore.collection(Constantes.TABELA_DATABASE_CURRICULO).document(firebaseAuth.getCurrentUser().getUid());
        Competencia competencia = new Competencia();
        competencia.setCompetencia(competenciaTitle);
        documentSnapshotTask.update("competencias", FieldValue.arrayUnion(competencia.toHash()))
        .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    SnackBarHelper.makeWithMensageOnly(mLayout, "Competencia adicionada",true, NovaCompetenciaActivity.this);
                } else {
                    SnackBarHelper.makeWithMensageOnly(mLayout, "Aconteceu um erro ao adicionar competencia",true, NovaCompetenciaActivity.this);
                }
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.navigation_save_item:
                saveCompetencia();
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_save, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
