package com.example.price.ui.home


import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.price.*
import com.example.price.databinding.ActivityMainBinding
import com.example.price.ui.ActivityIngresarDatos
import com.example.price.ui.ActivityQuienesSomos
import com.example.price.ui.historico.ActivityHistoricos
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {

    private lateinit var toolbar:androidx.appcompat.widget.Toolbar
    private lateinit var db: FirebaseFirestore

    private lateinit var binding:ActivityMainBinding
    private val homeViewModel: HomeViewModel by viewModels()

    private lateinit var tableRecyclerView : RecyclerView
    private lateinit var homeAdapter: HomeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /*val currentOrientation = this.resources.configuration.orientation
        if (currentOrientation == Configuration.ORIENTATION_PORTRAIT) {
            this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        } else {
            this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        }*/

        db = Firebase.firestore
        val settings = firestoreSettings {
            isPersistenceEnabled = true
        }
        db.firestoreSettings = settings

        homeViewModel.onCreate(db, this)

        homeViewModel.dolaresModel.observe(this, Observer {
            tableRecyclerView = binding.homeTableRecyclerView
            homeAdapter = HomeAdapter(it)

            tableRecyclerView.layoutManager = LinearLayoutManager(this)
            tableRecyclerView.adapter = homeAdapter
        })

        homeViewModel.isLoading.observe(this, Observer {
            binding.homeProgress.isVisible = it
        })

        toolbar = binding.include.appBar
        toolbar.title = "PRICE"
        setSupportActionBar(toolbar)
    }

    /*override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //Toast.makeText(baseContext, "Landscape Mode", Toast.LENGTH_SHORT).show()
            this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            Log.d(TAG, "landscape")
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            //Toast.makeText(baseContext, "Portrait Mode", Toast.LENGTH_SHORT).show()
            this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            Log.d(TAG, "Portrait")
        }
    }*/

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menuprincipal,menu)

        return super.onCreateOptionsMenu(menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.idDolarHoyButton -> refreshCurrentActivity()
            R.id.idHistoricoButton -> activityHistoricos()
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

    fun activityHistoricos(){
        val historicos= Intent(this, ActivityHistoricos::class.java)
        startActivity(historicos)
    }

    fun activityIngresarDatos(){
        val ingresarDatos= Intent(this, ActivityIngresarDatos::class.java)
        startActivity(ingresarDatos)
    }

    fun  activityQuienesSomos(){
        val quienesSomos= Intent(this, ActivityQuienesSomos::class.java)
        startActivity(quienesSomos)
    }

}
