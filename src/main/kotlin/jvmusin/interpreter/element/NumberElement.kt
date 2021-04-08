package jvmusin.interpreter.element

data class NumberElement(val value: Int) : Element {
    override fun invoke(environment: CallEnvironment) = value
    override fun validate(environment: CallEnvironment) {}
}
