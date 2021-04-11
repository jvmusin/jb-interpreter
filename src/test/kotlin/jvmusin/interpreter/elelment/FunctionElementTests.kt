package jvmusin.interpreter.elelment

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.throwable.shouldHaveMessage
import io.mockk.every
import io.mockk.mockk
import jvmusin.interpreter.element.*

class FunctionElementTests : StringSpec({
    "invoke(environment) returns body's execution result when no exceptions are thrown" {
        val body = mockk<Element>()
        every { body.invoke(any()) } returns 42
        val f = FunctionElement(12, "f", listOf("x"), body)
        f.invoke(CallEnvironment.EMPTY) shouldBe 42
    }
    "invoke(environment) throws a well-formatted InterpreterRuntimeError on runtime errors" {
        val body = mockk<Element>()
        val e = StackOverflowError()
        every { body.invoke(any()) } throws e
        every { body.toString() } returns "body"
        val f = FunctionElement(12, "f", listOf("x"), body)
        val thrown = shouldThrow<InterpreterRuntimeError> { f.invoke(CallEnvironment.EMPTY) }
        thrown.cause.shouldNotBeNull().cause shouldBe e
        thrown shouldHaveMessage "RUNTIME ERROR f(x)={body}:12"
    }
    "invoke(environment) passes an environment to body" {
        val body = mockk<Element>()
        val func = FunctionElement(1, "f", listOf("x"), body)
        val env = CallEnvironment(buildFunctions(listOf(func)), Variables(mapOf("x" to 5)))
        every { body.invoke(any()) } answers { arg<CallEnvironment>(0).getVariable("x") }
        func.invoke(env) shouldBe 5
    }
    "toString returns string in format 'name(arg1,arg2)={body}'" {
        val body = mockk<Element>()
        every { body.toString() } returns "calc"
        FunctionElement(12, "func", listOf("ab", "c"), body).toString() shouldBe "func(ab,c)={calc}"
    }
})
