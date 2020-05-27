package com.tcc.luis.poster.empresa.ui.minha_empresa.ui;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tcc.luis.poster.R;
import com.tcc.luis.poster.helpers.Constantes;
import com.tcc.luis.poster.model.Empresa;

public class EditEmpresaActivity extends AppCompatActivity {

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private Empresa empresa;
    private String empresaUid;

    private TextInputLayout mNomeEmpresa;
    private TextInputLayout mSlogan;
    private TextInputLayout mDescricao;
    private TextInputLayout mSite;
    private TextInputLayout mSetor;
    private TextInputLayout mTelefone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_empresa);
        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Editar empresa");
        initViews();

    }

    private void initViews() {
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        empresaUid = firebaseAuth.getCurrentUser().getUid();

        mNomeEmpresa = findViewById(R.id.edit_empresa_nome);
        mSlogan = findViewById(R.id.edit_empresa_slogan);
        mDescricao = findViewById(R.id.edit_empresa_descricao);
        mSite = findViewById(R.id.edit_empresa_site);
        mSetor = findViewById(R.id.edit_empresa_setor);
        mTelefone = findViewById(R.id.edit_empresa_telefone);

    }

    private void findEmpresa() {
        firebaseFirestore.collection(Constantes.TABELA_DATABASE_EMPRESA)
                .document(empresaUid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            empresa = task.getResult().toObject(Empresa.class);
                            loadUi(empresa);
                        } else {
                            Toast.makeText(EditEmpresaActivity.this, "Ocorreu um erro tente mais tarde", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                });
    }

    private void loadUi(Empresa empresa) {
        mNomeEmpresa.getEditText().setText(empresa.getNome() != null ? empresa.getNome() : "");
        mSlogan.getEditText().setText(empresa.getSlogan() != null ? empresa.getSlogan() : "");
        mSetor.getEditText().setText(empresa.getSetor() != null ? empresa.getSetor() : "");
        mSite.getEditText().setText(empresa.getSite() != null ? empresa.getSite() : "");
        mTelefone.getEditText().setText(empresa.getTelefone() != null ? empresa.getTelefone() : "");
        mDescricao.getEditText().setText(empresa.getDescricao() != null ? empresa.getDescricao() : "");

    }

    @Override
    protected void onResume() {
        super.onResume();
        findEmpresa();
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
        switch (item.getItemId()) {
            case R.id.navigation_save_item:
                Empresa empresa = atualizarEmpresa();
                atualizarEmpresaApi(empresa);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private Empresa atualizarEmpresa() {

        empresa.setNome(! mNomeEmpresa.getEditText().getText().toString().isEmpty() ? mNomeEmpresa.getEditText().getText().toString() : null);
        empresa.setDescricao(! mDescricao.getEditText().getText().toString().isEmpty() ? mDescricao.getEditText().getText().toString() : null);
        empresa.setSetor(! mSetor.getEditText().getText().toString().isEmpty() ? mSetor.getEditText().getText().toString() : null);
        empresa.setSite(! mSite.getEditText().getText().toString().isEmpty() ? mSite.getEditText().getText().toString() : null);
        empresa.setSlogan(! mSlogan.getEditText().getText().toString().isEmpty() ? mSlogan.getEditText().getText().toString() : null);
        empresa.setTelefone(! mTelefone.getEditText().getText().toString().isEmpty() ? mTelefone.getEditText().getText().toString() : null);
        return empresa;
    }

    private void atualizarEmpresaApi(Empresa empresa) {
        firebaseFirestore.collection(Constantes.TABELA_DATABASE_EMPRESA).document(empresaUid)
                .update(empresa.toMap())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(EditEmpresaActivity.this, "Empresa atualizada com sucesso", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                });
    }
}
