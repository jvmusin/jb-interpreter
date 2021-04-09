package jvmusin.interpreter.element

data class ProgramElement(private val functions: List<FunctionElement>, private val body: Element) : Element {

    private val bodyLineNumber = functions.size + 1

    private val startEnvironment = CallEnvironment(Functions(functions.associateBy { it.name }), Variables(emptyMap()))

    operator fun invoke() = invoke(startEnvironment)
    override fun invoke(environment: CallEnvironment) = invokeSafelyWithLineNumber(bodyLineNumber) {
        invokeUnsafely(startEnvironment)
    }

    override fun invokeUnsafely(environment: CallEnvironment) = body(startEnvironment)

    override fun validate(environment: CallEnvironment) = validate()
    fun validate() {
        val foundFunctionNames = mutableSetOf<String>()
        for (function in functions) {
            if (!foundFunctionNames.add(function.name)) {
                throw FunctionNamesNotDistinct(function.name).wrap(function.lineNumber)
            }
            val variables = Variables(function.parameterNames.associateWith { 0 })
            function.validate(startEnvironment.copy(variables = variables))
        }
        body.validateWithLineNumber(startEnvironment, bodyLineNumber)
    }

    override fun toString(): String {
        return functions.joinToString("") { it.toString() + "\n" } + body.toString()
    }
}
