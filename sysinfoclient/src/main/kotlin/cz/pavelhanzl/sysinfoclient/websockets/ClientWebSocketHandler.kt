package cz.pavelhanzl.sysinfoclient.websockets


import org.springframework.stereotype.Service
import org.springframework.web.socket.*
import org.springframework.web.socket.client.standard.StandardWebSocketClient
import org.springframework.web.socket.handler.TextWebSocketHandler
import java.io.FileInputStream
import java.security.KeyStore
import java.security.SecureRandom
import javax.net.ssl.*
import org.springframework.core.io.ClassPathResource


@Service
class ClientWebSocketHandler : WebSocketHandler {





    private val client = StandardWebSocketClient()
    private var session: WebSocketSession? = null
    private var uri = "wss://server:443/websocket-endpoint"
    //private var uri = "ws://server:8080/websocket-endpoint"


    // Kontrola, zda aplikace běží v Dockeru
    val isDockerEnv = System.getenv("DOCKER_ENV")?.toBoolean() ?: false


    init {
        tlsConfiguration()
        establishSession()
    }

    private fun tlsConfiguration() {
        val trustStore = KeyStore.getInstance(KeyStore.getDefaultType())
        val trustStorePassword = "123456Ab"





        if (isDockerEnv) {
            // Aplikace běží v Dockeru, použij výchozí truststore
            trustStore.load(null, null)
            println("Runing in docker")
        } else {
            // Aplikace běží lokálně, načti vlastní truststore
            trustStore.load(ClassPathResource("/certs/truststore.jks").inputStream, trustStorePassword.toCharArray())
        }



        val tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
        tmf.init(trustStore)

        val sslContext = SSLContext.getInstance("TLS")
        sslContext.init(null, tmf.trustManagers, SecureRandom())

        client.userProperties["org.apache.tomcat.websocket.SSL_CONTEXT"] = sslContext
    }

    private fun establishSession() {
        val handler: WebSocketHandler = object : TextWebSocketHandler() {
            override fun afterConnectionEstablished(session: WebSocketSession) {
                this@ClientWebSocketHandler.session = session
            }
        }
        //if is runing in docker, then handshake with default uri, else runing localy and handshake with local uri
        if (isDockerEnv) {
            client.doHandshake(handler, uri)
        } else {
            uri = "wss://localhost:443/websocket-endpoint"
            //uri = "ws://localhost:8080/websocket-endpoint"
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
       session.close()
    }

    override fun supportsPartialMessages(): Boolean {
        return false
    }

    fun sendMessage(message: String) {
        if (session != null && session!!.isOpen) {
            session?.sendMessage(TextMessage(message))
        } else {
            println("Session is not established. Trying to re-establish.")
            establishSession()
            // Možná přidat krátké zpoždění nebo logiku opakovaných pokusů
        }
    }
    // Implementace dalších metod
}