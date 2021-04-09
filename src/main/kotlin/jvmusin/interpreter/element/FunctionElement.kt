package jvmusin.interpreter.element

data class FunctionElement(
    val lineNumber: Int,
    val name: String,
    val parameterNames: List<String>,
    val body: Element
) : Element {
    override fun invoke(environment: CallEnvironment) = body(environment)

    override fun validate(environment: CallEnvironment) {
        if (parameterNames.distinct().size != parameterNames.size)
            throw ValidationError("ARGUMENT NAMES NOT DISTINCT $name:$lineNumber")
        body.validateWithLineNumber(environment, lineNumber)
    }

    override fun toString(): String {
        return "$name(${parameterNames.joinToString(",")})={$body}"
    }
}
