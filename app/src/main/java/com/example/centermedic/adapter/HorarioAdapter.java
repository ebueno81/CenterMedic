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
import com.example.centermedic.clases.HorarioDTO;
import com.example.centermedic.services.OnItemClickListenerHorario;

import java.util.List;

public class HorarioAdapter extends RecyclerView.Adapter<HorarioAdapter.MyViewHolder> {
    List<HorarioDTO> listDatos;
    private OnItemClickListenerHorario listener;

    public HorarioAdapter(List<HorarioDTO> listDatos, OnItemClickListenerHorario listener) {
        this.listDatos = listDatos;
        this.listener = listener;
    }

    @NonNull
    @Override
    public HorarioAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_items_list,parent,false);
        return new HorarioAdapter.MyViewHolder(v);
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onBindViewHolder(@NonNull HorarioAdapter.MyViewHolder holder, int position) {
        holder.tvTitle.setText(listDatos.get(position).getFecha() + " " + listDatos.get(position).getHora());
        holder.tvSubTitle.setText(listDatos.get(position).getEspecialidad());
        holder.tvSubTitle2.setText(listDatos.get(position).getDoctor());

        int drawableResourceId = holder.itemView.getResources().getIdentifier("1","drawable",holder.itemView.getContext().getOpPackageName());
        Glide.with(holder.itemView.getContext())
                .load(drawableResourceId)
                .transform(new GranularRoundedCorners(10,30,0,0))
                .into(holder.ivImage);

        HorarioDTO item = listDatos.get(position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(item);
            }
        });
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
