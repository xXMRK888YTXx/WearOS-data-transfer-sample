package com.xxmrk888ytxx.wearos

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import androidx.core.content.getSystemService
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xxmrk888ytxx.share.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.nio.charset.Charset


class MobileViewModel(
    private val context: Context = FakeDI.context!!,
    private val batteryInfoProvider: BatteryInfoProvider = FakeDI.batteryInfoProvider,
    private val batteryLevelChangedCallBack: BatteryLevelChangedCallBack = FakeDI.batteryLevelChangedCallBack,
    private val remoteBatteryLevelChangedCallBack: BatteryLevelChangedCallBack = FakeDI.remoteBatteryLevelSender,
    private val remoteBatteryLevelProvider: RemoteBatteryLevelProvider = FakeDI.remoteBatteryLevelProvider,
    private val messageManager: MessageManager = FakeDI.messageManager
) : ViewModel() {
    val localBatteryLevel = batteryInfoProvider.batteryLevel

    val remoteBatteryLevel = remoteBatteryLevelProvider.batteryLevel

    private fun updateBatteryLevel() {
        val batteryManager = context.getSystemService<BatteryManager>()!!
        val batteryLevel = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
        batteryLevelChangedCallBack.onChange(batteryLevel)
        remoteBatteryLevelChangedCallBack.onChange(batteryLevel)
    }

    private val reviver by lazy {
        object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent?) {
                updateBatteryLevel()
            }

        }
    }

    private val messageObserver by lazy {
        object : MessageObserver {
            override fun onMessageReceive(path: String, data: ByteArray) {
                when(path) {
                    Const.SEND_NEW_BATTERY_LEVEL_COMMAND -> {
                        val batteryManager = context.getSystemService<BatteryManager>()!!
                        val batteryLevel = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
                        viewModelScope.launch(Dispatchers.Default) {
                            messageManager.sendMessage(Const.UPDATE_BATTERY_LEVEL,batteryLevel.toString().toByteArray())
                        }
                    }

                    Const.UPDATE_BATTERY_LEVEL -> {
                        val batteryLevel = data.toString(Charset.defaultCharset()).toInt()
                        remoteBatteryLevelProvider.onChange(batteryLevel)
                    }
                }
            }

        }
    }



    fun onStart() {
        context.registerReceiver(reviver, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        remoteBatteryLevelProvider.connect()
        messageManager.addMessageObserver(messageObserver)
    }

    fun onStop() {
        remoteBatteryLevelProvider.disconnect()
        messageManager.removeMessageObserver()
        context.unregisterReceiver(reviver)
    }

    override fun onCleared() {
        onStop()
        super.onCleared()
    }

    fun updateRemoteBatteryLevel() {
        viewModelScope.launch(Dispatchers.IO) {
            messageManager.sendMessage(Const.SEND_NEW_BATTERY_LEVEL_COMMAND, "t".toByteArray())
        }
    }
}