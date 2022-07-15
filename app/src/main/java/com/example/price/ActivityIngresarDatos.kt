package com.example.price

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ActivityIngresarDatos : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    //Probando push
    private lateinit var toolbar:androidx.appcompat.widget.Toolbar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ingresar_datos)

        toolbar = findViewById(R.id.app_bar)
        toolbar.title = "PRICE"
        setSupportActionBar(toolbar)

        val spinner: Spinner = findViewById(R.id.spinnerIngresarDatos)
        spinner.onItemSelectedListener=this

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            this,
            R.array.tiposDolarSpinner,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }


    }


    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3001/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    //Llamada a api con Retrofit
    private fun dolarHistorico(tipoDolarPeticion:String){

        val btnIngresar = findViewById(R.id.btnIngresar) as Button

        btnIngresar.setOnClickListener {

        }

        CoroutineScope(Dispatchers.IO).launch {
            val call = getRetrofit().create(APIService::class.java)
                .getDolarHistorico("valoresHistoricosDolar/paginacion/$tipoDolarPeticion/1/10")

            val dolarHis = call.body()

            runOnUiThread {
                if (call.isSuccessful) {
                    val aux = dolarHis?.dolarHistorico ?: emptyList()
                    //println("HOLA:   ... .. ")
                } else {
                    //error
                }
            }
        }
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
            R.id.idIngresarDatosButton -> refreshCurrentActivity()
            R.id.idQuienesSomosButton -> activityQuienesSomos()
            // Toast.makeText(this,"valores Historicos paaa",Toast.LENGTH_LONG).show()
            else -> Toast.makeText(this,"ERROR",Toast.LENGTH_LONG).show()
        }

        return super.onOptionsItemSelected(item)
    }

    fun refreshCurrentActivity(){
        recreate()
    }

    fun activityDolarHoy(){
        val dolarHoy= Intent(this,MainActivity::class.java)
        startActivity(dolarHoy)
    }

    fun activityHistoricos(){
        val historicos= Intent(this,ActivityHistoricos::class.java)
        startActivity(historicos)
    }

    fun  activityQuienesSomos(){
        val quienesSomos= Intent(this,ActivityQuienesSomos::class.java)
        startActivity(quienesSomos)
    }
    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
        Toast.makeText(this, pos.toString(), Toast.LENGTH_LONG).show()
        //limpiarTABLE
        dolarHistorico(resources.getStringArray(R.array.tiposDolarPeticion)[pos])
    }
    override fun onNothingSelected(parent: AdapterView<*>) {
        // Another interface callback
    }
}