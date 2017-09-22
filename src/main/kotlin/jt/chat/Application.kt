package jt.chat

import java.util.*

/**
 * Application entry point
 *
 * @author Jitrapon Tiachunpun
 */
fun main(args: Array<String>) {

    Application.start()
}

/**
 * Singleton class at the core of the app. This class redirects command, depending on the user's input,
 * to particular functions in the server or the client.
 */
object Application {

    /* application's properties loaded from 'app.properties' file */
    private val properties: Properties by lazy {
        Properties().apply {
            load(Thread.currentThread().contextClassLoader.getResourceAsStream("app.properties"))
        }
    }

    /**
     * Starts the application and immediately and asks for user's input, which will forward to
     * a specific function matched by the available commands
     */
    fun start() {
        // add all the commands and their associated functions
        Commands.apply {
            add("join :ip :port", Application::joinRoom)
            add("host :port", Application::createRoom)
            add("help", Application::showAllCommands)
            add("exit", Application::exit)
        }

        println("Welcome to ${properties["artifactId"]} (v${properties["version"]}), " +
                "type commands to begin. If you need help with commands, type help")
        runMainCmdLoop()
    }

    /**
     * Begins the main command loop that will match user's input to a command
     * Can be called by the Client or Server to resume the main application loop
     */
    fun runMainCmdLoop() {
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

    /**
     * Attempts to join a room
     * Returns true to break main command loop
     */
    private fun joinRoom(args: List<String>): Boolean {
        Client(this, args[0], Integer.parseInt(args[1])).start()
        return true
    }

    /**
     * Creates a room for others to join
     * TODO returns true to break main command loop
     */
    private fun createRoom(args: List<String>): Boolean {
        Server.start(Integer.parseInt(args[0]))
        return false
    }

    /**
     * Displays all added commands
     * Returns false to allow main command loop to continue
     */
    @Suppress("UNUSED_PARAMETER")
    private fun showAllCommands(args: List<String>): Boolean {
        val commands = Commands.listAll()
        println(StringBuilder().apply {
            append("The following commands are available:\n")
            for (cmd in commands) {
                append(cmd).append("\n")
            }
        }.toString())
        return false
    }

    /**
     * Exits the application
     * Returns true to break main command loop
     */
    @Suppress("UNUSED_PARAMETER")
    private fun exit(args: List<String>): Boolean {
        println("Exiting...bye!")
        System.exit(0)
        return true
    }
}


