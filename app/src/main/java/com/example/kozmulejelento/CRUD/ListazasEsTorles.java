package com.example.kozmulejelento.CRUD;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.graphics.Color;
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
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

import com.example.kozmulejelento.model.VizOraJelentes;

public class ListazasEsTorles extends AppCompatActivity {
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
        setContentView(R.layout.listazas_es_torles);

        mItemsData = new ArrayList<>();
        mData = new ArrayList<>();
        mFirestore = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        mListView = findViewById(R.id.listOraszamok);
        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mData);
        mListView.setAdapter(mAdapter);

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
    }

    public void listazas(View view) {
        if (mData.isEmpty()) {
            Toast.makeText(this, "Nincs megjeleníthető adat", Toast.LENGTH_SHORT).show();
        } else {
            view.setBackgroundColor(Color.parseColor("#DD4242"));
            mListView.setOnItemClickListener((parent, view1, position, id) -> {
                if (position >= mItemsData.size()) {
                    Toast.makeText(this, "Érvénytelen elem kiválasztva", Toast.LENGTH_SHORT).show();
                    return;  // Prevents crashes by invalid index access
                }
                VizOraJelentes selectedReport = mItemsData.get(position);
                mFirestore.collection("OraJelentesek").document(selectedReport.getId())
                        .delete()
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(this, "Jelentés törölve: " + selectedReport.getOraAzonosito(), Toast.LENGTH_SHORT).show();
                            mData.remove(position);  // Remove the deleted item from the data list
                            mItemsData.remove(position);  // Remove from the item details list
                            mAdapter.notifyDataSetChanged();  // Notify the adapter to refresh the list
                        })
                        .addOnFailureListener(e -> Toast.makeText(this, "Hiba a törlés során", Toast.LENGTH_SHORT).show());
            });
        }
    }

    public void vissza(View view) {
        Intent intent = new Intent(this, Menu.class);
        intent.putExtra("TITKOS_KULCS", TITKOS_KULCS);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}