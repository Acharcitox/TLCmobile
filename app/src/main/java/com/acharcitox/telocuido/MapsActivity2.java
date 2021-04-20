package com.acharcitox.telocuido;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;

import com.acharcitox.telocuido.model.Comercios;
import com.acharcitox.telocuido.model.Operadores;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MapsActivity2 extends FragmentActivity implements OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener{

    // ES un MAPA PARA PROBAR, luego debemos eliminarlo.

    private GoogleMap mMap;
    //Variable para la bd
    private DatabaseReference mRootReference;

    //Creo un arraylist para almacenar los marcadores que voy a ver en el momento, Operadores
    private ArrayList<Marker> tmpRealTimeMarker = new ArrayList<>();
    //Para borrar los puntos anteriores y cargar los nuevos, Operadores
    private ArrayList<Marker> realTimeMarkers = new ArrayList<>();

    //Creo un arraylist para almacenar los marcadores que voy a ver en el momento, Comercios
    private ArrayList<Marker> tmpRealTimeMarkerComercios = new ArrayList<>();
    //Para borrar los puntos anteriores y cargar los nuevos, Comercios
    private ArrayList<Marker> realTimeMarkersComercios = new ArrayList<>();


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
        mMap.getUiSettings().setZoomControlsEnabled(true);

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
                    String nombre = operador.getNombre();
                    String hora_inicio = operador.getHora_inicio_trabajo();
                    String hora_fin = operador.getHora_fin_trabajo();
                    String tipo_operador = operador.getTipo_operador();
                    Integer calificacion = operador.getCalificacion_promedio();
                    Integer cantidadLugares = operador.getCantidad_restante_lugares();
                    MarkerOptions markerOptions = new MarkerOptions();
                    //Aca decido como mostrar el icono en google (.snippet puede agregar una descripcion chica .icon para agregar un icono)
                    //Decido pasarle la posicion, el titulo y mas datos dependiendo si es cuidacoches o un estacionamiento.
                    if(tipo_operador.equals("Cuidacoches")){

                        markerOptions.position(new LatLng(latitud,longitud))
                                .title(nombre+"Calificacion: "+calificacion)
                                .snippet("Lugares Disponibles: "+cantidadLugares + "Horario: "+ hora_inicio +"Hs a "+ hora_fin+ "Hs")
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                    } else {
                        markerOptions.position(new LatLng(latitud,longitud))
                                .title(nombre)
                                .snippet("Lugares Disponibles: "+cantidadLugares+"/N "+ "Horario: "+ hora_inicio +"Hs a "+ hora_fin+ "Hs")
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                    }


                    //Aca agrego las marcas al mapa, cada punto es la longitud y latitud de la tabla operadores.
                    tmpRealTimeMarker.add(mMap.addMarker(markerOptions));

                }

                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {

                        Intent i = new Intent(MapsActivity2.this, OverlayOperador.class);
                        // pasamos los datos de operador a un nuevo intent-

                        startActivity(i);

                        return false;
                    }
                });


                //Borro marcas guardadas
                realTimeMarkers.clear();
                //Guardo todas las marcas del for en la variable que esta limpia
                realTimeMarkers.addAll(tmpRealTimeMarker);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        mRootReference.child("Comercios").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Recorro la variable y borro todas las marcas que estan guardadas.
                for (Marker marker:realTimeMarkersComercios){
                    marker.remove();
                }
                //Hacemos un FOR para recorrer los datos de latitud y longitud de cada Comercio
                for(DataSnapshot comercios : snapshot.getChildren()){
                    //Inicializo el model Operadores
                    Comercios comercio = comercios.getValue(Comercios.class);
                    //TRaigo la latitud y longitud de cada operador
                    Float latitud = comercio.getLatitud();
                    Float longitud = comercio.getLongitud();
                    String nombre = comercio.getNombre();
                    String rubro = comercio.getRubro();
                    String descripcion = comercio.getDescripcion();
                    MarkerOptions markerOptions = new MarkerOptions();
                    //Aca decido como mostrar el icono en google (.snippet puede agregar una descripcion chica .icon para agregar un icono)
                    //Decido pasarle la posicion
                    markerOptions.position(new LatLng(latitud,longitud))
                            .title(nombre+" "+rubro)
                            .snippet(descripcion)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));

                    //Aca agrego las marcas al mapa, cada punto es la longitud y latitud de la tabla operadores.
                    tmpRealTimeMarkerComercios.add(mMap.addMarker(markerOptions));

                }

                //Borro marcas guardadas
                realTimeMarkersComercios.clear();
                //Guardo todas las marcas del for en la variable que esta limpia
                realTimeMarkersComercios.addAll(tmpRealTimeMarkerComercios);



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

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }
}