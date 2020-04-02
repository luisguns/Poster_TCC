package com.tcc.luis.poster.empresa;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.tcc.luis.poster.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainCompanyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_company);
        BottomNavigationView navView = findViewById(R.id.nav_view_company);
        initBottonNavigation(navView);
    }

    private void initBottonNavigation(BottomNavigationView navView) {
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_oportunidades, R.id.navigation_intereseds, R.id.navigation_my_company)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_company);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }


}
