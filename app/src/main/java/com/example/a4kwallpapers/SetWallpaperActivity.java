package com.example.a4kwallpapers;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.WallpaperManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.bumptech.glide.Glide;

public class SetWallpaperActivity extends AppCompatActivity {

    ImageButton backBtn , menuBtn;
    ImageView image;
    AppCompatButton setWall;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_wallpaper);

        getWindow().setStatusBarColor(getResources().getColor(R.color.grey));

        backBtn = findViewById(R.id.btn_back);
        menuBtn = findViewById(R.id.btn_down_wall);
        image = findViewById(R.id.set_wallpaper_img);
        setWall = findViewById(R.id.btn_set_wll);

        Bundle in = getIntent().getExtras();
        String img = in.getString("image");
        Glide.with(this).load(img).into(image);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        menuBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {

                PopupMenu menu = new PopupMenu(v.getContext() , v );
                menu.setGravity(Gravity.END);
                menu.getMenu().add("Download").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        DownloadManager downloadManager = (DownloadManager)getSystemService(getApplicationContext().DOWNLOAD_SERVICE);
                        Uri uri = Uri.parse(img);
                        DownloadManager.Request request = new DownloadManager.Request(uri);
                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                        downloadManager.enqueue(request);
                        Toast.makeText(SetWallpaperActivity.this, "Downloading start", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                });
                menu.getMenu().add("About").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        dialogBox();

//                        AlertDialog.Builder builder = new AlertDialog.Builder(v.getRootView().getContext());
//                        View view = LayoutInflater.from(v.getRootView().getContext()).inflate(R.layout.m_dailog_layout ,null);
//                        ImageButton btn;
//                        btn = findViewById(R.id.btn_close);
//                        btn.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                builder.setCancelable(true);
//
//                            }
//                        });
//                        builder.setView(view);
//                        builder.show();
                        return false;
                    }
                });
                menu.show();
            }
        });

        setWall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                WallpaperManager wallpaperManager = WallpaperManager.getInstance(SetWallpaperActivity.this);
                Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();

                try {
                    wallpaperManager.setBitmap(bitmap);
                    Toast.makeText(SetWallpaperActivity.this, "Wallpaper set", Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });


    }

    private void dialogBox() {

        final Dialog dialog = new Dialog(SetWallpaperActivity.this);
        dialog.setContentView(R.layout.m_dailog_layout);

        ImageButton btn_Clos;

        btn_Clos = dialog.findViewById(R.id.btn_close);

        btn_Clos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

}