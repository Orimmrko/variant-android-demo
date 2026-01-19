package com.example.shopdemo

import android.app.Application
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import androidx.lifecycle.AndroidViewModel
import io.variant.android.core.Variant
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

// UI State
data class ShopUiState(
    val buttonColor: Color = Color.Blue,
    val buttonText: String = "Buy Now",
    val titleFontSize: TextUnit = 24.sp
)

class ShopViewModel(application: Application) : AndroidViewModel(application), Variant.VariantListener {

    private val _uiState = MutableStateFlow(ShopUiState())
    val uiState: StateFlow<ShopUiState> = _uiState.asStateFlow()

    init {
        Variant.setListener(this)
        updateUiFromSdk()
    }

    override fun onConfigUpdated() {
        updateUiFromSdk()
    }

    private fun updateUiFromSdk() {
        val colorHex = Variant.getString("btn_color", "#0000FF")
        val text = Variant.getString("cta_text", "Buy Now")
        val sizeStr = Variant.getString("font_size", "24")

        _uiState.update {
            it.copy(
                buttonColor = parseColor(colorHex),
                buttonText = text,
                titleFontSize = (sizeStr.toFloatOrNull() ?: 24f).sp
            )
        }
    }

    fun onBuyClicked() {
        Variant.track("btn_color", "buy_click")
    }

    fun onResetUserClicked() {
        // ✅ תיקון: הסרנו את ה-ID. ה-SDK יבצע ריסט רק עם הקונטקסט.
        Variant.resetUser(getApplication())

        Variant.setListener(this)
    }

    private fun parseColor(input: String): Color {
        // 1. נרמול הקלט: מחיקת רווחים והמרה לאותיות קטנות (כדי ש-Red ו-red יעבדו)
        val cleanInput = input.trim().lowercase()

        // 2. בדיקה מול רשימת צבעים מוכרים
        return when (cleanInput) {
            "red" -> Color.Red
            "blue" -> Color.Blue
            "green" -> Color.Green
            "black" -> Color.Black
            "white" -> Color.White
            "yellow" -> Color.Yellow
            "cyan" -> Color.Cyan
            "magenta" -> Color.Magenta
            "gray", "grey" -> Color.Gray
            "purple" -> Color(0xFF800080) // צבעים שאין ב-Color המובנה אפשר להוסיף ידנית
            "orange" -> Color(0xFFFFA500)

            // 3. אם לא מצאנו שם, ננסה לפענח כ-HEX (כדי לשמור על תמיכה לאחור!)
            else -> try {
                Color(android.graphics.Color.parseColor(input))
            } catch (e: Exception) {
                // 4. ברירת מחדל אם הקלט לא תקין
                Color.Blue
            }
        }
    }
}