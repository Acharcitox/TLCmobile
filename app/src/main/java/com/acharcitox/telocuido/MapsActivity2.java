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

public class MapsActivity2 extends FragmentActivity implements OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener {

    // ES un MAPA PARA PROBAR, luego debemos eliminarlo.

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
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        mapView = mapFragment.getView();

        // Esto para traer los datos de los operadores de firebase
        mRootReference = FirebaseDatabase.getInstance().getReference();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MapsActivity2.this);

        setAutoCompleteFragment();
    }



    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setMyLocationEnabled(true);/*Como ya tengo los permisos en otra activity es solo poner en true*/
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setTrafficEnabled(true);



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
                    String hora_inicio = operador.getHora_inicio_trabajo();
                    String hora_fin = operador.getHora_fin_trabajo();
                    String tipo_operador = operador.getTipo_operador();
                    Integer calificacion = operador.getCalificacion_promedio();
                    Integer cantidadLugares = operador.getCantidad_restante_lugares();
                    Integer ci_operadorMapa = operador.getCI_operador();
                    Integer id_lugarAsignado = operador.getId_lugar_asignado();
                    MarkerOptions markerOptions = new MarkerOptions();

                    //Aca decido como mostrar el icono en google (.snippet puede agregar una descripcion chica .icon para agregar un icono)
                    //Decido pasarle la posicion, el titulo y mas datos dependiendo si es cuidacoches o un estacionamiento.
                    if(tipo_operador.equals("Cuidacoches")){

                        markerOptions.position(new LatLng(latitud,longitud))
                                .title(nombre+"Calificacion: "+calificacion)
                                .snippet("Lugares Disponibles: "+cantidadLugares + "Horario: "+ hora_inicio +"Hs a "+ hora_fin+ "Hs")
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));



//ZOOM para probar y ver los puntos en el momento

                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitud, longitud), 17));

                    } else {
                        markerOptions.position(new LatLng(latitud,longitud))
                                .title(nombre)
                                .snippet("Lugares Disponibles: "+cantidadLugares+"/N "+ "Horario: "+ hora_inicio +"Hs a "+ hora_fin+ "Hs")
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                    }

                    if (cantidadLugares.equals(1) && tipo_operador.equals("Cuidacoches")) {
                        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_lugar_1));
                    }
                    if (cantidadLugares.equals(2) && tipo_operador.equals("Cuidacoches")) {
                        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_lugar_2));
                    }
                    if (cantidadLugares.equals(3) && tipo_operador.equals("Cuidacoches")) {
                        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_lugar_3));
                    }
                    if (cantidadLugares.equals(4) && tipo_operador.equals("Cuidacoches")) {
                        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_lugar_4));
                    }
                    if (cantidadLugares.equals(5) && tipo_operador.equals("Cuidacoches")) {
                        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_lugar_5));
                    }
                    if (cantidadLugares.equals(6) && tipo_operador.equals("Cuidacoches")) {
                        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_lugar_6));
                    }
                    if (cantidadLugares.equals(7) && tipo_operador.equals("Cuidacoches")) {
                        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_lugar_7));
                    }
                    if (cantidadLugares.equals(8) && tipo_operador.equals("Cuidacoches")) {
                        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_lugar_8));
                    }
                    if (cantidadLugares.equals(9) && tipo_operador.equals("Cuidacoches")) {
                        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_lugar_9));
                    }
                    if (cantidadLugares.equals(10) && tipo_operador.equals("Cuidacoches")) {
                        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_lugar_10));
                    }
                    if (cantidadLugares.equals(11) && tipo_operador.equals("Cuidacoches")) {
                        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_lugar_11));
                    }
                    if (cantidadLugares.equals(12) && tipo_operador.equals("Cuidacoches")) {
                        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_lugar_12));
                    }
                    if (cantidadLugares.equals(13) && tipo_operador.equals("Cuidacoches")) {
                        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_lugar_13));
                    }
                    if (cantidadLugares.equals(14) && tipo_operador.equals("Cuidacoches")) {
                        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_lugar_14));
                    }
                    if (cantidadLugares.equals(15) && tipo_operador.equals("Cuidacoches")) {
                        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_lugar_15));
                    }

                    //Aca agrego las marcas al mapa, cada punto es la longitud y latitud de la tabla operadores.
                    tmpRealTimeMarker.add(mMap.addMarker(markerOptions));

                    mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(Marker marker) {

                            //for(DataSnapshot operadores : datasnapshot.getChildren()){
                            //Para hacer doble click y elegir lugar.
                            if (doubleBackToExitPressedOnce){
                                Intent i = new Intent(MapsActivity2.this, OverlayOperador.class);
                                // pasamos los datos de operador a un nuevo intent-
                                i.putExtra("ci_operador", marker.getId());
                                i.putExtra("id_lugar_asignado", id_lugarAsignado);
                                startActivity(i);
                            } else {
                                doubleBackToExitPressedOnce = true;
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        doubleBackToExitPressedOnce = false;
                                    }
                                }, 1000);
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





        /*googleMap.setOnMarkerClickListener(this);

        int height = 80;
        int width = 80;
        /*BitmapDrawable bitmapDrawable = (BitmapDrawable) getResources().getDrawable(R.drawable.icon_prueba_cuidacoches);
        Bitmap b = bitmapDrawable.getBitmap();*/
        /*final Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);*/
        /*mOperadores.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot s : dataSnapshot.getChildren()) {
                    operadoresPojo operadores = s.getValue(operadoresPojo.class);
                    LatLng latLng = new LatLng(operadores.Latitud, operadores.Longitude);
                    mMap.addMarker(new MarkerOptions().position(latLng).title(operadores.Nombre)).setIcon(BitmapDescriptorFactory.defaultMarker());
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/


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

        SettingsClient settingsClient = LocationServices.getSettingsClient(MapsActivity2.this);
        Task<LocationSettingsResponse> task = settingsClient.checkLocationSettings(builder.build());

        task.addOnSuccessListener(MapsActivity2.this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override //si el Gps está encendido se llama esta función
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                getUserLocation();
            }
        });

        task.addOnFailureListener(MapsActivity2.this, new OnFailureListener() {
            @Override //si el Gps NO está encendido se llama esta función
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                    try {
                        resolvableApiException.startResolutionForResult(MapsActivity2.this, 51);
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
                            Toast.makeText(MapsActivity2.this, "No hemos podido encontrar tu ubicación", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }



    private void Cuidacoches(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng manolo = new LatLng(-34.906035, -56.20038);
        LatLng pocho = new LatLng(-34.906031, -56.20094);
        LatLng joselo = new LatLng(-34.90664, -56.19870);

        //mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mMap.addMarker(new MarkerOptions().position(manolo).title(" MANOLO "));
        mMap.addMarker(new MarkerOptions().position(pocho).title("POCHO "));
        mMap.addMarker(new MarkerOptions().position(joselo).title("JOSELO "));

        //mMap.moveCamera(CameraUpdateFactory.newLatLng(montevideo));
        /*mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(manolo, 16));*/
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

                mMap.clear();
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

    /*private void operadores (GoogleMap googleMap) {
        mMap = googleMap;

        googleMap.setOnMarkerClickListener(this);

        int height = 80;
        int width = 80;
        BitmapDrawable bitmapDrawable = (BitmapDrawable) getResources().getDrawable(R.drawable.icon_prueba_cuidacoches);
        Bitmap b = bitmapDrawable.getBitmap();
        final Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
        mOperadores.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot s : dataSnapshot.getChildren()) {
                    operadoresPojo operadores = s.getValue(operadoresPojo.class);
                    LatLng latLng = new LatLng(operadores.Latitud, operadores.Longitude);
                    mMap.addMarker(new MarkerOptions().position(latLng).title(operadores.Nombre)).setIcon(BitmapDescriptorFactory.fromBitmap(smallMarker));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }*/

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

}