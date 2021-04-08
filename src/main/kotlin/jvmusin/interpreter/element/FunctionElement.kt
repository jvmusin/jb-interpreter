package jvmusin.interpreter.element

data class FunctionElement(
    val lineNumber: Int,
    val name: String,
    val parameterNames: List<String>,
    val body: Element
) : Element {
    override fun invoke(environment: CallEnvironment): Int {
        try {
            return body(environment)
        } catch (e: Throwable) {
            throw IllegalArgumentException("FIX IT")
        }
    }

    override fun validate(environment: CallEnvironment) {
        try {
            body.validate(environment)
        } catch (e: ArgumentNumberMismatchError) {
            throw ArgumentNumberMismatchError("ARGUMENT NUMBER MISMATCH ${e.message}:$lineNumber")
        } catch (e: FunctionNotFoundError) {
            throw FunctionNotFoundError("FUNCTION NOT FOUND ${e.message}:$lineNumber")
        } catch (e: ParameterNotFoundError) {
            throw ParameterNotFoundError("PARAMETER NOT FOUND ${e.message}:$lineNumber")
        }
    }
}
