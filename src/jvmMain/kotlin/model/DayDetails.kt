package model


data class DayDetails(
    var name: String = "",
    val department: String = "",
    var day: String = "",
    val month: String = "",
    val year: String = "",
    var wardia: String = "",
    var typeOfDay: String = "",
    var partTime: Double = 0.0,
    var earlyAccess: String = "",
    var earlyAccessNote: String = "",
    var notes: String = ""
){
    fun getMembersDayDetails() = listOf(
        name,
        department,
        day,
        month,
        year,
        wardia,
        typeOfDay,
        partTime,
        earlyAccess,
        earlyAccessNote,
        notes

    )

//    fun getResult(day :String) : DayDetails{
//        if ( This.day == day){
//
//        }
//
//    }
}

