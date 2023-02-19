package model

data class RegisterAttends( //Day_register
     val emp_name: String?,
     val department: String?,
     val oDate: String?,
     val day: String?,
     val status: String?,
     val in_time: String?,
     val out_time: String?,
     val late: Float?,
     val early: Float?
)
