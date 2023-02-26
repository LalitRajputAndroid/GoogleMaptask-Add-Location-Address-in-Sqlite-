package com.example.livelocation.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.livelocation.Modal;
import com.example.livelocation.R;

import java.util.ArrayList;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder> {

    Context context;
    ArrayList<Modal> list ;

    public AddressAdapter(Context context, ArrayList<Modal> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater  = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.single_row,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.areat1.setText("AreaName ::  "+list.get(position).getAreaName());
        holder.landmarkt2.setText("Landmark ::  "+list.get(position).getLandmark());
        holder.cityt3.setText("City ::  "+list.get(position).getCity());
        holder.statet4.setText("State ::  "+list.get(position).getState());
        holder.countryt5.setText("Country ::  "+list.get(position).getCountry());
        holder.pincodet6.setText("Pincode ::  "+list.get(position).getPincode());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView areat1,landmarkt2,cityt3,statet4,countryt5,pincodet6;
        Button editbtn,deletebtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            areat1 = itemView.findViewById(R.id.areatext_id);
            landmarkt2 = itemView.findViewById(R.id.landmarktext_id);
            cityt3 = itemView.findViewById(R.id.citytext_id);
            statet4 = itemView.findViewById(R.id.statetext_id);
            countryt5 = itemView.findViewById(R.id.countrytext_id);
            pincodet6 = itemView.findViewById(R.id.pincodetext_id);
            editbtn = itemView.findViewById(R.id.editbtn_id);
            deletebtn = itemView.findViewById(R.id.detelebtn_id);

        }
    }


}
