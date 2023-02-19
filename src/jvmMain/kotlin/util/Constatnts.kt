package org.example.util

class Constatnts {
    companion object {
        fun getMonth(mm: String): String {

            return when (mm.length) {
                1 -> {
                    "0".plus(mm.toString())

                }
                2 -> {
                    mm

                }
                else -> "please enter month : "
            }
        }

        fun getLastDay(month: Int, year: Int): Int {
            var day = 28
            if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) {
                day = 31
            } else if (month == 4 || month == 6 || month == 9 || month == 11) {
                day = 30
            } else if (month == 2 && year % 4 == 0) {
                day = 29
            }
            return day
        }

         fun getWardiaAsNum(wardia: String): Int {
            return when (wardia) {
                "morning" -> {
                    1
                }
                "afternoon" -> {
                    2
                }
                "night" -> {
                    3
                }
                "morning" -> {
                    1
                }
                else -> {
                    0
                }
            }
        }
    }



}