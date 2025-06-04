package com.example.solobookkeeping.sample

import com.example.solobookkeeping.model.Bookkeeping
import com.example.solobookkeeping.model.BookkeepingType
import java.time.LocalDate

class SampleData {
    companion object {
        val sampleBookkeepingList = listOf(
            Bookkeeping(
                "早餐",
                "便利商店咖啡與三明治",
                BookkeepingType.EXPENSE,
                85.0,
                LocalDate.now().minusDays(2)
            ),
            Bookkeeping(
                "午餐",
                "自助餐",
                BookkeepingType.EXPENSE,
                120.0,
                LocalDate.now().minusDays(2)
            ),
            Bookkeeping(
                "薪資",
                "兼職薪資收入",
                BookkeepingType.INCOME,
                2500.0,
                LocalDate.now().minusDays(1)
            ),
            Bookkeeping(
                "交通",
                "捷運車資",
                BookkeepingType.EXPENSE,
                50.0,
                LocalDate.now().minusDays(1)
            ),
            Bookkeeping("晚餐", "拉麵店", BookkeepingType.EXPENSE, 190.0, LocalDate.now()),
            Bookkeeping("紅包", "家人給的紅包", BookkeepingType.INCOME, 1000.0, LocalDate.now()),
            Bookkeeping(
                "飲料",
                "珍奶一杯",
                BookkeepingType.EXPENSE,
                60.0,
                LocalDate.now().plusDays(1)
            ),
            Bookkeeping(
                "投資回報",
                "基金配息",
                BookkeepingType.INCOME,
                320.0,
                LocalDate.now().plusDays(1)
            ),
            Bookkeeping(
                "宵夜",
                "鹽酥雞與可樂",
                BookkeepingType.EXPENSE,
                150.0,
                LocalDate.now().plusDays(2)
            ),
            Bookkeeping(
                "退稅",
                "報稅退稅金",
                BookkeepingType.INCOME,
                700.0,
                LocalDate.now().plusDays(2)
            )
        )
    }
}