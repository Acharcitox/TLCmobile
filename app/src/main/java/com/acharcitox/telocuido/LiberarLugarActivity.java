package com.acharcitox.telocuido;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class LiberarLugarActivity extends AppCompatActivity implements View.OnClickListener {

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance ();

    //Variable que se va utilizar para subir los datos a firebase
    Button mButtonSubirDatosFirebase;
    TextView edittext_nombreusuario;

    //Para dar formato a las fechas
    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    Date date = new Date();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liberar_lugar);

        //Obtengo los datos de la activity anterios
        Bundle extras = getIntent().getExtras();
        String Nombre_conductor_liberar = extras.getString("nombre_conductor");


        //Se vincula boton de la activity con la variable creada aqui
        mButtonSubirDatosFirebase = findViewById(R.id.btnLiberarLugar);
        edittext_nombreusuario = findViewById(R.id.etNombreUsuario);

        edittext_nombreusuario.setText("Hola, "+ Nombre_conductor_liberar);

        mButtonSubirDatosFirebase.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {

        // Creo una variable para recibir la id de la transaccion de la activity anterior.
        Bundle extras = getIntent().getExtras();
        String Id_transaccion = extras.getString("id_transaccion");

        // Tomo los nuevos datos de los campos de texto de esta activity
        Boolean Esta_libre = true;
        String Fecha_hora_fin = dateFormat.format(date);

        //Se referencia la base y nos ubicamos en el lugar correspondiente para insertar los nuevos campos.
        DatabaseReference ref = firebaseDatabase.getReference();
        DatabaseReference ocupaRef = ref.child("Ocupar_lugar");
        DatabaseReference hopperRef = ocupaRef.child(Id_transaccion);

        // Inserto los nuevos campos en la transaccion indicada.
        Map<String, Object> hopperUpdates = new HashMap<>();
        hopperUpdates.put("esta_libre",(Esta_libre));
        hopperUpdates.put("fecha_hora_fin",(Fecha_hora_fin));

        //Con esto agrego los nuevos campos.
        hopperRef.updateChildren(hopperUpdates);

        //Muestro carte de agregado.
        Toast.makeText(this, "Se libero el lugar!!", Toast.LENGTH_LONG).show();

        //Envio los datos de la transaccion a la proxima activity
        Intent i = new Intent(this, CalificarActivity.class);
        i.putExtra("id_transaccion", Id_transaccion);
        startActivity(i);

    }


}
