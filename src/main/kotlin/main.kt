import jvmusin.interpreter.SymbolQueue
import jvmusin.interpreter.token.ProgramTokenReader

private fun fixInput(input: String) = input.replace("\r\n", "\n").trimEnd('\n')

fun main() {
    val input = System.`in`.readAllBytes().decodeToString()
    println(main(fixInput(input)))
}

fun main(input: String) = try {
    val program = ProgramTokenReader.read(SymbolQueue(input)).toElement()
    program.validate()
    program().toString()
} catch (e: Throwable) {
    e.printStackTrace()
    e.message ?: throw e
}