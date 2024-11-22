package com.capstone.bindetective.model

data class User(
    val userId: String = "",  // Default empty value for userId
    val userName: String,
    val dateOfBirth: String? = null  // Birthdate is nullable
)




