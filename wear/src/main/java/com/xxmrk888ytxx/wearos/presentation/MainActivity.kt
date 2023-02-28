/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter and
 * https://github.com/android/wear-os-samples/tree/main/ComposeAdvanced to find the most up to date
 * changes to the libraries and their usages.
 */

package com.xxmrk888ytxx.wearos.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.Text

class MainActivity : ComponentActivity() {

    private val wearViewModel by viewModels<WearViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val batteryLevel = wearViewModel.myBatteryLevel.collectAsState(initial = null)
            val remoteBatteryLevel = wearViewModel.removeBatteryLevel.collectAsState(initial = null)
            Column(
                Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "My battery level:${if(batteryLevel.value == null) "no data"
                else batteryLevel.value.toString()}")

                Text(text = "Mobile battery level:${if(remoteBatteryLevel.value == null) "no data"
                else remoteBatteryLevel.value.toString()}")

                Button(onClick = { wearViewModel.updateRemoteBatteryLevel() }) {
                    Text(text = "Update")
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        wearViewModel.onStart()
    }

    override fun onStop() {
        super.onStop()
        wearViewModel.onStop()
    }
}
