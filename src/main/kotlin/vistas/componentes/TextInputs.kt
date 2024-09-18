package vistas.componentes

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import vistas.colores.textColor

@Composable
fun TextInputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    isError: Boolean = false,
    errorMessage: String = ""
) {
    val focusedColor = Color(0xFF4A90E2) // A nice blue
    val unfocusedColor = Color(0xFF9B9B9B) // A subtle gray
    val errorColor = Color(0xFFE74C3C) // A soft red
    val backgroundColor = Color(0xFFF8F8F8) // A very light gray, almost white

    Column {
        Text(
            text = label,
            color = if (isError) errorColor else textColor,
            style = TextStyle(
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                letterSpacing = 0.25.sp
            ),
            modifier = Modifier.padding(bottom = 8.dp, start = 4.dp)
        )
        TextField(
            value = value,
            onValueChange = onValueChange,
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color(0xFF2C3E50), // Dark blue-gray
                unfocusedTextColor = Color(0xFF34495E), // Slightly lighter blue-gray
                focusedContainerColor = backgroundColor,
                unfocusedContainerColor = backgroundColor,
                cursorColor = focusedColor,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                errorContainerColor = backgroundColor,
                errorIndicatorColor = errorColor,
                errorCursorColor = errorColor,
                errorTextColor = errorColor
            ),
            textStyle = TextStyle(fontSize = 12.sp, letterSpacing = 0.5.sp),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = keyboardType),
            isError = isError,
            modifier = Modifier
                .fillMaxWidth()
                .height(45.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(backgroundColor)
                .border(
                    width = 1.dp,
                    color = if (isError) errorColor else Color.Black,
                    shape = RoundedCornerShape(12.dp)
                )
                .shadow(6.dp, RoundedCornerShape(12.dp))
        )
        if (isError) {
            Text(
                text = errorMessage,
                color = errorColor,
                style = TextStyle(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    letterSpacing = 0.4.sp
                ),
                modifier = Modifier.padding(top = 4.dp, start = 4.dp)
            )
        }
    }
}

@Composable
fun TextAreaField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String
) {
    val focusedColor = Color(0xFF4A90E2) // A nice blue
    val unfocusedColor = Color(0xFF9B9B9B) // A subtle gray
    val errorColor = Color(0xFFE74C3C) // A soft red
    val backgroundColor = Color(0xFFF8F8F8) // A very light gray, almost white
    Column {
        Text(
            text = label,
            color =  textColor,
            style = TextStyle(
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                letterSpacing = 0.25.sp
            ),
            modifier = Modifier.padding(bottom = 8.dp, start = 4.dp)
        )
        TextField(
            value = value,
            onValueChange = onValueChange,
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color(0xFF2C3E50), // Dark blue-gray
                unfocusedTextColor = Color(0xFF34495E), // Slightly lighter blue-gray
                focusedContainerColor = backgroundColor,
                unfocusedContainerColor = backgroundColor,
                cursorColor = focusedColor,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                errorContainerColor = backgroundColor,
                errorIndicatorColor = errorColor,
                errorCursorColor = errorColor,
                errorTextColor = errorColor
            ),
            textStyle = TextStyle(fontSize = 12.sp, letterSpacing = 0.5.sp),
            modifier = Modifier
                .fillMaxWidth()
                .height(45.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(backgroundColor)
                .border(
                    width = 1.dp,
                    color =  Color.Black,
                    shape = RoundedCornerShape(12.dp)
                )
                .shadow(6.dp, RoundedCornerShape(12.dp)),
            maxLines = 5

        )
    }
}