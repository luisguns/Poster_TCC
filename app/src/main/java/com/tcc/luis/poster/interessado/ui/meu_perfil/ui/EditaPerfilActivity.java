package com.tcc.luis.poster.interessado.ui.meu_perfil.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.tcc.luis.poster.R;
import com.tcc.luis.poster.helpers.Constantes;
import com.tcc.luis.poster.helpers.ImageHelper;
import com.tcc.luis.poster.helpers.ProgressHelper;
import com.tcc.luis.poster.helpers.SnackBarHelper;
import com.tcc.luis.poster.model.Usuario;

import java.io.File;
import java.io.IOException;

import id.zelory.compressor.Compressor;
import id.zelory.compressor.FileUtil;

public class EditaPerfilActivity extends AppCompatActivity {
    private static final int IMAGE_GALLERY_REQUEST = 1;
    private static final String TAG = "EditarPerfil";

    private ImageView mImgPerfil;
    private Activity activity;
    private Uri mImagemUri;

    private ProgressBar mProgressGeral;
    private ProgressBar mProgressImg;
    private ConstraintLayout mContainerViews;

    private TextInputLayout mInpNome;
    private TextInputLayout mInpSobreNome;
    private TextInputLayout mInpOndeFormou;
    private TextInputLayout mInpSobre;
    private FirebaseFirestore firebaseFirestore;
    private ConstraintLayout mLayout;
    private Usuario usuario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edita_perfil);
        assert getSupportActionBar() != null;   //null check
        initViews();
        findUsuario();


        mImgPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImageClick();
            }
        });
    }

    private void findUsuario() {
        ProgressHelper.show(mProgressGeral,mContainerViews,true);
        this.firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection(Constantes.TABELA_DATABASE_USUARIO).document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            ProgressHelper.show(mProgressGeral,mLayout,false);
                            usuario = task.getResult().toObject(Usuario.class);
                            updateUiWhitUsuario(usuario);

                        } else {
                            Toast.makeText(activity, "Erro ao tentar identificar usuario, tente mais tarde", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void updateUiWhitUsuario(Usuario usuario) {
        mInpNome.getEditText().setText(usuario.getName() != null ? usuario.getName() : "");
        mInpSobre.getEditText().setText(usuario.getSobre() != null ? usuario.getSobre() : "");
        mInpSobreNome.getEditText().setText(usuario.getLastName() != null ? usuario.getLastName() : "");
        mInpOndeFormou.getEditText().setText(usuario.getOcupacao() != null ? usuario.getOcupacao() : "");
        ProgressHelper.show(mProgressGeral,mContainerViews,false);
        if (!usuario.getProfileImage().isEmpty()) {
            ProgressHelper.show(mProgressImg,null,true);
            ImageHelper.carregaImagem(mImgPerfil, usuario.getProfileImage(), EditaPerfilActivity.this,mProgressImg);
        }
    }

    private void initViews() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity = EditaPerfilActivity.this;
        mImgPerfil = findViewById(R.id.editar_perfil_img_perfil);
        mInpSobre = findViewById(R.id.editar_perfil_sobre);
        mInpNome = findViewById(R.id.editar_perfil_nome);
        mInpSobreNome = findViewById(R.id.editar_perfil_sobrenome);
        mInpOndeFormou = findViewById(R.id.editar_perfil_atual_formacao);
        mLayout = findViewById(R.id.layout_activity_editar_perfil);

        mProgressGeral = findViewById(R.id.editar_perfil_progress_geral);
        mProgressImg= findViewById(R.id.editar_perfil_progress_image);
        mContainerViews = findViewById(R.id.editar_perfil_container_views);

    }

    public void selectImageClick() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(
                    Intent.createChooser(
                            intent,
                            getString(R.string.select_picture_title)),
                    IMAGE_GALLERY_REQUEST);
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    IMAGE_GALLERY_REQUEST);
        }
    }

    private void updateImage(Uri data1) {
        String paste = "image/profile/";
        StorageReference reference = FirebaseStorage.getInstance().getReference();
        StorageReference child = reference.child(paste + String.valueOf(System.currentTimeMillis()));


        StorageTask<UploadTask.TaskSnapshot> taskSnapshotStorageTask = child.putFile(data1)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        Task<Uri> downloadUrl = taskSnapshot.getStorage().getDownloadUrl();
                        downloadUrl.addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                updateProfile(uri.toString());
                            }
                        })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(EditaPerfilActivity.this, "Ocorreu um erro durante o upload de sua imagem, Tente mais tarde", Toast.LENGTH_SHORT).show();
                                    }
                                });


//                        updateProfile(s);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                    }
                });

    }

    private void updateProfile(final String uri) {
        FirebaseFirestore instance = FirebaseFirestore.getInstance();
        instance.collection(Constantes.TABELA_DATABASE_USUARIO).document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .update("profileImage", uri)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            ImageHelper.carregaImagem(mImgPerfil,uri,EditaPerfilActivity.this,null);
                            findUsuario();
                        } else {
                            Toast.makeText(EditaPerfilActivity.this, "Ocorreu um erro ao atualizar sua imagem, tente novamente", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_GALLERY_REQUEST && resultCode == RESULT_OK) {
            Uri data1 = data.getData();
            try {
                File fileImagem = FileUtil.from(EditaPerfilActivity.this, data.getData());
                fileImagem = Compressor.getDefault(EditaPerfilActivity.this).compressToFile(fileImagem);
                mImagemUri = Uri.fromFile(fileImagem);
                ImageHelper.carregaImagem(mImgPerfil, mImagemUri.toString(), EditaPerfilActivity.this,null);
                updateImage(mImagemUri);
            } catch (IOException e) {
                e.printStackTrace();

            }

//           updateImage(data1);

        }
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
                updateUsuario();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void updateUsuario() {
        String nome = mInpNome.getEditText().getText().toString();
        String sobrenome = mInpSobreNome.getEditText().getText().toString();
        String ocupacao = mInpOndeFormou.getEditText().getText().toString();
        String sobre = mInpSobre.getEditText().getText().toString();


        firebaseFirestore.collection(Constantes.TABELA_DATABASE_USUARIO).document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .update("name", nome,
                        "lastName", sobrenome,
                        "sobre", sobre,
                        "ocupacao", ocupacao)
        .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    SnackBarHelper.makeWithMensageAndAction(mLayout, "Dados atualizados", "OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                        }
                    }, true, EditaPerfilActivity.this);
                }
            }
        })
        ;
    }


}
