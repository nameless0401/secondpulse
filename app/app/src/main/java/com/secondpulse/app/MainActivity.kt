package com.secondpulse.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.secondpulse.app.ui.SecondPulseRoot
import com.secondpulse.app.ui.theme.SecondPulseTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { SecondPulseTheme { SecondPulseRoot() } }
    }
}
