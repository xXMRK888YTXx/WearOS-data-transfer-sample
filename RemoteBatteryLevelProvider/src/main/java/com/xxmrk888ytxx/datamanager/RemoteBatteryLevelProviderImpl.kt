package com.xxmrk888ytxx.datamanager

import android.annotation.SuppressLint
import android.content.Context
import com.google.android.gms.wearable.DataClient
import com.google.android.gms.wearable.DataMapItem
import com.google.android.gms.wearable.Wearable
import com.xxmrk888ytxx.share.Const
import com.xxmrk888ytxx.share.RemoteBatteryLevelProvider
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

@SuppressLint("VisibleForTests")
class RemoteBatteryLevelProviderImpl(
    private val path:String,
    private val context: Context
) : RemoteBatteryLevelProvider {

    private val _removeBatteryState = MutableStateFlow<Int?>(null)

    override fun connect() {
        Wearable.getDataClient(context).addListener(observer)
    }

    override fun disconnect() {
        Wearable.getDataClient(context).removeListener(observer)
    }

    override val batteryLevel: Flow<Int?> = _removeBatteryState

    override fun onChange(newBatteryLevel: Int) {
        scope.launch { _removeBatteryState.emit(newBatteryLevel) }
    }

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private val observer:DataClient.OnDataChangedListener by lazy {
        DataClient.OnDataChangedListener {
            it.forEach { dataEvent ->
                if(dataEvent.dataItem.uri.path == path) {
                    val dataMap = DataMapItem.fromDataItem(dataEvent.dataItem)
                    val batteryLevel = dataMap.dataMap.getInt(Const.BATTERY_LEVEL_KEY,-1)
                    if(batteryLevel != -1) {
                        scope.launch {
                            _removeBatteryState.emit(batteryLevel)
                        }
                    }
                }
            }
        }
    }

}