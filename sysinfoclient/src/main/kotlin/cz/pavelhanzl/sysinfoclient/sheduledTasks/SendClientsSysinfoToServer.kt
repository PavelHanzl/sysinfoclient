package cz.pavelhanzl.sysinfoclient.sheduledTasks

import cz.pavelhanzl.sysinfoclient.websockets.ClientWebSocketHandler
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import kotlin.random.Random
import com.google.gson.Gson
import cz.pavelhanzl.sysinfoclient.websockets.SysInfoRecord

@Component
class SendClientsSysinfoToServer(private val clientWebSocketHandler: ClientWebSocketHandler) {
    val runtime = Runtime.getRuntime()
    fun getClientsName(): String {
        return System.getenv("SRV_PORT") ?: "Local client"
    }
    init {
        simulateMemoryUsage()
    }
    @Scheduled(fixedRate = 5000)
    fun sendSysInfo() {

        val sysInfoRecord = SysInfoRecord(
            clientName = getClientsName(),
            usedMemory = (runtime.totalMemory()-runtime.freeMemory())/ 1024 / 1024,
            freeMemory = runtime.freeMemory()/ 1024 / 1024,
            totalMemory = runtime.totalMemory()/ 1024 / 1024
        )

        val jsonMessage = Gson().toJson(sysInfoRecord)

        clientWebSocketHandler.sendMessage(jsonMessage)
        println(jsonMessage)
    }

    private final fun simulateMemoryUsage(){
        GlobalScope.launch {
            var counter = 0
            var largeArray:Array<IntArray>?
            while (isActive) {
                counter++
                delay(5000) // Čekání 5s

                //uměle zaplní paměť
                if (counter==3){
                    largeArray = Array(1_000_000) { IntArray(Random.nextInt(10, 20)) }
                }

                //uměle uvolní paměť explicitním spuštěním garbage collectoru
                if (counter==7){
                    largeArray=null
                    System.gc()
                }

            }}

    }
}