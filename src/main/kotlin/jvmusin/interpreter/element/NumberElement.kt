package jvmusin.interpreter.element

data class NumberElement(val value: Int) : Element {
    override fun invokeUnsafely(environment: CallEnvironment) = value
    override fun validate(environment: CallEnvironment) {}
    override fun toString() = value.toString()
}
