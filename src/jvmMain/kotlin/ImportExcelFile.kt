import com.toxicbakery.logging.Arbor
import model.*
import org.apache.poi.ss.usermodel.WorkbookFactory
import org.example.util.Constatnts.Companion.getLastDay
import org.example.util.Constatnts.Companion.getMonth
import org.example.util.Constatnts.Companion.getWardiaAsNum
import util.AbsentType
import java.io.File
import java.io.FileInputStream
import java.nio.file.Files
import java.time.*
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.roundToInt

class ImportExcelFile
    (val month: String = "", val year: String = "")
//@Inject constructor()
{
    //    var month: String = "8"
//    var year: String = "2022"
    private var startDate = LocalDate.parse("$year-${getMonth((month.toInt() - 1).toString())}-21")
    private var endDate = LocalDate.parse("$year-${getMonth(month)}-20")

    private val daysOfMonth = ChronoUnit.DAYS.between(startDate, endDate) + 1
    private val days = getLastDay((month.toInt() - 1), year.toInt())
    private val pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")


    /**
     * Reads the value from the cell at the first row and first column of worksheet.
     */

    suspend fun getAllEmployeeInfo(list: List<String>): List<CamRegisterDay> {
//        val pathList = getPath(path, list)
        val empList = mutableListOf<CamRegisterDay>()


        if (list.isNotEmpty()) {

            list.forEach { pathFile ->
                val tempList = readFromExcelFile(pathFile)
                empList.addAll(tempList)
//        empList += importer.readFromExcelFile(path)
            }
        } else {
            println("Folder is Empty")
//            LCE.ERROR("Folder is Empty")
        }

        return empList
    }

    suspend fun getPath(path: String? = null, pList: List<String>? = null): List<String> {
        val pathList = if (pList == null) mutableListOf<String>() else return pList
        try {
            path.let { isPath ->
                val file = File(isPath.toString())
                if (file.isDirectory) {
                    if (file.list()?.isNotEmpty()!!) {
                        file.list()?.forEach { path ->
                            val sFile = file.path + File.separator + path
                            if (!Files.isDirectory(File(sFile).toPath()))
                                pathList.add(sFile)

                        }
                    }
                }
            }


        } catch (e: Exception) {
            println(e.message.toString())
            return emptyList()


        }
        return pathList

    }

    suspend fun getEmployReport(empList: List<CamRegisterDay>): List<EmployeeResult> {

        return try {

//            val file = File(path);
//            println("file name is ( ${file.name} )")

//            if (file.isDirectory) {

//                    file.list()?.forEach {
//                        println("file ...\n ${it}")
//                        val sFile = file.path + File.separator + it
//                        if (!Files.isDirectory(File(sFile).toPath()))
//                            pathList.add(sFile)
//
//                    }
//                val empList = getAllEmployeeInfo(path, pathList)
            println("empList = ${empList.size}")


            if (empList.isNotEmpty()) {
                println("excel imported successful")
                val employeeResultList = doSomeWork(empList)
                if (employeeResultList.isNotEmpty()) {
                    println("extracted employee result successful")
//                    empList.clear()
//                    LCE.CONTENT(employeeResultList)
                    employeeResultList

                } else {
                    println("No Employee Details Found")
//                    LCE.ERROR("No Employee Details Found")
                    emptyList()
                }


            } else {
                println("No Results Details Found")
//                LCE.ERROR("No Results Details Found")
                emptyList()
            }

//
//        }
//          else {
////                Arbor.sow(Seedling())
//                Arbor.d("Invalid Path Please check path again!")
////                println("Invalid Path Please check path again")
//                LCE.ERROR("Invalid Path Please check path again")
//            }


        } catch (e: Exception) {
            e.printStackTrace()
//            LCE.ERROR(e.message.toString())
            emptyList()
        }
    }


    @Throws
    fun readFromExcelFile(filePath: String): List<CamRegisterDay> {
        val empList = mutableListOf<CamRegisterDay>()
        try {
            val inputStream = FileInputStream(filePath)
//            val file = File(filePath)
            //Instantiate Excel workbook using existing file:
            val xlWb = WorkbookFactory.create(inputStream)
//            val xlWb = WorkbookFactory.create(file)

            //Row index specifies the row in the worksheet (starting at 0):
            var rowNumber = 0
            //Cell index specifies the column within the chosen row (starting at 0):

            //Get reference to first sheet:
            val xlWs = xlWb.getSheetAt(0)
            var stop: String?
            println(xlWs.lastRowNum)
            xlWs.forEach { row ->
                val emp = mutableListOf<String>()
                val status = mutableMapOf<String, String>()

                if (row.rowNum >= 1) {
                    for (columnNumber in 0..4) {
                        val value = xlWs.getRow(row.rowNum).getCell(columnNumber).toString()
                        when (columnNumber) {
                            0->{
                                status["id"] = value.substringAfter("'")
                            }
                            1 -> { // empName
                                status["empName"] = value
                            }

                            2 -> {//department
                                val ll = value.split("/")
                                status["department"] = ll[1]

//                                    status.add(ll[1])

                            }

                            3 -> {//date
                                status["date&time"] = value
                                val ll = value.split(" ")
                                status["date"] = ll[0]
                                status["time"] = ll[1]
                                status["hour"] = ll[1].split(":")[0]

//                                    status.add(ll[0])
//                                    status.add(ll[1])

                            }

                            4 -> {//date
                                status["attendState"] = value

                            }


                        }


                    }
                    //

                    val shift = getTimeShift(dateTime = status["date&time"]!!, status["attendState"]!!)

                    val employ = CamRegisterDay(
                        id = status["id"],
                        empName = status["empName"],
                        departName = status["department"],
                        oDATE = status["date"],
                        day = LocalDate.parse(status["date"]).dayOfWeek.toString(),
                        time = status["time"],
                        hour = status["hour"],
                        status = status["attendState"],
                        shift = shift
                    )
                    val patternCamRegisterDay = DateTimeFormatter.ofPattern("yyyy-MM-dd")

                    if ((LocalDate.parse(status["date"], patternCamRegisterDay)) in startDate..endDate)
                        empList.add(employ)


                }


            }


        } catch (e: Exception) {
            println("import error : ${e.message}")
            return emptyList()

        }


        println("period = ${daysOfMonth}")
//        getEmpReport(empList,"Mahmoud Ali")

        return empList
    }


    private fun getEmpReport(list: List<CamRegisterDay>, id: String = ""): EmployeeResult? {


        val l = list.filter {
            it.id == id
        }
        if (l.isNotEmpty()) {
            Arbor.d(startDate.toString())
            Arbor.d(endDate.toString())

            val ll = l.filter {

                val date = LocalDate.parse(it.oDATE)
                date in startDate..endDate
            }
            return if (ll.isNotEmpty()) {
                getAbsentDays(ll)
            } else {
                println("please enter correct month  :::: \n ll size = ${ll.size} ")
                null

            }

        } else {
            println("employee ( $id ) not found please enter right name")
            return null
        }


//        println(getAbsentDays(l).toString())
        println(l.size)

        return null
    }


    /**
     * extract DayDetail
     */
    suspend fun getDayDetail(camDayList: List<CamRegisterDay>) {
        val dayDetail = mutableListOf<DayDetails?>()
        val dayRegisterAttendsList = mutableListOf<RegisterAttends?>()
        val absentDays = mutableListOf<AbsentDay>()


//        camDayList.forEach { camDay ->
        var oDate = ""

//            val dayIn = LocalDateTime.parse("${empList[i].oDATE} ${empList[i].time}", pattern)
//            val dayOut = LocalDateTime.parse("${empList[i + 1].oDATE} ${empList[i + 1].time}", pattern)
        val patternCamRegisterDay = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        for (i in 21..days) {

            val tempDate = LocalDate.of(year.toInt(), month.toInt(), i)
            val specificDayList = camDayList.filter { camDay ->
                oDate = camDay.oDATE!!

                tempDate == LocalDate.parse(camDay.oDATE, patternCamRegisterDay)

            }
            val status = mutableMapOf<String, String>()
            val hourList = mutableListOf<String>()

            if (specificDayList.isNotEmpty()) {
                specificDayList.forEach { cam ->
                    val state = cam.status
                    status["empName"] = cam.empName.toString()
                    status["department"] = cam.departName.toString()
                    status["date"] = cam.oDATE.toString()
                    val hour = cam.shift.toString()
                    if (!hourList.contains(hour))
                        hourList.add(hour)
                    status["attendState"] = cam.status.toString()
                    status["timeIn"] = cam.time.toString()
                    status["timeOut"] = cam.time.toString()

                }
                repeat(hourList.size) {
                    RegisterAttends(
                        emp_name = status["empName"],
                        department = status["department"],
                        oDate = status["date"],
                        day = LocalDate.parse(status["date"]).dayOfWeek.toString(),
                        status = if (status["attendState"].equals("Check-in")) "Attend" else "",
                        in_time = if (status["attendState"].equals("Check-in")) status["timeIn"] else null,
                        out_time = if (status["attendState"].equals("Check-out")) status["timeOut"] else null,

                        late = null,
                        early = null,

                        ).apply {
                        if (!dayRegisterAttendsList.contains(this))
                            dayRegisterAttendsList.add(this)
                    }
                }


            } else {
                AbsentDay(
                    absnt_type = AbsentType.RegularVacation,
                    absnt_date = LocalDate.of(year.toInt(), month.toInt(), i),
                    absnt_reason = "",
                    month = month,
                    year = year,
                    name = status["empName"],
                    department = status["empName"]!!
                ).apply {
                    absentDays.add(this)
                }
            }

        }

        for (i in 1..20) {

        }

//            val day = camDayList.filter { tempCam ->
//                camDay.oDATE == tempCam.oDATE
//
//            }


//        }
    }

    /**
     * calculate time
     */
    fun calculateTime(time: String, state: String, constLateTime: LocalTime, ConstEarlyTime: LocalTime) {
        val tempTime = LocalTime.parse(time)
        if (state == "Check-in") {

        } else {

        }

    }

    fun getEmpReportById(list: List<CamRegisterDay>, id: String = "", empName: String = ""): EmployeeResult? {
        Arbor.d(empName)

        val l = list.filter {
//            it.id == id ||
            it.id == id
        }
        if (l.isNotEmpty()) {
            val ll = l.filter {

                val date = LocalDate.parse(it.oDATE)
                date in startDate..endDate
            }
            return if (ll.isNotEmpty()) {
                getAbsentDays(ll)
            } else {
                println("please enter month  :::: \n ll size = ${ll.size} ")
                null

            }

        } else {
            println("employee ( $empName ) not found please enter right name")
            return null
        }


//        println(getAbsentDays(l).toString())
        println(l.size)

        return null
    }

    private fun getTimeShift(dateTime: String, state: String): String {
        var shift = ""

        val date = LocalDateTime.parse(dateTime, this.pattern)


        date.withHour(8).withMinute(30)


//        println(time)

        if (state == "Check-in") {
            if (date.isAfter(date.withHour(7).withMinute(30)) && date.isBefore(date.withHour(8).withMinute(15))) {
//                println("check 1 ")

                shift = "morning"
            } else if (date.isAfter(date.withHour(13).withMinute(0)) && date.isBefore(
                    date.withHour(17).withMinute(0)
                )
            ) {
//                println("check 2 ")

                shift = "afternoon"
            } else if (date.isAfter(
                    date.withHour(22).withMinute(0)
                ) && date.isBefore(date.withHour(23).withMinute(59))
            ) {
//                println("check 3 ")

                shift = "night"
            } else if (date.isAfter(date.withHour(7).withMinute(30)) && date.isBefore(
                    date.withHour(8).withMinute(45)
                )
            ) {

                shift = "morning"

            } else {
                shift = "late"

            }
        } else {

            if (date.isAfter(date.withHour(15).withMinute(0)) && date.isBefore(date.withHour(16).withMinute(30))) {
//                println("check 1 ")

                shift = "morning"
            } else if (date.isAfter(date.withHour(22).withMinute(0)) && date.isBefore(
                    date.withHour(23).withMinute(0)
                )
            ) {
//                println("check 2 ")

                shift = "afternoon"
            } else if (date.isAfter(//date.dayOfMonth + 1
                    date.withDayOfMonth(date.minusDays(1).dayOfMonth).withHour(7).withMinute(0)
                ) && date.withDayOfMonth(date.minusDays(1).dayOfMonth).isBefore(date.withHour(8).withMinute(30))
            ) {
//                println("check 3 ")

                shift = "night"
            } else if (date.isAfter(//date.dayOfMonth + 1
                    date.withHour(7).withMinute(0)
                ) && date.isBefore(date.withHour(8).withMinute(30))
            ) {
//                println("check 3 ")

                shift = "night"
            }
        }

        return shift
    }


    private fun getDayDetails(employee: CamRegisterDay): String {
        var wardia = ""

        val date = LocalDateTime.parse("${employee.oDATE} ${employee.time}", pattern)

        val state = employee.status

        val time = date.toLocalTime()
        date.withHour(8).withMinute(30)


//        println(time)

        if (state == "Check-in") {
            if (date.isAfter(date.withHour(7).withMinute(30)) && date.isBefore(date.withHour(8).withMinute(15))) {
//                println("check 1 ")

                wardia = "اولى"
            } else if (date.isAfter(date.withHour(13).withMinute(0)) && date.isBefore(
                    date.withHour(17).withMinute(0)
                )
            ) {
//                println("check 2 ")

                wardia = "ثانية"
            } else if (date.isAfter(
                    date.withHour(22).withMinute(0)
                ) && date.isBefore(date.withHour(23).withMinute(59))
            ) {
//                println("check 3 ")

                wardia = "ثالثة"
            } else if (date.isAfter(date.withHour(7).withMinute(30)) && date.isBefore(
                    date.withHour(8).withMinute(45)
                )
            ) {

                wardia = "اولى متاخر"

            } else {
                wardia = "متاخر"

            }
        } else {

            if (date.isAfter(date.withHour(15).withMinute(0)) && date.isBefore(date.withHour(16).withMinute(30))) {
//                println("check 1 ")

                wardia = "اولى"
            } else if (date.isAfter(date.withHour(22).withMinute(0)) && date.isBefore(
                    date.withHour(23).withMinute(0)
                )
            ) {
//                println("check 2 ")

                wardia = "ثانية"
            } else if (date.isAfter(//date.dayOfMonth + 1
                    date.withDayOfMonth(date.minusDays(1).dayOfMonth).withHour(7).withMinute(0)
                ) && date.withDayOfMonth(date.minusDays(1).dayOfMonth).isBefore(date.withHour(8).withMinute(30))
            ) {
//                println("check 3 ")

                wardia = "ثالثة"
            } else if (date.isAfter(//date.dayOfMonth + 1
                    date.withHour(7).withMinute(0)
                ) && date.isBefore(date.withHour(8).withMinute(30))
            ) {
//                println("check 3 ")

                wardia = "ثالثة"
            }
        }


        return wardia
    }

    //    fun doSomeWork(list: List<Employee>){
    fun doSomeWork(list: List<CamRegisterDay>): List<EmployeeResult> {
        val employeeResultList = mutableListOf<EmployeeResult>()

        val empNameList =
            list.map {
                it.id
            }.distinct()
        println("empNameList size = ${empNameList.size}")
        var i = 0
        empNameList.forEach { empName ->
//            val empName = it.name
//            if (!empNameList.contains(empName)) {
//            println("empName = $empName")
            val empResult = getEmpReport(list, id = empName!!)
            if (empResult != null) {
                employeeResultList.add(empResult)
            }
            i++
//            println("done${i}")
//           getEmpReport(list, empName)


        }

        return employeeResultList

    }


    fun getAbsentDays(list: List<CamRegisterDay>): EmployeeResult {
        var empList = list.toMutableList()
        var numberOfAttendantDays = 0
        var attendDays = mutableListOf<DayDetails>()

//        var attendDays: String = ""
        var wardia = ""
        var daysToCheckNoted = ""
        var partTime = 0.0
        var partTimeDays = ""
        var totalEarlyAccessTime = 0.0
        var totalEarlyAccessTimeDays = ""
        var earlyAccess = 0.0
        var earlyAccessNote = ""
        var earlyAccessTime = ""


        var numberOfAbsentDays = 0
//        var absentDays: String = ""
        val absentDays: MutableList<AbsentDay> = mutableListOf<AbsentDay>()

        var i = 0
        var temp = 0

        var pTime = 0.0
        var dayType = ""
        while (i < empList.size) {
            if (empList[i].status == "Check-in") {
                if (i + 1 < empList.size) {

                    val dayIn = LocalDateTime.parse("${empList[i].oDATE} ${empList[i].time}", pattern)
                    val dayOut = LocalDateTime.parse("${empList[i + 1].oDATE} ${empList[i + 1].time}", pattern)

                    val timeWork = ChronoUnit.MINUTES.between(dayIn, dayOut) / 60

//                    println("timeWork = $timeWork")

                    if (empList[i + 1].status == "Check-out") {

                        wardia = getDayDetails(empList[i])

                        val intWardia = getWardiaAsNum(wardia)

                        if (temp != dayIn.dayOfMonth) {
                            if (empList[i + 1].status == "Check-out") {
//                                pTime = getPartTimeDetails(dayOut, intWardia)
//                                earlyAccess = getEarlyTimeAccess(dayOut, intWardia)
                                pTime = (getPartTimeDetails(dayOut, intWardia) * 100.0).roundToInt() / 100.0
                                earlyAccess = (getEarlyTimeAccess(dayOut, intWardia) * 100.0).roundToInt() / 100.0

                                if (pTime != 0.0) partTimeDays += dayIn.dayOfMonth.toString() + " , "
                                if (earlyAccess != 0.0) totalEarlyAccessTimeDays += dayIn.dayOfMonth.toString() + " , "

                                totalEarlyAccessTime += earlyAccess
//                                println("earlyAccess = $earlyAccess")

                                if (earlyAccess != 0.0) {
                                    earlyAccessNote = "خروج بدرى"
                                    earlyAccessTime = dayOut.toLocalTime().format(
                                        DateTimeFormatter.ofPattern("HH:mm")
                                    ).toString()
                                }
                            }
                        } else {
                            dayType = "مطبق"

                        }
                        partTime += pTime


                        val dayDetails =
                            DayDetails(
                                empList[i].empName ?: "",
                                empList[i].departName ?: "",
                                dayIn.dayOfMonth.toString(),
                                month,
                                year,
                                wardia,
                                dayType,
                                pTime,
                                earlyAccessTime,
                                earlyAccessNote,
                                ""
                            )
//                        println(dayDetails)
                        attendDays.add(dayDetails)
                        numberOfAttendantDays++
                        temp = dayIn.dayOfMonth
                        dayType = ""
                        earlyAccessTime = ""
                        earlyAccessNote = ""
                        pTime = 0.0
                        earlyAccess = 0.0
                        i++

                    } else {

                        if (LocalDate.parse(empList[i].oDATE).dayOfMonth == LocalDate.parse(empList[i + 1].oDATE).dayOfMonth && LocalTime.parse(
                                empList[i].time
                            ).hour == LocalTime.parse(empList[i + 1].time).hour
                        ) {
                            empList.removeAt(i + 1)
                        } else {
                            val dayIn = LocalDateTime.parse("${empList[i].oDATE} ${empList[i].time}", pattern)
                            val note = "ناقص خروج"
                            daysToCheckNoted += dayIn.dayOfMonth.toString() + " , "
                            val dayDetails =
                                DayDetails(
                                    empList[i].empName ?: "",
                                    empList[i].departName ?: "",
                                    dayIn.dayOfMonth.toString(),

                                    month,
                                    year,
                                    getDayDetails(empList[i + 1]),
                                    dayType,
                                    pTime,
                                    note
                                )

//                            println(dayDetails)
                            attendDays.add(dayDetails)
                            numberOfAttendantDays++
                            temp = dayOut.dayOfMonth
                            dayType = ""
                            earlyAccessTime = ""
                            earlyAccessNote = ""
                            pTime = 0.0
                            earlyAccess = 0.0
                            i++
                        }

                    }
                } else if (i + 1 == empList.size) {
                    val dayIn = LocalDateTime.parse("${empList[i].oDATE} ${empList[i].time}", pattern)
                    val note = "ناقص دخول"
                    daysToCheckNoted += dayIn.dayOfMonth.toString() + " , "

                    val dayDetails =
                        DayDetails(
                            empList[i].empName ?: "",
                            empList[i].departName ?: "",
                            dayIn.dayOfMonth.toString(),
                            month,
                            year,
                            getDayDetails(empList[i]),
                            dayType, pTime, note
                        )

//                    println(dayDetails)
                    attendDays.add(dayDetails)
                    numberOfAttendantDays++
                    temp = dayIn.dayOfMonth
                    dayType = ""
                    earlyAccessTime = ""
                    earlyAccessNote = ""
                    pTime = 0.0
                    earlyAccess = 0.0
                    i++
                } else {
                    i++
                }


            } else { // check out
                if (i == 0) {

                    wardia = getDayDetails(empList[i])
                    val intWardia = getWardiaAsNum(wardia)
                    if (intWardia == 3) { // if wardia 3 delete
                        empList.removeAt(i)
                    } else {
                        val dayOut = LocalDateTime.parse("${empList[i].oDATE} ${empList[i].time}", pattern)
                        val note = "ناقص دخول"
                        daysToCheckNoted += dayOut.dayOfMonth.toString() + " , "
                        if (temp != dayOut.dayOfMonth) {
                            if (i + 1 < empList.size) {
                                if (empList[i + 1].status == "Check-out") {
                                    empList.removeAt(i)
                                } else {
                                    pTime = (getPartTimeDetails(dayOut, intWardia) * 100.0).roundToInt() / 100.0
                                    earlyAccess = (getEarlyTimeAccess(dayOut, intWardia) * 100.0).roundToInt() / 100.0


                                    if (pTime != 0.0) partTimeDays += dayOut.dayOfMonth.toString() + " , "
                                    if (earlyAccess != 0.0) totalEarlyAccessTimeDays += dayOut.dayOfMonth.toString() + " , "

                                    totalEarlyAccessTime += earlyAccess
//                            println("earlyAccess = $earlyAccess")

                                    if (earlyAccess != 0.0) {
                                        earlyAccessNote = "خروج بدرى"
                                        earlyAccessTime = dayOut.toLocalTime().format(
                                            DateTimeFormatter.ofPattern("HH:mm")
                                        ).toString()
                                    }
                                }
                            }

                        } else {
                            dayType = "مطبق"

                        }

                        val dayDetails =
                            DayDetails(
                                empList[i].empName ?: "",
                                empList[i].departName ?: "",
                                dayOut.dayOfMonth.toString(),

                                month,
                                year,
                                wardia,
                                dayType,
                                pTime,
                                earlyAccessTime,
                                earlyAccessNote,
                                note
                            )

//                    println(dayDetails)
                        attendDays.add(dayDetails)
                        numberOfAttendantDays++
                        temp = dayOut.dayOfMonth
                        dayType = ""
                        dayType = ""
                        earlyAccessTime = ""
                        earlyAccessNote = ""
                        pTime = 0.0
                        earlyAccess = 0.0


                        i++
                    }


                } else if (i + 1 < empList.size) {
                    if (empList[i + 1].status == "Check-in") {

                        i++

                    } else {
                        if (LocalDate.parse(empList[i].oDATE).dayOfMonth == LocalDate.parse(empList[i + 1].oDATE).dayOfMonth
                            && LocalTime.parse(empList[i].time).hour == LocalTime.parse(empList[i + 1].time).hour
                        ) {
                            empList.removeAt(i)
                        } else {
                            val dayOut = LocalDateTime.parse("${empList[i + 1].oDATE} ${empList[i + 1].time}", pattern)
                            val note = "ناقص دخول"
                            daysToCheckNoted += dayOut.dayOfMonth.toString() + " , "

                            wardia = getDayDetails(empList[i])
                            val intWardia = getWardiaAsNum(wardia)

                            if (temp != dayOut.dayOfMonth) {
                                if (empList[i + 1].status == "Check-out") {
                                    pTime = (getPartTimeDetails(dayOut, intWardia) * 100.0).roundToInt() / 100.0
                                    earlyAccess = (getEarlyTimeAccess(dayOut, intWardia) * 100.0).roundToInt() / 100.0

                                    if (pTime != 0.0) partTimeDays += dayOut.dayOfMonth.toString() + " , "
                                    if (earlyAccess != 0.0) totalEarlyAccessTimeDays += dayOut.dayOfMonth.toString() + " , "


                                    totalEarlyAccessTime += earlyAccess
//                                    println("earlyAccess = $earlyAccess")

//                                    println(earlyAccess)

                                    if (earlyAccess != 0.0) {
                                        earlyAccessNote = "خروج بدرى"
                                        earlyAccessTime = dayOut.toLocalTime().format(
                                            DateTimeFormatter.ofPattern("HH:mm")
                                        ).toString()
                                    }
                                }
                            } else {
                                dayType = "مطبق"

                            }

                            val dayDetails =
                                DayDetails(
                                    empList[i + 1].empName ?: "",
                                    empList[i + 1].departName ?: "",
                                    dayOut.dayOfMonth.toString(),

                                    month,
                                    year,
                                    wardia,
                                    dayType,
                                    pTime,
                                    earlyAccessTime,
                                    earlyAccessNote,
                                    note
                                )

//                            println(dayDetails)
                            attendDays.add(dayDetails)
                            numberOfAttendantDays++
                            temp = dayOut.dayOfMonth
                            dayType = ""
                            dayType = ""
                            earlyAccessTime = ""
                            earlyAccessNote = ""
                            pTime = 0.0
                            earlyAccess = 0.0
                            i++
                        }
                    }
                } else if (i + 1 == empList.size && LocalDate.parse(empList[i].oDATE).dayOfMonth != LocalDate.parse(
                        empList[i].oDATE
                    ).dayOfMonth
                ) {
                    val dayIn = LocalDateTime.parse("${empList[i].oDATE} ${empList[i].time}", pattern)
                    val note = "ناقص خروج"
                    daysToCheckNoted += dayIn.dayOfMonth.toString() + " , "

                    val dayDetails =
                        DayDetails(
                            empList[i].empName ?: "",
                            empList[i].departName ?: "",
                            dayIn.dayOfMonth.toString(),

                            month,
                            year,
                            getDayDetails(empList[i]),
                            dayType,
                            pTime,
                            note
                        )

//                    println(dayDetails)
                    attendDays.add(dayDetails)
                    numberOfAttendantDays++
                    temp = dayIn.dayOfMonth
                    dayType = ""
                    earlyAccessTime = ""
                    earlyAccessNote = ""
                    pTime = 0.0
                    earlyAccess = 0.0
                    i++
                } else {
                    i++
                }


            }


        }


        val checkInList = attendDays.map { attendDays ->
            attendDays.day.toInt()
        }


        for (i in 21..days) {
            if (!checkInList.contains(i)) {//check to see absent days
                AbsentDay(
                    absnt_type = AbsentType.RegularVacation,
                    absnt_date = LocalDate.of(year.toInt(), month.toInt(), i),
                    absnt_reason = "",
                    month = month,
                    year = year,
                    name = list[0].empName!!,
                    department = list[0].departName!!
                ).apply {
                    absentDays.add(this)
                }
//                absentDays = absentDays.plus(i.toString().plus(","))

                numberOfAbsentDays++
            }

        }
        for (i in 1..20) {
            if (!checkInList.contains(i)) {//check to see absent days
                AbsentDay(
                    absnt_type = AbsentType.RegularVacation,
                    absnt_date = LocalDate.of(year.toInt(), month.toInt(), i),
                    absnt_reason = "",
                    month = month,
                    year = year,
                    name = list[0].empName!!,
                    department = list[0].departName!!
                ).apply {
                    absentDays.add(this)
                }
//                absentDays = absentDays.plus(i.toString().plus(","))

                numberOfAbsentDays++
            }

        }


//        println(attendDays.toString())


        val dayDetails = attendDays.toString()
        val totalPartTime = (partTime * 100.0).roundToInt() / 100.0
        val totalAccessTime = (totalEarlyAccessTime * 100.0).roundToInt() / 100.0
        val employeeResult = EmployeeResult(
//            list[0].id,
            list[0].empName!!,
            list[0].departName!!,
            month,
            year,
            numberOfAttendantDays,
            daysToCheckNoted,
            numberOfAbsentDays,
            totalPartTime,
            partTimeDays,
            totalEarlyAccessTime,
            totalEarlyAccessTimeDays,
            absentDays,
            attendDays
        )
//        println(employeeResult.toString())

        return employeeResult
    }


    private fun getPartTimeDetails(checkOutTime: LocalDateTime, wardia: Int): Double {
        var partTime = 0.0
        val time = checkOutTime.toLocalTime()


//        println(time)
        if (wardia == 1 && checkOutTime.isAfter(   //4 pm
                checkOutTime.withHour(17).withMinute(0)
            ) && checkOutTime.isBefore(checkOutTime.withHour(22).withMinute(0))//10pm
        ) {
//            println("check 1 ")

            val t = LocalTime.of(16, 0)
            val ii = Duration.between(t, time).toMinutes()

//            println("kkkkkkkkkk $ii")
            partTime = ii.toDouble() / 60
        } else if (wardia == 2 &&
            checkOutTime.isBefore(
                checkOutTime.withHour(8).withMinute(0)
            ) &&
            checkOutTime.isAfter(
                checkOutTime.withDayOfMonth(checkOutTime.dayOfMonth + 1).withHour(0).withMinute(0)
            )
        ) {
//            println("check 2 ")

            val t = LocalTime.of(23, 0)
            val ii = Duration.between(t, time).toMinutes()

//            println("kkkkkkkkkk $ii")
            partTime = ii.toDouble() / 60
        } else if (wardia == 3 &&
            checkOutTime.isBefore(
                checkOutTime.withHour(16).withMinute(0)
            ) &&
            checkOutTime.isAfter(
                checkOutTime.withHour(9).withMinute(0)
            )
        ) {
//            println("check 3 ")

            val t = LocalTime.of(8, 0)
            val ii = Duration.between(t, time).toMinutes()

//            println("kkkkkkkkkk $ii")
            partTime = ii.toDouble() / 60
        }

        return partTime
    }


    private fun getEarlyTimeAccess(checkOutTime: LocalDateTime, wardia: Int): Double {
        var earlyAccess = 0.0
        val time = checkOutTime.toLocalTime()


//        println(time)
        if (wardia == 1 && checkOutTime.isBefore(   //4 pm
                checkOutTime.withHour(15).withMinute(20)//3:20 pm
            )
        ) {
//            println("check 1 ")

            val t = LocalTime.of(16, 0) // 4:00 pm
            val ii = Duration.between(time, t).toMinutes()

//            println("EarlyTimeAccess $ii")
            earlyAccess = ii.toDouble() / 60
        } else if (wardia == 2 &&
            checkOutTime.isBefore(
                checkOutTime.withHour(22).withMinute(20)
            )
        ) {
//            println("check 2 ")

            val t = LocalTime.of(23, 0)
            val ii = Duration.between(time, t).toMinutes()

//            println("EarlyTimeAccess $ii")
            earlyAccess = ii.toDouble() / 60
        } else if (wardia == 3 &&
            checkOutTime.isBefore(
                checkOutTime.withHour(7).withMinute(20)
            )
        ) {
//            println("check 3 ")

            val t = LocalTime.of(8, 0)
            val ii = Duration.between(time, t).toMinutes()

//            println("EarlyTimeAccess $ii")
            earlyAccess = ii.toDouble() / 60
        }

        return earlyAccess
    }


    private fun extractDate(list: List<Employee>) {
        val list = list.map {
            it.day.split("-")[0]


        }.distinct()
        var year = 0
        if (list.size == 1) {
            year = list[0].toInt()
        } else {
            list.forEach { year ->
                var tempYear = year
                var count = 0
                if (tempYear == year)
                    count++


            }

        }


//    println(empList[empList.size-1])
    }

    fun countDaysBetweenTwoCalendar(calendarStart: Calendar, calendarEnd: Calendar): Int {
        val millionSeconds = calendarEnd.timeInMillis - calendarStart.timeInMillis
        val days = TimeUnit.MILLISECONDS.toDays(millionSeconds) //this way not round number
        val daysRounded = (millionSeconds / (1000.0 * 60 * 60 * 24)).roundToInt()
        return daysRounded
    }

}