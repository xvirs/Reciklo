package com.jetbrains.kmpapp.data.models

import kotlinx.serialization.Serializable

@Serializable
data class ProfileDTO(
    val active: Boolean,
    val defaultLanguage: String?,
    val emailAddress: String?,
    val firstName: String?,
    val groups: List<Group>?,
    val id: Int,
    val insuranceCompanies: List<String?>,
    val lastAccess: String?,
    val lastName: String?,
    val loginTime: String?,
    val merchant: String?,
    val permissions: List<Permission>?,
    val userName: String?
)

@Serializable
data class Group(
    val id: Int,
    val name: String,
    val type: String?
)

@Serializable
data class Permission(
    val id: Int,
    val name: String
)