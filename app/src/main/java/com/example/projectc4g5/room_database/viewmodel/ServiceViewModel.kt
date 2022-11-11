package com.example.projectc4g5.room_database.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectc4g5.room_database.Service
import com.example.projectc4g5.room_database.repository.ServiceRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ServiceViewModel (private val repository: ServiceRepository): ViewModel() {

    var services: List<Service>? = null

    fun getAllServices(): Job {
        return viewModelScope.async {
            services = repository.getAllServices()
        }
    }

    fun getTheServices(): List<Service>? {
        return services
    }

    fun insertService(service: Service):Long{
        var idService: Long = 0
        viewModelScope.launch {
            idService=repository.insertService(service)
        }
        return idService
    }

    fun insertServices(service: List<Service>?):List<Long>?{
        var idService: List<Long>? = null
        viewModelScope.launch {
            idService=repository.insertServices(service)
        }
        return idService
    }

}