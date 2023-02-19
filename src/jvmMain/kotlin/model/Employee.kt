package model

data class Employee(
    val id: String,
    var name: String,
    val department: String,
    val day: String = "2022-01-01",
    val time: String = "00:00:00",
    val statue: String = ""
)