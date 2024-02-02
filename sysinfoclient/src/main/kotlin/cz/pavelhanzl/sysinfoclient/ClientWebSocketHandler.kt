package cz.pavelhanzl.sysinfoclient


import org.springframework.stereotype.Service
import org.springframework.web.socket.*


@Service
class ClientWebSocketHandler : WebSocketHandler {

    fun getPort(): String {
        return System.getenv("SRV_PORT") ?: "Not Specified"
    }
    override fun afterConnectionEstablished(session: WebSocketSession) {

        val message = TextMessage( getPort()+" Hello Server!")
        session.sendMessage(message)
    }

    override fun handleMessage(session: WebSocketSession, message: WebSocketMessage<*>) {
        println(message.payload)
    }

    override fun handleTransportError(session: WebSocketSession, exception: Throwable) {
        println("Transport error")
    }

    override fun afterConnectionClosed(session: WebSocketSession, closeStatus: CloseStatus) {
        println("Connection closed")
    }

    override fun supportsPartialMessages(): Boolean {
        return false
    }
    // Implementace dalších metod
}