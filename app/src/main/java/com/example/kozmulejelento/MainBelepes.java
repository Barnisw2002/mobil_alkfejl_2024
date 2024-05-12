package com.example.kozmulejelento;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainBelepes extends AppCompatActivity {
    private static final String LOG_TAG = MainBelepes.class.getSimpleName();
    private static final int TITKOS_KULCS = 99;

    private FirebaseAuth hitelesito;
    private EditText felhasznaloEmail;
    private EditText jelszo;

    @Override
    protected void onCreate(Bundle mentettAllapot) {
        super.onCreate(mentettAllapot);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.login);

        Animation zoomin = AnimationUtils.loadAnimation(this, R.anim.zoom_in);
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);

        hitelesito = FirebaseAuth.getInstance();
        felhasznaloEmail = findViewById(R.id.editTextUserEmail);
        jelszo = findViewById(R.id.editTextPassword);


        Button gomb1 = findViewById(R.id.login);
        Button gomb2 = findViewById(R.id.regist);

        felhasznaloEmail.startAnimation(fadeIn);
        jelszo.startAnimation(fadeIn);
        gomb1.startAnimation(zoomin);
        gomb2.startAnimation(zoomin);

    }

    public void bejelentkezes(View nezet) {
        String email = felhasznaloEmail.getText().toString().trim();
        String jelszoSzoveg = jelszo.getText().toString().trim();

        if (!validateInput(email, jelszoSzoveg)) {
            return; // Stop the function if validation fails
        }

        Log.i(LOG_TAG, "Bejelentkezés: " + email);
        hitelesito.signInWithEmailAndPassword(email, jelszoSzoveg).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> feladat) {
                if (feladat.isSuccessful()) {
                    navigalFomenuhez();
                } else {
                    Toast.makeText(MainBelepes.this, "Hiba a bejelentkezés során", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private boolean validateInput(String email, String password) {
        if (email.isEmpty() || !email.contains("@")) {
            Toast.makeText(this, "Érvényes email cím szükséges!", Toast.LENGTH_LONG).show();
            return false;
        } else if (password.isEmpty() || password.length() < 6) {
            Toast.makeText(this, "A jelszó legalább 6 karakter hosszú kell legyen!", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void navigalFomenuhez() {
        Log.i(LOG_TAG, "Navigálás a főmenühöz");
        Intent intent = new Intent(this, Menu.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("TITKOS_KULCS", TITKOS_KULCS);
        startActivity(intent);
    }

    public void regisztracio(View nezet) {
        Intent intent = new Intent(this, Regisztracio.class);
        intent.putExtra("TITKOS_KULCS", TITKOS_KULCS);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        jelszo.setText("");
    }
}
