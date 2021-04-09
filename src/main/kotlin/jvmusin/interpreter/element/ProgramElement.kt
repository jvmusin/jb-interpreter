package jvmusin.interpreter.element

data class ProgramElement(private val functions: List<FunctionElement>, private val body: Element) : Element {

    private val startEnvironment = CallEnvironment(Functions(functions.associateBy { it.name }), Variables(emptyMap()))

    override fun invoke(environment: CallEnvironment) = invoke()
    operator fun invoke() = body(startEnvironment)

    override fun validate(environment: CallEnvironment) = run { validate() }
    fun validate() {
        if (functions.distinctBy { it.name }.size != functions.size)
            throw ValidationError("FUNCTION NAMES NOT DISTINCT")
        for (function in functions) {
            val variables = Variables(function.parameterNames.associateWith { 0 })
            function.validate(startEnvironment.copy(variables = variables))
        }
        body.validateWithLineNumber(startEnvironment, functions.size + 1)
    }

    override fun toString(): String {
        return functions.joinToString("") { it.toString() + "\n" } + body.toString()
    }
}
