package com.xxmrk888ytxx.share

import android.os.Bundle

interface DataClientManager {
    fun addObserver(dataPathObserver: DataPathObserver)

    fun removeObserver(dataPathObserver: DataPathObserver)

    fun updateDataPath(path:String,data:Bundle)
}