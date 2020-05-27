package com.tcc.luis.poster.usuario.fragments;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.tcc.luis.poster.R;
import com.tcc.luis.poster.helpers.Constantes;
import com.tcc.luis.poster.helpers.ImageHelper;
import com.tcc.luis.poster.helpers.ProgressHelper;
import com.tcc.luis.poster.helpers.SnackBarHelper;
import com.tcc.luis.poster.model.Curriculo;
import com.tcc.luis.poster.model.Usuario;
import com.tcc.luis.poster.usuario.activits.LoginActivity;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class FormularioCadastroUsuarioFragment extends Fragment {

    private static final String TAG = "FinalizarCadastroTAG";
    private ConstraintLayout mLayout;
    private TextInputLayout mInpEmail;
    private TextInputLayout mInpSenha;
    private TextInputLayout mInpName;
    private TextInputLayout mInpLastName;
    private TextView mTxtEmailFromGoogleUser;
    private ImageView mImgImageFromGoogleUser;
    private RelativeLayout mContainerGoogleUserLoged;
    private Usuario mUsuario;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private Switch mSwtCompanyRepresentative;
    private View v;
    private boolean isRepresentative;

    public FormularioCadastroUsuarioFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_formulario_cadastro_usuario, container, false);

        initView();

        mSwtCompanyRepresentative.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    isRepresentative = true;
                } else {
                    isRepresentative = false;
                }
            }
        });


        return v;
    }

    private void initView() {
        mInpEmail = v.findViewById(R.id.fragment_cadastrar_usuario_email);
        mInpName = v.findViewById(R.id.fragment_finalizar_cadastro_nome);
        mInpSenha = v.findViewById(R.id.fragment_finalizar_cadastro_senha);
        mInpLastName = v.findViewById(R.id.fragment_finalizar_cadastro_sobrenome);
        mLayout = v.findViewById(R.id.layout_form_cadastro);
        mContainerGoogleUserLoged = v.findViewById(R.id.layout_google_user_loged);
        mImgImageFromGoogleUser = v.findViewById(R.id.formulario_cadastro_image_google);
        mTxtEmailFromGoogleUser = v.findViewById(R.id.formulario_cadastro_email_google);
        mUsuario = new Usuario();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        mSwtCompanyRepresentative = v.findViewById(R.id.fragment_finalizar_cadastro_representative);

    }


    public void makeRegister(ProgressBar pb) {
        Usuario user = createUser();
        ProgressHelper.show(pb,getView(),true);

        if (user.isGoogleUser()) {
            saveFirebase(user, pb);
        } else {
            authWithEmailAndPassword(user, pb);
        }
    }

    private void authWithEmailAndPassword(final Usuario user, final ProgressBar pb) {
        String email = mInpEmail.getEditText().getText().toString();
        String senha = mInpSenha.getEditText().getText().toString();
        if(email.isEmpty() || senha.isEmpty()){
            SnackBarHelper.makeWithMensageOnly(mLayout,"Todos os campos são obrigatorios", true, getActivity());
        }
        firebaseAuth.createUserWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser firebaseUser = task.getResult().getUser();
                    user.setUidUser(firebaseUser.getUid());
                    user.setEmail(firebaseUser.getEmail());
                    saveFirebase(user, pb);
                } else {
                    ProgressHelper.show(pb,getView(),false);

                    try {
                        throw task.getException();
                    } catch (FirebaseAuthUserCollisionException e) {
                        Snackbar mensage = Snackbar.make(mLayout, "Email ja cadastrado em outra conta.", Snackbar.LENGTH_LONG);
                        mensage.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Snackbar mensage = Snackbar.make(mLayout, "Aconteceu um erro, tente novamente mais tarde :(", Snackbar.LENGTH_LONG);
                        mensage.show();
                    }
                }
            }
        });
    }

    private void saveFirebase(Usuario user, final ProgressBar pb) {
        HashMap<String, String> map = new HashMap<>();
        map.put("uidUsuario", firebaseAuth.getCurrentUser().getUid());
        map.put("profileImage", user.getProfileImage() != null ? user.getProfileImage() : "");

        if(!user.isCompanyRepresentative()){
            firebaseFirestore
                    .collection(Constantes.TABELA_DATABASE_CURRICULO)
                    .document(firebaseAuth.getCurrentUser().getUid())
                    .set(map);
        }
        firebaseFirestore.collection(Constantes.TABELA_DATABASE_USUARIO)
                .document(user.getUidUser())
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        ProgressHelper.show(pb,getView(),false);
                        Log.d(TAG, "DocumentSnapshot added with ID: ");
                        Snackbar snake = Snackbar.make(mLayout, "Usuario cadastrado com sucesso", Snackbar.LENGTH_INDEFINITE);
                        snake.setAction("OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent irLogin = new Intent(getActivity(), LoginActivity.class);
                                startActivity(irLogin);
                            }
                        }).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        ProgressHelper.show(pb,getView(),false);
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }


    private Usuario createUser() {
        mUsuario.setUidUser(mUsuario.getUidUser() != null ? mUsuario.getUidUser() : "");
        mUsuario.setProfileImage(mUsuario.getProfileImage() != null ? mUsuario.getProfileImage() : "");
        mUsuario.setName(mInpName.getEditText().getText().toString());
        mUsuario.setLastName(mInpLastName.getEditText().getText().toString());
        mUsuario.setCompanyRepresentative(isRepresentative);
        return this.mUsuario;
    }

    public void loadUserUIForGoogle(Usuario user) {

        if (user != null) {
            if (user.isGoogleUser()) {

                mUsuario = user;
                mInpEmail.setVisibility(View.GONE);
                mInpSenha.setVisibility(View.GONE);
                mTxtEmailFromGoogleUser.setText(user.getEmail());
                Picasso.with(getContext()).load(user.getProfileImage()).resize(80, 80)
                        .into(mImgImageFromGoogleUser, new Callback() {
                            @Override
                            public void onSuccess() {
                                Bitmap imageBitmap = ((BitmapDrawable) mImgImageFromGoogleUser.getDrawable()).getBitmap();
                                RoundedBitmapDrawable imageDrawable = ImageHelper.makeRoundDrawer(imageBitmap, getResources());
                                mImgImageFromGoogleUser.setImageDrawable(imageDrawable);
                            }

                            @Override
                            public void onError() {
                                mImgImageFromGoogleUser.setVisibility(View.GONE);
                            }
                        });
                ;
                mContainerGoogleUserLoged.setVisibility(View.VISIBLE);
            }
        }
    }


}

