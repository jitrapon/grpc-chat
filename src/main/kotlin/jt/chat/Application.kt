package jt.chat

import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.*

fun main(args: Array<String>) {

    Application.start()
}

object Application {

    private var client: Client? = null

    private val properties: Properties by lazy {
        Properties().apply {
            load(Thread.currentThread().contextClassLoader.getResourceAsStream("app.properties"))
        }
    }

    fun start() {
        Commands.apply {
            add("join :ip :port", Application::joinRoom)
            add("host :port", Application::createRoom)
            add("help", Application::showAllCommands)
            add("exit", Application::exit)
        }

        println("Welcome to ${properties["artifactId"]} (v${properties["version"]}), " +
                "type commands to begin. If you need help with commands, type help")
        BufferedReader(InputStreamReader(System.`in`)).forEachLine {
            if (!it.trim().isEmpty()) {
                try {
                    Commands.run(it)
                }
                catch (ex: Exception) {
                    println(ex.message)
                }
            }
        }
    }

    fun joinRoom(args: List<String>) {
        client = client ?: Client(args[0], Integer.parseInt(args[1]))
        try {
            client!!.joinRoom()
        }
        finally {
            client!!.shutdown(3)
        }
    }

    fun createRoom(args: List<String>) {
        Server.start(Integer.parseInt(args[0]))
    }

    fun showAllCommands(args: List<String>) {
        val commands = Commands.listAll()
        println(StringBuilder().apply {
            append("The following commands are available:\n")
            for (cmd in commands) {
                append(cmd).append("\n")
            }
        }.toString())
    }

    fun exit(args: List<String>) {
        println("Exiting...bye!")
        System.exit(0)
    }
}


