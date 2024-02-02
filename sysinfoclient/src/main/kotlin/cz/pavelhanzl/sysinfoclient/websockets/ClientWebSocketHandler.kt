package cz.pavelhanzl.sysinfoclient.websockets


import org.springframework.stereotype.Service
import org.springframework.web.socket.*
import org.springframework.web.socket.client.standard.StandardWebSocketClient
import org.springframework.web.socket.handler.TextWebSocketHandler



@Service
class ClientWebSocketHandler : WebSocketHandler {

    private val client = StandardWebSocketClient()
    private var session: WebSocketSession? = null
    //private final val uri = "ws://server:8080/websocket-endpoint"
    private final val uri = "ws://localhost:8080/websocket-endpoint"



    init {
        val handler: WebSocketHandler = object : TextWebSocketHandler() {
            override fun afterConnectionEstablished(session: WebSocketSession) {
                this@ClientWebSocketHandler.session = session
            }
        }

        client.doHandshake(handler, uri)
    }

    override fun afterConnectionEstablished(session: WebSocketSession) {
        val message = TextMessage( " Hello Server!")
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

    fun sendMessage(message: String) {
        session?.sendMessage(TextMessage(message)) ?: println("Session is not established.")
    }
    // Implementace dalších metod
}