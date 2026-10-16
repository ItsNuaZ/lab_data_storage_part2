package com.example.studentmanagement.data.remote

data class StudentCreateRequest(
    val student_code: String,
    val name: String,
    val major: String
)