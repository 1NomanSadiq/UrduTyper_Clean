package me.nomi.urdutyper.data.repository


import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import me.nomi.urdutyper.data.mapper.Mapper.toDomain
import me.nomi.urdutyper.domain.entity.Image
import me.nomi.urdutyper.domain.entity.User
import me.nomi.urdutyper.domain.repository.FirebaseRepository
import me.nomi.urdutyper.domain.utils.Result
import me.nomi.urdutyper.domain.utils.dispatchers.DispatchersProviders
import javax.inject.Inject

class FirebaseRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val fireStoreDatabase: FirebaseFirestore,
    private val firebaseDatabase: FirebaseDatabase,
    private val dispatchers: DispatchersProviders
) : FirebaseRepository {

    override suspend fun register(
        email: String,
        password: String,
        user: User
    ): Result<User> = withContext(dispatchers.getIO()) {
        return@withContext safeApiCall {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            fireStoreDatabase.collection("User")
                .document(firebaseAuth.currentUser!!.uid)
                .set(user).await()
            result.user?.toDomain() ?: throw Exception("User not found")
        }
    }

    override suspend fun login(email: String, password: String): Result<User> {
        return safeApiCall {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            val user = result.user

            if (user != null) {
                if (user.isEmailVerified) {
                    user.toDomain()
                } else {
                    throw Exception("Please verify your email first")
                }
            } else {
                throw Exception("User not found")
            }
        }
    }


    override suspend fun logOut(): Result<Unit> {
        return safeApiCall {
            firebaseAuth.signOut()
        }
    }

    override suspend fun getUserData(): Result<User> {
        return safeApiCall {
            val currentUser = firebaseAuth.currentUser
            currentUser?.toDomain() ?: throw Exception("No user data found")
        }
    }

    override suspend fun loadImages(uid: String): Result<List<Image>> {
        return safeApiCall {
            val images = mutableListOf<Image>()
            val reference = firebaseDatabase.reference.child(uid).child("Images")
            reference.keepSynced(true)
            val snapshot = reference.get().await()
            for (childSnapshot in snapshot.children) {
                val filename = childSnapshot.key.toString()
                val url = childSnapshot.value.toString()
                images.add(Image(filename, url))
            }
            images
        }
    }
}