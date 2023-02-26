package com.example.livelocation.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.livelocation.Database.MapDatabase;
import com.example.livelocation.Modal;
import com.example.livelocation.R;

import java.util.ArrayList;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder> {

    Context context;
    ArrayList<Modal> list;

    public AddressAdapter(Context context, ArrayList<Modal> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.single_row, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        final Modal modal = list.get(position);

        holder.areat1.setText("AreaName ::  " + modal.getAreaName());
        holder.landmarkt2.setText("Landmark ::  " + modal.getLandmark());
        holder.cityt3.setText("City ::  " + modal.getCity());
        holder.statet4.setText("State ::  " + modal.getState());
        holder.countryt5.setText("Country ::  " + modal.getCountry());
        holder.pincodet6.setText("Pincode ::  " + modal.getPincode());

        holder.deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setIcon(R.drawable.baseline_delete_24);
                builder.setTitle("Delete!");
                builder.setMessage("Are You Sure ?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        MapDatabase database = new MapDatabase(context);
                        database.deletedata(modal.getC_id());

                        list.remove(modal);
                        notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.create();
                builder.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView areat1, landmarkt2, cityt3, statet4, countryt5, pincodet6;
        Button editbtn, deletebtn;

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
