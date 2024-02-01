package cz.pavelhanzl.sysinfoclient

import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class Controller {

    @Value("\${server.port}")
    var Port: Int = 0
    @RequestMapping("/port")
    fun getPortNumber():String{
        return "Client on port $Port"
    }
}