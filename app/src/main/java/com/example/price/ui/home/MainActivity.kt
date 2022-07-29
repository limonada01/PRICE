package com.example.price.ui.home


import android.content.Intent
import android.os.Bundle
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
import com.example.price.ui.InputActivity
import com.example.price.ui.historico.HistoryActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
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

        db = Firebase.firestore

        homeViewModel.onCreate(db, this)

        homeViewModel.dollarsLiveData.observe(this, Observer {
            //Actualizo RecyclerView
            tableRecyclerView = binding.homeTableRecyclerView
            homeAdapter = HomeAdapter(it)

            tableRecyclerView.layoutManager = LinearLayoutManager(this)
            tableRecyclerView.adapter = homeAdapter
        })

        homeViewModel.isLoading.observe(this, Observer {
            //Actualizo botón de carga
            binding.homeProgress.isVisible = it
        })

        toolbar = binding.include.appBar
        toolbar.title = "PRICE"
        setSupportActionBar(toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu,menu)

        return super.onCreateOptionsMenu(menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.idDolarHoyButton -> refreshCurrentActivity()
            R.id.idHistoricoButton -> historyActivity()
            R.id.idIngresarDatosButton -> inputActivity()
            else -> Toast.makeText(this,"ERROR",Toast.LENGTH_LONG).show()
        }

        return super.onOptionsItemSelected(item)
    }

    fun refreshCurrentActivity(){
        recreate()
    }

    fun historyActivity(){
        val historyAct= Intent(this, HistoryActivity::class.java)
        startActivity(historyAct)
    }

    fun inputActivity(){
        val inputAct= Intent(this, InputActivity::class.java)
        startActivity(inputAct)
    }

}
