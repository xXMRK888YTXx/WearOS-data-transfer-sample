package com.xxmrk888ytxx.wearos

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier


class MainActivity : ComponentActivity() {

    private val mobileViewModel by viewModels<MobileViewModel>()

    @SuppressLint("VisibleForTests")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val batteryLevel = mobileViewModel.localBatteryLevel.collectAsState(initial = null)
            val remoteBatteryLevel = mobileViewModel.remoteBatteryLevel.collectAsState(initial = null)
            Column(
                Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "My battery level:${if(batteryLevel.value == null) "no data"
                else batteryLevel.value.toString()}")

                Text(text = "Wear battery level:${if(remoteBatteryLevel.value == null) "no data"
                else remoteBatteryLevel.value.toString()}")

                Button(onClick = { mobileViewModel.updateRemoteBatteryLevel() }) {
                    Text(text = "Update")
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        mobileViewModel.onStart()
    }

    override fun onStop() {
        super.onStop()
        mobileViewModel.onStop()
    }
}