package com.example.kozmulejelento;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;

import com.example.kozmulejelento.CRUD.ListaMod;
import com.example.kozmulejelento.CRUD.ListazasEsTorles;
import com.example.kozmulejelento.CRUD.Hozzadas;
import com.google.firebase.auth.FirebaseAuth;

public class Menu extends AppCompatActivity {
    private static final int TITKOS_KULCS = 99;
    private FirebaseAuth hitelesito;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
        hitelesito = FirebaseAuth.getInstance();

        if (!felhasznaloEllenorzes() || !titkosKulcsEllenorzes()) {
            Toast.makeText(this, "Hozzáférés megtagadva: Kérjük, jelentkezzen be!", Toast.LENGTH_LONG).show();
            kijelentkezesEsBejelentkezes();
        }

        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.zoom_in);

        Button gomb1 = findViewById(R.id.adatmodosit);
        Button gomb2 = findViewById(R.id.kilep);
        Button gomb3 = findViewById(R.id.OraJelentes);
        Button gomb4 = findViewById(R.id.ujOraJelentes);

        gomb1.startAnimation(fadeIn);
        gomb2.startAnimation(fadeIn);
        gomb3.startAnimation(fadeIn);
        gomb4.startAnimation(fadeIn);

    }

    private boolean felhasznaloEllenorzes() {
        return hitelesito.getCurrentUser() != null;
    }

    private boolean titkosKulcsEllenorzes() {
        int kapottKulcs = getIntent().getIntExtra("TITKOS_KULCS", 0);
        return kapottKulcs == TITKOS_KULCS;
    }

    private void kijelentkezesEsBejelentkezes() {
        hitelesito.signOut();
        Intent intent = new Intent(this, MainBelepes.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    public void oraAllasokInit(View view) {
        startActivity(new Intent(this, ListazasEsTorles.class));
    }

    public void ujOraAllas(View view) {
        startActivity(new Intent(this, Hozzadas.class));
    }

    public void oraNullazas(View view) {
        startActivity(new Intent(this, ListaMod.class));
    }

    public void kijelentkezes(View view) {
        kijelentkezesEsBejelentkezes();
    }
}