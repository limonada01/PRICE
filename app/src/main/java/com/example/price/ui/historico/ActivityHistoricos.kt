package com.example.price.ui.historico

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.price.ui.ActivityIngresarDatos
import com.example.price.ui.ActivityQuienesSomos
import com.example.price.R
import com.example.price.databinding.ActivityHistoricosBinding
import com.example.price.ui.home.MainActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ActivityHistoricos : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private lateinit var toolbar:androidx.appcompat.widget.Toolbar
    private lateinit var db: FirebaseFirestore

    private lateinit var binding: ActivityHistoricosBinding
    private val historicosViewModel: HistoricosViewModel by viewModels()

    private lateinit var tableRecyclerView : RecyclerView
    private lateinit var historicosAdapter: HistoricosAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoricosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        toolbar = findViewById(R.id.app_bar)
        toolbar.title = "PRICE"
        setSupportActionBar(toolbar)

        val spinner: Spinner = binding.spinnerHistoricos
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

        db = Firebase.firestore

        historicosViewModel.dolaresHistoricos("oficial", db, this)

        historicosViewModel.dolaresModel.observe(this, Observer {
            tableRecyclerView = binding.tableRecyclerView
            historicosAdapter = HistoricosAdapter(it)

            tableRecyclerView.layoutManager = LinearLayoutManager(this)
            tableRecyclerView.adapter = historicosAdapter
        })

        historicosViewModel.isLoading.observe(this, Observer {
            binding.progress.isVisible = it
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menuprincipal,menu)

        return super.onCreateOptionsMenu(menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.idDolarHoyButton -> activityDolarHoy()
            R.id.idHistoricoButton -> refreshCurrentActivity()
            R.id.idIngresarDatosButton -> activityIngresarDatos()
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

    fun activityIngresarDatos(){
        val ingresarDatos= Intent(this, ActivityIngresarDatos::class.java)
        startActivity(ingresarDatos)
    }

    fun  activityQuienesSomos(){
        val quienesSomos= Intent(this, ActivityQuienesSomos::class.java)
        startActivity(quienesSomos)
    }
    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
        Toast.makeText(this, pos.toString(), Toast.LENGTH_LONG).show()
        historicosViewModel.dolaresHistoricos(resources.getStringArray(R.array.tiposDolarPeticion)[pos], db, this)
    }

    override fun onNothingSelected(parent: AdapterView<*>) {
        // Another interface callback
    }
}



