package com.example.exampleproject

object ArrayUtil {

    fun demoTest() {
        // 示例
        val interval1 = listOf(listOf(0, 30), listOf(5, 10), listOf(15, 20))
        val interval2 = listOf(listOf(7, 10), listOf(2, 4))
        val interval3 = listOf(listOf(1, 5), listOf(8, 9), listOf(8, 10))

        println(deferAttendMeeting(interval1)) // 输出: false
        println(deferAttendMeeting(interval2)) // 输出: true
        println(deferAttendMeeting(interval3)) // 输出: false
    }

    fun deferAttendMeeting(intervals: List<List<Int>>) : Boolean{
        // 先根据开始时间对会议进行排序
        val sortedIntervals = intervals.sortedWith(compareBy { it[0] })

        // 遍历排序后的会议
        for (i in 1 until sortedIntervals.size) {
            val previousEnd = sortedIntervals[i - 1][1]
            val currentStart = sortedIntervals[i][0]

            // 如果当前会议的开始时间小于或等于前一个会议的结束时间，存在重叠
            if (currentStart <= previousEnd) {
                return false
            }
        }

        // 没有重叠，可以参加所有会议
        return true
    }
}