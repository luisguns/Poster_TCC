package com.tcc.luis.poster.interessado.ui.meu_perfil.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tcc.luis.poster.R;
import com.tcc.luis.poster.helpers.Constantes;
import com.tcc.luis.poster.helpers.SnackBarHelper;

public class EditarSobreInteressadoActivity extends AppCompatActivity {

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private ConstraintLayout mLayout;
    private TextInputLayout mInpSobre;
    private String TAG = "TagDebugEditarSobre";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_sobre_interessado);
        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Editar Sobre");
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        mInpSobre = findViewById(R.id.editar_sobre_sobre_voce);
        mLayout = findViewById(R.id.layout_activity_editar_sobre);


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
                saveAbout();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveAbout() {
        firebaseFirestore.collection(Constantes.TABELA_DATABASE_USUARIO).document(firebaseAuth.getCurrentUser().getUid()).update("sobre",mInpSobre.getEditText().getText().toString())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    SnackBarHelper.makeWithMensageAndAction(mLayout, "Sobre você editado com sucesso :D", "OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                        }
                    }, true, EditarSobreInteressadoActivity.this);
                } else {
                    try {

                    throw  task.getException();
                    } catch (Exception e ){
                        Log.d(TAG, "onComplete: " + e.getMessage());
                    }

                }
            }
        });
    }

    ;
}
