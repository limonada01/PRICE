package com.example.price.ui.historico

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.price.Dolar
import com.example.price.network.api.DollarService
import com.example.price.network.isOnline
import com.example.price.network.loadFromDatabaseHistorico
import com.example.price.network.saveToDatabase
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class HistoryViewModel: ViewModel() {
    val dollarsLiveData = MutableLiveData<MutableList<Dolar>>()
    val isLoading = MutableLiveData<Boolean>()
    val rangoActual = MutableLiveData<Int>()
    val tipoActual = MutableLiveData<String>()

    val dollarService = DollarService()

    fun updateDollars(tipoDolar: String, rango: Int, db: FirebaseFirestore, context: Context){
        var content: MutableList<Dolar>
        isLoading.postValue(true)
        rangoActual.postValue(rango)
        tipoActual.postValue(tipoDolar)
        viewModelScope.launch {
            if(isOnline(context)) { //Si dispositivo tiene conexión...
                content = dollarService.getDollarHistory(tipoDolar, rango)
                saveToDatabase(db, content, "DolaresHistorico")
            }else{
                content = loadFromDatabaseHistorico(db, tipoDolar, rango)
            }
            if(!content.isNullOrEmpty()){
                //Actualizo LiveData
                dollarsLiveData.postValue(content)
                isLoading.postValue(false)
            }else{
                Log.d(TAG, "Datos no pudieron ser actualizados")
            }
        }
    }
}