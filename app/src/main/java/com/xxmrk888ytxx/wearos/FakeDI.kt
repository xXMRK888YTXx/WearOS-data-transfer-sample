package com.xxmrk888ytxx.wearos

import android.annotation.SuppressLint
import android.content.Context
import com.xxmrk888ytxx.batterylevelprovider.LocalBatteryLevelProvider
import com.xxmrk888ytxx.datamanager.RemoteBatteryLevelProviderImpl
import com.xxmrk888ytxx.messagemanager.MessageManagerImpl
import com.xxmrk888ytxx.remotebatterylevelprovider.RemoteBatteryLevelSender
import com.xxmrk888ytxx.share.*

@SuppressLint("StaticFieldLeak")
internal object FakeDI : DI {

    var context:Context? = null
    private set

    fun provideContext(context: Context) {
        this.context = context
    }

    private val localBatteryLevelProvider:LocalBatteryLevelProvider by lazy {
        LocalBatteryLevelProvider()
    }

    private val RemoteBatteryLevelSenderImpl by lazy {
        RemoteBatteryLevelSender(Const.BATTERY_PATH_MOBILE, context!!)
    }

    private val remoteBatteryLevelProviderImpl by lazy {
        RemoteBatteryLevelProviderImpl(Const.BATTERY_PATH_WEAR, context!!)
    }

    private val messageManagerImpl by lazy {
        MessageManagerImpl(context!!)
    }


    override val batteryInfoProvider: BatteryInfoProvider
        get() = localBatteryLevelProvider
    override val batteryLevelChangedCallBack: BatteryLevelChangedCallBack
        get() = localBatteryLevelProvider
    override val remoteBatteryLevelSender: BatteryLevelChangedCallBack
        get() = RemoteBatteryLevelSenderImpl
    override val remoteBatteryLevelProvider: RemoteBatteryLevelProvider
        get() = remoteBatteryLevelProviderImpl
    override val messageManager: MessageManager
        get() = messageManagerImpl

}