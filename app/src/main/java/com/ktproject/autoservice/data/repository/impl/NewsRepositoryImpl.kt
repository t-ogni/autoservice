package com.ktproject.autoservice.data.repository.impl

import com.ktproject.autoservice.data.model.*
import com.ktproject.autoservice.data.remote.*
import com.ktproject.autoservice.data.repository.*

class ServiceRepositoryImpl(private val api: ServiceApi) : ServiceRepository {
    override suspend fun getAllServices(): List<Service> {
        return api.getServices().data.orEmpty()
    }

    override suspend fun getServiceById(serviceId: String): Service? {
        return api.getServiceById(serviceId).data
    }

    override suspend fun addService(name: String, description: String, price: String) {
        api.addService(ServiceRequest(name, description, price))
    }

    override suspend fun deleteService(serviceId: String) {
        api.deleteService(serviceId)
    }
}

class RequestRepositoryImpl(private val api: RequestApi) : RequestRepository {
    override suspend fun createRequest(serviceId: String, description: String): String {
        val request = CreateRequest(serviceId, description, "", "")
        return api.createRequest(request).data?.id ?: ""
    }

    override suspend fun createRequest(serviceId: String, description: String, date: String, time: String): String {
        val request = CreateRequest(serviceId, description, date, time)
        return api.createRequest(request).data?.id ?: ""
    }

    override suspend fun getMyRequests(): List<Request> {
        return api.getUserRequests().data.orEmpty()
    }

    override suspend fun getAllRequests(): List<Request> {
        return api.getAllRequests().data.orEmpty()
    }

    override suspend fun updateRequestStatus(requestId: String, status: String, result: String?) {
        api.updateRequestStatus(requestId, UpdateRequestStatus(status, result))
    }
}

class NewsRepositoryImpl(private val api: NewsApi) : NewsRepository {
    override suspend fun getNews(): ResultState<List<News>> {
        return try {
            val result = api.getNews().data.orEmpty()
            ResultState.Success(result)
        } catch (e: Exception) {
            ResultState.Error(e.message ?: "Unable to fetch news")
        }
    }

    override suspend fun getNewsById(newsId: String): ResultState<News> {
        return try {
            val result = api.getNewsById(newsId).data!!
            ResultState.Success(result)
        } catch (e: Exception) {
            ResultState.Error(e.message ?: "News not found")
        }
    }

    override suspend fun addNews(title: String, content: String, date: String): ResultState<Unit> {
        return try {
            api.addNews(NewsRequest(title, content, date))
            ResultState.Success(Unit)
        } catch (e: Exception) {
            ResultState.Error(e.message ?: "Failed to add news")
        }
    }

    override suspend fun updateNews(newsId: String, title: String, content: String, date: String): ResultState<Unit> {
        return try {
            api.updateNews(newsId, NewsRequest(title, content, date))
            ResultState.Success(Unit)
        } catch (e: Exception) {
            ResultState.Error(e.message ?: "Failed to update news")
        }
    }

    override suspend fun deleteNews(newsId: String): ResultState<Unit> {
        return try {
            api.deleteNews(newsId)
            ResultState.Success(Unit)
        } catch (e: Exception) {
            ResultState.Error(e.message ?: "Failed to delete news")
        }
    }
}
