package com.example.deliveryboy;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.deliveryboy.PojoClasses.OrderDetails;

import java.util.List;

public class OrderViewAdapter extends RecyclerView.Adapter<OrderViewAdapter.orderHolder> {


    private Context context;
    private List<OrderDetails> orderList;

    public OrderViewAdapter(Context context, List<OrderDetails> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrderViewAdapter.orderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.order_display_view , parent , false);
        orderHolder orderHolder = new orderHolder(view);
        return orderHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewAdapter.orderHolder holder, int position) {

        holder.orderName.setText(orderList.get(position).getName());
        holder.orderNumber.setText(orderList.get(position).getPhoneNumber());
        holder.orderPicked.setTextColor(orderList.get(position).getPickupIndex().equals("yes") ? Color.parseColor("#07ed1a") : Color.parseColor("#e60b0b"));
        holder.orderPlaced.setTextColor(orderList.get(position).getPlacedIndex().equals("yes") ? Color.parseColor("#07ed1a") : Color.parseColor("#e60b0b"));
        holder.orderDelivered.setTextColor(orderList.get(position).getDeliveryIndex().equals("yes") ? Color.parseColor("#07ed1a") : Color.parseColor("#e60b0b"));


        holder.orderCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent orderDetails = new Intent(context,orderDetailsDisplay.class);
               orderDetails.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(orderDetails);
            }
        });

    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class orderHolder extends RecyclerView.ViewHolder {

        private TextView orderName , orderNumber , orderPicked ,orderPlaced , orderDelivered;
        private ImageButton openMaps;
        private CardView orderCardView;

        public orderHolder(@NonNull View itemView) {
            super(itemView);

            orderName = itemView.findViewById(R.id.orderName);
            orderNumber = itemView.findViewById(R.id.orderPhoneNumber);
            orderPicked = itemView.findViewById(R.id.orderPicked);
            orderPlaced = itemView.findViewById(R.id.orderPlaced);
            orderDelivered = itemView.findViewById(R.id.orderDelivered);
            openMaps = itemView.findViewById(R.id.openLocation);
            orderCardView = itemView.findViewById(R.id.orderCardView);
        }
    }
}
