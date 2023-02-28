package com.xxmrk888ytxx.share

interface MessageManager {

    fun addMessageObserver(messageObserver:MessageObserver)

    fun removeMessageObserver()

    fun sendMessage(path:String,message:ByteArray)
}