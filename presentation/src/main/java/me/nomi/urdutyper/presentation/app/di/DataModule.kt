package me.nomi.urdutyper.presentation.app.di

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import me.nomi.urdutyper.data.repository.FirebaseRepositoryImpl
import me.nomi.urdutyper.domain.repository.FirebaseRepository
import me.nomi.urdutyper.domain.usecase.LoadImages
import me.nomi.urdutyper.domain.usecase.LoginUseCase
import me.nomi.urdutyper.domain.usecase.Logout
import me.nomi.urdutyper.domain.usecase.Register
import me.nomi.urdutyper.domain.usecase.UserData
import me.nomi.urdutyper.domain.utils.dispatchers.DispatchersProviders
import me.nomi.urdutyper.data.source.SharedPreference
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Singleton
    @Provides
    fun providefirebaseRepository(
        firebaseAuth: FirebaseAuth,
        fireStoreDatabase: FirebaseFirestore,
        firebaseDatabase: FirebaseDatabase,
        dispatcherProvider: DispatchersProviders
    ): FirebaseRepository =
        FirebaseRepositoryImpl(
            firebaseAuth,
            fireStoreDatabase,
            firebaseDatabase,
            dispatcherProvider
        )

    @Singleton
    @Provides
    fun provideSharedPref(@ApplicationContext context: Context): SharedPreference =
        SharedPreference(context)

    @Provides
    fun provideLogin(firebaseRepository: FirebaseRepository): LoginUseCase =
        LoginUseCase(firebaseRepository)

    @Provides
    fun provideLogout(firebaseRepository: FirebaseRepository): Logout =
        Logout(firebaseRepository)

    @Provides
    fun provideRegister(firebaseRepository: FirebaseRepository): Register =
        Register(firebaseRepository)

    @Provides
    fun provideUserData(firebaseRepository: FirebaseRepository): UserData =
        UserData(firebaseRepository)

    @Provides
    fun loadImages(firebaseRepository: FirebaseRepository) = LoadImages(firebaseRepository)
}
