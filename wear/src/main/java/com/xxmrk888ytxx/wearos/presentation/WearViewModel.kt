package com.xxmrk888ytxx.wearos.presentation

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import androidx.core.content.getSystemService
import androidx.lifecycle.*
import com.xxmrk888ytxx.batterylevelprovider.LocalBatteryLevelProvider
import com.xxmrk888ytxx.share.*
import com.xxmrk888ytxx.share.Const.SEND_NEW_BATTERY_LEVEL_COMMAND
import com.xxmrk888ytxx.share.Const.UPDATE_BATTERY_LEVEL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.nio.charset.Charset

class WearViewModel(
    private val context: Context = FakeDI.context!!,
    private val batteryInfoProvider: BatteryInfoProvider = FakeDI.batteryInfoProvider,
    private val batteryLevelChangedCallBack: BatteryLevelChangedCallBack = FakeDI.batteryLevelChangedCallBack,
    private val remoteBatteryLevelChangedCallBack: BatteryLevelChangedCallBack = FakeDI.remoteBatteryLevelSender,
    private val remoteBatteryLevelProvider: RemoteBatteryLevelProvider = FakeDI.remoteBatteryLevelProvider,
    private val messageManager: MessageManager = FakeDI.messageManager
) : ViewModel() {

    val myBatteryLevel = batteryInfoProvider.batteryLevel

    val removeBatteryLevel = remoteBatteryLevelProvider.batteryLevel


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
                    SEND_NEW_BATTERY_LEVEL_COMMAND -> {
                        val batteryManager = context.getSystemService<BatteryManager>()!!
                        val batteryLevel = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
                        viewModelScope.launch(Dispatchers.Default) {
                            messageManager.sendMessage(UPDATE_BATTERY_LEVEL,batteryLevel.toString().toByteArray())
                        }
                    }

                    UPDATE_BATTERY_LEVEL -> {
                        val batteryLevel = data.toString(Charset.defaultCharset()).toInt()
                        remoteBatteryLevelProvider.onChange(batteryLevel)
                    }
                }
            }

        }
    }

    private fun updateBatteryLevel() {
        val batteryManager = context.getSystemService<BatteryManager>()!!
        val batteryLevel = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
        batteryLevelChangedCallBack.onChange(batteryLevel)
        remoteBatteryLevelChangedCallBack.onChange(batteryLevel)
    }

    fun onStart() {
        context.registerReceiver(reviver, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        remoteBatteryLevelProvider.connect()
        messageManager.addMessageObserver(messageObserver)
    }

    fun onStop() {
        context.unregisterReceiver(reviver)
        remoteBatteryLevelProvider.disconnect()
        messageManager.removeMessageObserver()
    }


    override fun onCleared() {
        onStop()
        super.onCleared()
    }

    fun updateRemoteBatteryLevel() {
        viewModelScope.launch(Dispatchers.IO) {
            messageManager.sendMessage(SEND_NEW_BATTERY_LEVEL_COMMAND, "t".toByteArray())
        }
    }
}