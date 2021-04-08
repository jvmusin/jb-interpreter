package jvmusin.interpreter.element

data class Operation(val representation: Char, val function: (Int, Int) -> Int) {
    operator fun invoke(a: Int, b: Int) = function(a, b)
}
