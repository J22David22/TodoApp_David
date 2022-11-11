package com.example.projectc4g5.room_database.repository

import com.example.projectc4g5.room_database.Service
import com.example.projectc4g5.room_database.ServiceDAO

class ServiceRepository (val serviceDao: ServiceDAO){

    suspend fun getAllServices(): List<Service>{
        return serviceDao.getAllServices()
    }

    suspend fun insertService(service: Service): Long{
        return serviceDao.insertService(service)
    }

    suspend fun deleteService(service: Service){
        return serviceDao.deleteService(service)
    }

    suspend fun insertServices(service: List<Service>?): List<Long>{
        return serviceDao.insertServices(service)
    }
}