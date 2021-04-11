package jvmusin.interpreter.runner

/**
 * Binary operation.
 *
 * Represents a binary function on two ints.
 *
 * @property representation A character representation of this operation.
 * @property function The operation itself.
 */
data class BinaryOperation(val representation: Char, val function: (Int, Int) -> Int) {
    /**
     * Invokes [function] on parameters [a] and [b] and returns it's result.
     */
    operator fun invoke(a: Int, b: Int) = function(a, b)
}
