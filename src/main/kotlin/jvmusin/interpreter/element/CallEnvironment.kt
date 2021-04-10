package jvmusin.interpreter.element

/**
 * Call environment.
 *
 * Contains all known [functions] and the current scope [variables].
 */
data class CallEnvironment(private val functions: Functions, private val variables: Variables) {
    /**
     * Returns function with the given [name] or throws [FunctionNotFoundError] if the function is not found.
     */
    fun getFunction(name: String) = functions[name]

    /**
     * Returns value of a variable with the given [name] or throws [ParameterNotFoundError] if the variable is not found.
     */
    fun getVariable(name: String) = variables[name]
}

/**
 * Functions.
 *
 * Contains all known functions and allows to access them by name.
 */
data class Functions(private val functionByName: Map<String, FunctionElement>) {
    /**
     * Returns function with the given [name] or throws [FunctionNotFoundError] if the function is not found.
     */
    operator fun get(name: String) = functionByName[name] ?: throw FunctionNotFoundError(name)
}

/**
 * Variables.
 *
 * Contains all variables in the current scope and allows to access them by name.
 */
class Variables(private val values: Map<String, Int>) {
    /**
     * Returns value of a variable with the given [name] or throws [ParameterNotFoundError] if the variable is not found.
     */
    operator fun get(name: String) = values[name] ?: throw ParameterNotFoundError(name)
}
