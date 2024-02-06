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
import java.net.InetAddress

@Component
class SendClientsSysinfoToServer(private val clientWebSocketHandler: ClientWebSocketHandler) {

    val runtime: Runtime = Runtime.getRuntime()

    // Checking whether an application is running in Docker
    val isDockerEnv = System.getenv("DOCKER_ENV")?.toBoolean() ?: false

    init {
        simulateMemoryUsage()
    }

    //sends sysinfo to server every 3 seconds
    @Scheduled(fixedRate = 3000)
    fun sendSysInfo() {

        //makes sysinfoRecord object
        val sysInfoRecord = SysInfoRecord(
            clientName = getClientsName(),
            usedMemory = (runtime.totalMemory() - runtime.freeMemory()) / 1024 / 1024,
            freeMemory = runtime.freeMemory() / 1024 / 1024,
            totalMemory = runtime.totalMemory() / 1024 / 1024
        )

        //converts it to json
        val jsonMessage = Gson().toJson(sysInfoRecord)

        //send json to server using websockets
        clientWebSocketHandler.sendMessage(jsonMessage)
    }


    fun getClientsName(): String {
        return if (isDockerEnv) { // Aplikace běží v Dockeru
            InetAddress.getLocalHost().hostName
        } else {
            "Local client"
        }
    }


    private final fun simulateMemoryUsage() {

        // asynchronously in another thread triggers an artificial memory fill
        // for simulation purposes in the frontend react application
        GlobalScope.launch {
            var counter = 0
            var largeArray: Array<IntArray>?
            while (isActive) {
                counter++
                delay(2000) // Waits 2s

                //artificially fills the memory
                if (counter == 7) {
                    largeArray = Array(1_000_000) { IntArray(Random.nextInt(10, 30)) }
                }

                //artificially free memory by explicitly starting the garbage collector
                if (counter == 10) {
                    largeArray = null
                    System.gc()
                }

            }
        }

    }


}