package com.tcc.luis.poster.empresa;

import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tcc.luis.poster.R;
import com.tcc.luis.poster.helpers.Constantes;
import com.tcc.luis.poster.helpers.ProgressHelper;
import com.tcc.luis.poster.helpers.SnackBarHelper;
import com.tcc.luis.poster.model.Empresa;
import com.tcc.luis.poster.model.OportunidadeDeEmprego;

public class CadastrarOportunidadesActivity extends AppCompatActivity {
    private static final String TAG = "debugCadastroOportunidade";
    private ConstraintLayout mLayout;
    private ProgressBar mProgress;
    private TextInputLayout mInpCargo;
    private TextInputLayout mInpDescricao;
    public Empresa empresa;
    private TextInputLayout mInpCategoria;
    private Button mBtnCadastrar;
    private FirebaseFirestore firebaseFirestore;
    private String uidEmpresa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_oportunidades);
        initToolBar();
        initViews();
        uidEmpresa = FirebaseAuth.getInstance().getCurrentUser().getUid();
        getEmpresa();
        mBtnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressHelper.show(mProgress,mLayout, true);
                OportunidadeDeEmprego job = generateJob();
                saveJobOportunity(job);
            }
        });
    }

    private void getEmpresa() {
        ProgressHelper.show(mProgress,mLayout, true);
        DocumentReference documentSnapshotTask = firebaseFirestore.collection(Constantes.TABELA_DATABASE_EMPRESA).document(uidEmpresa);
        documentSnapshotTask.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    Empresa mEmpresa = task.getResult().toObject(Empresa.class);
                    ProgressHelper.show(mProgress,mLayout, false);
                    empresa = mEmpresa;
                    Log.d(TAG, "onComplete: " + empresa.getNome());


                }
            }
        });
    }

    private void saveJobOportunity(OportunidadeDeEmprego job) {
        firebaseFirestore.collection(Constantes.TABELA_DATABASE_OPORTUNIDADE_EMPREGO)
                .add(job)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        ProgressHelper.show(mProgress, mLayout, false);
                        if(task.isSuccessful()){
                            SnackBarHelper.makeWithMensageAndAction(mLayout, "Oportunidade Salva :D", "OK", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    finish();
                                }
                            }, true, CadastrarOportunidadesActivity.this);
                        } else {
                            SnackBarHelper.makeWithMensageOnly(mLayout,"Ocorreu um erro ao salvar a oportunidade :(", true, CadastrarOportunidadesActivity.this);
                        }
                    }
                });
    }

    private OportunidadeDeEmprego generateJob() {
        OportunidadeDeEmprego op = new OportunidadeDeEmprego();
        op.setEmpresa(empresa);
        op.setCargo(mInpCargo.getEditText().getText().toString());
        op.setDescricao(mInpDescricao.getEditText().getText().toString());
        op.setCategoria(mInpCategoria.getEditText().getText().toString());
        op.setEmpresaUid(uidEmpresa);
        return op;
    }

    private void initViews() {
        mInpCargo = findViewById(R.id.cadastrar_oportunidade_cargo);
        mInpCategoria = findViewById(R.id.cadastrar_oportunidade_categoria);
        mInpDescricao = findViewById(R.id.cadastrar_oportunidade_descricao);
        mBtnCadastrar = findViewById(R.id.cadastrar_oportunidade_btn_cadastrar);
        mLayout = findViewById(R.id.layout_activity_cadastrar_oportunidade);
        mProgress = findViewById(R.id.progressbar_cyclic_cadastro_oportunidade);
        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    private void initToolBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setTitle("Cadastrar oportunidade");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
