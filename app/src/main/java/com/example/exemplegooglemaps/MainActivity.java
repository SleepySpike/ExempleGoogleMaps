package com.example.exemplegooglemaps;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Context context;
    ArrayList<String> permissions;
    ArrayList<String> permissionsRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;

        // Téléphone supérieur à Android 5.1
        if(Build.VERSION.SDK_INT >= 23){

            permissions = new ArrayList<>();
            //Liste non exhaustive des permissions que l'on demande
            permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
            permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);

            //On appelle la méthode permission
            callPermissions();

        }
        else {
            callIntent();
        }
    }

    private void callPermissions(){

        //On instancie notre permissionRequest
        permissionsRequest = new ArrayList<>();

        for (int i = 0; i < permissions.size() ; i++){

            String permissionDemande = permissions.get(i).toString();

            if (ContextCompat.checkSelfPermission(context, permissionDemande) !=PackageManager.PERMISSION_GRANTED) {

                permissionsRequest.add(permissionDemande);
            }
        }

        if (permissionsRequest.isEmpty()) //Toutes les permission ont été acceptées
        {

            callIntent();
        }
        else {

            String[] request = new String[permissionsRequest.size()];

            request = permissionsRequest.toArray(request);

            ActivityCompat.requestPermissions(this, request, 100);

        }

    }

    private void callIntent(){

        Intent intent = new Intent(context, MapsActivity.class);
        startActivity(intent);
    }

}