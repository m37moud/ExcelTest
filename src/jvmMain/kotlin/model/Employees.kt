package model

data class Employees(
   val emp_id: Long,
//   val id: Long,
   val fname: String,
   val department_name: String,
   val totaldays: Long = 21,
   val bith_day: String ="1990-11-16",
   val salary: Float = 1300f,
   val vacanition: Long= 0,
   val vbalance: Long = 0,
   val bdl_balance: Long = 0
)

