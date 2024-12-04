package com.capstone.bindetective.model

data class User(
    val userId: String,
    val userName: String,
    val dateOfBirth: String? = null,
    val profilePictureUrl: String? = null
)




