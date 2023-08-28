package com.hyzoka.xperience.repository

import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.HttpException
import com.google.firebase.firestore.FirebaseFirestore
import com.hyzoka.xperience.model.Position
import com.hyzoka.xperience.model.Positions
import com.hyzoka.xperience.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.io.IOException

class PositionRepository {

    private val fireStoreDatabase = FirebaseFirestore.getInstance()

    fun getPositionsData(): Flow<Resource<List<Positions>>> = flow {
        emit(Resource.Loading())
        try {
            val documentSnapshot = fireStoreDatabase.collection("positions") .get().await().documents

            emit(Resource.Success(data = documentSnapshot.map { Positions(id = it.id, position = it.toObject(
                Position::class.java)!!) }))

        } catch (e: HttpException) {
            emit(Resource.Error(message = e.localizedMessage ?: "Unknown Error"))
        } catch (e: IOException) {
            emit(
                Resource.Error(
                    message = e.localizedMessage ?: "Check Your Internet Connection"
                )
            )
        } catch (e: Exception) {
            emit(Resource.Error(message = e.localizedMessage ?: ""))
        }
    }
}