package com.acharcitox.telocuido;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.acharcitox.telocuido.model.Ocupar_lugar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class CalificarActivity extends AppCompatActivity implements View.OnClickListener {

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    //Declaro las variables para cada campo
    EditText editTextCalificacion, editTextComentario;

    //Variable que se va utilizar para subir los datos a firebase
    Button mButtonSubirDatosFirebase, mButtonomitir;
    RatingBar ratingstar;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calificar);



        //Se vincula boton de la activity con la variable creada aqui
        mButtonSubirDatosFirebase = findViewById(R.id.btnCalificar);

        Bundle extras = getIntent().getExtras();
        String Id_transaccion = extras.getString("id_transaccion");

        mButtonSubirDatosFirebase.setOnClickListener(this);

        editTextComentario = findViewById(R.id.eTComentario);

        mButtonSubirDatosFirebase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseReference comentarioReference = firebaseDatabase.getReference().child("Ocupar_lugar").child(Id_transaccion);

                comentarioReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if ( snapshot.exists()){
                            String comentario = editTextComentario.getText().toString();
                            comentarioReference.child("Comentario").setValue(comentario);
                            //Envio los datos de la transaccion a la proxima activity
                            Intent i = new Intent(CalificarActivity.this, DarPropinaActivity.class);
                            i.putExtra("id_transaccion", Id_transaccion);
                            startActivity(i);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        //para cargar variable con valor de las estrellas
        ratingstar = findViewById(R.id.ratingoperator);

        DatabaseReference mReference = firebaseDatabase.getInstance().getReference().child("Ocupar_lugar").child(Id_transaccion);

        ratingstar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b){
                // Cargar variable para firebase aqui
                mReference.child("Calificacion").setValue(v);

            }
        });



        //editTextCalificacion = findViewById(R.id.eTCalificacion);
        //editTextComentario = findViewById(R.id.eTComentario);



        //Ver a donde salir con este boton
        mButtonomitir = findViewById(R.id.btnOmitir);


    }


    @Override
    public void onClick(View v) {

        // Creo una variable para recibir la id de la transaccion de la activity anterior.
        Bundle extras = getIntent().getExtras();
        String Id_transaccion = extras.getString("id_transaccion");



        //arreglar el if para que valide bien
        if(editTextComentario.getText().toString().isEmpty()){

            //Muestro carte de agregado.
            Toast.makeText(this, "Debe ingresar una calificación para continuar.", Toast.LENGTH_LONG).show();

        } else{

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

}
