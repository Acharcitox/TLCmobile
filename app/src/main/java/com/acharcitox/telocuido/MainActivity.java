package com.acharcitox.telocuido;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.view.MenuInflater;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }



    public void Login (View v) {
        Intent i = new Intent(this, MapsActivity.class);
        startActivity(i);
    }

    public void registrarUsuario(View v) {
        Intent i = new Intent(this, RegisterActivity.class);
        startActivity(i);
    }
}