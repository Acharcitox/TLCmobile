package com.acharcitox.telocuido;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.acharcitox.telocuido.model.Operadores;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MapsActivity2 extends FragmentActivity implements OnMapReadyCallback {

    // ES un MAPA PARA PROBAR, luego debemos eliminarlo.

    private GoogleMap mMap;
    //Variable para la bd
    private DatabaseReference mRootReference;

    //Creo un arraylist para almacenar los marcadores que voy a ver en el momento
    private ArrayList<Marker> tmpRealTimeMarker = new ArrayList<>();
    //Para borrar los puntos anteriores y cargar los nuevos
    private ArrayList<Marker> realTimeMarkers = new ArrayList<>();



// DATOS PARA CARGAR EL MAPA
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps2);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mRootReference = FirebaseDatabase.getInstance().getReference();
    }


// Para ver el mapa de google maps
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mRootReference.child("Operadores").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                //Recorro la variable y borro todas las marcas que estan guardadas.
                for (Marker marker:realTimeMarkers){
                    marker.remove();
                }
                //Hacemos un FOR para recorrer los datos de latitud y longitud de cada Operador
                for(DataSnapshot operadores : datasnapshot.getChildren()){
                    //Inicializo el model Operadores
                    Operadores operador = operadores.getValue(Operadores.class);
                    //TRaigo la latitud y longitud de cada operador
                    Float latitud = operador.getLatitud();
                    Float longitud = operador.getLongitud();
                    MarkerOptions markerOptions = new MarkerOptions();
                    //Aca decido como mostrar el icono en google (.snippet puede agregar una descripcion chica .icon para agregar un icono)
                    //Decido pasarle la posicion
                    markerOptions.position(new LatLng(latitud,longitud));

                    //Aca agrego las marcas al mapa, cada punto es la longitud y latitud de la tabla operadores.
                    tmpRealTimeMarker.add(mMap.addMarker(markerOptions));

                }

                //Borro marcas guardadas
                realTimeMarkers.clear();
                //Guardo todas las marcas del for en la variable que esta limpia
                realTimeMarkers.addAll(tmpRealTimeMarker);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // Add a marker in Sydney and move the camera
      //  LatLng sydney = new LatLng(-34, 151);
      //  mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
      //  mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}