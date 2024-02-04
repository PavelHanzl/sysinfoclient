package cz.pavelhanzl.sysinfoclient.websockets


import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Service
import org.springframework.web.socket.*
import org.springframework.web.socket.client.standard.StandardWebSocketClient
import org.springframework.web.socket.handler.TextWebSocketHandler



@Service
class ClientWebSocketHandler : WebSocketHandler {

    private val client = StandardWebSocketClient()
    private var session: WebSocketSession? = null
    private var uri = "ws://server:8080/websocket-endpoint"



    init {
        establishSession()
    }

    private fun establishSession() {
        val handler: WebSocketHandler = object : TextWebSocketHandler() {
            override fun afterConnectionEstablished(session: WebSocketSession) {
                this@ClientWebSocketHandler.session = session
            }
        }
        //if not runing in docker, then handshake with default uri, else runing localy and handshake with local uri
        if (System.getenv("SRV_PORT") != null) {
            client.doHandshake(handler, uri)
        } else {
            uri = "ws://localhost:8080/websocket-endpoint"
            client.doHandshake(handler, uri)
        }
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
        if (session != null) {
            session?.sendMessage(TextMessage(message))
        } else {
            println("Session is not established. Trying to re-establish.")
            establishSession()
            // Možná přidat krátké zpoždění nebo logiku opakovaných pokusů
        }
    }
    // Implementace dalších metod
}