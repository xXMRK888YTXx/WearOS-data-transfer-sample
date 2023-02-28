package com.xxmrk888ytxx.share

interface DI {
    val batteryInfoProvider:BatteryInfoProvider

    val batteryLevelChangedCallBack:BatteryLevelChangedCallBack

    val remoteBatteryLevelSender : BatteryLevelChangedCallBack

    val remoteBatteryLevelProvider : RemoteBatteryLevelProvider

    val messageManager : MessageManager
}