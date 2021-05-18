package com.example.deliveryboy;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class OrderViewAdapter extends RecyclerView.Adapter<OrderViewAdapter.orderHolder> {


    private Context context;


    @NonNull
    @Override
    public OrderViewAdapter.orderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewAdapter.orderHolder holder, int position) {


        holder.orderCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent orderDetails = new Intent(context,orderDetailsDisplay.class);
                context.startActivity(orderDetails);
            }
        });

    }

    @Override
    public int getItemCount() {
        return 0;
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
