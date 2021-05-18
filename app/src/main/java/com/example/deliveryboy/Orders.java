package com.example.deliveryboy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.deliveryboy.PojoClasses.OrderDetails;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Orders extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseReference fireBaseRealtimeDatabase;
    private OrderDetails orderDetails;
    private List<OrderDetails> orderList;
    private OrderViewAdapter orderViewAdapter;
    private List<String> orderKeys;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView = findViewById(R.id.ordersView);
        recyclerView.setLayoutManager(layoutManager);

        orderList = new ArrayList<>();
        orderKeys = new ArrayList<>();

        fireBaseRealtimeDatabase = FirebaseDatabase.getInstance().getReference().child("Orders");

     

        fireBaseRealtimeDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

               OrderDetails orderDetails = snapshot.getValue(OrderDetails.class);
               orderList.add(orderDetails);
               orderKeys.add(snapshot.getKey());
                orderViewAdapter = new OrderViewAdapter(getApplicationContext() , orderList);
                recyclerView.setAdapter(orderViewAdapter);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                OrderDetails changedOrderDetails = snapshot.getValue(OrderDetails.class);
                int pos = orderKeys.indexOf(snapshot.getKey());
                orderList.remove(pos);
                orderList.add(changedOrderDetails);
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


}