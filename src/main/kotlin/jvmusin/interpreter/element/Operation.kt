package jvmusin.interpreter.element

data class Operation(val representation: Char, val function: (Int, Int) -> Int) {
    operator fun invoke(a: Int, b: Int) = function(a, b)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Operation

        if (representation != other.representation) return false

        return true
    }

    override fun hashCode(): Int {
        return representation.hashCode()
    }
}
