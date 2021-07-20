package com.example.deliveryboy.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.deliveryboy.PojoClasses.OrderDetails;
import com.example.deliveryboy.R;
import com.example.deliveryboy.orderDetailsDisplay;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

import java.util.List;

public class OrderViewAdapter extends RecyclerView.Adapter<OrderViewAdapter.orderHolder> {


    private Context context;
    private List<OrderDetails> orderList;
    private List<String> orderKeys;

    public OrderViewAdapter(Context context, List<OrderDetails> orderList, List<String> orderKeys) {
        this.context = context;
        this.orderList = orderList;
        this.orderKeys = orderKeys;

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
        holder.orderPicked.setVisibility(orderList.get(position).getPickupIndex().equals("yes") ? View.VISIBLE : View.INVISIBLE);
        holder.orderPlaced.setVisibility(orderList.get(position).getPlacedIndex().equals("yes") ? View.VISIBLE: View.INVISIBLE);
        holder.orderDelivered.setVisibility(orderList.get(position).getDeliveryIndex().equals("yes") ? View.VISIBLE :View.INVISIBLE);
        holder.date.setText(orderList.get(position).getTime()+" "+orderList.get(position).getDate());
        holder.orderCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openOrderDetailsDisplay = new Intent(context , orderDetailsDisplay.class);
                openOrderDetailsDisplay.putExtra("OrderDetails" , orderList.get(holder.getAdapterPosition()));
                openOrderDetailsDisplay.putExtra("OrderKey" ,  orderKeys.get(holder.getAdapterPosition()));
                openOrderDetailsDisplay.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(openOrderDetailsDisplay);
            }
        });
        holder.orderNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+orderList.get(position).getPhoneNumber()+"\""));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        holder.openMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Uri gmmIntentUri = Uri.parse("geo:"+orderList.get(position).getLatitude()+","+orderList.get(position).getLongitude()+"z=5");
//                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
//                mapIntent.setPackage("com.google.android.apps.maps");
//                mapIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                context.startActivity(mapIntent);

                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?daddr="+orderList.get(position).getLatitude()+","+orderList.get(position).getLongitude()+"\""));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        });

        holder.assign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.assign.isChecked()){
                    Toast.makeText(context,"assigned",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context,"not assigned",Toast.LENGTH_SHORT).show();

                }

            }
        });


    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class orderHolder extends RecyclerView.ViewHolder {

        private TextView orderName , orderPicked ,orderPlaced , orderDelivered;
        private MaterialButton openMaps,orderNumber;
        private CardView orderCardView;
        private MaterialTextView date;
        private CheckBox assign;
        public orderHolder(@NonNull View itemView) {
            super(itemView);

            orderName = itemView.findViewById(R.id.orderName);
            orderNumber = itemView.findViewById(R.id.orderPhoneNumber);
            orderPicked = itemView.findViewById(R.id.orderPicked);
            orderPlaced = itemView.findViewById(R.id.orderPlaced);
            orderDelivered = itemView.findViewById(R.id.orderDelivered);
            openMaps = itemView.findViewById(R.id.openLocation);
            orderCardView = itemView.findViewById(R.id.orderCardView);
            assign = itemView.findViewById(R.id.assign);
            date=itemView.findViewById(R.id.date);
        }
    }
}
