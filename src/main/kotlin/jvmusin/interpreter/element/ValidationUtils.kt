package jvmusin.interpreter.element

fun Element.validateWithLineNumber(environment: CallEnvironment, lineNumber: Int) {
    try {
        validate(environment)
    } catch (e: ArgumentNumberMismatchError) {
        throw ArgumentNumberMismatchError("ARGUMENT NUMBER MISMATCH ${e.message}:$lineNumber")
    } catch (e: FunctionNotFoundError) {
        throw FunctionNotFoundError("FUNCTION NOT FOUND ${e.message}:$lineNumber")
    } catch (e: ParameterNotFoundError) {
        throw ParameterNotFoundError("PARAMETER NOT FOUND ${e.message}:$lineNumber")
    }
}
