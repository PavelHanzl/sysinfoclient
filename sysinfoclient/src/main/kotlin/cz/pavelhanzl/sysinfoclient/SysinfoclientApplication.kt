package cz.pavelhanzl.sysinfoclient

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.io.File
import java.lang.management.ManagementFactory
import java.lang.management.ThreadInfo
import java.lang.management.ThreadMXBean
import org.springframework.scheduling.annotation.EnableScheduling


@SpringBootApplication
@EnableScheduling
class SysinfoclientApplication

fun main(args: Array<String>) {
	runApplication<SysinfoclientApplication>(*args)

	val memoryMXBean = ManagementFactory.getMemoryMXBean()
	println(
		String.format(
			"Initial memory: %.2f GB",
			memoryMXBean.heapMemoryUsage.init.toDouble() / 1073741824
		)
	)
	println(
		String.format(
			"Used heap memory: %.2f GB",
			memoryMXBean.heapMemoryUsage.used.toDouble() / 1073741824
		)
	)
	println(
		String.format(
			"Max heap memory: %.2f GB",
			memoryMXBean.heapMemoryUsage.max.toDouble() / 1073741824
		)
	)
	println(
		String.format(
			"Committed memory: %.2f GB",
			memoryMXBean.heapMemoryUsage.committed.toDouble() / 1073741824
		)
	)




	val threadMXBean: ThreadMXBean = ManagementFactory.getThreadMXBean()

	for (threadID in threadMXBean.getAllThreadIds()) {
		val info: ThreadInfo = threadMXBean.getThreadInfo(threadID)
		println("Thread name: " + info.threadName)
		println("Thread State: " + info.threadState)
		println(
			java.lang.String.format(
				"CPU time: %s ns",
				threadMXBean.getThreadCpuTime(threadID)
			)
		)
	}

	val cDrive = File("C:")
	println("Total space: ${cDrive.getTotalSpace() / 1073741824} GB")
	println("Free space: ${cDrive.getFreeSpace() / 1073741824} GB")
	println("Usable space: ${cDrive.getUsableSpace() / 1073741824} GB")


}
