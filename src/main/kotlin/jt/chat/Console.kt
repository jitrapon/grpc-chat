package jt.chat

/**
 * This file contains utility methods for reading/writing to console. Abstracts the underlying
 * call to log or console
 *
 * @author Jitrapon Tiachunpun
 */

/**
 * Reads input from standard input stream. Returns non-empty String.
 */
fun readInput(): String {
    while (true) {
        readLine().let {
            if (!it.isNullOrBlank()) {
                return it!!
            }
        }
    }
}