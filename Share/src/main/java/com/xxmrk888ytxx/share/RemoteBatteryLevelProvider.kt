package com.xxmrk888ytxx.share

interface RemoteBatteryLevelProvider : BatteryInfoProvider,BatteryLevelChangedCallBack {
    fun connect()

    fun disconnect()
}