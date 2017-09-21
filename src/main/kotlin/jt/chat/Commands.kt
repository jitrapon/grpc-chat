package jt.chat

import java.util.ArrayList
import kotlin.collections.HashMap
import kotlin.collections.component1
import kotlin.collections.component2

/**
 * Contains functions that manages all the available commands to be used
 */
object Commands {

    private const val PARAM_PREFIX_CHAR = ':'

    private val map: HashMap<String, (List<String>) -> Boolean> = HashMap()

    fun add(cmd: String, func: (args: List<String>) -> Boolean) {
        if (cmd.isEmpty()) throw Exception("Command must not be empty!")
        map.put(cmd, func)
    }

    fun run(input: String): Boolean {
        for ((cmd, func) in map) {
            val (isValid, args) = extractCommand(input, cmd)
            if (isValid) {
                return func(args)
            }
        }
        throw NoCommandException("No command found for $input")
    }

    fun listAll(): List<String> = map.keys.toList()

    private fun extractCommand(input: String, cmd: String): Pair<Boolean, List<String>> {
        val inputTokens = input.split(' ')
        val cmdTokens = cmd.split( ' ')

        val args = ArrayList<String>()
        var nextCmd: String? = null
        var cmdIndex = 0
        var isInputArgument = false
        val stringBuffer = StringBuffer()
        var correctCount = 0

        for (i in 0 until inputTokens.size) {
            if (cmdIndex < cmdTokens.size) {
                if (cmdTokens[cmdIndex].equals(inputTokens[i], true)) {
                    cmdIndex++
                    correctCount++
                    continue
                }
                if (cmdTokens[cmdIndex].startsWith(PARAM_PREFIX_CHAR)) {
                    nextCmd = if (cmdIndex + 1 == cmdTokens.size) null else cmdTokens[cmdIndex + 1]
                    isInputArgument = true
                }
                else {
                    cmdIndex++
                }
            }

            if (isInputArgument) {
                if (nextCmd == null || !inputTokens[i].equals(nextCmd, false)) {
                    stringBuffer.append(" " + inputTokens[i])
                    if (nextCmd != null && nextCmd.startsWith(PARAM_PREFIX_CHAR)) {
                        args.add(stringBuffer.toString().trim())
                        stringBuffer.setLength(0)
                        isInputArgument = false
                        nextCmd = null
                        cmdIndex++
                    }
                }
                else {
                    args.add(stringBuffer.toString().trim())
                    stringBuffer.setLength(0)
                    isInputArgument = false
                    nextCmd = null
                    cmdIndex += 2
                    correctCount++
                }
            }
        }
        stringBuffer.toString().trim().let {
            if (it.isNotEmpty()) args.add(it)
        }

        return (correctCount == (cmdTokens.size - args.size)) to args
    }
}

class NoCommandException(message: String) : Exception(message)
