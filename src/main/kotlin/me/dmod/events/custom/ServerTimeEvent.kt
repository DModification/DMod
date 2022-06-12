package me.dmod.events.custom

import me.dmod.utils.Variables
import java.time.Instant

object ServerTimeEvent {
    private var lastUpdate: Instant? = null
    private val offsetTicks: MutableList<Long> = ArrayList()

    @JvmStatic
    fun timeUpdate() {
        Variables.lastTimeEvent = Instant.now()

        if (lastUpdate != null) {
            val offset = if (Instant.now().toEpochMilli() - lastUpdate!!.toEpochMilli() < 1000) 1000 - (Instant.now().toEpochMilli() - lastUpdate!!.toEpochMilli()) else Instant.now().toEpochMilli() - lastUpdate!!.toEpochMilli() - 1000
            if (offsetTicks.size > 60) {
                offsetTicks.removeAt(0)
            }
            offsetTicks.add(offset / 20)
        }
        var sum: Long = 0
        for (it in offsetTicks) {
            sum += it
        }
        Variables.tps = 20 - sum.toFloat() / offsetTicks.size.toFloat()
        lastUpdate = Instant.now()
    }

    @JvmStatic
    fun resetArray() {
        offsetTicks.clear()
    }
}