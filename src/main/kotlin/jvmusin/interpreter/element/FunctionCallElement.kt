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
}
