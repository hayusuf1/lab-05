package com.example.listycity

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateListOf
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore


class CityRepository {

    private val db = Firebase.firestore

    private val citiesRef = db.collection("cities")



    init {
        citiesRef.addSnapshotListener { snapshots, error ->
            if (error != null){
                return@addSnapshotListener
            }

            _cities.clear()

            snapshots?.documents?.forEach { document ->
                val city = document.toObject(City::class.java)
                if (city != null){
                    _cities.add(
                        city.copy(id = document.id)
                    )
                }
            }

        }
    }

    private val _cities = mutableStateListOf<City>()

    val cities: List<City>
        get() = _cities

    fun addCity(city: City) {
        citiesRef.add(
            mapOf(
                "name" to city.name,
                "province" to city.province
            )
        )
    }

    fun updateCity(oldCity: City, updatedCity: City) {
            citiesRef.document(oldCity.id).set(
                mapOf(
                "name" to updatedCity.name,
                "province" to updatedCity.province
                ))
    }

    fun deleteCity(city: City){
        citiesRef.document(city.id).delete()
    }
}