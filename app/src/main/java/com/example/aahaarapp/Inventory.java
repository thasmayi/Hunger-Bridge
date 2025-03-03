package com.example.aahaarapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Inventory extends AppCompatActivity {
//    private FirebaseFirestore db = FirebaseFirestore.getInstance();
//    private CollectionReference notebookref = db.collection("user data");
//    public static final String TAG = "TAG";
//    private TextView textViewData;
//    FirebaseAuth fAuth;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_inventory);
//        fAuth= FirebaseAuth.getInstance();
//        textViewData=findViewById(R.id.data);
//
//        loadNotes();
//    }
//
//    public void loadNotes() {
//        notebookref.get()
//            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                @Override
//                public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                    if (task.isSuccessful()) {
//                        String data="";
//                        for (QueryDocumentSnapshot document : task.getResult()) {
//                            Log.d(TAG, document.getId() + " => " + document.getData());
//                            if (document.contains("food item") && document.contains("description") && document.contains("userid")) {
//
//                                String name = (String) document.get("food item");
//                                String description = (String) document.get("description");
//                                String Userid = (String) document.get("userid");
//                                String userID = fAuth.getCurrentUser().getUid();
//                                Long qty = (Long) document.get("qty");
//                                Timestamp ts = (Timestamp) document.get("timestamp");
//                                String expiry=(String) document.get("expiry");
//                                String dateandtime=String.valueOf(ts.toDate());
//                                if(Userid.equals(userID)) {
//                                    data += "Name: " + name + "\nQuantity: " + qty + "\nDescription: " + description + "\nExpiry: " + expiry + "\nDate Added: " + dateandtime + "\n\n";
//                                }
//                                textViewData.setText(data);
//                            }
//                        }
//                        //textViewData.setText(data);
//                    } else {
//                        Log.d(TAG, "Error fetching data: ", task.getException());
//                    }
//                }
//            });
//    }
RecyclerView recyclerView;
    ArrayList<InventoryModel> datalist;
    FirebaseFirestore db;
    myInventoryadapter adapter;
    FirebaseAuth fAuth= FirebaseAuth.getInstance();
    public String userID = fAuth.getCurrentUser().getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inventorydata);

        recyclerView=(RecyclerView)findViewById(R.id.inv_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        datalist=new ArrayList<>();
        adapter=new myInventoryadapter(datalist);
        recyclerView.setAdapter(adapter);

        db=FirebaseFirestore.getInstance();
        db.collection("donation").orderBy("timestamp", Query.Direction.DESCENDING).get()
            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    List<DocumentSnapshot> list=queryDocumentSnapshots.getDocuments();
                    for(DocumentSnapshot d:list)
                    {
                        if (d.contains("item") && d.contains("description") && d.contains("userid")) {
                            String Userid = (String) d.get("userid");
                            if(Userid.equals(userID)) {
                                InventoryModel obj=d.toObject(InventoryModel.class);
                                datalist.add(obj);
                            }
                        }
                    }
                    adapter.notifyDataSetChanged();
                }
            });
    }
}
