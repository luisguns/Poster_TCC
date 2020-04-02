package com.tcc.luis.poster.interessado.ui.meu_perfil.ui.formacao;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

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
import com.tcc.luis.poster.model.FormacaoAcademica;

public class NovaFormacaoAcademicaActivity extends AppCompatActivity {
    private TextInputLayout mInpNameInstitution;
    private TextInputLayout mInpNameCourse;
    private TextInputLayout mInpConclusionDate;
    private TextInputLayout mInpInitiaionDate;
    private TextInputLayout mInpTitleAcademic;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private ConstraintLayout mLayout;
    private String TAG = "debugNovFormacao";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nova_formacao_academica);
        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Nova formação");
        initViews();


        
    }

    private void initViews() {
        mInpNameInstitution = findViewById(R.id.nova_formacao_academica_instituicao);
        mInpNameCourse = findViewById(R.id.nova_formacao_academica_nome_curso);
        mInpConclusionDate = findViewById(R.id.nova_formacao_academica_data_fim);
        mInpInitiaionDate = findViewById(R.id.nova_formacao_academica_data_inicio);
        mInpTitleAcademic = findViewById(R.id.nova_formacao_academica_titulo);
        mLayout = findViewById(R.id.layout_activity_nova_formacao);
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_save, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.navigation_save_item:
                FormacaoAcademica formacao = gerarFormacao();
                saveFormationAcademic(formacao);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private FormacaoAcademica gerarFormacao() {
        FormacaoAcademica fa = new FormacaoAcademica();
        fa.setUsuario(FirebaseAuth.getInstance().getCurrentUser().getUid());
        fa.setTituloFormacao(mInpTitleAcademic.getEditText().getText().toString());
        fa.setNomeCurso(mInpNameCourse.getEditText().getText().toString());
        fa.setNomeInstituicao(mInpNameInstitution.getEditText().getText().toString());
        fa.setDataInicio(mInpInitiaionDate.getEditText().getText().toString());
        fa.setDataConclusao(mInpConclusionDate.getEditText().getText().toString());

        return fa;
    }

    private void saveFormationAcademic(FormacaoAcademica formacaoAcademica) {
        DocumentReference documentSnapshotTask = firebaseFirestore.collection(Constantes.TABELA_DATABASE_CURRICULO).document(firebaseAuth.getCurrentUser().getUid());
        documentSnapshotTask.update("formacoesAcademicas", FieldValue.arrayUnion(formacaoAcademica.toHash())).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    SnackBarHelper.makeWithMensageAndAction(mLayout, "Formação adicionada com sucesso ;D", "OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                        }
                    }, true,NovaFormacaoAcademicaActivity.this);
                } else{
                    try {
                        throw task.getException();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(NovaFormacaoAcademicaActivity.this, "Ocorreu um erro tente novamente mais tarde", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
