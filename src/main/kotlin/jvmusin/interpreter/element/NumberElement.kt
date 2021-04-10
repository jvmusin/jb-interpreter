package jvmusin.interpreter.element

/**
 * Number element.
 *
 * Represents a single number with the given [value].
 */
data class NumberElement(val value: Int) : Element {
    /**
     * Returns [value].
     */
    override fun invokeUnsafely(environment: CallEnvironment) = value

    /**
     * Does not do anything.
     */
    override fun validate(environment: CallEnvironment) {}

    /**
     * Returns [value]-s string representation
     */
    override fun toString() = value.toString()
}
