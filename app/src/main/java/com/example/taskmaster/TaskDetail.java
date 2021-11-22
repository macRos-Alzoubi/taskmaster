package com.example.taskmaster;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TaskDetail extends AppCompatActivity implements OnMapReadyCallback {
    private static final String TAG = "TaskDetail";
    private GoogleMap googleMap;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        MainActivity.sendAnalytics(this.toString(), MainActivity.class.toString());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.task_details_map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        intent = getIntent();


        String taskTitle = intent.getStringExtra("title");
        TextView titleTextView = findViewById(R.id.text_detail_task_title);
        titleTextView.setText(taskTitle);

        String taskBody = intent.getStringExtra("body");
        TextView bodyTextView = findViewById(R.id.text_detail_task_description);
        bodyTextView.setText(taskBody);

        String taskStatus = intent.getStringExtra("status");
        TextView StatusTextView = findViewById(R.id.text_detail_task_status);
        StatusTextView.setText(taskStatus);

        String fileUrl = intent.getStringExtra("imageUrl");
//        "(https?:\\/\\/.*\\.(gif|jpe?g|tiff?|png|webp|bmp))"
//        Pattern pattern = Pattern.compile("/\\.(gif|jpe?g|tiff?|png|webp|bmp)$/", Pattern.CASE_INSENSITIVE);
        Pattern pattern = Pattern.compile("(https?:\\/\\/.*\\.(gif|jpe?g|tiff?|png|webp|bmp))", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(fileUrl);
        ImageView taskImg = findViewById(R.id.veiwTask_img);
        TextView imageUrl = findViewById(R.id.veiwTask_img_uri);
        imageUrl.setMovementMethod(new ScrollingMovementMethod());


        if (fileUrl.length() > 0) {
            if (matcher.find()) {
                taskImg.setVisibility(View.VISIBLE);
                Picasso.get().load(fileUrl).into(taskImg);
            } else {
                imageUrl.setVisibility(View.VISIBLE);
                imageUrl.setText(fileUrl);
            }
        }

        findViewById(R.id.veiwTask_back_btn).setOnClickListener(
                view -> startActivity(new Intent(getApplicationContext(), MainActivity.class)));
    }

    private void showMap() {
        double lat = intent.getDoubleExtra("lat", 0.0);
        double lon = intent.getDoubleExtra("lon", 0.0);

        if(lat != 0.0 && lon != 0.0){
            LatLng latLng = new LatLng(lat, lon);
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
            googleMap.animateCamera(cameraUpdate);
            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            googleMap.setTrafficEnabled(true);
            googleMap.setBuildingsEnabled(true);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.googleMap = googleMap;
        showMap();
    }
}