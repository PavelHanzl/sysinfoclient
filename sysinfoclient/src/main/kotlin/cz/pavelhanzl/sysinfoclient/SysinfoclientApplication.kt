package cz.pavelhanzl.sysinfoclient

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.core.io.ClassPathResource
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
}
