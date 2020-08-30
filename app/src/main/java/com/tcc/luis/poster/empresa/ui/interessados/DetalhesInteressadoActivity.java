package com.tcc.luis.poster.empresa.ui.interessados;

import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.tcc.luis.poster.R;
import com.tcc.luis.poster.helpers.Constantes;
import com.tcc.luis.poster.interessado.ui.meu_perfil.MeuPerfilFragment;

public class DetalhesInteressadoActivity extends AppCompatActivity {
    private FrameLayout mContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_interessado);
        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String keyInteressado = getIntent().getExtras().getString(Constantes.INTERESSADO_KEY);

        if (keyInteressado != null) {
            MeuPerfilFragment fr = new MeuPerfilFragment();
            Bundle bd = new Bundle();
            bd.putString(Constantes.INTERESSADO_KEY,keyInteressado);
            fr.setArguments(bd);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.container_detalhes_interessado, fr);
            transaction.addToBackStack(null);
            transaction.commit();
        } else {
            finish();
        }


    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
