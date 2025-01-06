package com.nikitar.command

class CommandReader private constructor() {
    companion object {
        private const val DEFAULT_PORT = 8080
        private const val DEFAULT_MODE = true

        fun parseArgs(args: Array<String>): CommandArguments {
            var port = DEFAULT_PORT
            var mode = DEFAULT_MODE

            args.forEachIndexed { i, arg ->
                when (arg) {
                    "-p" -> port = args.getOrNull(i + 1)?.toIntOrNull() ?: DEFAULT_PORT
                    "-m" -> mode = args.getOrNull(i + 1)?.toBooleanStrictOrNull() ?: DEFAULT_MODE
                }
            }

            return CommandArguments(port, mode)
        }
    }
}