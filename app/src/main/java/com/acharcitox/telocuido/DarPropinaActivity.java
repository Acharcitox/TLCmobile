package com.acharcitox.telocuido;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DarPropinaActivity extends AppCompatActivity implements View.OnClickListener {

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance ();

    //Declaro las variables para cada campo
    EditText editTextMonto;

    //Variable que se va utilizar para subir los datos a firebase
    Button mButtonSubirDatosFirebase;

    //Para dar formato a las fechas
    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    Date date = new Date();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dar_propina);

        //Se vincula boton de la activity con la variable creada aqui
        mButtonSubirDatosFirebase = findViewById(R.id.btnDarPropina);
        mButtonSubirDatosFirebase.setOnClickListener(this);

        editTextMonto = findViewById(R.id.txtotromonto);
    }

    @Override
    public void onClick(View v) {

        // Creo una variable para recibir la id de la transaccion de la activity anterior.
        Bundle extras = getIntent().getExtras();
        String Id_transaccion = extras.getString("id_transaccion");
        String id_conductor_d = extras.getString("id_conductor_c");
        String nombre_conductor_d = extras.getString("Nombre_conductor_c");




//arreglar el if para que valide bien
 /*       if(editTextMonto.getText().toString().isEmpty()){
            //Muestro carte de agregado.
            Toast.makeText(this, "Se dio propina correctamente.", Toast.LENGTH_LONG).show();

            //Lo mando al Mapa nuevamente
            Intent i = new Intent(this, DenunciarActivity.class);
            i.putExtra("id_transaccion", Id_transaccion);
            i.putExtra("nombre_conductor_d", nombre_conductor_d);
            i.putExtra("id_conductor_d", id_conductor_d);
            startActivity(i);

        } else {
*/
            // Tomo los nuevos datos de los campos de texto de esta activity
            int Monto_propina = Integer.parseInt(editTextMonto.getText().toString());
            String Fecha_hora_propina = dateFormat.format(date);

            //Se referencia la base y nos ubicamos en el lugar correspondiente para insertar los nuevos campos.
            DatabaseReference ref = firebaseDatabase.getReference();
            DatabaseReference ocupaRef = ref.child("Ocupar_lugar");
            DatabaseReference hopperRef = ocupaRef.child(Id_transaccion);

            // Inserto los nuevos campos en la transaccion indicada.
            Map<String, Object> hopperUpdates = new HashMap<>();
            hopperUpdates.put("Monto_propina",(Monto_propina));
            hopperUpdates.put("Fecha_hora_propina",(Fecha_hora_propina));

            //Con esto agrego los nuevos campos.
            hopperRef.updateChildren(hopperUpdates);

            //Lo mando al Mapa nuevamente
            Intent i = new Intent(this, DenunciarActivity.class);
            i.putExtra("id_transaccion", Id_transaccion);
            i.putExtra("nombre_conductor", nombre_conductor_d);
            i.putExtra("id_conductor_d", id_conductor_d);
            startActivity(i);

            //Muestro carte de agregado.
            Toast.makeText(this, "Se dio propina correctamente.", Toast.LENGTH_LONG).show();

        }

    }


