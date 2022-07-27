package com.example.price.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.example.price.R
import com.example.price.RequestBody
import com.example.price.databinding.ActivityIngresarDatosBinding
import com.example.price.network.api.DolarService
import com.example.price.ui.historico.ActivityHistoricos
import com.example.price.ui.home.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ActivityIngresarDatos : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private lateinit var toolbar:androidx.appcompat.widget.Toolbar

    private lateinit var binding:ActivityIngresarDatosBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIngresarDatosBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
    //Llamada a api con Retrofit
    private fun crearDolarHistorico(tipoDolarPeticion:String){
        val dolarService = DolarService()

        val btnIngresar = binding.btnIngresar as Button

        val inputFecha = binding.inputFecha as EditText
        val inputVenta = binding.inputVenta as EditText
        val inputCompra = binding.inputCompra as EditText

        btnIngresar.setOnClickListener {
            if(inputFecha.text.isNotEmpty() && inputVenta.text.isNotEmpty() && inputCompra.text.isNotEmpty()){
                val requestBody = RequestBody(tipoDolarPeticion, inputFecha.getText().toString(), inputVenta.getText().toString().toDouble(), inputCompra.getText().toString().toDouble())

                CoroutineScope(Dispatchers.IO).launch {
                    dolarService.postDolarHistorico(requestBody)
                }
            }else{
                Toast.makeText(this,"Ingresar todos los datos",Toast.LENGTH_LONG).show()
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
        val dolarHoy= Intent(this, MainActivity::class.java)
        startActivity(dolarHoy)
    }

    fun activityHistoricos(){
        val historicos= Intent(this, ActivityHistoricos::class.java)
        startActivity(historicos)
    }

    fun  activityQuienesSomos(){
        val quienesSomos= Intent(this, ActivityQuienesSomos::class.java)
        startActivity(quienesSomos)
    }
    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
        Toast.makeText(this, pos.toString(), Toast.LENGTH_LONG).show()
        //limpiarTABLE
        crearDolarHistorico(resources.getStringArray(R.array.tiposDolarPeticion)[pos])
    }
    override fun onNothingSelected(parent: AdapterView<*>) {
        // Another interface callback
    }
}