package com.example.livelocation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.livelocation.Adapter.AddressAdapter;
import com.example.livelocation.Database.MapDatabase;

import java.time.LocalDate;
import java.util.ArrayList;

public class ShowAddressesList extends AppCompatActivity {

    RecyclerView recyclerView;

    MapDatabase database;

    ArrayList<Modal> arrayList;

    AddressAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_addresses_list);

        recyclerView = findViewById(R.id.recycler_id);
        recyclerView.setLayoutManager(new LinearLayoutManager(ShowAddressesList.this));

        database = new MapDatabase(ShowAddressesList.this);

        arrayList = new ArrayList<>();
        arrayList = database.get_Address();

        adapter = new AddressAdapter(this, arrayList);

        recyclerView.setAdapter(adapter);
    }
}