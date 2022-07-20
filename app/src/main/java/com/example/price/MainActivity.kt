package com.example.price


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.view.*
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
        val tableLayout: TableLayout = findViewById(R.id.tableMain)

        for(i in 0..4){
            val registro = LayoutInflater.from(this).inflate(R.layout.table_row, null, false)
            val tv1 = registro.findViewById<View>(R.id.tv1) as TextView
            val tv2 = registro.findViewById<View>(R.id.tv2) as TextView
            val tv3 = registro.findViewById<View>(R.id.tv3) as TextView
            tv1.text = dolares[i].nombre
            tv2.text = dolares[i].dato.venta.toString()
            tv3.text = dolares[i].dato.compra.toString()
            tableLayout.addView(registro)
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
        val historicos= Intent(this,ActivityHistoricos::class.java)
        startActivity(historicos)
    }

    fun activityIngresarDatos(){
        val ingresarDatos= Intent(this,ActivityIngresarDatos::class.java)
        startActivity(ingresarDatos)
    }

    fun  activityQuienesSomos(){
        val quienesSomos= Intent(this,ActivityQuienesSomos::class.java)
        startActivity(quienesSomos)
    }

}
