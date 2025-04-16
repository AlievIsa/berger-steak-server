package com.alievisa.service

import com.alievisa.utils.CustomLogger
import java.util.concurrent.ConcurrentHashMap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class DeviceService(
    private val accessTokenMinutesToLive: Long = 15,
) {

    private val deviceIdSet = ConcurrentHashMap.newKeySet<String>()
    private val scope = CoroutineScope(Dispatchers.Default)

    fun isDeviceIdSuspiciouslyUsed(deviceId: String): Boolean {
        val wasAlreadyThere = !deviceIdSet.add(deviceId)
        if (wasAlreadyThere) {
            CustomLogger.log("DeviceService: deviceId $deviceId was suspiciously used within $accessTokenMinutesToLive minutes")
            return true
        }

        CustomLogger.log("DeviceService: deviceId $deviceId added to set")

        scope.launch {
            delay(accessTokenMinutesToLive* 60 * 1000)
            deviceIdSet.remove(deviceId)
            CustomLogger.log("DeviceService: deviceId $deviceId was removed from set")
        }

        return false
    }

    fun releaseDeviceId(deviceId: String) {
        deviceIdSet.remove(deviceId)
    }
}