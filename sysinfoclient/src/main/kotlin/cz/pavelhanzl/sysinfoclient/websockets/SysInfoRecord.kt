package cz.pavelhanzl.sysinfoclient.websockets

data class SysInfoRecord(
    val clientName: String,
    val usedMemory: Long,
    val freeMemory: Long,
    val totalMemory: Long,
)