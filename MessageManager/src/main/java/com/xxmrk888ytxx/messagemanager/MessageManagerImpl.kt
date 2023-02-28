package com.xxmrk888ytxx.messagemanager

import android.content.Context
import com.google.android.gms.tasks.Tasks
import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.Wearable
import com.xxmrk888ytxx.share.MessageManager
import com.xxmrk888ytxx.share.MessageObserver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class MessageManagerImpl(private val context: Context) : MessageManager {

    private val listener by lazy {
        MessageClient.OnMessageReceivedListener { event ->
            observers.forEach { it.onMessageReceive(event.path,event.data) }
        }
    }

    private val observers = mutableListOf<MessageObserver>()

    override fun addMessageObserver(messageObserver: MessageObserver) {
       observers.add(messageObserver)
       Wearable.getMessageClient(context).addListener(listener)
    }

    override fun removeMessageObserver() {
        Wearable.getMessageClient(context).removeListener(listener)
        observers.clear()
    }

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    override fun sendMessage(path: String, message: ByteArray) {
        scope.launch {
            val client = Wearable.getMessageClient(context)
            val nodesClient = Wearable.getNodeClient(context)
            val nodeList = Tasks.await(nodesClient.connectedNodes)
            val node = nodeList.first { it.isNearby }.id

            client.sendMessage(node,path,message)
        }
    }
}