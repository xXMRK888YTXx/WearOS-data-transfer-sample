package com.xxmrk888ytxx.share

import android.os.Bundle

interface DataPathObserver {
    val pathName:String

    fun onPathDataUpdated(bundle:Bundle)
}