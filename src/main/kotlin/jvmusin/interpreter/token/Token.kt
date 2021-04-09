package jvmusin.interpreter.token

/**
 * A single token defined in the grammar.
 */
interface Token {

    /**
     * Symbols used to represent this token.
     */
    val symbolsUsed: Int
}
