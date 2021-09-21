package com.example.deliveryboy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.service.autofill.Dataset;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.deliveryboy.PojoClasses.OrderDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class orderDetailsDisplay extends AppCompatActivity {

    private TextView customerName , phoneNumber , paymentMethod , hotel , dish , price , time , houseno ,housename , landmark , street , date;
    private CheckBox placed , picked , delivered;
    private OrderDetails orderDetails;
    private RecyclerView DishOrderDetails;
    private RecyclerView.LayoutManager layoutManager;
    private String dishNames , orderKey;
    private MaterialTextView hotelAddress;
    private boolean isAssignedStatus = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        setContentView(R.layout.activity_order_details_display);
        dishNames = new String();
        customerName = findViewById(R.id.orderDetailName);
        phoneNumber = findViewById(R.id.orderDetailPhoneNumber);
        paymentMethod = findViewById(R.id.paymentMethod);
        hotel = findViewById(R.id.Hotel);
        price = findViewById(R.id.price);
        time = findViewById(R.id.time);
        houseno = findViewById(R.id.houseNo);
        housename = findViewById(R.id.houseName);
        dish = findViewById(R.id.DishName);
        landmark = findViewById(R.id.landmark);
        street = findViewById(R.id.street);
        date = findViewById(R.id.date);
        hotelAddress=findViewById(R.id.Hotel_Address);
        placed = findViewById(R.id.orderPlacedCheck);
        picked = findViewById(R.id.orderPickedCheck);
        delivered = findViewById(R.id.orderDeliveredCheck);

        Intent getOrderDetails = getIntent();
        orderDetails = getOrderDetails.getParcelableExtra("OrderDetails");
        orderKey = getOrderDetails.getStringExtra("OrderKey");
        isAssignedStatus = getOrderDetails.getBooleanExtra("isAssignedStatus" , false);

        setCheckBoxes(orderKey);
        setOrderDetails(orderDetails);
        getHotel(orderDetails.getHotelId());
        getDish(orderKey);

        placed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                DatabaseReference setOrderChanges = FirebaseDatabase.getInstance().getReference().child(getApplicationContext().getResources().getString(R.string.OrderNode)).child(orderKey);
                if (!isAssignedStatus){
                    placed.setChecked(false);
                    Toast.makeText(getApplicationContext() , "Order not assigned !" , Toast.LENGTH_SHORT).show();
                }else{

                    if (isChecked)
                        setOrderChanges.child(getApplicationContext().getResources().getString(R.string.placedIndexStatus)).setValue("yes");
                    else
                        setOrderChanges.child(getApplicationContext().getResources().getString(R.string.placedIndexStatus)).setValue("no");

                }
            }
        });
        picked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                DatabaseReference setOrderChanges = FirebaseDatabase.getInstance().getReference().child(getApplicationContext().getResources().getString(R.string.OrderNode)).child(orderKey);
                Log.d("tag", "onCheckedChanged: "+isAssignedStatus);
                if (!isAssignedStatus){
                    picked.setChecked(false);
                    Toast.makeText(getApplicationContext() , "Order not assigned !" , Toast.LENGTH_SHORT).show();
                }else{

                    if (isChecked)
                        setOrderChanges.child(getApplicationContext().getResources().getString(R.string.pickupIndexStatus)).setValue("yes");
                    else
                        setOrderChanges.child(getApplicationContext().getResources().getString(R.string.pickupIndexStatus)).setValue("no");

                }

            }
        });
        delivered.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                DatabaseReference setOrderChanges = FirebaseDatabase.getInstance().getReference().child(getApplicationContext().getResources().getString(R.string.OrderNode)).child(orderKey);
                Log.d("tag", "onCheckedChanged: "+isAssignedStatus);
                if (!isAssignedStatus){
                    delivered.setChecked(false);
                    Toast.makeText(getApplicationContext() , "Order not assigned !" , Toast.LENGTH_SHORT).show();
                }else{

                    if (isChecked){
                        Toast.makeText(getApplicationContext(),"Order Delivered" , Toast.LENGTH_SHORT).show();
                        setOrderChanges.child(getApplicationContext().getResources().getString(R.string.deliveryIndexStatus)).setValue("yes");
                        placed.setClickable(false);
                        picked.setClickable(false);
                        delivered.setClickable(false);
                    }
                    else
                        setOrderChanges.child(getApplicationContext().getResources().getString(R.string.deliveryIndexStatus)).setValue("no");
                }

            }
        });

    }



    private void setCheckBoxes(String orderKey){
        DatabaseReference getOrderStatus = FirebaseDatabase.getInstance().getReference().child(getApplicationContext().getResources().getString(R.string.OrderNode)).child(orderKey);
        getOrderStatus.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String placedStatus =  snapshot.child(getApplicationContext().getResources().getString(R.string.placedIndexStatus)).getValue(String.class);
                String pickedStatus =  snapshot.child(getApplicationContext().getResources().getString(R.string.pickupIndexStatus)).getValue(String.class);
                String deliveredStatus =  snapshot.child(getApplicationContext().getResources().getString(R.string.deliveryIndexStatus)).getValue(String.class);

                placed.setChecked(placedStatus.equals("yes"));
                picked.setChecked(pickedStatus.equals("yes"));
                delivered.setChecked(deliveredStatus.equals("yes"));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void getHotel(String hotelId){
       DatabaseReference getHotelNameReference = FirebaseDatabase.getInstance().getReference().child(getApplicationContext().getResources().getString(R.string.HotelNode)).child(hotelId);
       getHotelNameReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
           @Override
           public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()){
                    String hotel_name = task.getResult().child("hotel_name").getValue(String.class);
                    String hotel_address=task.getResult().child("address").getValue(String.class);
                    hotel.setText(hotel_name);
                    hotelAddress.setText(hotel_address);
                }
           }
       });
    }

    private void getDish(String orderKey){
        DatabaseReference getDishDetailsReference = FirebaseDatabase.getInstance().getReference().child(getApplicationContext().getResources().getString(R.string.OrderNode)).child(orderKey).child(getApplicationContext().getResources().getString(R.string.DishNode));
        getDishDetailsReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()){

                    for (DataSnapshot snapshot : task.getResult().getChildren()){

                        String key = snapshot.getKey();
                        String Quantity = snapshot.child("Quantity").getValue(String.class);

                        DatabaseReference getDishName = FirebaseDatabase.getInstance().getReference().child(getApplicationContext().getResources().getString(R.string.DishNode)).child(key);
                        getDishName.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                if (task.isSuccessful()){

                                    String dishname = task.getResult().child("name").getValue(String.class);
                                    dishNames = dishNames + dishname + "("+Quantity+") \n";
                                    dish.setText(dishNames);

                                }
                                else
                                {
                                    Toast.makeText(getApplicationContext() , "Fetching Data Failed " , Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                    }

                }
                else
                {
                    Toast.makeText(getApplicationContext() , "Fetching Data Failed " , Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void setOrderDetails(OrderDetails orderDetails) {

        customerName.setText(orderDetails.getName());
        phoneNumber.setText(orderDetails.getPhoneNumber());
        paymentMethod.setText(orderDetails.getCOD().equals("yes")?"Cash On Delivery" : "Payment Done");
        price.setText(" ₹ "+orderDetails.getTotal());
        time.setText(orderDetails.getTime());
        houseno.setText(orderDetails.getHouseNo());
        housename.setText(orderDetails.getHouseName());
        landmark.setText(orderDetails.getLandmark());
        street.setText(orderDetails.getStreet());
        date.setText(orderDetails.getDate());
    }

}