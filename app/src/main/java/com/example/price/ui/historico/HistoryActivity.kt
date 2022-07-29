package com.example.price.ui.historico

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.price.ui.InputActivity
import com.example.price.R
import com.example.price.databinding.ActivityHistoryBinding
import com.example.price.ui.home.MainActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class HistoryActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private lateinit var toolbar:androidx.appcompat.widget.Toolbar
    private lateinit var db: FirebaseFirestore

    private lateinit var binding: ActivityHistoryBinding
    private val historyViewModel: HistoryViewModel by viewModels()

    private lateinit var tableRecyclerView : RecyclerView
    private lateinit var historyAdapter: HistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        toolbar = findViewById(R.id.app_bar)
        toolbar.title = "PRICE"
        setSupportActionBar(toolbar)

        val spinner: Spinner = binding.spinnerHistoricos
        spinner.onItemSelectedListener=this

        val spinnerRangoFecha = binding.spinnerRango
        spinnerRangoFecha.onItemSelectedListener=this

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


        //Adapter para spinner
        ArrayAdapter.createFromResource(
            this,
            R.array.opcionesRangoFecha,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinnerRangoFecha.adapter = adapter
        }

        db = Firebase.firestore

        //Llamada inicial
        historyViewModel.updateDollars("oficial", 7, db, this)

        historyViewModel.dollarsLiveData.observe(this, Observer {
            //Actualizo RecyclerView cuando se actualiza LiveData
            Log.d("TEST", it.toString())
            tableRecyclerView = binding.tableRecyclerView
            historyAdapter = HistoryAdapter(it)

            tableRecyclerView.layoutManager = LinearLayoutManager(this)
            tableRecyclerView.adapter = historyAdapter
        })

        historyViewModel.isLoading.observe(this, Observer {
            //Actualizo botón de carga
            binding.progress.isVisible = it
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu,menu)

        return super.onCreateOptionsMenu(menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.idDolarHoyButton -> homeActivity()
            R.id.idHistoricoButton -> refreshCurrentActivity()
            R.id.idIngresarDatosButton -> inputActivity()
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

    fun inputActivity(){
        val inputAct= Intent(this, InputActivity::class.java)
        startActivity(inputAct)
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
        //Toast.makeText(this, pos.toString(), Toast.LENGTH_LONG).show()
        when(parent.id){
            R.id.spinnerHistoricos -> historyViewModel.rangoActual.value?.let {
                historyViewModel.updateDollars(resources.getStringArray(R.array.tiposDolarPeticion)[pos],
                    it, db, this)
                Log.d("TEST", "ENTRO A spinnerHistoricos")
            }
            R.id.spinnerRango -> {
                val rango: Int
                if(parent.getItemAtPosition(pos).toString()=="Última semana"){
                    rango=7
                }else{
                    rango = 30
                }
                Log.d("TEST", "ENTRO A spinnerRango")
                historyViewModel.tipoActual.value?.let { historyViewModel.updateDollars(it, rango, db, this) }
            }
            else -> Log.d("TEST", "ERROR parent.id")
        }
        //historyViewModel.updateDollars(parent.getItemAtPosition(pos).toString(), 7, db, this)
        //historyViewModel.updateDollars(resources.getStringArray(R.array.tiposDolarPeticion)[pos], 7, db, this)
    }

    override fun onNothingSelected(parent: AdapterView<*>) {
        // Another interface callback
    }
}



