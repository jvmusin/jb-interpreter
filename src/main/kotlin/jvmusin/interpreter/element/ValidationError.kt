package jvmusin.interpreter.element

open class ValidationError(message: String) : Error(message)

class ParameterNotFoundError(message: String) : ValidationError(message)
class FunctionNotFoundError(message: String) : ValidationError(message)
class ArgumentNumberMismatchError(message: String) : ValidationError(message)
