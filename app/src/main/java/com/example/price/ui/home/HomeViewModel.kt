package com.example.price.ui.home

import android.content.ContentValues
import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.price.Dolar
import com.example.price.network.api.DollarService
import com.example.price.network.isOnline
import com.example.price.network.loadFromDatabase
import com.example.price.network.saveToDatabase
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class HomeViewModel: ViewModel() {
    val dollarsLiveData = MutableLiveData<MutableList<Dolar>>()
    val isLoading = MutableLiveData<Boolean>()

    val dollarService = DollarService() //API

    fun onCreate(db: FirebaseFirestore, context: Context){
        var content: MutableList<Dolar>

        isLoading.postValue(true) //Icono de carga mientras se recuperan datos
        viewModelScope.launch {
            if(isOnline(context)) { //Si el dispositivo tiene conexion --> llamo a API y guardo en Firebase
                content = dollarService.getDollarHome()
                saveToDatabase(db, content, "DolaresHoy")
            }else{ //Sino --> recupero datos desde Firebase
                content = loadFromDatabase(db)
            }
            if(!content.isNullOrEmpty()){
                //Actualizo LiveData
                dollarsLiveData.postValue(content)
                isLoading.postValue(false)
            }else{
                Log.d(ContentValues.TAG, "Datos no pudieron ser actualizados")
            }
        }
    }
}