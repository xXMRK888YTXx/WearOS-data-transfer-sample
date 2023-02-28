package com.xxmrk888ytxx.share

import kotlinx.coroutines.flow.Flow

interface BatteryInfoProvider {
    val batteryLevel: Flow<Int?>
}