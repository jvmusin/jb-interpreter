package jvmusin.interpreter.element

data class FunctionElement(
    val lineNumber: Int,
    val name: String,
    val parameterNames: List<String>,
    val body: Element
) : Element {

    override fun invoke(environment: CallEnvironment) = invokeSafelyWithLineNumber(lineNumber) {
        invokeUnsafely(environment)
    }

    override fun invokeUnsafely(environment: CallEnvironment) = body(environment)

    override fun validate(environment: CallEnvironment) {
        if (parameterNames.distinct().size != parameterNames.size)
            throw ArgumentNamesNotDistinct(name).wrap(lineNumber)
        body.validateWithLineNumber(environment, lineNumber)
    }

    override fun toString(): String {
        return "$name(${parameterNames.joinToString(",")})={$body}"
    }
}
