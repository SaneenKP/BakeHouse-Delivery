package com.example.deliveryboy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

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
    private EditText deliveryBoy_name , deliveryBoy_no;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

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
        sharedPreferences = getSharedPreferences("deliveryNotificationStatus" , Context.MODE_PRIVATE);
        fireBaseRealtimeDatabase = FirebaseDatabase.getInstance().getReference().child("Orders");
        linearProgressIndicator=findViewById(R.id.orderLoadProgress);
        linearProgressIndicator.setVisibility(View.INVISIBLE);
        orderViewAdapter = new OrderViewAdapter(getApplicationContext(), orderList , orderKeys);
        recyclerView.setAdapter(orderViewAdapter);
        switchMaterial=findViewById(R.id.switch1);
        deliveryBoy_name = findViewById(R.id.deliveryboy_name);
        deliveryBoy_no = findViewById(R.id.phoneNumber_deliveryboy);
        retrieveOrders();
        retrieveNotification();
    }

    private void retrieveNotification(){

        DatabaseReference firebaseNotificationDatabase = FirebaseDatabase.getInstance().getReference().child("Notification");
        firebaseNotificationDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable  String previousChildName) {
                String assigned = snapshot.child("assigned").getValue(String.class);
                if (assigned.equals("yes")){
                    serviceIntent.putExtra("title" , snapshot.child("name").getValue(String.class));
                    serviceIntent.putExtra("Message" , snapshot.child("location").getValue(String.class));
                }
            }

            @Override
            public void onChildChanged(@NonNull  DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull  DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull  DataSnapshot snapshot, @Nullable  String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull  DatabaseError error) {

            }
        });
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

        if (sharedPreferences.getBoolean("notification",false)){
            switchMaterial.setChecked(true);
        }

        serviceIntent=new Intent(Orders.this,NotifyService.class);


        switchMaterial.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (TextUtils.isEmpty(deliveryBoy_name.getText().toString()) || TextUtils.isEmpty(deliveryBoy_no.getText().toString())){
                    switchMaterial.setChecked(false);
                    Toast.makeText(getApplicationContext(), "Name and Number field should not be empty", Toast.LENGTH_SHORT).show();
                }else{
                    if (deliveryBoy_no.getText().toString().length() != 10){
                        switchMaterial.setChecked(false);
                        Toast.makeText(getApplicationContext(), "invalid phone number format", Toast.LENGTH_SHORT).show();
                    }else{
                        if(isChecked){
                            editor = sharedPreferences.edit();
                            editor.putBoolean("notification" , true);
                            startService(serviceIntent);
                        }else{
                            editor = sharedPreferences.edit();
                            editor.putBoolean("notification" , false);
                            stopService(serviceIntent);
                        }
                        editor.commit();
                    }

                }

            }
        });

    }


}








