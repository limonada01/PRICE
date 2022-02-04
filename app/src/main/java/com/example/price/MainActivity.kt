package com.example.price


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity() {

    private lateinit var toolbar:androidx.appcompat.widget.Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar = findViewById(R.id.app_bar)
        toolbar.title = "PRICE"
        setSupportActionBar(toolbar)

        addTable()
    }

    //Retrofit
    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("localhost:3000/api")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    //Llamada a api con Retrofit
    private fun dolarInicio(){
        CoroutineScope(Dispatchers.IO).launch {
            val call = getRetrofit().create(APIService::class.java).getDolaresInicio("/valorTiposDeDolarHoy")
            val dolaresInicio = call.body()
            if(call.isSuccessful){
                //show
            }else{
                //error
            }
        }
    }

    //Agregar datos a la tabla
    private fun addTable() {
        val tableLayout: TableLayout = findViewById(R.id.tableMain)

        //Acá iría llamada a dolarInicio() que devuelve body

        //Lleno tabla dinamicamente
        for(i in 0..4) {
            val tableRow = TableRow(this)

            //Lleno columna 1
            val textView1 = TextView(this)
            textView1.setText("info dolar prueba")
            tableRow.addView(textView1)

            //Llenar resto de columnas

            tableLayout.addView(tableRow)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menuprincipal,menu)

        return super.onCreateOptionsMenu(menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.idDolarHoyButton -> refreshCurrentActivity()
            R.id.idHistoricoButton -> activityHistoricos()
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
        val historicos= Intent(this,ActivityHistoricos::class.java)
        startActivity(historicos)
    }

    fun  activityQuienesSomos(){
        val quienesSomos= Intent(this,ActivityQuienesSomos::class.java)
        startActivity(quienesSomos)
    }

}
