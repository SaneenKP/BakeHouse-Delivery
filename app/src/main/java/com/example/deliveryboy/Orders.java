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
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.example.deliveryboy.Adapters.OrderViewAdapter;
import com.example.deliveryboy.PojoClasses.DeliveryBoyDetails;
import com.example.deliveryboy.PojoClasses.OrderDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
    private SharedPreferenceConfig sharedPreferenceConfig;

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
        sharedPreferenceConfig = new SharedPreferenceConfig(getApplicationContext());
        fireBaseRealtimeDatabase = FirebaseDatabase.getInstance().getReference().child("Orders");
        linearProgressIndicator=findViewById(R.id.orderLoadProgress);
        linearProgressIndicator.setVisibility(View.INVISIBLE);
        orderViewAdapter = new OrderViewAdapter(Orders.this, orderList, orderKeys, new SetDeliveryBoyInterface() {
            @Override
            public void setDeliveryBoy(String OrderKey , CheckBox assign) {
                 setDeliveryBoyDetails(OrderKey , assign);
            }

            @Override
            public boolean checkDeliveryBoyDetails() {
                return checkDetails();
            }
        });
        recyclerView.setAdapter(orderViewAdapter);
        switchMaterial=findViewById(R.id.switch1);
        deliveryBoy_name = findViewById(R.id.deliveryboy_name);
        deliveryBoy_no = findViewById(R.id.phoneNumber_deliveryboy);
        retrieveOrders();
    }



    private void setDeliveryBoyDetails(String orderKey , CheckBox assign){

        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference ordersDatabase = FirebaseDatabase.getInstance().getReference().child(getApplicationContext().getResources().getString(R.string.OrderNode)).child(orderKey);

        DeliveryBoyDetails deliveryBoyDetails = new DeliveryBoyDetails();
        deliveryBoyDetails.setName(deliveryBoy_name.getText().toString());
        deliveryBoyDetails.setNumber(deliveryBoy_no.getText().toString());
        sharedPreferenceConfig.writeDeliveryBoyDetails(deliveryBoyDetails);

        DatabaseReference deliveryBoyDatabase = FirebaseDatabase.getInstance().getReference().child(getApplicationContext().getResources().getString(R.string.DeliveryBoyNode));

        deliveryBoyDatabase.child(userID).setValue(deliveryBoyDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull  Task<Void> task) {

                if (task.isSuccessful()){

                    ordersDatabase.child(getApplicationContext().getResources().getString(R.string.DeliveryBoyNode)).setValue(userID).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull  Task<Void> task) {

                            if (task.isSuccessful()){

                                if (task.isSuccessful()){

                                    ordersDatabase.child("assigned").setValue("yes").addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull  Task<Void> task) {
                                            if (task.isSuccessful()){
                                                assign.setChecked(true);
                                                assign.setClickable(false);
                                                Toast.makeText(getApplicationContext() , "Order Assigned",Toast.LENGTH_SHORT).show();

                                            }else{
                                                assign.setChecked(false);
                                                Toast.makeText(getApplicationContext() , "Failed  "+ task.getException(),Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                                }else{
                                    assign.setChecked(false);
                                    Toast.makeText(getApplicationContext() , "Failed  "+ task.getException(),Toast.LENGTH_SHORT).show();
                                }

                            }
                            else{
                                Toast.makeText(getApplicationContext() , "Failed  to assign delivery boy"+ task.getException(),Toast.LENGTH_SHORT).show();

                            }

                        }

                    });

                }else{
                    Toast.makeText(getApplicationContext() , "Failed  to set delivery boy data"+ task.getException(),Toast.LENGTH_SHORT).show();
                }

            }
        });

     }



    private boolean checkDetails(){

        boolean status = !TextUtils.isEmpty(deliveryBoy_name.getText().toString()) && !TextUtils.isEmpty(deliveryBoy_no.getText().toString()) && (deliveryBoy_no.getText().toString().length() == 10);
        if (!status)
            Toast.makeText(getApplicationContext(), "Name and Number field should not be empty", Toast.LENGTH_SHORT).show();
        return status;
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


    public String getLatestOnlineTime(){
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        return timeFormat.format(Calendar.getInstance().getTime());
    }

    public String getLatestOnlineDate(){
        DateFormat timeFormat = new SimpleDateFormat("dd/MM/yyyy");
        return timeFormat.format(Calendar.getInstance().getTime());

    }

    @Override
    protected void onStart() {
        super.onStart();

        sharedPreferenceConfig.writeLatestOnlineTime(getLatestOnlineTime());
        sharedPreferenceConfig.writeLatestOnlineDate(getLatestOnlineDate());

        if (sharedPreferenceConfig.readNotificationStatus()){
            switchMaterial.setChecked(true);
        }
        if (!sharedPreferenceConfig.readDeliveryBoyName().equals("")){
            deliveryBoy_name.setText(sharedPreferenceConfig.readDeliveryBoyName());
        }
        if (!sharedPreferenceConfig.readDeliveryBoyNumber().equals("")){
            deliveryBoy_no.setText(sharedPreferenceConfig.readDeliveryBoyNumber());
        }

        serviceIntent=new Intent(Orders.this,NotifyService.class);


        switchMaterial.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (checkDetails()){

                    if(isChecked){
                        sharedPreferenceConfig.writeNotificationStatus(true);
                        startService(serviceIntent);
                    }else{
                        sharedPreferenceConfig.writeNotificationStatus(false);
                        stopService(serviceIntent);
                    }

                }else{
                    switchMaterial.setChecked(false);
                }

            }
        });

    }


}








