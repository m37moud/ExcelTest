import com.toxicbakery.logging.Arbor
import model.CamRegisterDay
import model.DayDetails
import model.EmployeeResult
import java.io.File
import java.nio.file.Files
import java.time.LocalDate
import java.time.Period
import java.time.temporal.ChronoUnit


val filepath0 = "D:\\desk\\شغل لعهد\\tutorial audting\\2022\\شهراغسطس8\\8\\0.xlsx"
val filepath1 = "D:\\desk\\شغل لعهد\\tutorial audting\\2022\\شهراغسطس8\\8\\1.xlsx"

//val filepath = "D:\\desk\\شغل لعهد\\tutorial audting\\2022\\شهراغسطس8\\2.xlsx"
val filepath2 = "D:\\desk\\شغل لعهد\\tutorial audting\\2022\\شهراغسطس8\\8\\3.xlsx"
val filepath3 = "D:\\desk\\شغل لعهد\\tutorial audting\\2022\\شهراغسطس8\\8\\4.xlsx"
val filepath4 = "D:\\desk\\شغل لعهد\\tutorial audting\\2022\\شهراغسطس8\\8\\5.xlsx"
val filepath = "D:\\desk\\شغل لعهد\\tutorial audting\\2022\\شهراغسطس8\\8\\6.xlsx"


val filepath11 = "D:\\desk\\شغل لعهد\\tutorial audting\\2022\\شهراغسطس8\\8\\8\\1.xlsx"
val filepath22 = "D:\\desk\\شغل لعهد\\tutorial audting\\2022\\شهراغسطس8\\8\\8\\2.xlsx"

var empList = mutableListOf<CamRegisterDay>()
var employeeResultList = listOf<EmployeeResult>()

//val pathList = listOf(filepath)
//val pathList = listOf(filepath0,filepath1,filepath2,filepath3,filepath4)
//val pathList = listOf(filepath11, filepath22)
val pathList = mutableListOf<String>()
var path = "F:\\8"
var year = "2022"
lateinit var importer: ImportExcelFile

var userExit = false


fun main(args: Array<String>) {


    println("Hello, World")

//    initEmployeeResult()


//    writeToExcelFile(filepath)
//    val p = path+"exported-test.xlsx"
//    val export = ExportExcelFile()
//    export.writeToExcelFile(p)


    run()

////
//        importer.readFromExcelFile(filepath)
//    val importer = ImportExcelFile("8", "2022")
//
//    takeAllPaths(pathList)
//
//    importer.getEmpReport(empList, "Mahmoud Ali")


//    println(LocalDate.parse("2022-08-08"))
//        println(LocalTime.parse("00:41:06" ))

//
//    val text = "2022-08-08 07:41:06"
//    val pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
//    val localDateTime = LocalDateTime.parse(text, pattern)
//    println(localDateTime.toLocalTime())
//
//    val text2 = "19:41:06"
//    val tt = LocalTime.parse(text2)
//
//
//    val t = LocalTime.of(16, 0)
//    val ii = tt.compareTo(tt)
//
//    println("kkkkkkkkkk ${Duration.between(t, tt)}")
    val endDate = LocalDate.parse("2022-03-20")

    val startDate = LocalDate.parse("2022-02-21")
    val period = Period.between(startDate, endDate)
    val days = ChronoUnit.DAYS.between(startDate, endDate) + 1

//    val month = importer.getLastDay(2,2022)

//    println("period $period\n days = $days\n month = $month")

}

private fun run() {
    while (!userExit) {
        println("welcom")
        start()


    }


}

private fun start() {
    print("Enter folder path : ")
    path = readLine()!!
    if (!path.isBlank()) {
        println("please wait ...")

        getPathToWork(path)


    } else {
        path = "F:\\8"
        getPathToWork(path)

    }
}

private fun initEmployeeResult() {
    val emp = EmployeeResult(
//        id = "171",
        name = "Mahmoud Ali",
        department = "Mayorca/المراجعة",
        numberOfAttendDays = 20,
        numberOfAbsentDays = 11,
        totalPartTime = 0.0,
        totalEarlyTime = 3.53,
        attendDays = listOf(
            DayDetails(
                day = "21",
                wardia = "اولى",
                typeOfDay = "",
                partTime = 0.0,
                earlyAccess = "",
                earlyAccessNote = "",
                notes = ""
            )
        )
//        , absentDays = "22, 23, 29, 30, 3, 4, 5, 6, 7, 12, 19,"
    )
    val columnNumber = emp.getMembers().toString()
    println("columnNumber = ${columnNumber.toString()}")
}

fun getPathToWork(path: String) {
    val file = File(path);

    println("file name is ( ${file.name} )")
    file.list().forEach {
        println("file ...\n ${it}")
        val file = file.path + File.separator + it
        if (!Files.isDirectory(File(file).toPath()))
            pathList.add(file)

    }
    importer = ImportExcelFile(file.name, year)
    takeAllPaths(pathList)
    println("empList = ${empList.size}")


    if (empList.isNotEmpty()) {
        println("excel imported succssesful")
        employeeResultList = importer.doSomeWork(empList)
        if (employeeResultList.isNotEmpty()){
            println("extracted employee result sucssesful")
            empList.clear()
            makeChoise()

        }else{
            println("imported failed system exit...")
            userExit = true

        }



    } else {
        println("imported failed system exit...")

        userExit = true
    }

//    makeChoise(screen())
////    importer.doSomeWork(empList)
}

private fun screen(): Int {
    println("****welcom main menu****")
    println("1 . export to excel  ")
    println("2. get employee details ")
    println("3. exit ")
    println("4. export to excel (2)  ")
    println("5. waraid details  ")


    print("please enter choise : ")


    return readLine()!!.toInt()
}

private fun makeChoise() {
    var exit = false
    while (!exit) {
        val choise: Int = screen()
        when (choise) {

            1 -> {

//               val rnr = RandomAccessFile("database", "r")
//                val rnw = RandomAccessFile(file, "rw")
//
//                while (rnr.getFilePointer() !== rnr.length()) {
//                    rnw.write(rnr.read())
//                }
//
//                Thread.sleep(1000) // wait for finish

                println("please wait ")
//                val list = importer.doSomeWork(empList)




                val export = ExportExcelFile()
                export.writeEmpsToExcelFile(path, employeeResultList)

            }
            2 -> {
                print("please enter employee name or id ")
                val entered = readLine().toString()
                println("you enter ( $entered ) is\n")

//                val emp = employeeResultList.filter {
////                    it.id == entered ||
//                            it.name == entered
//                }
//Mohamed Ramadan Abdelfatah
                val empw = importer.getEmpReportById(empList, entered, entered)
//                println(emp.toString())
                Arbor.d(empw.toString())
            }
            3 -> {

                println("system exiting ...")

                exit = true
                userExit = true
                empList.clear()

            }
            4 -> {
                println("export to excel (2) ...")
                val export = ExportExcelFile()
                export.writeEmpsToExcelFileNew(path, employeeResultList)

            }
            5 -> {
                println("employee waradi report  ...")
//                val list = importer.doSomeWork(empList)
                val waradiList = empWaradiReport(employeeResultList)
                println(waradiList.joinToString {
                    it.joinToString("\n")
                })
//                val export = ExportExcelFile()
//                export.writeEmpsToExcelFileNew(path, employeeResultList)

            }
            else -> {
                println("wrong choise")
            }
        }

    }


}

fun empWaradiReport(employeeResultList: List<EmployeeResult>): List<List<String>> {
    print("please enter employee name or id ")
    val entered = readLine().toString()
    println("Waradi Report for ( $entered ) \n")
    val list = employeeResultList.filter {
//        it.id == entered ||
                it.name == entered
    }
    if (list.isNotEmpty()){

        println(list.size.toString() + " size" )

//        println("ID : ${list[0].id} / employee name : ${list[0].name} / department :  ${list[0].department.substringAfter("/")}" )
        println("/ employee name : ${list[0].name} / department :  ${list[0].department.substringAfter("/")}" )

        val l = list.map {
            it.attendDays.map {

                " day : ( "+ it.day + " ) =  wardia : "+it.wardia
            }
        }
        println(list[0].numberOfAttendDays.toString() + " days" )
        return l
    }else{
        println("employee ( $entered ) not found please enter right name")

    }

    return emptyList()
}


fun takeAllPaths(list: List<String>) {

    list.forEach { path ->
        val tempList = importer.readFromExcelFile(path)
        empList.addAll(tempList)
//        empList += importer.readFromExcelFile(path)
    }

}
