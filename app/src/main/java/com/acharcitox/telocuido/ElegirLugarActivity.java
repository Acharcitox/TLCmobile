package com.acharcitox.telocuido;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.acharcitox.telocuido.model.Ocupar_lugar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class ElegirLugarActivity extends AppCompatActivity implements View.OnClickListener {

    //Se crea variable para hacer referencia a la base de datos
    DatabaseReference mRootReference;

    //PAra dar formato a las fechas
    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    Date date = new Date();

    //Variable que se va utilizar para subir los datos a firebase
    Button mButtonSubirDatosFirebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocupar_lugar);

        //Se vincula boton de la activity con la variable creada aqui
        mButtonSubirDatosFirebase = findViewById(R.id.btnSubirDatos);
        mButtonSubirDatosFirebase.setOnClickListener(this);

        //Aqui se utiliza la variable creada para hacer referencia a la base de datos.
        // Se inicializa la base de datos
        mRootReference = FirebaseDatabase.getInstance().getReference();
    }

    //Evento click sobre el boton
    @Override
    public void onClick(View v) {

        //Recibo campos de login
        Bundle extras = getIntent().getExtras();
        //String nombre_conductor = extras.getString("nombre_conductor");
        //String apellido_conductor = extras.getString("apellido_conductor");
        String id_conductor_conductor = extras.getString("id_conductor_conductor");

        //Vinculo las variables creadas con los campos de la base de datos.
        String Id_transaccion = UUID.randomUUID().toString();
        String id_conductor = id_conductor_conductor;
        int CI_operador = 10000;
        String Id_lugar_asignado = "50000";
        Boolean Esta_libre = false;

        //Ver como hacer que el dato sea fecha
        String Fecha_hora_inicio = dateFormat.format(date);

        //Creamos un metodo para crear un nuevo nodo dentro de la bd, en la "tabla" Ocupar_lugar, mediante Map,
        // para cargar todos los datos de una.
        Ocupar_lugar o = new Ocupar_lugar();

        o.setId_transaccion(Id_transaccion);
        o.setCI_operador(CI_operador);
        o.setId_conductor(id_conductor);
        o.setId_lugar_asignado(Id_lugar_asignado);
        o.setEsta_libre(Esta_libre);
        o.setFecha_hora_inicio(Fecha_hora_inicio);

        //Aca indico como guardar los datos en la bd, nodo persona con el nodo id dentro.
        mRootReference.child("Ocupar_lugar").child(o.getId_transaccion()).setValue(o);
        Toast.makeText(this, "Lugar ocupado.", Toast.LENGTH_LONG).show();

        //Envio los datos de la transaccion a la proxima activity
        Intent i = new Intent(this, LiberarLugarActivity.class);
        i.putExtra("id_transaccion", Id_transaccion);
        startActivity(i);
    }
}
