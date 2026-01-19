package com.example.shopdemo

import android.app.Application
import io.variant.android.core.Variant

class ShopApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // 1. Initialize SDK with the new 'appId' parameter for Multi-tenancy
        Variant.init(
            context = this,
            apiKey = "demo-key",
            appId = "shop-demo-app-id", // ✅ NEW: Unique Application ID
            defaults = mapOf(
                "btn_color" to "#0000FF", // Blue
                "cta_text" to "Buy Now",
                "font_size" to "24"
            )
        )
    }
}