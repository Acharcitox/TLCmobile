package com.acharcitox.telocuido;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.acharcitox.telocuido.model.Conductores;
import com.acharcitox.telocuido.model.Operadores;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class PermisosActivity extends AppCompatActivity {

    private Button btnPermiso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permisos);

        Bundle extras = getIntent().getExtras();
        String id_conductorLogin = extras.getString("id_conductor_conductor");
        String nombre_conductor_login = extras.getString("nombre_conductor");

        if(ContextCompat.checkSelfPermission(PermisosActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            Intent i = new Intent(this, MapsActivity.class);
            i.putExtra("id_conductorMap", id_conductorLogin);
            i.putExtra("Nombre_conductor_mapa", nombre_conductor_login);
            startActivity(i);
            finish();
            return;
        }

        btnPermiso = findViewById(R.id.btnPermiso);

        btnPermiso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dexter.withActivity(PermisosActivity.this)
                        .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse response) {
                                //startActivity(new Intent(PermisosActivity.this, MapsActivity.class));
                                Intent i = new Intent(PermisosActivity.this, MapsActivity.class);
                                i.putExtra("id_conductorMap", id_conductorLogin);
                                i.putExtra("Nombre_conductor_mapa", nombre_conductor_login);
                                startActivity(i);
                                finish();
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse response) {
                                if (response.isPermanentlyDenied()) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(PermisosActivity.this);
                                    builder.setTitle("Permiso Denegado")
                                            .setMessage("Has neado el permiso para acceder a la ubicación de este dspositivo. Debes ir a Configuaración de tu dispositivo para permitir el acceso")
                                            .setNegativeButton("Cancelar", null)
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Intent i = new Intent();
                                                    i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                                    i.setData(Uri.fromParts("package", getPackageName(), null));
                                                }
                                            })
                                            .show();
                                } else {
                                    Toast.makeText(PermisosActivity.this, "Permiso Denegado", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                                token.continuePermissionRequest();
                            }
                        })
                        .check();
            }
        });
    }
}