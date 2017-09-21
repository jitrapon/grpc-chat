package jt.chat

/**
 * Contains utility methods for reading/writing to console
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