package com.example.deliveryboy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.deliveryboy.Adapters.OrderViewAdapter;
import com.example.deliveryboy.PojoClasses.OrderDetails;
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
    private CustomAlertDialog customAlertDialog;
    private AlertDialog alertDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView = findViewById(R.id.ordersView);
        recyclerView.setLayoutManager(layoutManager);
        customAlertDialog = new CustomAlertDialog(Orders.this , getApplicationContext().getResources().getString(R.string.orderStatus));

        orderList = new ArrayList<>();
        orderKeys = new ArrayList<>();
        dishKeys = new ArrayList<>();

        fireBaseRealtimeDatabase = FirebaseDatabase.getInstance().getReference().child("Orders");


        alertDialog = customAlertDialog.showAlertDialog();
        fireBaseRealtimeDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                OrderDetails orderDetails = snapshot.getValue(OrderDetails.class);
                orderList.add(orderDetails);
                orderKeys.add(snapshot.getKey());
                Collections.reverse(orderList);
                Collections.reverse(orderKeys);
                alertDialog.dismiss();
                orderViewAdapter = new OrderViewAdapter(getApplicationContext(), orderList , orderKeys);
                recyclerView.setAdapter(orderViewAdapter);
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




}








