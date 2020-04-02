package com.tcc.luis.poster.usuario.activits;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.tcc.luis.poster.R;
import com.tcc.luis.poster.usuario.fragments.CadastrarUsuarioFragment;

public class CadastrarUsuarioActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_usuario);

        CadastrarUsuarioFragment fr = new CadastrarUsuarioFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.cadastrar_interessado_container, fr);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
       this.finish();
    }
}
