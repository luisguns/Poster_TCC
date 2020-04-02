package com.tcc.luis.poster.usuario.activits;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.tcc.luis.poster.interessado.InteressadoMainActivity;
import com.tcc.luis.poster.interessado.MainActivity;
import com.tcc.luis.poster.R;
import com.tcc.luis.poster.empresa.CompleteRegisterCompanyActivity;
import com.tcc.luis.poster.empresa.MainCompanyActivity;
import com.tcc.luis.poster.helpers.Constantes;
import com.tcc.luis.poster.helpers.SnackBarHelper;
import com.tcc.luis.poster.model.Usuario;

import java.util.List;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int GOOGLE_SIN = 1234;
    private static final String TAG = "LoginActivityTAG";
    FirebaseAuth mAuth;
    FirebaseFirestore mDatabase;



    private SignInButton logarGoogle;
    private Button mBtnCadastrar;
    private Button mBtnLogar;
    private TextInputLayout mInpEmail;
    private TextInputLayout mInpSenha;
    private ProgressBar mProgress;
    private ConstraintLayout mLayout;

    private GoogleSignInClient mGoogleSignInClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseFirestore.getInstance();
        logarGoogle = findViewById(R.id.btn_google_signin_login);
        mBtnCadastrar = findViewById(R.id.login_activity_cadastrar);
        mBtnLogar = findViewById(R.id.login_activity_btn_logar);
        mInpEmail = findViewById(R.id.login_activity_email);
        mInpSenha = findViewById(R.id.login_activity_senha);
        mProgress = findViewById(R.id.progressBar_cyclic_login);
        mLayout = findViewById(R.id.layout_activity_login);


        setGooglePlusButtonText(logarGoogle, "Logar com google");

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
           completeAuthFirebase(currentUser);
        }
        mBtnCadastrar.setOnClickListener(this);
        logarGoogle.setOnClickListener(this);
        mBtnLogar.setOnClickListener(this);




    }

    void sigInGoogle(){
        Intent siginIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(siginIntent, GOOGLE_SIN);
    }

    protected void setGooglePlusButtonText(SignInButton signInButton, String buttonText) {
        // Find the TextView that is inside of the SignInButton and set its text
        for (int i = 0; i < signInButton.getChildCount(); i++) {
            View v = signInButton.getChildAt(i);

            if (v instanceof TextView) {
                TextView tv = (TextView) v;
                tv.setText(buttonText);
                return;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GOOGLE_SIN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if(account != null){
                    firebaseAuthWithGoogle(account);
                }
            } catch (ApiException e){
                e.printStackTrace();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        Log.d(TAG, "FirebaseAuth: " + account.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Log.d(TAG, "FirebaseAuthSucess: " );
                        FirebaseUser user = mAuth.getCurrentUser();
                        completeAuthFirebase(user);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "FirebaseAuthFailure: " + e.getMessage());
            }
        });


    }

    private void completeAuthFirebase(FirebaseUser user) {
        String uid = user.getUid();
        CollectionReference usuarioReference = mDatabase.collection(Constantes.TABELA_DATABASE_USUARIO);
        Query query = usuarioReference.whereEqualTo("uidUser", uid);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<DocumentSnapshot> documents = task.getResult().getDocuments();
                    if(documents.size() > 0){
                    Usuario usuario = documents.get(0).toObject(Usuario.class);
                    autenticate(usuario);
                    } else {
                        SnackBarHelper.makeWithMensageOnly(mLayout, "Usuario não cadastrado", true, LoginActivity.this);
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });


    }

    private void autenticate(Usuario usuario) {
        Intent intent;
        if (usuario.isCompanyRepresentative()) {
            if(usuario.isCompanyCompleteRegister()){
            intent = new Intent(LoginActivity.this, MainCompanyActivity.class);
            } else {
                intent = new Intent(LoginActivity.this, CompleteRegisterCompanyActivity.class);
            }
        } else {
            intent = new Intent(LoginActivity.this, InteressadoMainActivity.class);
        }

        startActivity(intent);
    }
    private void authenticateWithEmailAndPassword() {

        String email = mInpEmail.getEditText().getText().toString();
        String senha = mInpSenha.getEditText().getText().toString();

        mAuth.signInWithEmailAndPassword(email,senha)
        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    SnackBarHelper.makeWithMensageOnly(mLayout,"Logado com sucesso",true, LoginActivity.this);
                    completeAuthFirebase(task.getResult().getUser());

                } else {
                    try {
                        throw task.getException();
                    }
                    catch (FirebaseAuthInvalidUserException e){
                        SnackBarHelper.makeWithMensageOnly(mLayout,"Nenhum usuario encontrado com esse email",true, LoginActivity.this);
                    }
                    catch (FirebaseAuthInvalidCredentialsException e){
                        SnackBarHelper.makeWithMensageOnly(mLayout,"Credenciais incorretas!",true, LoginActivity.this);

                    }
                    catch (Exception e) {
                        e.printStackTrace();
                        SnackBarHelper.makeWithMensageOnly(mLayout,e.getMessage(),true, LoginActivity.this);
                        Log.e(TAG, "onComplete: " + e.getMessage());
                    }

                }
            }
        })
        ;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_activity_cadastrar:
                Intent irCadastro = new Intent(LoginActivity.this, CadastrarUsuarioActivity.class);
                startActivity(irCadastro);
                break;
            case R.id.btn_google_signin_login:
                GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder()
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build();

                mGoogleSignInClient = GoogleSignIn.getClient(LoginActivity.this, googleSignInOptions);
                sigInGoogle();
                break;
            case R.id.login_activity_btn_logar:
                ((InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                        mInpSenha.getWindowToken(), 0);
                ((InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                        mInpEmail.getWindowToken(), 0);
                authenticateWithEmailAndPassword();
                break;
        }
    }


}
