package com.example.centermedic.adapter;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners;
import com.example.centermedic.R;
import com.example.centermedic.clases.MedicoDTO;

import java.util.List;

public class MedicoListarAdapter extends RecyclerView.Adapter<MedicoListarAdapter.MyViewHolder> {

    List<MedicoDTO> listDatos;

    public MedicoListarAdapter(List<MedicoDTO> listDatos) {
        this.listDatos = listDatos;
    }

    @NonNull
    @Override
    public MedicoListarAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_items_list,parent,false);
        return new MedicoListarAdapter.MyViewHolder(v);
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onBindViewHolder(@NonNull MedicoListarAdapter.MyViewHolder holder, int position) {
//        holder.tvTitle.setText(listDatos.get(position).getNombres() + " " + listDatos.get(position).getApellidoPaterno() + " " + listDatos.get(position).getApellidoMaterno());
//        holder.tvSubTitle.setText(listDatos.get(position).getEspecialidad() );
//        holder.tvSubTitle2.setText(listDatos.get(position).getCelular());
//
//        int drawableResourceId = holder.itemView.getResources().getIdentifier("profile","drawable",holder.itemView.getContext().getOpPackageName());
//        Glide.with(holder.itemView.getContext())
//                .load(drawableResourceId)
//                .transform(new GranularRoundedCorners(10,30,0,0))
//                .into(holder.ivImage);
        holder.tvTitle.setText(listDatos.get(position).getNombres() + " " + listDatos.get(position).getApellidoPaterno() + " " + listDatos.get(position).getApellidoMaterno());
        holder.tvSubTitle.setText(listDatos.get(position).getEspecialidad());
        holder.tvSubTitle2.setText(listDatos.get(position).getCelular());

        // Determinar qué imagen cargar según la posición
        int drawableResourceId;
        if (position % 2 == 0) {
            // Posición par
            drawableResourceId = holder.itemView.getResources().getIdentifier("doctora1", "drawable", holder.itemView.getContext().getOpPackageName());
        } else {
            // Posición impar
            drawableResourceId = holder.itemView.getResources().getIdentifier("profile", "drawable", holder.itemView.getContext().getOpPackageName());
        }

        Glide.with(holder.itemView.getContext())
                .load(drawableResourceId)
                .transform(new GranularRoundedCorners(10, 30, 0, 0))
                .into(holder.ivImage);

    }

    @Override
    public int getItemCount() {
        return listDatos.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImage;
        TextView tvTitle, tvSubTitle, tvSubTitle2;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.ivPic);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvSubTitle = itemView.findViewById(R.id.tvSubTitle);
            tvSubTitle2 = itemView.findViewById(R.id.tvSubTitle2);
        }
    }
}
