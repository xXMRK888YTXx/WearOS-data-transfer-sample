package com.xxmrk888ytxx.datamanager

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import com.google.android.gms.wearable.DataClient
import com.google.android.gms.wearable.DataMapItem
import com.google.android.gms.wearable.Wearable
import com.xxmrk888ytxx.share.Const
import com.xxmrk888ytxx.share.DataClientManager
import com.xxmrk888ytxx.share.DataPathObserver
import com.xxmrk888ytxx.share.RemoteBatteryLevelProvider
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

@SuppressLint("VisibleForTests")
class RemoteBatteryLevelProviderImpl(
    private val path:String,
    private val dataClientManager:DataClientManager
) : RemoteBatteryLevelProvider {

    private val _removeBatteryState = MutableStateFlow<Int?>(null)

    override fun connect() {
        dataClientManager.addObserver(observer)
    }

    override fun disconnect() {
        dataClientManager.removeObserver(observer)
    }

    override val batteryLevel: Flow<Int?> = _removeBatteryState

    override fun onChange(newBatteryLevel: Int) {
        scope.launch { _removeBatteryState.emit(newBatteryLevel) }
    }

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private val observer by lazy {
        object : DataPathObserver {
            override val pathName: String = path

            override fun onPathDataUpdated(bundle: Bundle) {
                val batteryLevel = bundle.getInt(Const.BATTERY_LEVEL_KEY,-1)

                if(batteryLevel != -1) {
                    onChange(batteryLevel)
                }
            }

        }
    }

}