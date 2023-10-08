package me.nomi.urdutyper.presentation.app.di

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import me.nomi.urdutyper.data.repository.FirebaseRepositoryImpl
import me.nomi.urdutyper.data.source.SharedPreference
import me.nomi.urdutyper.domain.repository.FirebaseRepository
import me.nomi.urdutyper.domain.repository.SharedPreferenceRepository
import me.nomi.urdutyper.domain.usecase.DeleteImage
import me.nomi.urdutyper.domain.usecase.LoadImages
import me.nomi.urdutyper.domain.usecase.LoginUseCase
import me.nomi.urdutyper.domain.usecase.Logout
import me.nomi.urdutyper.domain.usecase.Register
import me.nomi.urdutyper.domain.usecase.UploadImage
import me.nomi.urdutyper.domain.usecase.UserData
import me.nomi.urdutyper.domain.utils.dispatchers.DispatchersProviders
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Singleton
    @Provides
    fun providefirebaseRepository(
        firebaseAuth: FirebaseAuth,
        firebaseDatabase: FirebaseDatabase,
        firebaseStorage: FirebaseStorage,
        dispatcherProvider: DispatchersProviders
    ): FirebaseRepository =
        FirebaseRepositoryImpl(
            firebaseAuth,
            firebaseDatabase,
            firebaseStorage,
            dispatcherProvider
        )

    @Singleton
    @Provides
    fun provideSharedPref(@ApplicationContext context: Context): SharedPreferenceRepository =
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

    @Provides
    fun deleteImage(firebaseRepository: FirebaseRepository) = DeleteImage(firebaseRepository)

    @Provides
    fun uploadImage(firebaseRepository: FirebaseRepository) = UploadImage(firebaseRepository)
}
