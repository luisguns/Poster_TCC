package com.tcc.luis.poster.usuario.fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.tcc.luis.poster.R;
import com.tcc.luis.poster.helpers.Constantes;
import com.tcc.luis.poster.helpers.ProgressHelper;
import com.tcc.luis.poster.model.Usuario;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CadastrarUsuarioFragment extends Fragment {
    private static final int GOOGLE_SIN = 123;
    String TAG = "myTag";
    FirebaseAuth mAuth;
    SignInButton cadastrarGoole;
    private GoogleSignInClient mGoogleSignInClient;
    private FormularioCadastroUsuarioFragment fragmentCad;
    private Button mBtnCadastrar;
    private ProgressBar progressBar;
    private FirebaseFirestore firebaseFirestore;
    private View v;


    public CadastrarUsuarioFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_cadastrar_usuario, container, false);
        firebaseFirestore = FirebaseFirestore.getInstance();
        loadUI(v);
        mBtnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick:  entrei pow");
                fragmentCad.makeRegister(progressBar);
            }
        });
        cadastrarGoole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initGoogleAuth();
                sigInGoogle();
            }
        });
        loadFragment();
        return v;
    }

    private void loadFragment() {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.container_cad, fragmentCad);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void initGoogleAuth() {
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder()
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), googleSignInOptions);
    }

    private void loadUI(View v) {
        cadastrarGoole = v.findViewById(R.id.btn_google_signin_cadastrar_interessado);
        progressBar = v.findViewById(R.id.progressbar_cyclic_cadastro_usuario);
        mBtnCadastrar = v.findViewById(R.id.fragment_cadastrar_interessado_cadastrar);
        mAuth = FirebaseAuth.getInstance();
        this.setGooglePlusButtonText(cadastrarGoole, "Cadastrar com google");
        fragmentCad = new FormularioCadastroUsuarioFragment();
    }

    void sigInGoogle(){
        Intent siginIntent = mGoogleSignInClient.getSignInIntent();
        ProgressHelper.show(progressBar,fragmentCad.getView(),true);
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
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
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
                ProgressHelper.show(progressBar,fragmentCad.getView(),false);

                Log.d(TAG, "FirebaseAuthSucess: " );
                FirebaseUser user = mAuth.getCurrentUser();
                completeAuthFirebaseGoogle(user);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                ProgressHelper.show(progressBar,fragmentCad.getView(),false);

                Log.e(TAG, "FirebaseAuthFailure: " + e.getMessage());
            }
        });


    }

    private void completeAuthFirebaseGoogle(FirebaseUser user) {
        Usuario myUser = new Usuario();
        myUser.setEmail(user.getEmail());
        myUser.setProfileImage(String.valueOf(user.getPhotoUrl()));
        myUser.setUidUser(user.getUid());
        myUser.setGoogleUser(true);
        verifyUserExist(myUser);
    }

    private boolean verifyUserExist(final Usuario user) {
        final boolean[] isExist = new boolean[1];
        firebaseFirestore.collection(Constantes.TABELA_DATABASE_USUARIO)
                .whereEqualTo("uidUser", user.getUidUser())
                .get()
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: " + e.getMessage());
                    }
                })
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<DocumentSnapshot> documents = task.getResult().getDocuments();
                            if(documents.size() ==0){
                                fragmentCad.loadUserUIForGoogle(user);
                            } else {
                                final Snackbar snackbar = Snackbar.make(v,"Conta do google ja vinculada a uma conta", BaseTransientBottomBar.LENGTH_INDEFINITE);
                                snackbar.setAction("OK", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        snackbar.dismiss();
                                    }
                                });
                                snackbar.show();
                            }
                        } else {
                            Exception exception = task.getException();
                            Log.d(TAG, "onComplete: " + exception.getMessage());
                        }
                    }
                });
        return isExist[0];
    }
}
