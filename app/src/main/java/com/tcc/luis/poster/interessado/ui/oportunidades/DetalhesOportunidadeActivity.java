package com.tcc.luis.poster.interessado.ui.oportunidades;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.tcc.luis.poster.R;
import com.tcc.luis.poster.helpers.Constantes;
import com.tcc.luis.poster.helpers.SnackBarHelper;
import com.tcc.luis.poster.model.Interessado;
import com.tcc.luis.poster.model.OportunidadeDeEmprego;

public class DetalhesOportunidadeActivity extends AppCompatActivity {

    private ImageView mImgEmpresa;
    private TextView mNomeEmpresa;
    private TextView mSloganEmpresa;
    private TextView mRamoEmpresa;
    private TextView mContatoEmpresa;
    private TextView mTituloVaga;
    private TextView mDescVaga;
    private TextView mCategoriaVaga;
    private FirebaseFirestore firebaseFirestorager;
    private Button mBtnInteresse;
    private OportunidadeDeEmprego oportunidade;
    private ConstraintLayout mLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_oportunidade);
        final String oportunidadeId = getIntent().getExtras().getString(Constantes.OPORTUNIDADE_KEY);

        if(oportunidadeId != null){
            this.firebaseFirestorager = FirebaseFirestore.getInstance();
            loadViews();
            getOportunidadeFromApi(oportunidadeId);
            mBtnInteresse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Interessado interessado = new Interessado();
                    interessado.setIdInteressado(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    interessado.setIdEmpresa(oportunidade.getEmpresaUid());
                    interessado.setIdOportunidade(oportunidadeId);
                    if(interessado.getIdEmpresa() != null && interessado.getIdInteressado() != null){
                        
                    firebaseFirestorager.collection(Constantes.TABELA_DATABASE_INTERESSADOS).add(interessado)
                    .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if(task.isSuccessful()){
                                SnackBarHelper.makeWithMensageAndAction(mLayout, "Operação realizada com sucesso", "OK", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        finish();
                                    }
                                },true,DetalhesOportunidadeActivity.this);
                            }
                        }
                    });
                    } else {
                        Toast.makeText(DetalhesOportunidadeActivity.this, "Nenhuma empresa selecionada", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            
        } else {
            Toast.makeText(this, "Ocorreu um erro inesperado, tente mais tarde :(", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void getOportunidadeFromApi(String oportunidadeId) {
        firebaseFirestorager.collection(Constantes.TABELA_DATABASE_OPORTUNIDADE_EMPREGO).document(oportunidadeId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {

                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            OportunidadeDeEmprego oportunidadeDeEmprego = task.getResult().toObject(OportunidadeDeEmprego.class);
                            oportunidade = oportunidadeDeEmprego;
                            setViewsWithJob(oportunidadeDeEmprego);
                        } else {
                            Toast.makeText(DetalhesOportunidadeActivity.this, "Ocorreu um erro com o servidor, tente mais tarde :(", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void setViewsWithJob(OportunidadeDeEmprego oportunidadeDeEmprego) {
        Log.d("TAG", "setViewsWithJob: " + oportunidadeDeEmprego.getEmpresa().getImagemLogo());
        if(oportunidadeDeEmprego.getEmpresa().getImagemLogo() != null){
            if(!oportunidadeDeEmprego.getEmpresa().getImagemLogo().isEmpty()){
                Picasso.with(DetalhesOportunidadeActivity.this)
                        .load(oportunidadeDeEmprego.getEmpresa().getImagemLogo())
                        .networkPolicy(NetworkPolicy.OFFLINE).into(mImgEmpresa, new Callback() {
                    @Override
                    public void onSuccess() {
                        mImgEmpresa.setVisibility(View.VISIBLE);

                    }

                    @Override
                    public void onError() {
                        mImgEmpresa.setVisibility(View.GONE);
                    }
                });
            }
        }
        mNomeEmpresa.setText(oportunidadeDeEmprego.getEmpresa().getNome());
        mSloganEmpresa.setText(oportunidadeDeEmprego.getEmpresa().getSlogan() != null  ? oportunidadeDeEmprego.getEmpresa().getSlogan(): "");
        mRamoEmpresa.setText(oportunidadeDeEmprego.getEmpresa().getSetor() != null  ? oportunidadeDeEmprego.getEmpresa().getSetor(): "");
        mContatoEmpresa.setText(oportunidadeDeEmprego.getEmpresa().getTelefone() != null  ? oportunidadeDeEmprego.getEmpresa().getTelefone(): "");
        mTituloVaga.setText(oportunidadeDeEmprego.getCargo()!= null  ? oportunidadeDeEmprego.getCargo(): "");
        mCategoriaVaga.setText(oportunidadeDeEmprego.getCategoria()!= null  ? oportunidadeDeEmprego.getCategoria(): "");
        mDescVaga.setText(oportunidadeDeEmprego.getDescricao()!= null  ? oportunidadeDeEmprego.getDescricao(): "");

    }

    private void loadViews() {
        mImgEmpresa = findViewById(R.id.detalhes_oportunidade_img_empresa);
        mNomeEmpresa = findViewById(R.id.detalhes_oportunidade_nome_empresa);
        mSloganEmpresa = findViewById(R.id.detalhes_oportunidade_slogan_empresa);
        mRamoEmpresa = findViewById(R.id.detalhes_oportunidade_ramo_empresa);
        mContatoEmpresa = findViewById(R.id.detalhes_oportunidade_empresa_contato);
        mCategoriaVaga = findViewById(R.id.detalhes_oportunidade_categoria);
        mTituloVaga = findViewById(R.id.detalhes_oportunidade_vaga_titulo);
        mDescVaga = findViewById(R.id.detalhes_oportunidade_desc);
        mBtnInteresse = findViewById(R.id.detalhes_oportunidade_btn_interesse);
        mLayout = findViewById(R.id.layout_activity_detalhes_oportunidade);
        mImgEmpresa.setVisibility(View.GONE);

    }
}
