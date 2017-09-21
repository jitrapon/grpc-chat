package jt.chat

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
        var shouldEndCmdLoop = false
        while(!shouldEndCmdLoop) {
            readInput().let {
                try {
                    shouldEndCmdLoop = Commands.run(it)
                }
                catch (ex: Exception) {
                    println(ex.message)
                }
            }
        }
    }

    fun joinRoom(args: List<String>): Boolean {
        client = Client(args[0], Integer.parseInt(args[1])).apply {
            run()
        }
        return true
    }

    fun createRoom(args: List<String>): Boolean {
        Server.start(Integer.parseInt(args[0]))
        return false
    }

    fun showAllCommands(args: List<String>): Boolean {
        val commands = Commands.listAll()
        println(StringBuilder().apply {
            append("The following commands are available:\n")
            for (cmd in commands) {
                append(cmd).append("\n")
            }
        }.toString())
        return false
    }

    fun exit(args: List<String>): Boolean {
        println("Exiting...bye!")
        System.exit(0)
        return true
    }
}


