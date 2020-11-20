package com.serosoft.academiassu.Modules.TimeTable

import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import android.view.MenuItem
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.view.children
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.CalendarMonth
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.model.ScrollMode
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import com.kizitonwose.calendarview.utils.next
import com.kizitonwose.calendarview.utils.previous
import com.serosoft.academiassu.Interfaces.AsyncTaskCompleteListener
import com.serosoft.academiassu.Networking.OptimizedServerCallAsyncTask
import com.serosoft.academiassu.Modules.Dashboard.DashboardActivity
import com.serosoft.academiassu.Networking.KEYS
import com.serosoft.academiassu.R
import com.serosoft.academiassu.Utils.*
import kotlinx.android.synthetic.main.activity_time_table_day_wise.*
import kotlinx.android.synthetic.main.k_calendar_day.view.*
import kotlinx.android.synthetic.main.k_calendar_day_legend.view.*
import org.json.JSONArray
import org.json.JSONObject
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.YearMonth
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.TextStyle
import java.text.SimpleDateFormat
import java.util.*

class TimeTableDayWiseListActivityK:BaseActivity(), AsyncTaskCompleteListener {

    private var timeTableRL:RelativeLayout? = null
    private var noTimeTableDetail:TextView? = null
    private var toolbar:Toolbar? = null
    private var timeTableList:List<JSONObject>? = null
    private var holidayList:List<JSONObject>? = null

    private var startDate1 = ""
    private var startDate2 = ""
    private var endDate1 = ""
    private var endDate2 = ""
    private var periodId = ""
    private var isCalendarLoaded = false
    private var startTime:Long = 0;
    private var endTime:Long = 0;

    private var selectedDate: LocalDate? = null
    private val monthTitleFormatter = DateTimeFormatter.ofPattern("MMMM")

    override fun onCreate(savedInstanceState:Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_time_table_day_wise)

        Initialize()

        if (ConnectionDetector.isConnectingToInternet(this)) {

            periodId = sharedPrefrenceManager.periodIDFromKey.toString();
            showProgressDialog(this)
            OptimizedServerCallAsyncTask(this,
                    this, KEYS.SWITCH_STUDENT_CALENDAR_DURATION).execute(periodId)
        } else {
            Toast.makeText(this, KEYS.NET_ERROR_MESSAGE, Toast.LENGTH_SHORT).show()
        }
    }

    private fun displayCalendar(startTime: Long, endTime: Long) {

        if (!isCalendarLoaded) {

            exFiveRv.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
            exFiveRv.addItemDecoration(DividerItemDecoration(this, RecyclerView.VERTICAL))

            val daysOfWeek = daysOfWeekFromLocale()

            //Here set startMonth and endMonth
            val strMonth = ProjectUtils.getMonth(startTime)
            val endMonth = ProjectUtils.getMonth(endTime)

            val currentMonth = YearMonth.now()

            if(strMonth > currentMonth){
                exFiveCalendar.setup(currentMonth, endMonth, daysOfWeek.first())
                exFivePreviousMonthImage.setImageResource(R.drawable.k_calendar_left_disable)
            }else if(endMonth < currentMonth){
                exFiveCalendar.setup(strMonth, currentMonth, daysOfWeek.first())
                exFiveNextMonthImage.setImageResource(R.drawable.k_calendar_right_disable)
            }else{
                exFiveCalendar.setup(strMonth, endMonth, daysOfWeek.first())
                exFivePreviousMonthImage.setImageResource(R.drawable.k_calendar_left)
                exFiveNextMonthImage.setImageResource(R.drawable.k_calendar_right)
            }

            exFiveCalendar.scrollMode = ScrollMode.PAGED
            exFiveCalendar.scrollToMonth(currentMonth)

            class DayViewContainer(view: View) : ViewContainer(view) {

                lateinit var day: CalendarDay
                val textView = view.exFiveDayText
                val layout = view.exFiveDayLayout
                val viewForClassDot = view.viewForClassDot
                val viewForHolidayDot = view.viewForHolidayDot
                val viewForTodayDot = view.viewForTodayDot

                init {
                    view.setOnClickListener {

                        //Date is tapped! Do something with it.
                        val yy = day.date.year.toString()
                        val mm = day.date.month.value.toString()
                        val dd = day.date.dayOfMonth.toString()
                        val calendarDateString = "$yy-$mm-$dd"

                        //HOLIDAY:
                        var thisDateContainsHoliday = false
                        val arrayOfHolidays: MutableList<String> = ArrayList()

                        if (holidayList != null) {
                            for (i in holidayList!!.indices) {
                                val dict = holidayList!![i]
                                val longDate = dict.getLong("startDate")
                                val holidayDateString = convertLongToTime(longDate)
                                if (holidayDateString == calendarDateString) {
                                    thisDateContainsHoliday = true
                                    val reasonString = dict.getString("holidayReason")
                                    arrayOfHolidays.add(reasonString)
                                }
                            }
                        }
                        if (thisDateContainsHoliday) {

                            var stringForHolidays = ""
                            val titleText = "Holiday!"

                            if (arrayOfHolidays.size == 1) {

                                stringForHolidays = arrayOfHolidays[0]

                            } else if (arrayOfHolidays.size == 2) {

                                stringForHolidays = arrayOfHolidays[0] + " and " + arrayOfHolidays[1]

                            } else {

                                for (i in arrayOfHolidays.indices) {

                                    if (i == arrayOfHolidays.size - 1) {
                                        stringForHolidays = stringForHolidays + arrayOfHolidays[i]
                                    } else if (i == arrayOfHolidays.size - 2) {
                                        stringForHolidays = stringForHolidays + arrayOfHolidays[i] + ", and "
                                    } else {
                                        stringForHolidays = stringForHolidays + arrayOfHolidays[i] + ", "
                                    }
                                }
                            }

                            val messageText = "You have $stringForHolidays on this day."

                            //Show Holiday PopUp
                            showAlertDialog(this@TimeTableDayWiseListActivityK, titleText, messageText)
                        }
                        //@END:HOLIDAY:

                        //CLASS:
                        var thisDateContainsClass = false

                        if (timeTableList != null) {
                            for (i in timeTableList!!.indices) {
                                val dict = timeTableList!![i]
                                val longDate = dict.getLong("orgDate_long")
                                val timetableDateString = convertLongToTime(longDate)
                                if (timetableDateString == calendarDateString) {
                                    //Show timeTable screen
                                    thisDateContainsClass = true
                                }
                            }
                        }
                        if (thisDateContainsClass) {
                            val intent = Intent(this@TimeTableDayWiseListActivityK, TimeTableDayWiseDetailListActivity::class.java)
                            val jsArray = JSONArray(timeTableList)
                            TimeTableDayWiseData.getInstance().timeTableDayWiseString = jsArray.toString()

                            intent.putExtra("startDate", calendarDateString)
                            intent.putExtra("timeTableString", jsArray.toString())
                            startActivity(intent)
                        }
                    }
                }
            }

            exFiveCalendar.dayBinder = object : DayBinder<DayViewContainer> {
                override fun create(view: View) = DayViewContainer(view)
                override fun bind(container: DayViewContainer, day: CalendarDay) {
                    container.day = day
                    val textView = container.textView
                    val layout = container.layout
                    textView.text = day.date.dayOfMonth.toString()

                    val viewForClassDot = container.viewForClassDot
                    val viewForHolidayDot = container.viewForHolidayDot
                    val viewForTodayDot = container.viewForTodayDot

                    if (day.owner == DayOwner.THIS_MONTH) {
                        textView.setTextColorRes(R.color.k_cakendar_text_grey)
                        layout.setBackgroundResource(if (selectedDate == day.date) R.drawable.k_calendar_selected_bg else 0)

                        var showHoliday = false

                        if (holidayList != null) {
                            for (i in holidayList!!.indices) {
                                val dict = holidayList!![i]
                                val longDate = dict.getLong("startDate")
                                val holidayDateString = convertLongToTime(longDate)
                                val yy = day.date.year.toString()
                                val mm = day.date.month.value.toString()
                                val dd = day.date.dayOfMonth.toString()
                                val calendarDateString = "$yy-$mm-$dd"
                                if (holidayDateString == calendarDateString) {
                                    showHoliday = true
                                }
                            }
                        }

                        var showClass = false

                        if (timeTableList != null) {
                            for (i in timeTableList!!.indices) {
                                val dict = timeTableList!![i]
                                val longDate = dict.getLong("orgDate_long")
                                val timetableDateString = convertLongToTime(longDate)
                                val yy = day.date.year.toString()
                                val mm = day.date.month.value.toString()
                                val dd = day.date.dayOfMonth.toString()
                                val calendarDateString = "$yy-$mm-$dd"
                                if (timetableDateString == calendarDateString) {
                                    showClass = true
                                }
                            }
                        }

                        var showToday = false
                        val todayDate = System.currentTimeMillis()
                        val yy = day.date.year.toString()
                        val mm = day.date.month.value.toString()
                        val dd = day.date.dayOfMonth.toString()
                        val calendarDateString = "$yy-$mm-$dd"
                        val todayDateString = convertLongToTime(todayDate)
                        if (todayDateString == calendarDateString) {
                            showToday = true
                        }

                        if (showToday) {
                            viewForTodayDot.visibility = View.VISIBLE
                            textView.setTextColorRes(R.color.white)
                        } else {
                            viewForTodayDot.visibility = View.INVISIBLE
                        }

                        if (showClass == true && showHoliday == true) {
                            viewForClassDot.visibility = View.VISIBLE
                            viewForHolidayDot.visibility = View.VISIBLE
                        } else if (showClass == true && showHoliday == false) {
                            viewForClassDot.visibility = View.VISIBLE
                            viewForHolidayDot.visibility = View.GONE
                        } else if (showClass == false && showHoliday == true) {
                            viewForClassDot.visibility = View.GONE
                            viewForHolidayDot.visibility = View.VISIBLE
                        } else {
                            viewForClassDot.visibility = View.GONE
                            viewForHolidayDot.visibility = View.GONE
                        }

                    } else {
                        textView.setTextColorRes(R.color.k_cakendar_text_grey_light)
                        layout.background = null
                        viewForClassDot.visibility = View.GONE
                        viewForHolidayDot.visibility = View.GONE
                    }
                }
            }

            class MonthViewContainer(view: View) : ViewContainer(view) {
                val legendLayout = view.legendLayout
            }
            exFiveCalendar.monthHeaderBinder = object : MonthHeaderFooterBinder<MonthViewContainer> {
                override fun create(view: View) = MonthViewContainer(view)
                override fun bind(container: MonthViewContainer, month: CalendarMonth) {
                    // Setup each header day text if we have not done that already.
                    if (container.legendLayout.tag == null) {
                        container.legendLayout.tag = month.yearMonth
                        container.legendLayout.children.map { it as TextView }.forEachIndexed { index, tv ->
                            tv.text = daysOfWeek[index].getDisplayName(TextStyle.SHORT, Locale.ENGLISH)
                                    .toUpperCase(Locale.ENGLISH)
                            tv.setTextColorRes(R.color.colorBlue)
                            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
                        }
                        month.yearMonth
                    }
                }
            }

            exFiveCalendar.monthScrollListener = { month ->
                val title = "${monthTitleFormatter.format(month.yearMonth)} ${month.yearMonth.year}"
                exFiveMonthYearText.text = title

                //Month is swiped. Do something with it.
                var mm = month.month
                var yy = month.year

                var cal = Calendar.getInstance()
                cal.set(yy, mm, 0)
                var daycount = cal.get(Calendar.DAY_OF_MONTH).toString()
                var yyy = cal.get(Calendar.YEAR).toString()
                var mmm = (cal.get(Calendar.MONTH) + 1).toString()

                startDate1 = "$mmm-1-$yyy"
                endDate1 = "$mmm-$daycount-$yyy"

                startDate2 = "$yyy-$mmm-1"
                endDate2 = "$yyy-$mmm-$daycount"

                if (ConnectionDetector.isConnectingToInternet(this)) {

                    ProjectUtils.preventTwoClick(exFiveNextMonthImage)
                    ProjectUtils.preventTwoClick(exFivePreviousMonthImage)

                    hideProgressDialog()
                    showProgressDialog(this)
                    OptimizedServerCallAsyncTask(this,
                            this, KEYS.SWITCH_STUDENT_CALENDAR_MONTHWISE).execute(startDate1, endDate1)
                } else {
                    Toast.makeText(this, KEYS.NET_ERROR_MESSAGE, Toast.LENGTH_SHORT).show()
                }

                selectedDate?.let {
                    // Clear selection if we scroll to a new month.
                    selectedDate = null
                    exFiveCalendar.notifyDateChanged(it)
                }
            }

            exFiveNextMonthImage.setOnClickListener {
                exFiveCalendar.findFirstVisibleMonth()?.let {
                    exFiveCalendar.smoothScrollToMonth(it.yearMonth.next)
                }
            }

            exFivePreviousMonthImage.setOnClickListener {
                exFiveCalendar.findFirstVisibleMonth()?.let {
                    exFiveCalendar.smoothScrollToMonth(it.yearMonth.previous)
                }
            }
            isCalendarLoaded = true
        } else {
            val currentMonth = YearMonth.now()
            exFiveCalendar.smoothScrollToMonth(currentMonth)
        }

    }

    private fun Initialize() {

        toolbar = findViewById(R.id.group_toolbar)
        timeTableRL = findViewById(R.id.timeTableRL)
        noTimeTableDetail = findViewById(R.id.noTimeTableDetail)

        toolbar?.setTitle(translationManager.DAY_WISE_KEY.toUpperCase())
        setSupportActionBar(toolbar)
        var actionBar = getSupportActionBar()
        actionBar?.setDisplayHomeAsUpEnabled(true)
        timeTableList = ArrayList<JSONObject>() as List<JSONObject>?

        if (Flags.COLOR_FLAG) {
            this.setScreenThemeColor(R.color.colorTimetable, toolbar, this)
        }
    }

    override fun onTaskComplete(result:HashMap<String, String>) {
        val callFor = result.get(KEYS.CALL_FOR)
        val responseResult = result.get(KEYS.RETURN_RESPONSE)

        when (callFor) {
            KEYS.SWITCH_STUDENT_CALENDAR_DURATION -> {
                hideProgressDialog()

                try {
                    val responseObject = JSONObject(responseResult)
                    ProjectUtils.showLog("TAG", responseObject.toString());

                    startTime = responseObject.optLong("startDate")
                    endTime = responseObject.optLong("endDate")

                    displayCalendar(startTime,endTime)

                } catch (e: Exception) {
                    e.printStackTrace()
                    hideProgressDialog()
                    showAlertDialog(this, "OOPS!", "Parsing Error at " + this.localClassName)
                }
            }

            KEYS.SWITCH_STUDENT_CALENDAR_MONTHWISE -> {
                hideProgressDialog()

                try {
                    var ttList = ArrayList <JSONObject>()
                    var  arr: JSONArray? = JSONArray()
                    val responseObject = JSONObject(responseResult)
                    if (responseObject.has("whatever")) {
                        arr = responseObject.getJSONArray("whatever")
                        for (i in 0 until arr.length()) {
                            val obj = arr.getJSONObject(i)
                            ttList.add(obj)
                        }
                        timeTableList = ttList
                    }
                    OptimizedServerCallAsyncTask(this,
                            this, KEYS.SWITCH_STUDENT_HOLIDAYS_MONTHWISE).execute(startDate2, endDate2)

                } catch (e: Exception) {
                    e.printStackTrace()
                    hideProgressDialog()
                    showAlertDialog(this, "OOPS!", "Parsing Error at " + this.localClassName)
                }
            }

            KEYS.SWITCH_STUDENT_HOLIDAYS_MONTHWISE -> {
                try {
                    var hList = ArrayList <JSONObject>()
                    var  arr: JSONArray? = JSONArray()
                    val responseObject = JSONObject(responseResult)
                    if (responseObject.has("lstOfCalendarHoliday")) {
                        arr = responseObject.getJSONArray("lstOfCalendarHoliday")
                        for (i in 0 until arr.length()) {
                            val obj = arr.getJSONObject(i)
                            hList.add(obj)
                        }
                        holidayList = hList
                    }

                    exFiveCalendar.notifyCalendarChanged()
                    hideProgressDialog()

                } catch (e: Exception) {
                    e.printStackTrace()
                    hideProgressDialog()
                    showAlertDialog(this, "OOPS!", "Parsing Error at " + this.localClassName)
                }
            }

            KEYS.SWITCH_NOTIFICATIONS -> populateNotificationsList(responseResult)

            KEYS.SWITCH_NOTIFICATIONS_COUNT -> showNotificationCount(responseResult)
        }
    }

    fun convertLongToTime(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("yyyy-M-d")
        return format.format(date)
    }

    override fun onOptionsItemSelected(item:MenuItem):Boolean {
        when (item.getItemId()) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
            R.id.dashboardMenu -> {
                val intent = Intent(this, DashboardActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
                return true
            }
            R.id.refresh -> {
                //displayCalendar()

                getNotifications()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()

        hideProgressDialog();
    }
}

