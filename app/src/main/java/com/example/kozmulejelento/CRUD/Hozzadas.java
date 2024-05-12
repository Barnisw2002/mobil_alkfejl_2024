package com.example.kozmulejelento.CRUD;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.kozmulejelento.Menu;
import com.example.kozmulejelento.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.example.kozmulejelento.model.VizOraJelentes;

public class Hozzadas extends AppCompatActivity {
    private static final int TITKOS_KULCS = 99;
    private static final String LOG_TAG = Hozzadas.class.getSimpleName();
    private EditText oraAllasMezo;
    private EditText oraAzonositoMezo;
    private FirebaseUser aktualisFelhasznalo;
    private CollectionReference oraJelentesek;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hozzad);

        inicializalas();
    }

    private void inicializalas() {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        oraJelentesek = firestore.collection("OraJelentesek");
        aktualisFelhasznalo = FirebaseAuth.getInstance().getCurrentUser();

        oraAllasMezo = findViewById(R.id.oraAllas);
        oraAzonositoMezo = findViewById(R.id.oraAzonosito);
    }

    public void adatRogzites(View view) {
        String oraAzonosito = oraAzonositoMezo.getText().toString().trim();
        String oraAllas = oraAllasMezo.getText().toString().trim();

        if (oraAzonosito.isEmpty() || oraAllas.isEmpty()) {
            Toast.makeText(this, "Az óra azonosítója és állása megadása kötelező!", Toast.LENGTH_LONG).show();
            return;
        }

        VizOraJelentes ujJelentes = new VizOraJelentes(aktualisFelhasznalo.getEmail(), oraAzonosito, oraAllas, maiDatum());
        oraJelentesek.add(ujJelentes)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Sikeres adatrögzítés!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, Menu.class);
                    intent.putExtra("TITKOS_KULCS", TITKOS_KULCS);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    Log.e(LOG_TAG, "Hiba az adatrögzítés során: " + e.getMessage(), e);
                    Toast.makeText(this, "Adatrögzítés sikertelen!", Toast.LENGTH_LONG).show();
                });
    }

    private String maiDatum() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        return dateFormat.format(Calendar.getInstance().getTime());
    }
}
