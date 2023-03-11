package com.xxmrk888ytxx.remotebatterylevelprovider

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import com.google.android.gms.wearable.PutDataMapRequest
import com.google.android.gms.wearable.Wearable
import com.xxmrk888ytxx.share.BatteryLevelChangedCallBack
import com.xxmrk888ytxx.share.Const
import com.xxmrk888ytxx.share.DataClientManager

class RemoteBatteryLevelSender(
    private val path:String,
    private val dataClientManager: DataClientManager
) : BatteryLevelChangedCallBack {

    @SuppressLint("VisibleForTests")
    override fun onChange(newBatteryLevel: Int) {
        val bundle = Bundle()

        bundle.putInt(Const.BATTERY_LEVEL_KEY,newBatteryLevel)

        dataClientManager.updateDataPath(path,bundle)
    }


}