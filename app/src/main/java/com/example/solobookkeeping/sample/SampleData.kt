package com.example.solobookkeeping.sample

import com.example.solobookkeeping.model.Bookkeeping
import java.time.LocalDate

class SampleData {
    companion object {
        val sampleBookkeepingList = listOf(
            Bookkeeping(
                0,
                "早餐",
                "便利商店咖啡與三明治",
                -85.0,
                LocalDate.now().minusDays(2)
            ),
            Bookkeeping(
                1,
                "午餐",
                "自助餐",
                120.0,
                LocalDate.now().minusDays(2)
            ),
            Bookkeeping(
                2,
                "薪資",
                "兼職薪資收入",
                2500.0,
                LocalDate.now().minusDays(1)
            ),
            Bookkeeping(
                3,
                "交通",
                "捷運車資",
                -50.0,
                LocalDate.now().minusDays(1)
            ),
            Bookkeeping(4, "晚餐", "拉麵店",  -190.0, LocalDate.now()),
            Bookkeeping(5, "紅包", "家人給的紅包", 1000.0, LocalDate.now()),
            Bookkeeping(
                6,
                "飲料",
                "珍奶一杯",
                -60.0,
                LocalDate.now().plusDays(1)
            ),
            Bookkeeping(
                7,
                "投資回報",
                "基金配息",
                320.0,
                LocalDate.now().plusDays(1)
            ),
            Bookkeeping(
                8,
                "宵夜",
                "鹽酥雞與可樂",
                -150.0,
                LocalDate.now().plusDays(2)
            ),
            Bookkeeping(
                9,
                "退稅",
                "報稅退稅金",
                700.0,
                LocalDate.now().plusDays(2)
            )
        )
    }
}