package com.example.a4kwallpapers;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class WallpaperAdapter extends RecyclerView.Adapter<WallpaperAdapter.WallViewHolder>{

    private Context context;
    private List<WallpaperModel> wallpaperModelList;

    public WallpaperAdapter(Context context , List<WallpaperModel> wallpaperModelList){

        this.context = context;
        this.wallpaperModelList = wallpaperModelList;

    }

    @NonNull
    @Override
    public WallpaperAdapter.WallViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(context).inflate(R.layout.rcv_layout , parent , false);
        return new WallpaperAdapter.WallViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WallpaperAdapter.WallViewHolder holder, int position) {

        WallpaperModel wallpaperModel = wallpaperModelList.get(position);

        Glide.with(context).load(wallpaperModel.getPortrait()).into(holder.rcv_image);
        holder.rcv_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context , SetWallpaperActivity.class);
                intent.putExtra("image",wallpaperModel.getPortrait());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return wallpaperModelList.size();
    }

    public class WallViewHolder extends RecyclerView.ViewHolder {

        ImageView rcv_image;

        public WallViewHolder(@NonNull View itemView) {
            super(itemView);
            rcv_image = itemView.findViewById(R.id.rcv_img);
        }
    }
}
