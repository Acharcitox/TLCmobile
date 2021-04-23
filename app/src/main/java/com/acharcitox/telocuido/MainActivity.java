package com.acharcitox.telocuido;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.acharcitox.telocuido.model.Conductores;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends AppCompatActivity {

    //Se crea variable para hacer referencia a la base de datos
    DatabaseReference mRootReference = FirebaseDatabase.getInstance().getReference();
    DatabaseReference refDatos = mRootReference;

    //Variable que se va utilizar para consultar los datos a firebase
    Button mButtonLoginFirebase;
    EditText eTcontrasena, eTcorreo;
    TextView textVId_con;

    Button irMapa;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Vinculo los campos del layout con las variables
        mButtonLoginFirebase = findViewById(R.id.btnLogin);
        eTcorreo = findViewById(R.id.eTcorreoLogin);
        eTcontrasena = findViewById(R.id.eTcontrasenaLogin);
        textVId_con = findViewById(R.id.tvId_con);

        irMapa = findViewById(R.id.btnIrMapa);



        // Genero el evento al hacer click en el boton iniciar sesion
        mButtonLoginFirebase.setOnClickListener(v -> {
            //Transformo los datos ingresados en variables string
            String editTcorreo = eTcorreo.getText().toString();
            String editTcontrasena = eTcontrasena.getText().toString();

            //Cierro el teclado
            cerrarTeclado();

            //Limpio los textbox
            limpiarCajas();
            if (editTcorreo.equals("") || editTcontrasena.equals("")) {
                validacion();
            } else {

                //Poner variable en String para pasar la consulta
                Query q = refDatos.child("Conductores").orderByChild("Mail").equalTo(editTcorreo);
                q.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {

                            //Utilizo un for para recorrer el dataSnapshot
                            for (DataSnapshot conductor : dataSnapshot.getChildren()) {
                                Conductores conductorSeleccionado = conductor.getValue(Conductores.class);
                                String passw = conductorSeleccionado.getPassword();
                                String nombre = conductorSeleccionado.getNombre();
                               // String apellido = conductorSeleccionado.getApellido();
                                String id_conductor = conductorSeleccionado.getId_conductor();


                                //Utilizo el if para verficar si la contraseña ingresada es correcta
                                if (passw.equals(editTcontrasena)) {
                                    textVId_con.setText("Bienvenido a la mejor aplicacion!");

                                    //Paso los datos a la nueva activity, deberia ser el mapa
                                    Intent i = new Intent(MainActivity.this, PermisosActivity.class);
                                    i.putExtra("nombre_conductor", nombre);
                                  //  i.putExtra("apellido_conductor", apellido);
                                    i.putExtra("id_conductor_conductor", id_conductor);
                                    startActivity(i);

                                } else {
                                    limpiarCajas();
                                    textVId_con.setText("Usuario o contraseña incorrecta, intente nuevamente");
                                }

                            }
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });


    }


    public void registrarUsuario(View v) {
        Intent i = new Intent(this, RegisterActivity.class);
        startActivity(i);
    }

    //Metodo para validar campos vacios
    private void validacion() {
        String correo_ingresado = eTcorreo.getText().toString();
        String password_ingresada = eTcontrasena.getText().toString();

        if(correo_ingresado.equals("")){
            eTcorreo.setError("Required");
        }
        else if (password_ingresada.equals("")){
            eTcontrasena.setError("Required");
        }
    }

    //Metodo para limpiar textbox
    private void limpiarCajas() {
        eTcorreo.setText("");
        eTcontrasena.setText("");
        textVId_con.setText("");
    }

    //MEtodo para cerrar el teclado lugo que hagan click en iniciar sesion
    public void cerrarTeclado(){
        View view = this.getCurrentFocus();
        if(view!= null){
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(),0);
        }

    }

    public void irMapa (View v) {
        Intent i = new Intent(this, MapsActivity2.class);
        startActivity(i);
    }

}