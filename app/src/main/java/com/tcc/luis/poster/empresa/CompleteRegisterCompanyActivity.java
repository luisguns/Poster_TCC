package com.tcc.luis.poster.empresa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tcc.luis.poster.R;
import com.tcc.luis.poster.api.ServerGenerateLocalidades;
import com.tcc.luis.poster.api.model.Cidade;
import com.tcc.luis.poster.api.model.Estado;
import com.tcc.luis.poster.helpers.Constantes;
import com.tcc.luis.poster.helpers.CountHelper;
import com.tcc.luis.poster.helpers.SnackBarHelper;
import com.tcc.luis.poster.model.Empresa;
import com.tcc.luis.poster.model.Endereco;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompleteRegisterCompanyActivity extends AppCompatActivity {

    private static final String TAG = "CompleteRegisterCompanyTag";
    private TextInputLayout mInptNome;
    private ConstraintLayout mLayout;
    private TextInputLayout mInpCNPJ;
    private TextInputLayout mInpTelefone;
    private TextInputLayout mInptRua;
    private TextInputLayout mInpBairro;
    private TextInputLayout mInpSetor;
    private Spinner mSpnState;
    private Spinner mSpnCity;
    private Button mBtnComplete;

    private List<Estado> stateList;
    private List<Cidade> cityList;
    private Estado stateSelected;
    private Cidade citySelected;
    private FirebaseFirestore firebaseFirestore;
    private String mTag = "TAGCompleteRegisterCompany";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_register_company);
        setTitle("Concluir cadastro");
        showDialog();
        initViews();
        conectedApiForGetStates();
        applyListeners();

    }

    private void applyListeners() {
        mBtnComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Empresa company = createCompany();
                sendCompanyForFirebase(company);
            }
        });

        mSpnState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                stateSelected = stateList.get(position);
                conectedApiForGetCits(stateList.get(position).getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                stateSelected = stateList.get(0);
                conectedApiForGetCits(stateList.get(0).getId());
            }
        });

        mSpnCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                citySelected = cityList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                citySelected = cityList.get(0);

            }
        });
    }

    private void sendCompanyForFirebase(Empresa empresa) {
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection(Constantes.TABELA_DATABASE_EMPRESA).document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .set(empresa)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Log.d(mTag, "onComplete: " + "sucess");
                            firebaseFirestore.collection(Constantes.TABELA_DATABASE_USUARIO)
                                    .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .update("companyCompleteRegister",true)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                SnackBarHelper.makeWithMensageAndAction(mLayout, "Empresa cadastrada com suceso", "OK", new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        Intent intent = new Intent(CompleteRegisterCompanyActivity.this, MainCompanyActivity.class);
                                                        startActivity(intent);
                                                    }
                                                }, true, CompleteRegisterCompanyActivity.this);
                                            } else {
                                                Log.d(mTag, "onComplete: " + "error");
                                            }
                                        }
                                    })
                            ;
                        } else {
                            SnackBarHelper.makeWithMensageOnly(mLayout,"Ocorreu um erro ao criar a empresa, tente mais tarde :(",true, CompleteRegisterCompanyActivity.this);
                        }
                    }
                });
    }

    private Empresa createCompany() {
        Empresa mEmpresa = new Empresa();
        String nome = mInptNome.getEditText().getText().toString();
        String cnpj = mInpCNPJ.getEditText().getText().toString();
        String cidade = citySelected.getNome();
        String estado = stateSelected.getSigla();
        String rua = mInptRua.getEditText().getText().toString();
        String bairro = mInpBairro.getEditText().getText().toString();
        String telefone = mInpTelefone.getEditText().getText().toString();
        String setor = mInpSetor.getEditText().getText().toString();

        //Validat Inputs Here
        mEmpresa.setRepresentanteId(FirebaseAuth.getInstance().getCurrentUser().getUid());
        mEmpresa.setNome(nome);
        mEmpresa.setSetor(setor);
        mEmpresa.setCnpj(cnpj);
        mEmpresa.setTelefone(telefone);
        mEmpresa.setEndereco(new Endereco(estado,cidade,bairro,rua));


        return mEmpresa;
    }

    private void conectedApiForGetStates() {
        ServerGenerateLocalidades sv = new ServerGenerateLocalidades(getApplicationContext());
        Call<List<Estado>> request = sv.criarServer().pegarEstados();
        request.enqueue(new Callback<List<Estado>>() {
            @Override
            public void onResponse(Call<List<Estado>> call, Response<List<Estado>> response) {
                if(response.isSuccessful()){
                    stateList = response.body();
                    Collections.sort(stateList);
                    ArrayAdapter<Estado> adpterEstado =
                            new ArrayAdapter<Estado>(getApplicationContext(),  android.R.layout.simple_spinner_dropdown_item, stateList);
                    adpterEstado.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);

                    mSpnState.setAdapter(adpterEstado);

                    conectedApiForGetCits(stateList.get(0).getId());
                } else {
                    Log.d("TAG", "onResponse: " + response.message());

                }
            }

            @Override
            public void onFailure(Call<List<Estado>> call, Throwable t) {
                Log.d("TAG", "onFailure: " + t.getMessage());
            }
        });
    }

    private void conectedApiForGetCits(int id) {
        ServerGenerateLocalidades sv = new ServerGenerateLocalidades(getApplicationContext());
        Call<List<Cidade>> request = sv.criarServer().pegarCidadesPoEstados(id);
        request.enqueue(new Callback<List<Cidade>>() {
            @Override
            public void onResponse(Call<List<Cidade>> call, Response<List<Cidade>> response) {
                if(response.isSuccessful()){
                    cityList = response.body();
                    Collections.sort(cityList);
                    ArrayAdapter<Cidade> adpterCidade =
                            new ArrayAdapter<Cidade>(getApplicationContext(),  android.R.layout.simple_spinner_dropdown_item, cityList);
                    adpterCidade.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
                    mSpnCity.setAdapter(adpterCidade);

                } else {
                    Log.d("TAG", "onResponse: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Cidade>> call, Throwable t) {

            }
        });
    }

    private void initViews() {
        mInptNome = findViewById(R.id.complete_register_company_nome_empresa);
        mInpCNPJ = findViewById(R.id.complete_register_company_cnpj);
        mInpTelefone = findViewById(R.id.complete_register_company_telefone);
        mInpSetor = findViewById(R.id.complete_register_company_setor);
        mInptRua = findViewById(R.id.complete_register_company_rua);
        mInpBairro = findViewById(R.id.complete_register_company_bairro);
        mSpnCity = findViewById(R.id.complete_register_company_cidades);
        mSpnState = findViewById(R.id.complete_register_company_estados);
        mBtnComplete = findViewById(R.id.complete_register_company_btn_concluir);
        mLayout = findViewById(R.id.layout_activity_complete_register_company);
    }

    private void showDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Para continuar como representante de uma empresa, é necessario concluir o cadastro com dados da empresa");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("Depois", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                CountHelper.logout(CompleteRegisterCompanyActivity.this);
            }
        });

        builder.setCancelable(false);
        builder.show();
    }
}
