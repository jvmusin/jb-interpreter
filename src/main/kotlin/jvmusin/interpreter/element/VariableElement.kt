package jvmusin.interpreter.element

data class VariableElement(val name: String): Element {
    override fun invoke(environment: CallEnvironment) = environment.getVariable(name)
}
