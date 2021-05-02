package com.acharcitox.telocuido;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class DenunciarActivity extends AppCompatActivity   {

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance ();

    //Declaro las variables para cada campo
    EditText editTextTipo_Denuncia, editTextDenuncia;
    TextView textViewnombeusuario;

    //Variable que se va utilizar para subir los datos a firebase
    Button mButtonSubirDatosFirebase, buttonOmitir;


//    String id_conductor = extras.getString("id_conductor_conductor");
 //   String nombre_conductor = extras.getString("nombre_conductor");

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_denunciar);

        // Creo una variable para recibir la id de la transaccion de la activity anterior.
        Bundle extras = getIntent().getExtras();
        String Id_transaccion = extras.getString("id_transaccion");
        String nombre_conductor_de = extras.getString("nombre_conductor_d");
        String id_conductor_de = extras.getString("id_conductor_d");

        //Se vincula boton de la activity con la variable creada aqui
        mButtonSubirDatosFirebase = findViewById(R.id.btnDenunciar);
        buttonOmitir = findViewById(R.id.btnOmitir);

        editTextTipo_Denuncia = findViewById(R.id.eTTipo_Denuncia);
        editTextDenuncia = findViewById(R.id.txtdenuncua);
        textViewnombeusuario = findViewById(R.id.nombeusr);

        textViewnombeusuario.setText(""+nombre_conductor_de);

        mButtonSubirDatosFirebase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseReference denunciaReference = firebaseDatabase.getReference().child("Ocupar_lugar").child(Id_transaccion);

                denunciaReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.exists()) {

                            // Tomo los nuevos datos de los campos de texto de esta activity
                            String Tipo_denuncia = editTextTipo_Denuncia.getText().toString();
                            String Texto_denuncia = editTextDenuncia.getText().toString();

                            // Inserto los nuevos campos en la transaccion indicada.
                            denunciaReference.child("Tipo_denuncia").setValue(Tipo_denuncia);
                            denunciaReference.child("Texto_denuncia").setValue(Texto_denuncia);

                            //Envio los datos de la transaccion a la proxima activity
                            Intent i = new Intent(DenunciarActivity.this, MapsActivity.class);
                            i.putExtra("Nombre_conductor_mapa", nombre_conductor_de);
                            i.putExtra("id_conductorMap", id_conductor_de);
                            startActivity(i);

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }

                });

            }
    });

        buttonOmitir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                //Envio los datos de la transaccion a la proxima activity
                Intent i = new Intent(DenunciarActivity.this, MapsActivity.class);
                i.putExtra("Nombre_conductor_mapa", nombre_conductor_de);
                i.putExtra("id_conductorMap", id_conductor_de);
                startActivity(i);

                //Muestro carte de agregado.
                Toast.makeText(DenunciarActivity.this, "Se omitio el ingreso de la denuncia.", Toast.LENGTH_LONG).show();

            }
        });


}

}

