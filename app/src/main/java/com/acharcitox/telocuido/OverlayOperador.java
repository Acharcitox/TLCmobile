package com.acharcitox.telocuido;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.acharcitox.telocuido.model.Ocupar_lugar;
import com.acharcitox.telocuido.model.Operadores;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class OverlayOperador extends AppCompatActivity {

    // Declaramos las variables
    TextView tv_operador, tv_calificacion, tv_lugares_disponibles, tv_Horario;
    Button buttonOcuparLugar;

    // Declaramos el objeto
    private DatabaseReference mRootReference;

    //PAra dar formato a las fechas
    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    Date date = new Date();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overlay_operador);

        //Aca tomo los datos de la activity anterior (mapsactivity)
        Bundle extras = getIntent().getExtras();
        String dato = extras.getString("ci_operador");
        String id_conductorMap = extras.getString("id_conductorMap");
        String nombre_conductor_over = extras.getString("Nombre_conductor_mapa");

        // Instanciamos
        tv_operador = findViewById(R.id.tvTipoOperador);
        tv_calificacion = findViewById(R.id.tvCalificacion);
        tv_lugares_disponibles = findViewById(R.id.tvLugaresDisponibles);
        tv_Horario =findViewById(R.id.tvHorario);
        buttonOcuparLugar = findViewById(R.id.btnOcuparLugar);

        // Instanciamos y hacemos referencia a la base de datos
        mRootReference = FirebaseDatabase.getInstance().getReference();

        Query q = mRootReference.child("Operadores").orderByChild("Nombre").equalTo(dato);
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot operadores : snapshot.getChildren()){
                        Operadores operador = operadores.getValue(Operadores.class);

                        String tipo_operador = operador.getTipo_operador();
                        String nombre = operador.getNombre();
                        Integer calificacion = operador.getCalificacion_promedio();
                        Integer lugares_disponibles = operador.getCantidad_restante_lugares();
                        Integer ci_operador = operador.getCI_operador();
                        Integer id_lugar_asignado = operador.getCI_operador();
                        String hora_inicio = operador.getHora_inicio_trabajo();
                        String hora_fin = operador.getHora_fin_trabajo();

                        tv_operador.setText("Ustedes eligio el " + tipo_operador + " " + nombre);
                        tv_calificacion.setText("Calificacion: "+ calificacion);
                        tv_lugares_disponibles.setText("Los lugares disponibles son: "+ lugares_disponibles);
                        tv_Horario.setText("Horario: "+ hora_inicio +"Hs a "+ hora_fin + "Hs.");

                        buttonOcuparLugar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                //Voy a crear las variables para luego grabar la transaccion
                                String Id_transaccion = UUID.randomUUID().toString();
                                String id_conductor = id_conductorMap;
                                int CI_operador = ci_operador;
                                int Id_lugar_asignado = id_lugar_asignado;
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
                                Toast.makeText(OverlayOperador.this, "Lugar ocupado correctamente!!!", Toast.LENGTH_LONG).show();

                                //Envio los datos de la transaccion a la proxima activity
                                Intent i = new Intent(OverlayOperador.this, LiberarLugarActivity.class);
                                i.putExtra("id_transaccion", Id_transaccion);
                                i.putExtra("nombre_conductor", nombre_conductor_over);

                                startActivity(i);

                            }
                        });

                }

                } else {
                    tv_operador.setText("En Contruccion. Debe elegir un Cuidacoches o un Parking.");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}