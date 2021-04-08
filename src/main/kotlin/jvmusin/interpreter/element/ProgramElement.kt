package jvmusin.interpreter.element

data class ProgramElement(private val functions: List<FunctionElement>, private val body: Element) : Element {

    private val environment = CallEnvironment(Functions(functions.associateBy { it.name }), Variables(emptyMap()))

    override fun invoke(environment: CallEnvironment) = body(this.environment)

    override fun validate(environment: CallEnvironment) {
        if (functions.distinctBy { it.name }.size != functions.size)
            throw ValidationError("FUNCTION NAMES NOT DISTINCT")
        functions.forEach { it.validate(this.environment) }
        body.validate(this.environment)
    }

    override fun toExpressionString(): String {
        return functions.joinToString("") { it.toExpressionString() + "\n" } + body.toExpressionString()
    }
}
