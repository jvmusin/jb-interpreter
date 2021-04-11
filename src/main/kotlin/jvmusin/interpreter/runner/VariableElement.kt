package jvmusin.interpreter.runner

/**
 * Variable element.
 *
 * Represents a variable with the given [name].
 */
data class VariableElement(val name: String) : Element {
    /**
     * Returns value of a variable with the given [name] in [environment].
     */
    override fun invokeUnsafely(environment: CallEnvironment) = environment.getVariable(name)

    /**
     * Validates this variable.
     *
     * Throws [ParameterNotFoundError] if it's not in [environment]-s scope.
     */
    override fun validate(environment: CallEnvironment): Unit = run { environment.getVariable(name) }

    /**
     * Returns [name] of the variable.
     */
    override fun toString() = name
}
