package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TaskDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        MainActivity.sendAnalytics(this.toString(), MainActivity.class.toString());

        Intent intent = getIntent();

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
}