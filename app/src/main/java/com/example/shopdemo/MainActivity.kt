package com.example.shopdemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.variant.android.core.Variant

class MainActivity : ComponentActivity() {
    private val viewModel: ShopViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShopScreen(viewModel)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShopScreen(viewModel: ShopViewModel) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Variant Shop Demo") },
                actions = {
                    // כפתור איפוס כדי לבדוק החלפת משתמשים
                    IconButton(onClick = { viewModel.onResetUserClicked() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Reset User")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // כרטיס תמונה
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize().background(Color.LightGray),
                    contentAlignment = Alignment.Center
                ) {
                    // אייקון זמני במקום תמונה
                    Image(
                        painter = painterResource(android.R.drawable.ic_menu_camera),
                        contentDescription = "Product",
                        modifier = Modifier.size(80.dp),
                        contentScale = ContentScale.Fit
                    )
                }
            }

            // כותרת דינמית (נשלטת ע"י font_size)
            Text(
                text = "Premium Variant Camera",
                fontSize = state.titleFontSize,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Text(
                text = "The best camera for A/B testing your photography skills.",
                fontSize = 16.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.weight(1f))

            // כפתור דינמי (נשלט ע"י btn_color ו-cta_text)
            Button(
                onClick = {
                    Variant.track("btn_color", "conversion")
                    Variant.track("font_size", "conversion")
                    Variant.track("cta_text", "conversion")
                    viewModel.onBuyClicked() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = state.buttonColor),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = state.buttonText,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}