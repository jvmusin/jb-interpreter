package jvmusin.interpreter.token

interface RunnableToken : Token {
    fun run(callEnvironment: CallEnvironment): Int
}
