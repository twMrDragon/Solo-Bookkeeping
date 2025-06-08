
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowLeft
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.solobookkeeping.core.util.monthIntToAbbreviation
import java.time.YearMonth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YearMonthPickerDialog(
    show: Boolean,
    onDismiss: () -> Unit,
    year: Int = YearMonth.now().year,
    month: Int = YearMonth.now().monthValue,
    onConfirm: (year: Int, month: Int) -> Unit
) {
    if (show) {
        var selectedYear by remember { mutableIntStateOf(year) }
        var selectedMonth by remember { mutableIntStateOf(month) }

        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text("選擇年月") },
            text = {
                Column {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = { selectedYear-- }
                        ) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowLeft,
                                contentDescription = "減少年份"
                            )
                        }
                        OutlinedTextField(
                            value = selectedYear.toString(),
                            onValueChange = {
                                selectedYear = it.toIntOrNull() ?: 0
                            },
                            label = { Text("Year") },
                            singleLine = true,
                            modifier = Modifier
                                .padding(horizontal = 8.dp)
                                .weight(1f),
                            keyboardOptions = KeyboardOptions.Default.copy(
                                keyboardType = KeyboardType.Number
                            ),
                        )
                        IconButton(
                            onClick = { if (selectedYear < YearMonth.now().year) selectedYear++ }
                        ) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowRight,
                                contentDescription = "增加年份"
                            )
                        }
                    }

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        modifier = Modifier.padding(vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp), // 元素之間的垂直間距
                        horizontalArrangement = Arrangement.spacedBy(8.dp), // 元素之間的水平間距
                    ) {
                        items(12) { monthItem ->
                            val month = monthItem + 1
                            val monthString = monthIntToAbbreviation(month)

                            val backgroundColor = if (month == selectedMonth) MaterialTheme.colorScheme.primary else  MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
//                            val backgroundColor by animateColorAsState(
//                                if (month == selectedMonth) MaterialTheme.colorScheme.primary else  MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
//                                label = "background"
//                            )

                            val contentColor = if (month == selectedMonth) Color.White else MaterialTheme.colorScheme.onSurface
//                            val contentColor by animateColorAsState(
//                                if (month == selectedMonth) Color.White else MaterialTheme.colorScheme.onSurface,
//                                label = "textColor"
//                            )

                            Box(
                                modifier = Modifier
                                    .height(40.dp)
                                    .clip(MaterialTheme.shapes.small)
                                    .background(backgroundColor)
                                    .clickable { selectedMonth = month },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = monthString,
                                    color = contentColor,
                                    style = MaterialTheme.typography.labelLarge
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { onConfirm(selectedYear, selectedMonth) }) {
                    Text("確定")
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text("取消")
                }
            }
        )
    }
}