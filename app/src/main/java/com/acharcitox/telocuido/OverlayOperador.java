package com.acharcitox.telocuido;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.acharcitox.telocuido.model.Operadores;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OverlayOperador extends AppCompatActivity {

    // Declaramos las variables
    TextView tv_tipo_operador;
    TextView tv_nombre;
    TextView tv_lugares_disponibles;

    // Declaramos el objeto
    private DatabaseReference mDBOperadores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overlay_operador);

        // Instanciamos
        tv_tipo_operador = (TextView)findViewById(R.id.tvTipoOperador);
        tv_nombre = (TextView)findViewById(R.id.tvNombre);
        tv_lugares_disponibles = (TextView)findViewById(R.id.tvLugaresDisponibles);

        // Instanciamos y hacemos referencia a la base de datos
        mDBOperadores = FirebaseDatabase.getInstance().getReference();

        // Esto está crasheando
        mDBOperadores.child("Operadores").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 for (DataSnapshot operadores : dataSnapshot.getChildren()) {
                     Operadores operador = operadores.getValue(Operadores.class);

                     String tipo_operador = operador.getTipo_operador();
                     String nombre = operador.getNombre();
                     int lugares_disponibles = operador.getCantidad_restante_lugares();
                     tv_tipo_operador.setText("Tipo de operador: "+ tipo_operador);
                     tv_nombre.setText("Nombre: " + nombre);
                     tv_lugares_disponibles.setText(lugares_disponibles);

                 }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}