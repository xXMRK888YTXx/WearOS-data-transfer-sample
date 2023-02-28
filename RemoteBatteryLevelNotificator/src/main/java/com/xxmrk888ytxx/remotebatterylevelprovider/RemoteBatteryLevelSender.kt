package com.xxmrk888ytxx.remotebatterylevelprovider

import android.annotation.SuppressLint
import android.content.Context
import com.google.android.gms.wearable.PutDataMapRequest
import com.google.android.gms.wearable.Wearable
import com.xxmrk888ytxx.share.BatteryLevelChangedCallBack
import com.xxmrk888ytxx.share.Const

class RemoteBatteryLevelSender(
    private val path:String,
    private val context: Context
) : BatteryLevelChangedCallBack {

    @SuppressLint("VisibleForTests")
    override fun onChange(newBatteryLevel: Int) {
        val dataMapRequest = PutDataMapRequest.create(path)
        dataMapRequest.dataMap.putInt(Const.BATTERY_LEVEL_KEY,newBatteryLevel)
        val dataRequest = dataMapRequest.asPutDataRequest()
        Wearable.getDataClient(context).putDataItem(dataRequest)
    }


}