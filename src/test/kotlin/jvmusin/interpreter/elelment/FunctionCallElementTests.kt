package jvmusin.interpreter.elelment

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.*
import jvmusin.interpreter.element.*

class FunctionCallElementTests : StringSpec({
    "toString returns string representation in form of 'functionName(arg1,arg2)'" {
        val arg1 = mockk<Element>()
        val arg2 = mockk<Element>()

        every { arg1.toString() } returns "a"
        every { arg2.toString() } returns "b"

        FunctionCallElement("fun", listOf(arg1, arg2)).toString() shouldBe "fun(a,b)"
    }
    "validate calls environment.getFunction and calls validate on arguments" {
        val environment = mockk<CallEnvironment>()
        val arg1 = mockk<Element>()
        val arg2 = mockk<Element>()

        every { environment.getFunction("f", 2) } returns mockk()
        every { arg1.validate(environment) } just Runs
        every { arg2.validate(environment) } just Runs

        FunctionCallElement("f", listOf(arg1, arg2)).validate(environment)

        verifyAll {
            environment.getFunction("f", 2)
            arg1.validate(environment)
            arg2.validate(environment)
        }
    }
    "invoke calls invoke on arguments and then calls function with modified environment" {
        val fBody = mockk<Element>()
        val f = FunctionElement(1, "f", listOf("a", "b"), fBody)
        val functions = buildFunctions(listOf(f))

        val startEnvironment = CallEnvironment(functions, Variables(mapOf("x" to 1)))
        val f2Environment = CallEnvironment(functions, Variables(mapOf("a" to 123, "b" to 45)))

        every { fBody(f2Environment) } returns 42

        FunctionCallElement("f", listOf(NumberElement(123), NumberElement(45)))(startEnvironment)

        verify { fBody(f2Environment) }
    }
})