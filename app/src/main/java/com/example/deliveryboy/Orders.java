package com.example.deliveryboy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import com.example.deliveryboy.Adapters.OrderViewAdapter;
import com.example.deliveryboy.PojoClasses.OrderDetails;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
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
    private SwitchMaterial switchMaterial;
    private Intent serviceIntent;

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
        fireBaseRealtimeDatabase = FirebaseDatabase.getInstance().getReference().child("Notification");
        linearProgressIndicator=findViewById(R.id.orderLoadProgress);
        linearProgressIndicator.setVisibility(View.INVISIBLE);
        orderViewAdapter = new OrderViewAdapter(getApplicationContext(), orderList , orderKeys);
        recyclerView.setAdapter(orderViewAdapter);
        switchMaterial=findViewById(R.id.switch1);
        retrieveOrders();
    }

    private void retrieveOrders() {
        linearProgressIndicator.setVisibility(View.VISIBLE);
        fireBaseRealtimeDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                
                linearProgressIndicator.setVisibility(View.INVISIBLE);
                OrderDetails orderDetails = snapshot.getValue(OrderDetails.class);
                orderList.add(0,orderDetails);
                orderKeys.add(0,snapshot.getKey());
                orderViewAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                orderList.set(orderKeys.indexOf(snapshot.getKey()),snapshot.getValue(OrderDetails.class));
                orderViewAdapter.notifyDataSetChanged();
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
    protected void onStart() {
        super.onStart();
        serviceIntent=new Intent(Orders.this,NotifyService.class);
        serviceIntent.putExtra("title" , "this is the title");
        serviceIntent.putExtra("Message" , "this is the message");
        switchMaterial.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    startService(serviceIntent);
                }else{
                    stopService(serviceIntent);
                }
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("KEPLER", "On Pause Called");

    }
}








