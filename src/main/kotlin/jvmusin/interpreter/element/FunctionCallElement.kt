package jvmusin.interpreter.element

data class FunctionCallElement(
    val functionName: String,
    val arguments: List<Element>
) : Element {
    override fun invoke(environment: CallEnvironment): Int {
        val argumentValues = arguments.map { it.invoke(environment) }
        val function = environment.getFunction(functionName)
        val parameterNames = function.parameterNames
        return function(environment.copy(variables = Variables(parameterNames.zip(argumentValues).toMap())))
    }

    override fun validate(environment: CallEnvironment) {
        arguments.forEach { it.validate(environment) }
        if (environment.getFunction(functionName).parameterNames.size != arguments.size)
            throw ArgumentNumberMismatchError(functionName)
    }

    override fun toExpressionString(): String {
        return "$functionName(${arguments.joinToString(",") { it.toExpressionString() }})"
    }
}
