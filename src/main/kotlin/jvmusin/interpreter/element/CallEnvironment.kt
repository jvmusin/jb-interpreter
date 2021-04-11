package jvmusin.interpreter.element

/**
 * Call environment.
 *
 * Contains all known [functions] and the current scope [variables].
 */
data class CallEnvironment(private val functions: Functions, private val variables: Variables) {

    companion object {
        val EMPTY = CallEnvironment(Functions(emptyMap()), Variables(emptyMap()))
    }

    /**
     * Returns function with the given [name] and [parameterCount].
     *
     * Throws [FunctionNotFoundError] if the function with this [name] is not found.
     *
     * Throws [ArgumentNumberMismatchError] if the function found, but not for this [parameterCount].
     */
    fun getFunction(name: String, parameterCount: Int) = functions[name, parameterCount]

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
data class Functions(private val functionByNameAndParameterCount: Map<String, Map<Int, FunctionElement>>) {
    /**
     * Returns function with the given [name] and [parameterCount].
     *
     * Throws [FunctionNotFoundError] if the function is not found.
     *
     * Throws [ArgumentNumberMismatchError] if the function found, but not for this [parameterCount].
     */
    operator fun get(name: String, parameterCount: Int): FunctionElement {
        val parameterCountToFunction = functionByNameAndParameterCount[name] ?: throw FunctionNotFoundError(name)
        return parameterCountToFunction[parameterCount] ?: throw ArgumentNumberMismatchError(name)
    }
}

/**
 * Returns [Functions] object containing the given [functions].
 *
 * If there are several functions with the same name and parameter count, then only the last one in the list is stored.
 */
fun buildFunctions(functions: List<FunctionElement>): Functions {
    val functionByNameAndParameterCount = functions
        .groupBy { it.name }
        .mapValues { (_, funcs) -> funcs.associateBy { it.parameterNames.size } }
    return Functions(functionByNameAndParameterCount)
}

/**
 * Variables.
 *
 * Contains all variables with their respective [values] in the current scope and allows to access them by name.
 */
data class Variables(private val values: Map<String, Int>) {
    /**
     * Returns value of a variable with the given [name] or throws [ParameterNotFoundError] if the variable is not found.
     */
    operator fun get(name: String) = values[name] ?: throw ParameterNotFoundError(name)
}
