package com.boomingarage.shared.data.repository

import com.boomingarage.shared.data.mapper.UserMapper
import com.boomingarage.shared.data.remote.dto.UserDto
import com.boomingarage.shared.domain.model.CarInfo
import com.boomingarage.shared.domain.model.User
import com.boomingarage.shared.domain.model.UserRole
import com.boomingarage.shared.domain.repository.AuthRepository
import com.boomingarage.shared.util.Constants
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.firestore.firestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AuthRepositoryImpl : AuthRepository {
    private val auth = Firebase.auth
    private val firestore = Firebase.firestore

    override val currentUser: Flow<User?> = auth.authStateChanged.map { firebaseUser ->
        firebaseUser?.let { fbUser ->
            try {
                val doc = firestore.collection(Constants.COLLECTION_USERS).document(fbUser.uid).get()
                val dto = doc.data<UserDto>()
                UserMapper.toDomain(dto)
            } catch (_: Exception) {
                User(
                    id = fbUser.uid,
                    email = fbUser.email ?: "",
                    displayName = fbUser.displayName ?: ""
                )
            }
        }
    }

    override suspend fun signIn(email: String, password: String): Result<User> = runCatching {
        val result = auth.signInWithEmailAndPassword(email, password)
        val uid = result.user?.uid ?: throw Exception("Sign in failed")
        val doc = firestore.collection(Constants.COLLECTION_USERS).document(uid).get()
        val dto = doc.data<UserDto>()
        UserMapper.toDomain(dto)
    }

    override suspend fun signUp(
        email: String,
        password: String,
        displayName: String,
        phone: String,
        role: UserRole
    ): Result<User> = runCatching {
        val result = auth.createUserWithEmailAndPassword(email, password)
        val uid = result.user?.uid ?: throw Exception("Sign up failed")

        val userDto = UserDto(
            id = uid,
            email = email,
            displayName = displayName,
            phone = phone,
            role = role.name,
            createdAt = kotlinx.datetime.Clock.System.now().toEpochMilliseconds()
        )

        firestore.collection(Constants.COLLECTION_USERS).document(uid).set(userDto)
        UserMapper.toDomain(userDto)
    }

    override suspend fun signOut() {
        auth.signOut()
    }

    override suspend fun updateProfile(
        displayName: String?,
        phone: String?,
        carInfo: CarInfo?
    ): Result<User> = runCatching {
        val uid = auth.currentUser?.uid ?: throw Exception("Not signed in")
        val updates = mutableMapOf<String, Any>()
        displayName?.let { updates["displayName"] = it }
        phone?.let { updates["phone"] = it }
        carInfo?.let {
            updates["carMake"] = it.make
            updates["carModel"] = it.model
            updates["carYear"] = it.year
            updates["carColor"] = it.color
            updates["carLicensePlate"] = it.licensePlate
        }

        val docRef = firestore.collection(Constants.COLLECTION_USERS).document(uid)
        docRef.update(updates)
        val doc = docRef.get()
        UserMapper.toDomain(doc.data<UserDto>())
    }

    override suspend fun sendEmailVerification(): Result<Unit> = runCatching {
        auth.currentUser?.sendEmailVerification() ?: throw Exception("Not signed in")
    }

    override suspend fun getCurrentUser(): User? {
        val uid = auth.currentUser?.uid ?: return null
        return try {
            val doc = firestore.collection(Constants.COLLECTION_USERS).document(uid).get()
            UserMapper.toDomain(doc.data<UserDto>())
        } catch (_: Exception) {
            null
        }
    }
}
