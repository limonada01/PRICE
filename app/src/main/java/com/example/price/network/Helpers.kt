package com.example.price.network

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import com.example.price.Dolar
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import kotlinx.coroutines.tasks.await

fun isOnline(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val capabilities =
        connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
    if (capabilities != null) {
        if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
            Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
            return true
        } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
            Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
            return true
        } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
            Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
            return true
        }
    }
    return false
}

fun saveToDatabase(db: FirebaseFirestore, aux: MutableList<Dolar>, colName: String){
    for (dolar in aux){
        db.collection(colName).document(dolar.id)
            .set(dolar)
            .addOnSuccessListener {
                Log.d(ContentValues.TAG, "Success")
            }
            .addOnFailureListener { e ->
                Log.w(ContentValues.TAG, "Error", e)
            }
    }
}

//[105.48, 2021-10-15blue, Blue, 222.21, blue0]
suspend fun loadFromDatabase(db: FirebaseFirestore): MutableList<Dolar>{
    val docRef = db.collection("DolaresHoy")
    val source = Source.CACHE
    val auxlist = mutableListOf<Dolar>()
    return try {
        val result = docRef.orderBy("id").get(source).await()
        if (result != null) {
            for (document in result) { //Por cada documento de colección
                val aux = document.data.values
                val prop: Dolar = Dolar( //Obtengo valores y creo instancia de Dolar
                    id = aux.elementAt(4) as String,
                    tipo = aux.elementAt(2) as String,
                    fecha = aux.elementAt(1) as String,
                    venta = aux.elementAt(3) as Double,
                    compra = aux.elementAt(0) as Double
                )
                auxlist.add(prop)
            }
        }
        auxlist
    } catch(e: Exception) {
        Log.e(TAG, "Error: ${e.message.orEmpty()}")
        mutableListOf()
    }
}

suspend fun loadFromDatabaseHistorico(db: FirebaseFirestore, tipoDolar: String, rango:Int): MutableList<Dolar>{
    val docRef = db.collection("DolaresHistorico")
    val source = Source.CACHE
    val auxlist = mutableListOf<Dolar>()
    return try {
        val result = docRef.whereEqualTo("tipo", tipoDolar.replaceFirstChar{it.uppercaseChar()}).orderBy("fecha").get(source).await()
        if (result != null) {
            for (document in result) {
                val aux = document.data.values
                val prop: Dolar = Dolar(
                    id = aux.elementAt(4) as String,
                    tipo = aux.elementAt(2) as String,
                    fecha = aux.elementAt(1) as String,
                    venta = aux.elementAt(3) as Double,
                    compra = aux.elementAt(0) as Double
                )
                //Log.d(TAG, "data-: ${prop}")
                auxlist.add(prop)
            }
        }
        var toIndex: Int = rango
        if(rango>auxlist.size){
            toIndex = auxlist.size
        }
        auxlist.asReversed().subList(0, toIndex)
    } catch(e: Exception) {
        Log.e(TAG, "Error: ${e.message.orEmpty()}")
        mutableListOf()
    }
}