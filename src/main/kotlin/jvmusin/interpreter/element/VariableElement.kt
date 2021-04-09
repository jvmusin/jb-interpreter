package jvmusin.interpreter.element

data class VariableElement(val name: String) : Element {
    override fun invokeUnsafely(environment: CallEnvironment) = environment.getVariable(name)
    override fun validate(environment: CallEnvironment): Unit = run { environment.getVariable(name) }
    override fun toString() = name
}
