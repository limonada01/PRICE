package com.example.price


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
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

        dolarInicio()
    }

    //Retrofit
    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3001/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    //Llamada a api con Retrofit
    private fun dolarInicio(){
        CoroutineScope(Dispatchers.IO).launch {
            val call = getRetrofit().create(APIService::class.java)
                .getDolaresInicio("valorTiposDeDolarHoy")
            val dolaresInicio = call.body()
            runOnUiThread {
                if (call.isSuccessful) {
                    val aux = dolaresInicio?.dolares ?: emptyList()

                    addTable(aux)
                } else {
                    //error
                }
            }
        }
    }

    //Agregar datos a la tabla
    private fun addTable(dolares: List<Dolar>) {
        val tableLayout: TableLayout = findViewById(R.id.tableHistoricos)

        //Lleno tabla dinamicamente
        for(i in 0..4) {
            val tableRow = TableRow(this)


            //Lleno columna 1
            val textView1 = TextView(this)
            textView1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14.0F)

            textView1.setText(dolares[i].nombre)
            tableRow.addView(textView1)

            val textView2 = TextView(this)
            textView2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14.0F)
            textView2.setText(dolares[i].venta.toString())
            tableRow.addView(textView2)

            val textView3 = TextView(this)
            textView3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14.0F)
            textView3.setText(dolares[i].compra.toString())
            tableRow.addView(textView3)

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
