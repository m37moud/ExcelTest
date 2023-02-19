import model.DayDetails
import model.EmployeeResult
import org.apache.poi.xssf.usermodel.XSSFRow
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.example.util.Constatnts.Companion.getLastDay
import org.example.util.Constatnts.Companion.getMonth
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.nio.file.Files
import java.time.LocalDate

class ExportExcelFile {
    var fileName = ""

    private val month = "8"
    private val year = "2022"
    private val days = getLastDay((month.toInt() - 1), year.toInt())
    private var startDate = LocalDate.parse("$year-${getMonth((month.toInt() - 1).toString())}-21")
    private var endDate = LocalDate.parse("$year-${getMonth(month)}-20")

    /**
     * Writes the value "TEST" to the cell at the first row and first column of worksheet.
     */

    fun writeToExcelFile(filepath: String) {
        //Instantiate Excel workbook:
        val xlWb = XSSFWorkbook()
        //Instantiate Excel worksheet:
        val xlWs = xlWb.createSheet()

        //Row index specifies the row in the worksheet (starting at 0):
        val rowNumber = 0
        //Cell index specifies the column within the chosen row (starting at 0):
        val address = listOf(
            "id",
            "name",
            "department",
            "numberOfAttendDays",
            "numberOfAbsentDays",
            "totalPartTime",
            "totalEarlyTime",
            "absentDays",
            "attendDays"
        )
        for (i in address.indices) {
            //Write text value to cell located at ROW_NUMBER / COLUMN_NUMBER:

//            xlWs.createRow(rowNumber).createCell(i).setCellValue(txt)
        }

        //Write file:
        val outputStream = FileOutputStream(filepath)
        xlWb.write(outputStream)
        xlWb.close()
        println("export succssesfull")

    }

    fun writeEmpsToExcelFile(filepath: String, list: List<EmployeeResult>) {

        createExcelFile(filepath)
        if (!Files.isReadable(File(filepath).toPath())) {
            println("File is opened please close it")
            return
        }
        try {
            //Instantiate Excel workbook:
            val xlWb = XSSFWorkbook()
            //Instantiate Excel worksheet:
            val xlWs = xlWb.createSheet()

            //Row index specifies the row in the worksheet (starting at 0):
            val rowNumber = list.size - 1
            //Cell index specifies the column within the chosen row (starting at 0):

            var addresRow = xlWs.createRow(0)
            setAdress1(addresRow)

            for (i in 0..rowNumber) {
                val row = xlWs.createRow(i + 1)

                val columnNumber = (list[i].getMembers().size - 1) //+ (list[i].attendDays.size-1)
                println("columnNumber = $columnNumber")
                var counterColumnCheck = 0

                for (j in 0..columnNumber) {
//                    if (i == 0) { // set address
//                        val txt = address[j]
//                        row.createCell(j).setCellValue(txt)
//
//                    } else {
                    val empResult = list[i].getMembers()

//                        println(empResult)
                    val txt = empResult[j]
                    if (txt is List<*>) {


                        txt.forEach {
                            println(it.toString())

                            row.createCell(counterColumnCheck).setCellValue(it.toString())
                            counterColumnCheck++

                        }
                        println(txt.size)

                    } else {
                        val txt = empResult[j]

                        row.createCell(counterColumnCheck).setCellValue(txt.toString())

                    }
                    counterColumnCheck++
//                    }


                }
            }

            //Write text value to cell located at ROW_NUMBER / COLUMN_NUMBER:
//        xlWs.createRow(rowNumber).createCell(columnNumber).setCellValue("TEST")

            //Write file:
            val outputStream = FileOutputStream(fileName)
            xlWb.write(outputStream)
            xlWb.close()
            println("export succssesfull")

        } catch (e: Exception) {
            println("exporting error : ${e.message}")
            println(" error : ${e.cause}")
            println(" error : ${e.stackTrace}")
            println(" error : ${e.stackTrace}")

        } catch (io: IOException) {
            println("IOException error : ${io.message}")
            println(" IOException : ${io.cause}")
            println(" IOException : ${io.stackTrace}")
        }
    }


    /*
    test new export
     */
    fun writeEmpsToExcelFileNew(filepath: String, list: List<EmployeeResult>) {

        createExcelFile(filepath)
        if (!Files.isReadable(File(filepath).toPath())) {
            println("File is opened please close it")
            return
        }
        try {
            //Instantiate Excel workbook:
            val xlWb = XSSFWorkbook()
            //Instantiate Excel worksheet:
            val xlWs = xlWb.createSheet()

            //Row index specifies the row in the worksheet (starting at 0):
            val rowNumber = list.size - 1
            //Cell index specifies the column within the chosen row (starting at 0):

            val addresRow = xlWs.createRow(0)

            setAdress(addresRow)

            for (i in 0..4) {
                val row = xlWs.createRow(i + 1)

                val columnNumber = (list[i].getMembers().size - 1) //+ (list[i].attendDays.size-1)
                println("columnNumber = $columnNumber")
                var counterColumnCheck = 0

                for (j in 0..columnNumber) {
//                    if (i == 0) { // set address
//                        val txt = address[j]
//                        row.createCell(j).setCellValue(txt)
//
//                    } else {
                    val empResult = list[i].getMembers()

//                        println(empResult)
                    val txt = empResult[j]
                    if (txt is List<*>) {
                        val listDay = txt as List<DayDetails>
                        var tempStartDay = 21
                        var tempEndDay = 1
                        loop@ for (emp in listDay) {
//                                dayDetails ->
//                            var d = 0

                            when {
                                emp.day.toInt() in 21..days -> {

                                    loop2@ for (d in tempStartDay..days) {
                                        if (emp.day == d.toString()) {
                                            val txt = insertDayDetails(emp)
                                            row.createCell(counterColumnCheck).setCellValue(emp.toString())
                                            counterColumnCheck++
                                            tempStartDay++
                                            continue@loop


                                        } else {
                                            row.createCell(counterColumnCheck).setCellValue("غ")
                                            tempStartDay++
                                            counterColumnCheck++


                                        }

                                    }

                                }
                                emp.day.toInt() in 1..20 -> {

                                    loop3@ for (dd in tempEndDay..20) {

                                        if (emp.day == dd.toString()) {
                                            row.createCell(counterColumnCheck).setCellValue(emp.toString())
                                            counterColumnCheck++
                                            tempEndDay++
                                            continue@loop


                                        } else {
                                            row.createCell(counterColumnCheck).setCellValue("غ")
                                            tempEndDay++
                                            counterColumnCheck++


                                        }
                                    }

                                }
//                                else -> {
//                                    row.createCell(counterColumnCheck).setCellValue("غ")
//                                }
                            }
//                            counterColumnCheck++


                        }

//                        txt.forEach {day ->
//
//                            println(day.toString())
//
//                            row.createCell(counterColumnCheck).setCellValue(day.toString())
//                            counterColumnCheck++
//
//                        }
//                        println(txt.size)

                    } else {
                        val txt = empResult[j]

                        row.createCell(counterColumnCheck).setCellValue(txt.toString())

                    }
                    counterColumnCheck++
//                    }


                }
            }

            //Write file:
            val outputStream = FileOutputStream(fileName)
            xlWb.write(outputStream)
            xlWb.close()
            println("export succssesfull")

        } catch (e: Exception) {
            println("exporting error : ${e.message}")
            println(" error : ${e.cause}")
            println(" error : ${e.stackTrace}")
            println(" error : ${e.stackTrace}")

        } catch (io: IOException) {
            println("IOException error : ${io.message}")
            println(" IOException : ${io.cause}")
            println(" IOException : ${io.stackTrace}")
        }
    }

    private fun insertDayDetails(emp: DayDetails): String {


        return ""
    }

    private fun setAdress(addresRow: XSSFRow) {
        val address = listOf(
            "id",
            "name",
            "department",
            "Attend Days",
            "Days Check Noted",
            "Absent Days",
            "Total PartTime",
            "Part Time Days",
            "Total Early Time",
            "Early Access TimeDays",
            "Absent Days",
            "Attend Days"
        )


        for (i in address.indices) {
            val txt = address[i]
            if (i == address.size - 1) {
                var dayTxt = ""
                var addressCounter = 0

                for (j in 21..days) {
                    dayTxt =
                        startDate.withDayOfMonth(j).dayOfWeek.toString() + " - " + startDate.withDayOfMonth(j).dayOfMonth.toString()
                    addresRow.createCell(i + addressCounter).setCellValue(dayTxt)
                    addressCounter++

                }
                for (d in 1..20) {
                    dayTxt =
                        endDate.withDayOfMonth(d).dayOfWeek.toString() + " - " + endDate.withDayOfMonth(d).dayOfMonth.toString()
                    addresRow.createCell(i + addressCounter).setCellValue(dayTxt)
                    addressCounter++

                }


            } else {//Write text value to cell located at ROW_NUMBER / COLUMN_NUMBER:
                addresRow.createCell(i).setCellValue(txt)
            }
        }
    }
    private fun setAdress1(addresRow: XSSFRow) {
        val address = listOf(
            "id",
            "name",
            "department",
            "Attend Days",
            "Days Check Noted",
            "Absent Days",
            "Total PartTime",
            "Part Time Days",
            "Total Early Time",
            "Early Access TimeDays",
            "Absent Days",
            "Attend Days"
        )

        for (i in address.indices) {
            val txt = address[i]
            addresRow.createCell(i).setCellValue(txt)

        }
    }

    private fun createExcelFile(filepath: String) {

        try {
            val p = filepath + File.separator + "export"
            val file = File(p)
            if (!Files.exists(file.toPath())) Files.createDirectory(file.toPath())


            println(file.name)

            fileName = file.path + File.separator + "exported-test.xlsx"
        } catch (e: Exception) {
            println("createFolder error : ${e.message}")

        }
    }

}