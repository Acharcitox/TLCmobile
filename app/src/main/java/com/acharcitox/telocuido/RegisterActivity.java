package com.acharcitox.telocuido;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.acharcitox.telocuido.model.Conductores;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    //Se crea variable para hacer referencia a la base de datos
    DatabaseReference mRootReference;

    //Declaro variables para los campos del formulario
    EditText editTextUsuario, editTextNombre, editTextApellido, editTextTelefono, editTextCorreo, editTextPass, editTextPassC;

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

        editTextUsuario = findViewById(R.id.eTUsuario);
        editTextNombre = findViewById(R.id.eTNombre);
        editTextApellido = findViewById(R.id.eTApellido);
        editTextTelefono = findViewById(R.id.eTTelefono);
        editTextCorreo = findViewById(R.id.eTCorreo);
        editTextPass = findViewById(R.id.eTPass);
        editTextPassC = findViewById(R.id.eTPassC);

        }

    //Evento click sobre el boton
    @Override
    public void onClick(View v) {

        //Vinculo las variables creadas con los campos de la base de datos.
        String Id_conductor = UUID.randomUUID().toString();
        String Nombre = editTextNombre.getText().toString();
        String Apellido = editTextApellido.getText().toString();
        String Mail = editTextCorreo.getText().toString();
        String Usuario = editTextUsuario.getText().toString();
        String Telefono = editTextTelefono.getText().toString();
        String Password = editTextPass.getText().toString();
        String PasswordC = editTextPassC.getText().toString();
        Boolean Estado = true;

            ///////Falta validacion si existe el usuario////////////
            //Validacion si coinciden las contraseñas
        //    if (editTextPass=editTextPassC) {
                //if (Nombre.equals("")|| Apellido.equals("")|| Mail.equals("")|| Usuario.equals("")|| Telefono.equals("")|| Password.equals("")|| PasswordC.equals("")){
                 //  validacion();

                Conductores c = new Conductores();

                c.setId_conductor(Id_conductor);
                c.setNombre(Nombre);
                c.setApellido(Apellido);
                c.setMail(Mail);
                c.setUsuario(Usuario);
                c.setTelefono(Telefono);
                c.setPassword(Password);
                c.setEstado(Estado);

                //Aca indico como guardar los datos en la bd, nodo persona con el nodo id dentro.
                mRootReference.child("Conductores").child(c.getId_conductor()).setValue(c);
                Toast.makeText(this, "Conductor agregado correctamente", Toast.LENGTH_LONG).show();

                //Envio los datos de la transaccion a la proxima activity
                Intent i = new Intent(this, MainActivity.class);
                startActivity(i);
       //     } else {
       //         Toast.makeText(this, "Las contraseñas no coinciden, favor de verificar", Toast.LENGTH_LONG).show();
       //    }

        }


    private void validacion() {
        String Usuario = editTextUsuario.getText().toString();
        String Nombre = editTextNombre.getText().toString();
        String Apellido = editTextApellido.getText().toString();
        String Telefono = editTextTelefono.getText().toString();
        String Correo = editTextCorreo.getText().toString();
        String Pass = editTextPass.getText().toString();
        String Pass1 = editTextPassC.getText().toString();

        if (Usuario.equals("")) {
            editTextUsuario.setError("Required");
        } else if (Nombre.equals("")) {
            editTextNombre.setError("Required");
        } else if (Apellido.equals("")) {
            editTextApellido.setError("Required");
        } else if (Telefono.equals("")) {
            editTextTelefono.setError("Required");
        } else if (Correo.equals("")) {
            editTextCorreo.setError("Required");
        } else if (Pass.equals("")) {
            editTextPass.setError("Required");
        } else if (Pass1.equals("")) {
            editTextPassC.setError("Required");
        }
    }
}