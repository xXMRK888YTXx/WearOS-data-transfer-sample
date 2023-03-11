package com.xxmrk888ytxx.dataclientmanager

import android.content.Context
import android.os.Bundle
import com.google.android.gms.wearable.*
import com.xxmrk888ytxx.share.Const
import com.xxmrk888ytxx.share.DataClientManager
import com.xxmrk888ytxx.share.DataPathObserver
import kotlinx.coroutines.launch

class DataClientManagerImpl(private val context: Context) : DataClientManager {

    private val observers = mutableSetOf<DataPathObserver>()

    private val dataClientObserver by lazy {
        DataClient.OnDataChangedListener {
            it.forEach { dataEvent ->
                val path = dataEvent.dataItem.uri.path
                val dataMap = DataMapItem.fromDataItem(dataEvent.dataItem).dataMap
                observers.filter { pathObserver ->  pathObserver.pathName == path }.forEach {
                    it.onPathDataUpdated(dataMap.toBundle())
                }
            }
        }
    }

    override fun addObserver(dataPathObserver: DataPathObserver) {
        observers.add(dataPathObserver)

        Wearable.getDataClient(context).addListener(dataClientObserver)
    }

    override fun removeObserver(dataPathObserver: DataPathObserver) {
        observers.remove(dataPathObserver)

        if(observers.isEmpty()) {
            Wearable.getDataClient(context).removeListener(dataClientObserver)
        }
    }

    override fun updateDataPath(path: String, data:Bundle) {
        val dataMapRequest = PutDataMapRequest.create(path)
        dataMapRequest.dataMap.putAll(DataMap.fromBundle(data))
        val dataRequest = dataMapRequest.asPutDataRequest()
        Wearable.getDataClient(context).putDataItem(dataRequest)
    }


}