package com.example.kozmulejelento;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Regisztracio extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final int TITKOS_KULCS = 99;

    private EditText emailMezo;
    private EditText nevMezo;
    private EditText ugyfelszamMezo;
    private EditText jelszoMezo;
    private Spinner tipusSpinner;

    private FirebaseAuth hitelesito;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.regisztracio);

        if (!ellenoTitkosKulcsot()) {
            finish();
            return;
        }

        inicializalas();
        hitelesito = FirebaseAuth.getInstance();
    }

    private boolean ellenoTitkosKulcsot() {
        int kapottKulcs = getIntent().getIntExtra("TITKOS_KULCS", 0);
        return kapottKulcs == TITKOS_KULCS;
    }

    private boolean ellenorzes() {
        String email = emailMezo.getText().toString().trim();
        String nev = nevMezo.getText().toString().trim();
        String ugyfelszam = ugyfelszamMezo.getText().toString().trim();
        String jelszo = jelszoMezo.getText().toString().trim();

        if (!email.contains("@")) {
            Toast.makeText(this, "Kérem érvényes email címet adjon meg!", Toast.LENGTH_LONG).show();
            return false;
        } else if (nev.length() < 3) {
            Toast.makeText(this, "A név legalább 3 karakter hosszú kell legyen!", Toast.LENGTH_LONG).show();
            return false;
        } else if (ugyfelszam.length() < 6) {
            Toast.makeText(this, "Az ügyfélszám legalább 6 számjegyből kell álljon!", Toast.LENGTH_LONG).show();
            return false;
        } else if (jelszo.length() < 6) {
            Toast.makeText(this, "A jelszó legalább 6 karakter hosszú kell legyen!", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void inicializalas() {
        emailMezo = findViewById(R.id.registEmailText);
        nevMezo = findViewById(R.id.registNameText);
        ugyfelszamMezo = findViewById(R.id.registNumberText);
        jelszoMezo = findViewById(R.id.registPassText);
        tipusSpinner = findViewById(R.id.elofizetesTipus);

        TextView nev = findViewById(R.id.registLable);
        Button gomb1 = findViewById(R.id.registOkButton);
        Button gomb2 = findViewById(R.id.registCancelButton);

        Animation zoomin = AnimationUtils.loadAnimation(this, R.anim.zoom_in);
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);

        nev.startAnimation(fadeIn);
        emailMezo.startAnimation(fadeIn);
        nevMezo.startAnimation(fadeIn);
        ugyfelszamMezo.startAnimation(fadeIn);
        jelszoMezo.startAnimation(fadeIn);
        tipusSpinner.startAnimation(fadeIn);
        gomb1.startAnimation(zoomin);
        gomb2.startAnimation(zoomin);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.ugyfeltipus, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tipusSpinner.setAdapter(adapter);
        tipusSpinner.setOnItemSelectedListener(this);

        // Set input types
        ugyfelszamMezo.setInputType(InputType.TYPE_CLASS_NUMBER);
    }

    public void megszakitas(View nezet) {
        Intent intent = new Intent(this, MainBelepes.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("TITKOS_KULCS", TITKOS_KULCS);
        startActivity(intent);
    }

    public void regisztral(View nezet) {
        if (ellenorzes()) {
            hitelesito.createUserWithEmailAndPassword(emailMezo.getText().toString().trim(), jelszoMezo.getText().toString().trim())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                navigalFomenuhez();
                            } else {
                                Toast.makeText(Regisztracio.this, "Regisztráció sikertelen: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    }

    private void navigalFomenuhez() {
        Intent intent = new Intent(this, Menu.class);
        intent.putExtra("TITKOS_KULCS", TITKOS_KULCS);
        startActivity(intent);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    @Override
    protected void onStop() {
        super.onStop();
        jelszoMezo.setText("");
    }
}
