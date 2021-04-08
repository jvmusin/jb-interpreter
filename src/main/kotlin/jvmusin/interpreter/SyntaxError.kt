package jvmusin.interpreter

import java.lang.Exception

class SyntaxError: Exception {
    constructor() : super()
    constructor(message: String?) : super(message)
}
