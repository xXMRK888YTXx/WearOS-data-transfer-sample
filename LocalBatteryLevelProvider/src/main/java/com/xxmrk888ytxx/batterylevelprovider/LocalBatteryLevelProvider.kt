package com.xxmrk888ytxx.batterylevelprovider

import android.content.Context
import android.os.BatteryManager
import androidx.core.content.getSystemService
import com.xxmrk888ytxx.share.BatteryInfoProvider
import com.xxmrk888ytxx.share.BatteryLevelChangedCallBack
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class LocalBatteryLevelProvider : BatteryInfoProvider,BatteryLevelChangedCallBack {
    private val _batteryLevel = MutableStateFlow<Int?>(null)

    override val batteryLevel: Flow<Int?> = _batteryLevel.asStateFlow()

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    override fun onChange(newBatteryLevel: Int) {
        scope.launch { _batteryLevel.emit(newBatteryLevel) }
    }
}