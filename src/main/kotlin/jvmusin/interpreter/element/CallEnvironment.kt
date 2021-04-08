package jvmusin.interpreter.element

data class CallEnvironment(private val functions: Functions, private val variables: Variables) {
    fun getFunction(name: String) = functions[name]
    fun getVariable(name: String) = variables[name]
}

data class Functions(private val functionByName: Map<String, FunctionElement>) {
    operator fun get(name: String) = functionByName[name] ?: throw FunctionNotFoundError(name)
}

class Variables(private val values: Map<String, Int>) {
    operator fun get(name: String) = values[name] ?: throw ParameterNotFoundError(name)
}
