package me.dmod.utils

abstract class DModRunnable {

    abstract fun run()

    private var cancelled = false

    fun scheduleTask(delay: Long) {
        Thread{
            Thread.sleep(delay)
            if(cancelled) return@Thread
            run()
        }.start()
    }

    fun scheduleRepeatingTask(delay: Long = 0, interval: Long, iterations: Long = -1) {
        var i: Long = 0
        Thread{
            Thread.sleep(delay)
            while(!cancelled) {
                if(iterations > -1 && i >= iterations) break
                run()
                Thread.sleep(interval)
                i++
            }
        }.start()
    }

    fun cancel() {
        cancelled = true
    }
}