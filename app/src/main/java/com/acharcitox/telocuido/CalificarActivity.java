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

import java.util.HashMap;
import java.util.Map;

public class CalificarActivity extends AppCompatActivity implements View.OnClickListener {

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance ();

    //Declaro las variables para cada campo
    EditText editTextCalificacion, editTextComentario;

    //Variable que se va utilizar para subir los datos a firebase
    Button mButtonSubirDatosFirebase, mButtonomitir;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calificar);

        //Se vincula boton de la activity con la variable creada aqui
        mButtonSubirDatosFirebase = findViewById(R.id.btnCalificar);
        mButtonSubirDatosFirebase.setOnClickListener(this);

        editTextCalificacion = findViewById(R.id.eTCalificacion);
        editTextComentario = findViewById(R.id.eTComentario);

        //Ver a donde salir con este boton
        mButtonomitir = findViewById(R.id.btnOmitir);

    }


    @Override
    public void onClick(View v) {

        // Creo una variable para recibir la id de la transaccion de la activity anterior.
        Bundle extras = getIntent().getExtras();
        String Id_transaccion = extras.getString("id_transaccion");

        // Tomo los nuevos datos de los campos de texto de esta activity
        int Calificacion = Integer.parseInt(editTextCalificacion.getText().toString());
        String Comentario = editTextComentario.getText().toString();

        //Se referencia la base y nos ubicamos en el lugar correspondiente para insertar los nuevos campos.
        DatabaseReference ref = firebaseDatabase.getReference();
        DatabaseReference ocupaRef = ref.child("Ocupar_lugar");
        DatabaseReference hopperRef = ocupaRef.child(Id_transaccion);

        // Inserto los nuevos campos en la transaccion indicada.
        Map<String, Object> hopperUpdates = new HashMap<>();
        hopperUpdates.put("calificacion",(Calificacion));
        hopperUpdates.put("comentario",(Comentario));

        //Con esto agrego los nuevos campos.
        hopperRef.updateChildren(hopperUpdates);

        //Muestro carte de agregado.
        Toast.makeText(this, "Se califico correctamente.", Toast.LENGTH_LONG).show();

        //Envio los datos de la transaccion a la proxima activity
        Intent i = new Intent(this, DarPropinaActivity.class);
        i.putExtra("id_transaccion", Id_transaccion);
        startActivity(i);

    }
}
