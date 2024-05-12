package com.example.kozmulejelento.CRUD;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.kozmulejelento.Menu;
import com.example.kozmulejelento.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

import com.example.kozmulejelento.model.VizOraJelentes;

public class ListaMod extends AppCompatActivity {
    private static final int TITKOS_KULCS = 99;
    private FirebaseFirestore mFirestore;
    private ArrayList<VizOraJelentes> mItemsData;
    private ListView mListView;
    private ArrayAdapter<String> mAdapter;
    private ArrayList<String> mData;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listazas_es_modositas);
        mFirestore = FirebaseFirestore.getInstance();
        mItemsData = new ArrayList<>();
        mData = new ArrayList<>();
        mListView = findViewById(R.id.listOraszamok);
        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mData);
        mListView.setAdapter(mAdapter);
        user = FirebaseAuth.getInstance().getCurrentUser();
        queryData();
    }
    private void queryData() {
        mFirestore.collection("OraJelentesek")
                .orderBy("datum")
                .limit(30)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        VizOraJelentes item = document.toObject(VizOraJelentes.class);
                        if (item.getUserID().equals(user.getEmail())) {
                            item.setId(document.getId());
                            mData.add(item.toString());
                            mItemsData.add(item);
                        }
                    }
                    mAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error loading documents: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        setupListViewClickListener();
    }

    private void setupListViewClickListener() {
        mListView.setOnItemClickListener((parent, view, position, id) -> {
            if (position >= mItemsData.size()) {
                Toast.makeText(this, "Nincs adat ezen a pozíción", Toast.LENGTH_SHORT).show();
                return;  // Korai visszatérés: megakadályozza a további kód futtatását, ha nincs adat
            }
            VizOraJelentes selectedReport = mItemsData.get(position);
            if (selectedReport != null) {  // Ellenőrizzük, hogy a kiválasztott jelentés nem null-e
                Intent modIntent = new Intent(ListaMod.this, Modositas.class);
                modIntent.putExtra("DOCUMENT_ID", selectedReport.getId());
                modIntent.putExtra("ORA_AZONOSITO", selectedReport.getOraAzonosito());
                modIntent.putExtra("ORA_ALLAS", selectedReport.getOraAllas());
                startActivity(modIntent);
            } else {
                Toast.makeText(this, "Hiba a jelentés kiválasztásakor", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void vissza(View view) {
        Intent intent = new Intent(this, Menu.class);
        intent.putExtra("TITKOS_KULCS", TITKOS_KULCS);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}