package com.example.price.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import com.example.price.R
import com.example.price.ui.historico.ActivityHistoricos
import com.example.price.ui.home.MainActivity

class ActivityQuienesSomos : AppCompatActivity() {

    private lateinit var toolbar:androidx.appcompat.widget.Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quienes_somos)

        toolbar = findViewById(R.id.app_bar)
        toolbar.title = "PRICE"
        setSupportActionBar(toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menuprincipal,menu)

        return super.onCreateOptionsMenu(menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.idDolarHoyButton -> activityDolarHoy()
            R.id.idHistoricoButton -> activityHistoricos()
            R.id.idIngresarDatosButton -> activityIngresarDatos()
            R.id.idQuienesSomosButton -> refreshCurrentActivity()
            // Toast.makeText(this,"valores Historicos paaa",Toast.LENGTH_LONG).show()
            else -> Toast.makeText(this,"ERROR",Toast.LENGTH_LONG).show()
        }

        return super.onOptionsItemSelected(item)
    }

    fun refreshCurrentActivity(){
        recreate()
    }

    fun activityHistoricos(){
        val historicos= Intent(this, ActivityHistoricos::class.java)
        startActivity(historicos)
    }

    fun  activityDolarHoy(){
        val dolarHoy= Intent(this, MainActivity::class.java)
        startActivity(dolarHoy)
    }

    fun activityIngresarDatos(){
        val ingresarDatos= Intent(this, ActivityIngresarDatos::class.java)
        startActivity(ingresarDatos)
    }
}