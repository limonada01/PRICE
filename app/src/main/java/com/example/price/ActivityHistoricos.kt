package com.example.price

import android.app.Activity
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

class ActivityHistoricos : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    //Probando push
    private lateinit var toolbar:androidx.appcompat.widget.Toolbar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_historicos)

        toolbar = findViewById(R.id.app_bar)
        toolbar.title = "PRICE"
        setSupportActionBar(toolbar)

        val spinner: Spinner = findViewById(R.id.spinnerHistoricos)
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

        CoroutineScope(Dispatchers.IO).launch {
            val call = getRetrofit().create(APIService::class.java)
                .getDolarHistorico("valoresHistoricosDolar/paginacion/$tipoDolarPeticion/1/10")

            val dolarHis = call.body()

            runOnUiThread {
                if (call.isSuccessful) {
                    val aux = dolarHis?.dolarHistorico ?: emptyList()
                    //println("HOLA:   ... .. ")
                    addTable(aux)
                } else {
                    //error
                }
            }
        }
    }
    //Agregar datos a la tabla
    private fun addTable(dolarHistorico: List<DolarHistorico>) {
        val tableHistoricos: TableLayout = findViewById(R.id.tableHistoricos)

        for(i in 0..8){
            val registro = LayoutInflater.from(this).inflate(R.layout.table_row, null, false)
            val tv1 = registro.findViewById<View>(R.id.tv1) as TextView
            val tv2 = registro.findViewById<View>(R.id.tv2) as TextView
            val tv3 = registro.findViewById<View>(R.id.tv3) as TextView
            tv1.text = dolarHistorico[i].fecha
            tv2.text = dolarHistorico[i].venta.toString()
            tv3.text = dolarHistorico[i].compra.toString()
            tableHistoricos.addView(registro)
        }
        //Lleno tabla dinamicamente
        /*for(i in 0..8) {
            val tableRow = TableRow(this)


            //Lleno columna 1
            val textView1 = TextView(this)
            textView1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14.0F)

            textView1.setText(dolarHistorico[i].fecha+"                         ")
            tableRow.addView(textView1)

            val textView2 = TextView(this)
            textView2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14.0F)
            textView2.setText(dolarHistorico[i].venta.toString()+"                     ")
            tableRow.addView(textView2)

            val textView3 = TextView(this)
            textView3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14.0F)
            textView3.setText(dolarHistorico[i].compra.toString())
            tableRow.addView(textView3)

            tableHistoricos.addView(tableRow)
        }*/

    }

    fun limpiarTableHistoricos(){
        val tableHistoricos: TableLayout = findViewById(R.id.tableHistoricos)
        if(tableHistoricos.childCount>1) {
            tableHistoricos.removeViews(1, 9)
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
        val dolarHoy= Intent(this,MainActivity::class.java)
        startActivity(dolarHoy)
    }

    fun activityIngresarDatos(){
        val ingresarDatos= Intent(this,ActivityIngresarDatos::class.java)
        startActivity(ingresarDatos)
    }

    fun  activityQuienesSomos(){
        val quienesSomos= Intent(this,ActivityQuienesSomos::class.java)
        startActivity(quienesSomos)
    }
    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
        Toast.makeText(this, pos.toString(), Toast.LENGTH_LONG).show()
        //limpiarTABLA
        limpiarTableHistoricos()
        dolarHistorico(resources.getStringArray(R.array.tiposDolarPeticion)[pos])


    }

    override fun onNothingSelected(parent: AdapterView<*>) {
        // Another interface callback
    }
}



