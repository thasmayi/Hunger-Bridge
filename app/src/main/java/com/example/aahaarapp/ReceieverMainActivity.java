package com.example.aahaarapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;


public class ReceieverMainActivity extends AppCompatActivity {

    CardView schedule, registerNgo, logout, foodmap, history;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receiever_main);

        schedule = findViewById(R.id.schedulePickup);
        registerNgo = findViewById(R.id.registerNgo);
        logout = findViewById(R.id.cardLogout);
        foodmap = findViewById(R.id.cardFoodmap);
        history = findViewById(R.id.cardHistory);

        fAuth= FirebaseAuth.getInstance();
        if(fAuth.getCurrentUser() ==null){
            Intent intent = new Intent(ReceieverMainActivity.this, Landingpage.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

        schedule.setOnClickListener(new View.OnClickListener ()
        {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReceieverMainActivity.this, SchedulePickup.class);
                startActivity(intent);
            }
        });
        registerNgo.setOnClickListener(new View.OnClickListener ()
        {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReceieverMainActivity.this, RegisterNgo.class);
                startActivity(intent);
            }
        });
        foodmap.setOnClickListener(new View.OnClickListener ()
        {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReceieverMainActivity.this, FoodMap.class);
                startActivity(intent);
            }
        });
//        mypin.setOnClickListener(new View.OnClickListener ()
//        {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(ReceieverMainActivity.this, MyPin.class);
//                startActivity(intent);
//            }
//        });
        history.setOnClickListener(new View.OnClickListener ()
        {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReceieverMainActivity.this, UserdataActivity.class);
                startActivity(intent);
            }
        });
        logout.setOnClickListener(new View.OnClickListener ()
        {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(ReceieverMainActivity.this, Landingpage.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }
}