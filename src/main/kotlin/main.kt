import jvmusin.interpreter.parser.ProgramTokenReader
import jvmusin.interpreter.parser.SymbolQueue

/**
 * Replaces `\r\n` newlines with `\n` ones and drops trailing newlines in the [input] string.
 */
private fun fixInput(input: String) = input.replace("\r\n", "\n").trimEnd('\n')

/**
 * Runs the program.
 *
 * Reads all the input and then parses and runs the program.
 */
fun main() {
    val input = System.`in`.readAllBytes().decodeToString()
    println(main(fixInput(input)))
}

/**
 * Runs the program, assuming that [input] has `\n` newline symbols and no trailing end-line symbols.
 * This can be achieved by calling [fixInput].
 */
fun main(input: String) = try {
    val program = ProgramTokenReader.read(SymbolQueue(input)).toElement()
    program.validate()
    program().toString()
} catch (e: Throwable) {
    e.message ?: throw e
}
