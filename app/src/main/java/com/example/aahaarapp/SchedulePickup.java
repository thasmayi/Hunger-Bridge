package com.example.aahaarapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.util.Calendar;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.ServerTimestamp;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SchedulePickup extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<InventoryModel> datalist;
    FirebaseFirestore db;
    pickupAdapter adapter;
    FirebaseAuth fAuth= FirebaseAuth.getInstance();
    public String userID = fAuth.getCurrentUser().getUid();
    Button pickedup;
    CheckBoxTracker checkBoxTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_pickup);
        recyclerView = (RecyclerView) findViewById(R.id.sc_inv_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        datalist = new ArrayList<>();
        adapter = new pickupAdapter(datalist);
        recyclerView.setAdapter(adapter);
        pickedup = findViewById(R.id.pickupButton);
        checkBoxTracker = CheckBoxTracker.getInstance();
        db = FirebaseFirestore.getInstance();

        DocumentReference docRef = db.collection("location").document(userID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("TAG", "DocumentSnapshot data: " + document.getData());
                    }
                    else {
                        Toast.makeText(getApplicationContext(),"Please Register NGO Before Pickup!",Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(SchedulePickup.this, ReceieverMainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(),"Error!",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SchedulePickup.this, ReceieverMainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        });

        db.collection("donation").orderBy("timestamp", Query.Direction.DESCENDING).get()
        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                for (DocumentSnapshot d : list) {
                    if (d.contains("item") && d.contains("description") && d.contains("userid")) {
                        String type = (String) d.get("type");
                        if (type.equals("Store")) {
                            InventoryModel obj = d.toObject(InventoryModel.class);
                            datalist.add(obj);
                        }
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });

        pickedup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> items = checkBoxTracker.getItem();
                for (int i = 0; i < items.size(); i++)
                {
                    DocumentReference documentReference = db.collection("donation").document(items.get(i));
                    documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                int quantity = Integer.parseInt(document.get("qty").toString());
                                if (quantity == 1) {
                                    documentReference
                                        .delete()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d("TAG", "DocumentSnapshot successfully deleted!");
                                                Toast.makeText(getApplicationContext(), "Success!", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w("TAG", "Error deleting document", e);
                                                Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                } else {
                                    documentReference
                                        .update("qty", quantity - 1)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(getApplicationContext(), "Success!", Toast.LENGTH_SHORT).show();
                                                Log.d("TAG", "DocumentSnapshot successfully updated!");
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_SHORT).show();
                                                Log.w("TAG", "Error updating document", e);
                                            }
                                        });
                                }

                            }
                            else {
                                Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                DocumentReference documentReference = db.collection("pickup").document();
                Map<String,Object> user = new HashMap<>();
                user.put("timestamp", FieldValue.serverTimestamp());
                user.put("items",String.join(",", items));
                user.put("userid",userID);

                documentReference.set(user)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(getApplicationContext(),"Success!",Toast.LENGTH_SHORT).show();
                            Log.d("TAG","Success!");
                            Intent intent = new Intent(SchedulePickup.this, ReceieverMainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(),"Error!",Toast.LENGTH_SHORT).show();
                            Log.w("TAG", "Error!", e);
                        }
                    });
            }
        });
    }
}