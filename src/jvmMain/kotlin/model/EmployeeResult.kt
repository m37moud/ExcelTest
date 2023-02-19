package model

import model.AbsentDay
import model.DayDetails

data class EmployeeResult(
//    val id: String = "",
    var name: String = "",
    val department: String = "",
    val month: String = "",
    val year: String = "",
    val numberOfAttendDays: Int = 0,
    val daysToCheckNoted: String = "",
    val numberOfAbsentDays: Int = 0,
    val totalPartTime: Double = 0.0,
    val partTimeDays: String = "",
    val totalEarlyTime: Double = 0.0,
    val totalEarlyAccessTimeDays: String = "",
    val absentDays:  List<AbsentDay> = emptyList(),
    val attendDays: List<DayDetails> = emptyList()

    ) {
    fun getMembers() = listOf(
//        id,
        name,
        department,
        month,
        year,
        numberOfAttendDays,
        daysToCheckNoted,
        numberOfAbsentDays,
        totalPartTime,
        partTimeDays,
        totalEarlyTime,
        totalEarlyAccessTimeDays,
        absentDays,
        attendDays
        )


}
