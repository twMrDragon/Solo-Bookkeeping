package com.example.solobookkeeping.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Checkroom
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.House
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "bookkeeping")
data class Bookkeeping(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val category: Category,
    val title: String,
    val depiction: String,
    val amount: Double,
    val date: LocalDate
)

enum class Category(
    val icon: ImageVector,
    val title: String
) {
    FOOD(
        icon = Icons.Default.Restaurant,
        title = "飲食"
    ),
    CLOTHING(icon = Icons.Default.Checkroom,
        title = "衣物"
    ),
    TRANSPORTATION(icon = Icons.Default.DirectionsCar,
        title = "交通"
    ),
    ENTERTAINMENT(icon = Icons.Default.SportsEsports,
        title = "娛樂"
    ),
    MEDICAL(icon = Icons.Default.MedicalServices,
        title = "醫療"
    ),
    HOUSING(icon = Icons.Default.House,
        title = "居家"
    ),
    EDUCATION(icon = Icons.Default.School,
        title = "教育"
    ),
    MISCELLANEOUS(icon = Icons.Default.Inventory,
        title = "雜項"
    );
}