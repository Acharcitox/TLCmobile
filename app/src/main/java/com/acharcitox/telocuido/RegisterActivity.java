package com.acharcitox.telocuido;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.acharcitox.telocuido.model.Conductores;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.UUID;


public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    //Se crea variable para hacer referencia a la base de datos
    DatabaseReference mRootReference;

    //Declaro variables para los campos del formulario
    EditText editTextNombre, editTextApellido, editTextTelefono, editTextCorreo, editTextPass, editTextPassC;
    TextView textVmensaje;

    //Variable que se va utilizar para subir los datos a firebase
    Button mButtonSubirDatosFirebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        // Initialize Firebase connections
        mRootReference = FirebaseDatabase.getInstance().getReference();

        //Se vincula boton de la activity con la variable creada aqui
        mButtonSubirDatosFirebase = findViewById(R.id.btnCrearUsuario);
        mButtonSubirDatosFirebase.setOnClickListener(this);

        editTextNombre = findViewById(R.id.eTNombre);
        editTextApellido = findViewById(R.id.eTApellido);
        editTextTelefono = findViewById(R.id.eTTelefono);
        editTextCorreo = findViewById(R.id.eTCorreo);
        editTextPass = findViewById(R.id.eTPass);
        editTextPassC = findViewById(R.id.eTPassC);
        textVmensaje = findViewById(R.id.tVmensaje);
        }

    //Evento click sobre el boton
    @Override
    public void onClick(View v) {
        //Transformo los datos ingresados en variables string

        //Vinculo las variables creadas con los campos de la base de datos.
        String Id_conductor = UUID.randomUUID().toString();
        String Nombre = editTextNombre.getText().toString();
        String Apellido = editTextApellido.getText().toString();
        String Mail_ingresado = editTextCorreo.getText().toString();
        String Telefono = editTextTelefono.getText().toString();
        String Password = editTextPass.getText().toString();
        String PasswordC = editTextPassC.getText().toString();
        Boolean Estado = true;

        //Cierro el teclado
        cerrarTeclado();

        //Validacion si esta vacio
        if (Nombre.equals("")|| Mail_ingresado.equals("")|| Telefono.equals("")|| Password.equals("")|| PasswordC.equals("")){
            validacion_vacio();
        } else {
            Query q = mRootReference.child("Conductores").orderByChild("Mail").equalTo(Mail_ingresado);
            q.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    //Si me da un resultado la query anterior aviso que el correo ya existe
                    if (snapshot.exists()) {
                                textVmensaje.setText("El Mail ingresado ya existe en los registros de Te Lo Cuido.");
                        } else {
                                // VERIFICO SI LAS CONTRASEÑAS COINCIDEN
                                if (Password.equals(PasswordC)){
                                    // cargo la informacion para realizar el insert en la base de datos.
                                    Conductores c = new Conductores();

                                    c.setId_conductor(Id_conductor);
                                    c.setUsuario(Mail_ingresado);
                                    c.setNombre(Nombre);
                                    c.setApellido(Apellido);
                                    c.setMail(Mail_ingresado);
                                    c.setTelefono(Telefono);
                                    c.setPassword(Password);
                                    c.setEstado(Estado);

                                    //Aca indico como guardar los datos en la bd, nodo persona con el nodo id dentro.
                                    mRootReference.child("Conductores").child(c.getId_conductor()).setValue(c);
                                    textVmensaje.setText("Conductor agregado correctamente! Redirigiendo al login.");
                                    limpiar_todo();

                                    //Para esperar unos segundos en pasar a la siguiente pantalla.
                                    new Handler().postDelayed(() -> {
                                        //Envio los datos de la transaccion a la proxima activity
                                        Intent i = new Intent(RegisterActivity.this, MainActivity.class);
                                        startActivity(i);
                                    }, 2000); //3 segundos


                                }else{
                                    textVmensaje.setText("Las contraseñas no coinciden, favor de verificar");
                                    limpiarCajasPass();
                                }

                            }
                    }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }

            });

        }
        }


        //Validar que los campos tengan datos
    private void validacion_vacio() {
        String Nombre = editTextNombre.getText().toString();
        String Telefono = editTextTelefono.getText().toString();
        String Correo = editTextCorreo.getText().toString();
        String Pass = editTextPass.getText().toString();
        String Pass1 = editTextPassC.getText().toString();

        if (Nombre.equals("")) {
            editTextNombre.setError("Required");
        }  else if (Telefono.equals("")) {
            editTextTelefono.setError("Required");
        } else if (Correo.equals("")) {
            editTextCorreo.setError("Required");
        } else if (Pass.equals("")) {
            editTextPass.setError("Required");
        } else if (Pass1.equals("")) {
            editTextPassC.setError("Required");
        }
    }

    //Metodo para limpiar textbox
    private void limpiarCajasPass() {
        editTextPass.setText("");
        editTextPassC.setText("");
    }

    //Metodo para limpiar textbox
    private void limpiar_todo() {
        editTextCorreo.setText("");
        editTextTelefono.setText("");
        editTextApellido.setText("");
        editTextNombre.setText("");
        editTextPass.setText("");
        editTextPassC.setText("");
    }

    //MEtodo para cerrar el teclado lugo que hagan click en iniciar sesion
    public void cerrarTeclado(){
        View view = this.getCurrentFocus();
        if(view!= null){
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(),0);
        }
    }

}