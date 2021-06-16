package com.example.deliveryboy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.deliveryboy.Adapters.OrderViewAdapter;
import com.example.deliveryboy.PojoClasses.OrderDetails;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Orders extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseReference fireBaseRealtimeDatabase;
    private OrderDetails orderDetails;
    private List<OrderDetails> orderList;
    private OrderViewAdapter orderViewAdapter;
    private List<String> orderKeys , dishKeys;
    private LinearProgressIndicator linearProgressIndicator;
    private ChildEventListener childEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        setContentView(R.layout.activity_orders);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView = findViewById(R.id.ordersView);
        recyclerView.setLayoutManager(layoutManager);
        orderList = new ArrayList<>();
        orderKeys = new ArrayList<>();
        dishKeys = new ArrayList<>();
        fireBaseRealtimeDatabase = FirebaseDatabase.getInstance().getReference().child("Orders");
        linearProgressIndicator=findViewById(R.id.orderLoadProgress);
        linearProgressIndicator.setVisibility(View.INVISIBLE);
        orderViewAdapter = new OrderViewAdapter(getApplicationContext(), orderList , orderKeys);
        recyclerView.setAdapter(orderViewAdapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("KEPLER", "On Start Called");
        linearProgressIndicator.setVisibility(View.VISIBLE);
        fireBaseRealtimeDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                OrderDetails orderDetails = snapshot.getValue(OrderDetails.class);
                orderList.add(orderDetails);
                orderKeys.add(snapshot.getKey());
                Collections.reverse(orderList);
                Collections.reverse(orderKeys);
                orderViewAdapter.notifyDataSetChanged();
                linearProgressIndicator.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {


            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("KEPLER", "On Pause Called");

    }
}








