package com.xxmrk888ytxx.share

interface MessageObserver {
    fun onMessageReceive(path:String,data:ByteArray)
}