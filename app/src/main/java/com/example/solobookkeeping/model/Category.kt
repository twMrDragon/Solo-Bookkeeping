package com.example.solobookkeeping.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Checkroom
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.House
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector


interface Category {
    val title: String
    val icon: ImageVector
    val color: Color
}

enum class ExpenseCategory(
    override val title: String,
    override val icon: ImageVector,
    override val color: Color
) : Category {
    FOOD(
        icon = Icons.Default.Restaurant,
        title = "飲食",
        color = com.example.solobookkeeping.ui.theme.FOOD
    ),
    CLOTHING(
        icon = Icons.Default.Checkroom,
        title = "衣物",
        color = com.example.solobookkeeping.ui.theme.CLOTHING
    ),
    TRANSPORTATION(
        icon = Icons.Default.DirectionsCar,
        title = "交通",
        color = com.example.solobookkeeping.ui.theme.TRANSPORTATION
    ),
    ENTERTAINMENT(
        icon = Icons.Default.SportsEsports,
        title = "娛樂",
        color = com.example.solobookkeeping.ui.theme.ENTERTAINMENT
    ),
    MEDICAL(
        icon = Icons.Default.MedicalServices,
        title = "醫療",
        color = com.example.solobookkeeping.ui.theme.MEDICAL
    ),
    HOUSING(
        icon = Icons.Default.House,
        title = "居家",
        color = com.example.solobookkeeping.ui.theme.HOUSING
    ),
    EDUCATION(
        icon = Icons.Default.School,
        title = "教育",
        color = com.example.solobookkeeping.ui.theme.EDUCATION
    ),
    MISCELLANEOUS(
        icon = Icons.Default.Inventory,
        title = "雜項",
        color = com.example.solobookkeeping.ui.theme.MISCELLANEOUS
    );
}

enum class IncomeCategory(
    override val title: String,
    override val icon: ImageVector,
    override val color: Color
) : Category {
    Salary(
        title = "薪水",
        icon = Icons.Default.AttachMoney,
        color = com.example.solobookkeeping.ui.theme.Salary
    ),
    Investment(
        title = "投資",
        icon = Icons.Default.TrendingUp,
        color = com.example.solobookkeeping.ui.theme.Investment
    ),
    Bonus(
        title = "獎金",
        icon = Icons.Default.EmojiEvents,
        color = com.example.solobookkeeping.ui.theme.Bonus
    ),
    Other(
        title = "其他",
        icon = Icons.Default.MoreHoriz,
        color = com.example.solobookkeeping.ui.theme.Other
    );
}


//enum class Category(
//    val icon: ImageVector, val title: String, val color: Color
//) {
//    FOOD(
//        icon = Icons.Default.Restaurant,
//        title = "飲食",
//        color = com.example.solobookkeeping.ui.theme.FOOD
//    ),
//    CLOTHING(
//        icon = Icons.Default.Checkroom,
//        title = "衣物",
//        color = com.example.solobookkeeping.ui.theme.CLOTHING
//    ),
//    TRANSPORTATION(
//        icon = Icons.Default.DirectionsCar,
//        title = "交通",
//        color = com.example.solobookkeeping.ui.theme.TRANSPORTATION
//    ),
//    ENTERTAINMENT(
//        icon = Icons.Default.SportsEsports,
//        title = "娛樂",
//        color = com.example.solobookkeeping.ui.theme.ENTERTAINMENT
//    ),
//    MEDICAL(
//        icon = Icons.Default.MedicalServices,
//        title = "醫療",
//        color = com.example.solobookkeeping.ui.theme.MEDICAL
//    ),
//    HOUSING(
//        icon = Icons.Default.House,
//        title = "居家",
//        color = com.example.solobookkeeping.ui.theme.HOUSING
//    ),
//    EDUCATION(
//        icon = Icons.Default.School,
//        title = "教育",
//        color = com.example.solobookkeeping.ui.theme.EDUCATION
//    ),
//    MISCELLANEOUS(
//        icon = Icons.Default.Inventory,
//        title = "雜項",
//        color = com.example.solobookkeeping.ui.theme.MISCELLANEOUS
//    );
//}