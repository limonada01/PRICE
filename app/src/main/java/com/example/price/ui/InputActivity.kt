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
import com.example.price.databinding.ActivityInputBinding
import com.example.price.network.api.DollarService
import com.example.price.ui.historico.HistoryActivity
import com.example.price.ui.home.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class InputActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private lateinit var toolbar:androidx.appcompat.widget.Toolbar

    private lateinit var binding:ActivityInputBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInputBinding.inflate(layoutInflater)
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
        val dollarService = DollarService()

        val btnIngresar = binding.btnIngresar as Button

        val inputFecha = binding.inputFecha as EditText
        val inputVenta = binding.inputVenta as EditText
        val inputCompra = binding.inputCompra as EditText

        btnIngresar.setOnClickListener {
            if(inputFecha.text.isNotEmpty() && inputVenta.text.isNotEmpty() && inputCompra.text.isNotEmpty()){
                val requestBody = RequestBody(tipoDolarPeticion, inputFecha.getText().toString(), inputVenta.getText().toString().toDouble(), inputCompra.getText().toString().toDouble())

                CoroutineScope(Dispatchers.IO).launch {
                    dollarService.postDollarHistory(requestBody)
                }
            }else{
                Toast.makeText(this,"Ingresar todos los datos",Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu,menu)

        return super.onCreateOptionsMenu(menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.idDolarHoyButton -> homeActivity()
            R.id.idHistoricoButton -> historyActivity()
            R.id.idIngresarDatosButton -> refreshCurrentActivity()
            else -> Toast.makeText(this,"ERROR",Toast.LENGTH_LONG).show()
        }

        return super.onOptionsItemSelected(item)
    }

    fun refreshCurrentActivity(){
        recreate()
    }

    fun homeActivity(){
        val homeAct= Intent(this, MainActivity::class.java)
        startActivity(homeAct)
    }

    fun historyActivity(){
        val historyAct= Intent(this, HistoryActivity::class.java)
        startActivity(historyAct)
    }
    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
        Toast.makeText(this, pos.toString(), Toast.LENGTH_LONG).show()
        crearDolarHistorico(resources.getStringArray(R.array.tiposDolarPeticion)[pos])
    }
    override fun onNothingSelected(parent: AdapterView<*>) {
        // Another interface callback
    }
}