package com.acharcitox.telocuido;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import android.widget.RelativeLayout;
import android.widget.Toast;


import com.acharcitox.telocuido.model.Comercios;
import com.acharcitox.telocuido.model.Operadores;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;



import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;

    //Creo un arraylist para almacenar los marcadores que voy a ver en el momento, Operadores
    private ArrayList<Marker> tmpRealTimeMarker = new ArrayList<>();
    //Para borrar los puntos anteriores y cargar los nuevos, Operadores
    private ArrayList<Marker> realTimeMarkers = new ArrayList<>();

    //Creo un arraylist para almacenar los marcadores que voy a ver en el momento, Comercios
    private ArrayList<Marker> tmpRealTimeMarkerComercios = new ArrayList<>();
    //Para borrar los puntos anteriores y cargar los nuevos, Comercios
    private ArrayList<Marker> realTimeMarkersComercios = new ArrayList<>();

    //Variable para la bd
    private DatabaseReference mRootReference;

    FusedLocationProviderClient fusedLocationProviderClient;

    //acá variable donde guardamos la ubicación actual:
    private Location LastLocation;
    //acá variable para actualizar si LastLocation es null:
    private LocationCallback locationCallback;

    private static final int REQUEST_CODE = 101;

    private Button btnBuscar;
    //variable para mover el boton de recuperar ubicación de ususario
    private View mapView;
    private final float DEFAULT_ZOOM = 17;

    //Para hacer doble click sobre las marcas
    boolean doubleBackToExitPressedOnce = false;



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        mapView = mapFragment.getView();

        // Esto para traer los datos de los operadores de firebase
        mRootReference = FirebaseDatabase.getInstance().getReference();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MapsActivity.this);

        setAutoCompleteFragment();
    }



    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // limpio los marcadores por default de maps
        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.style_json));

        mMap.setMyLocationEnabled(true);/*Como ya tengo los permisos en otra activity es solo poner en true*/
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setTrafficEnabled(true);

        //Aca recibimos la id del conductor que ingreso
        Bundle extras = getIntent().getExtras();
        String id_conductorLogin = extras.getString("id_conductorMap");
        String nombre_conductor_login = extras.getString("Nombre_conductor_mapa");


        //Para mostrar cuidacoches
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
                    String tipo_operador = operador.getTipo_operador();
                    Integer cantidadLugares = operador.getCantidad_restante_lugares();
                    MarkerOptions markerOptions = new MarkerOptions();


                    //Aca decido como mostrar el icono en google (.snippet puede agregar una descripcion chica .icon para agregar un icono)
                    //Decido pasarle la posicion, el titulo y mas datos dependiendo si es cuidacoches o un estacionamiento.
                    if(tipo_operador.equals("Cuidacoches")){

                        markerOptions.position(new LatLng(latitud,longitud))
                                .title(nombre)
                                .snippet("Doble click para elegir este lugar. " + "Es un " + tipo_operador )
                                //.snippet(tipo_operador + " Calificacion: "+ calificacion + " Horario: "+ hora_inicio +"Hs a "+ hora_fin+ "Hs")
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

//ZOOM para probar y ver los puntos en el momento
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitud, longitud), 17));

                        if (cantidadLugares.equals(1)) {
                            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_lugar_1));
                        }
                        if (cantidadLugares.equals(2)) {
                            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_lugar_2));
                        }
                        if (cantidadLugares.equals(3)) {
                            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_lugar_3));
                        }
                        if (cantidadLugares.equals(4)) {
                            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_lugar_4));
                        }
                        if (cantidadLugares.equals(5)) {
                            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_lugar_5));
                        }
                        if (cantidadLugares.equals(6)) {
                            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_lugar_6));
                        }
                        if (cantidadLugares.equals(7)) {
                            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_lugar_7));
                        }
                        if (cantidadLugares.equals(8)) {
                            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_lugar_8));
                        }
                        if (cantidadLugares.equals(9)) {
                            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_lugar_9));
                        }
                        if (cantidadLugares.equals(10)) {
                            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_lugar_10));
                        }
                        if (cantidadLugares.equals(11)) {
                            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_lugar_11));
                        }
                        if (cantidadLugares.equals(12)) {
                            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_lugar_12));
                        }
                        if (cantidadLugares.equals(13)) {
                            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_lugar_13));
                        }
                        if (cantidadLugares.equals(14)) {
                            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_lugar_14));
                        }
                        if (cantidadLugares.equals(15)) {
                            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_lugar_15));
                        }

                    } else {
                        markerOptions.position(new LatLng(latitud,longitud))
                                .title(nombre)
                                .snippet("Doble click para elegir este lugar. " + "Es un " + tipo_operador )
                                //.snippet(tipo_operador + " Lugares Disponibles: "+cantidadLugares+ " Horario: "+ hora_inicio +"Hs a "+ hora_fin+ "Hs")
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                    }



                    //Aca agrego las marcas al mapa, cada punto es la longitud y latitud de la tabla operadores.
                    tmpRealTimeMarker.add(mMap.addMarker(markerOptions));

                    mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(Marker marker) {
                            //Para hacer doble click y elegir lugar.
                            if (doubleBackToExitPressedOnce){
                                Intent i = new Intent(MapsActivity.this, OverlayOperador.class);
                                // pasamos los datos de operador a un nuevo intent-
                                i.putExtra("ci_operador", marker.getTitle());
                                i.putExtra("id_conductorMap", id_conductorLogin);
                                i.putExtra("Nombre_conductor_mapa", nombre_conductor_login);
                                startActivity(i);
                            } else {
                                doubleBackToExitPressedOnce = true;
                                new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 1000);
                            }
                            return false;
                        }
                    });

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


        //Para mostrar los Comercios
        mRootReference.child("Comercios").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Recorro la variable y borro todas las marcas que estan guardadas.
                for (Marker marker1:realTimeMarkersComercios){
                    marker1.remove();
                }
                //Hacemos un FOR para recorrer los datos de latitud y longitud de cada Comercio
                for(DataSnapshot comercios : snapshot.getChildren()){
                    if (snapshot.exists()){
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




        //acá movemos el botón de recuperación de ubicación de usuario para abajo y que no quede tapado por el SearchView
        if(mapView != null && mapView.findViewById(Integer.parseInt("1")) != null)  {
            View locationBtn = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) locationBtn.getLayoutParams();
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            layoutParams.setMargins(0,0, 40, 260);
        }


        //comprueba si el Gps está conectado y sino le pide al usuario
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(500);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);

        SettingsClient settingsClient = LocationServices.getSettingsClient(MapsActivity.this);
        Task<LocationSettingsResponse> task = settingsClient.checkLocationSettings(builder.build());

        task.addOnSuccessListener(MapsActivity.this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override //si el Gps está encendido se llama esta función
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                getUserLocation();
            }
        });

        task.addOnFailureListener(MapsActivity.this, new OnFailureListener() {
            @Override //si el Gps NO está encendido se llama esta función
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                    try {
                        resolvableApiException.startResolutionForResult(MapsActivity.this, 51);
                    } catch (IntentSender.SendIntentException sendIntentException) {
                        sendIntentException.printStackTrace();
                    }
                }
            }
        });

    }

    //Si el usuario acepta encender el gps en el telefono
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 51) {
            if(resultCode == RESULT_OK) {
                getUserLocation(); //si enciende el gps se mueve y muestra la ubicación actual del usuario
            }
        }
    }



    //recupera la ubicación del usuario y maneja errores
    @SuppressLint("MissingPermission")
    private void getUserLocation() {
        fusedLocationProviderClient.getLastLocation()
                .addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if(task.isSuccessful()) {
                            LastLocation = task.getResult();
                            if(LastLocation != null) {
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(LastLocation.getLatitude(), LastLocation.getLongitude()), DEFAULT_ZOOM));
                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(LastLocation.getLatitude(), LastLocation.getLongitude()), DEFAULT_ZOOM));
                            } else {
                                final LocationRequest locationRequest = LocationRequest.create();
                                locationRequest.setInterval(1000);
                                locationRequest.setFastestInterval(500);
                                locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                                //locationCallback es la función que se ejecuta cuando se recibe un actualización de la ubicación
                                locationCallback = new LocationCallback() {
                                    @Override
                                    public void onLocationResult(LocationResult locationResult) {
                                        super.onLocationResult(locationResult);
                                        if(locationRequest == null) {
                                            return;
                                        }
                                        LastLocation = locationResult.getLastLocation();
                                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(LastLocation.getLatitude(), LastLocation.getLongitude()), DEFAULT_ZOOM));
                                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(LastLocation.getLatitude(), LastLocation.getLongitude()), DEFAULT_ZOOM));
                                        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
                                    }
                                };
                                fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
                            }
                        } else {
                            Toast.makeText(MapsActivity.this, "No hemos podido encontrar tu ubicación", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }





    //Maneja la api Places de google
    private void setAutoCompleteFragment () {

        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), getString(R.string.api_key));
        }
        PlacesClient placesClient = Places.createClient(this);
        AutocompleteSupportFragment autocompleteSupportFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autoComplete);
        autocompleteSupportFragment.setHint(getString(R.string.sv_buscar));

        autocompleteSupportFragment.setPlaceFields(Arrays.asList(Place.Field.ADDRESS, Place.Field.LAT_LNG));
        autocompleteSupportFragment.setCountry("UY");

        autocompleteSupportFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {

            @Override
            public void onPlaceSelected(@NonNull Place place) {

                mMap.addMarker(new MarkerOptions().position(place.getLatLng()).title(place.getAddress()));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(place.getLatLng()));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), DEFAULT_ZOOM));
                Log.i("Tag", "Lugar elegido: " + place.getAddress());
            }

            @Override
            public void onError(@NonNull Status status) {
                // TODO
            }
        });
    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

}