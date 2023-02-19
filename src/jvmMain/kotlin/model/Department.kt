package model

data class Department(
    var depart_id: Long? = null,
    var department: String = "",
    var commetion_rate: Long?= 15,
    var depart_type: String?= "ادارى",
    var commetion_type: String?= null,
    var commetion_month: String?= null
)
