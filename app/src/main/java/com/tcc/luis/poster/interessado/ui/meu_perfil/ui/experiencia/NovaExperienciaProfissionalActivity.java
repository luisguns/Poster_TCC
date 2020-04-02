package com.tcc.luis.poster.interessado.ui.meu_perfil.ui.experiencia;

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
import com.tcc.luis.poster.interessado.ui.meu_perfil.ui.competencias.NovaCompetenciaActivity;
import com.tcc.luis.poster.model.ExperienciaProfissional;

public class NovaExperienciaProfissionalActivity extends AppCompatActivity {

    private TextInputLayout mInpCargo;
    private TextInputLayout mInpEmpresa;
    private TextInputLayout mInpDataInicio;
    private TextInputLayout mInpDataFim;
    private TextInputLayout mInpDesc;
    private ConstraintLayout mLayout;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nova_experiencia_profissional);
        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Nova experiencia profissional");

        initViews();



    }

    private void initViews() {
        mInpCargo = findViewById(R.id.nova_experiencia_cargo);
        mInpEmpresa = findViewById(R.id.nova_experiencia_empresa);
        mInpDataInicio = findViewById(R.id.nova_experiencia_data_inicio);
        mInpDataFim = findViewById(R.id.nova_experiencia_data_fim);
        mInpDesc = findViewById(R.id.nova_experiencia_descricao);
        mLayout = findViewById(R.id.layout_activity_nova_experiencia);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_save,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.navigation_save_item:
                saveExperiencia();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void saveExperiencia() {
        ExperienciaProfissional experiencia = generateExperiencia();
        DocumentReference documentSnapshotTask = firebaseFirestore.collection(Constantes.TABELA_DATABASE_CURRICULO).document(firebaseAuth.getCurrentUser().getUid());
        documentSnapshotTask.update("experienciasProfissionais", FieldValue.arrayUnion(experiencia.toHash()))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            SnackBarHelper.makeWithMensageOnly(mLayout, "Experiencia profissional adicionada",true, NovaExperienciaProfissionalActivity.this);
                        } else {
                            SnackBarHelper.makeWithMensageOnly(mLayout, "Aconteceu um erro ao adicionar a experiencia profissional",true, NovaExperienciaProfissionalActivity.this);
                        }
                    }
                });
    }

    private ExperienciaProfissional generateExperiencia() {

        ExperienciaProfissional ex = new ExperienciaProfissional();
        ex.setCargo(mInpCargo.getEditText().getText().toString());
        ex.setEmpresa(mInpEmpresa.getEditText().getText().toString());
        ex.setDescricao(mInpDesc.getEditText().getText().toString());
        ex.setDataFim(mInpDataFim.getEditText().getText().toString());
        ex.setDataInicio(mInpDataInicio.getEditText().getText().toString());

        return ex;
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
