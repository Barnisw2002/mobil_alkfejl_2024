package com.example.kozmulejelento.CRUD;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

import com.example.kozmulejelento.MainBelepes;
import com.example.kozmulejelento.Menu;
import com.example.kozmulejelento.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class Modositas extends AppCompatActivity {
    private static final int TITKOS_KULCS = 99;
    private EditText mOraAzonositoEdit, mOraAllasEdit;
    private Button mSaveButton;
    private FirebaseFirestore mFirestore;
    private String documentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modositas);

        mOraAzonositoEdit = findViewById(R.id.editOraAzonosito);
        mOraAllasEdit = findViewById(R.id.editOraAllas);
        mSaveButton = findViewById(R.id.saveButton);
        mFirestore = FirebaseFirestore.getInstance();

        // Get the data passed from ListazasEsModositas Activity
        Intent intent = getIntent();
        documentId = intent.getStringExtra("DOCUMENT_ID");
        String initialOraAzonosito = intent.getStringExtra("ORA_AZONOSITO");
        String initialOraAllas = intent.getStringExtra("ORA_ALLAS");

        // Set the EditText fields with the data
        mOraAzonositoEdit.setText(initialOraAzonosito);
        mOraAllasEdit.setText(initialOraAllas);

        mSaveButton.setOnClickListener(v -> saveModifications());
    }

    private void saveModifications() {
        String newOraAzonosito = mOraAzonositoEdit.getText().toString().trim();
        String newOraAllas = mOraAllasEdit.getText().toString().trim();

        mFirestore.collection("OraJelentesek").document(documentId)
                .update("oraAzonosito", newOraAzonosito, "oraAllas", newOraAllas)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Modifications saved.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, Menu.class);
                    intent.putExtra("TITKOS_KULCS", TITKOS_KULCS);
                    startActivity(intent);
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error saving modifications: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
