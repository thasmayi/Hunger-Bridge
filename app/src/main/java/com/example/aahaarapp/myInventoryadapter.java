package com.example.aahaarapp;


import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class myInventoryadapter extends RecyclerView.Adapter<myInventoryadapter.myviewholder>
{
    ArrayList<InventoryModel> datalist;
    FirebaseAuth fAuth= FirebaseAuth.getInstance();
    public String userID = fAuth.getCurrentUser().getUid();
    public String uid;

    public myInventoryadapter(ArrayList<InventoryModel> datalist) {
        this.datalist = datalist;
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder (@NonNull ViewGroup parent,int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.inventory_row, parent, false);
        return new myviewholder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull myviewholder holder, int position) {
        if(datalist.get(position).getQty() > 2) {
           holder.cardView.setBackgroundColor(Color.parseColor("#bdeca0"));
        }
        else {
            holder.cardView.setBackgroundColor(Color.parseColor("#ffbbd0"));
        }
        holder.tname.setText(datalist.get(position).getItem());
        holder.tqty.setText(datalist.get(position).getQty().toString());
        holder.tdescription.setText(datalist.get(position).getDescription());
        holder.tExpiryDate.setText(datalist.get(position).getExpiry());
    }

    @Override
    public int getItemCount() {
        return datalist.size();
    }

    class myviewholder extends RecyclerView.ViewHolder
    {
        View cardView;
        TextView tname,tqty, tdescription, tExpiryDate, tStoreName;
        public myviewholder(@NonNull View itemView) {
            super(itemView);
            tname = itemView.findViewById(R.id.item_name);
            tqty = itemView.findViewById(R.id.qty_text);
            tdescription = itemView.findViewById(R.id.description_t);
            tExpiryDate = itemView.findViewById(R.id.expiry_t);
            cardView = itemView;
        }
    }
}
