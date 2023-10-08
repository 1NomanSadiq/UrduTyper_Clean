package me.nomi.urdutyper.domain.repository

import me.nomi.urdutyper.domain.entity.Image
import me.nomi.urdutyper.domain.entity.User
import me.nomi.urdutyper.domain.utils.Result

interface FirebaseRepository {
    suspend fun register(email: String, password: String): Result<User>
    suspend fun login(email: String, password: String): Result<User>
    suspend fun logOut(): Result<Unit>
    suspend fun getUserData(): Result<User>
    suspend fun loadImages(uid: String): Result<List<Image>>
    suspend fun deleteImage(uid: String, file: Image): Result<Unit>
}