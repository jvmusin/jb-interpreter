package jvmusin.interpreter.token

data class CallEnvironment(private val functions: Functions, private val parameters: Parameters) {
    fun getParameter(name: String) = parameters[name]
    fun getFunction(name: String) = functions[name]
}

class Parameters(private val values: Map<String, Int>) {
    operator fun get(name: String) = values[name]!!
}

class Functions(private val values: Map<String, FunctionDefinitionToken>) {
    operator fun get(name: String) = values[name] ?: throw FunctionNotFoundException("FUNCTION NOT FOUND $name")
}

class ParameterNotFoundException(message: String?) : Exception(message)
class FunctionNotFoundException(message: String?) : Exception(message)