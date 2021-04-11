package jvmusin.interpreter.runner

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.throwable.shouldHaveMessage
import io.mockk.*

class ProgramElementTests : StringSpec({

    val f1Body = mockk<Element>()
    val f2Body = mockk<Element>()
    val f3Body = mockk<Element>()
    val main = mockk<Element>()

    val f1 = FunctionElement(1, "f1", listOf("x"), f1Body)
    val f2 = FunctionElement(2, "f2", listOf("a", "b"), f2Body)
    val f3 = FunctionElement(3, "f3", listOf("param1", "param2"), f3Body)

    val functionsList = listOf(f1, f2, f3)
    val functions = buildFunctions(functionsList)

    beforeEach {
        clearMocks(f1Body, f2Body, f3Body, main)
    }

    "toString returns all defined functions and then the main method" {
        every { f1Body.toString() } returns "body1"
        every { f2Body.toString() } returns "body2"
        every { f3Body.toString() } returns "body3"
        every { main.toString() } returns "(f1(5)+f2(6,7))"

        ProgramElement(listOf(f1, f2, f3), main).toString() shouldBe """
            f1(x)={body1}
            f2(a,b)={body2}
            f3(param1,param2)={body3}
            (f1(5)+f2(6,7))
        """.trimIndent()
    }
    "validate(environment) does not call anything on environment" {
        ProgramElement(emptyList(), NumberElement(1)).validate(mockk())
    }
    "invoke(environment) does not call anything on environment" {
        ProgramElement(emptyList(), NumberElement(1))(mockk())
    }
    "validate calls validate on all the functions with all their parameters set" {
        fun List<String>.toVariables() = Variables(associateWith { 0 })
        every { f1Body.validate(CallEnvironment(functions, f1.parameterNames.toVariables())) } just Runs
        every { f2Body.validate(CallEnvironment(functions, f2.parameterNames.toVariables())) } just Runs
        every { f3Body.validate(CallEnvironment(functions, f3.parameterNames.toVariables())) } just Runs
        every { main.validate(CallEnvironment(functions, Variables(emptyMap()))) } just Runs
        ProgramElement(functionsList, main).validate()
        verifyAll {
            f1Body.validate(CallEnvironment(functions, f1.parameterNames.toVariables()))
            f2Body.validate(CallEnvironment(functions, f2.parameterNames.toVariables()))
            f3Body.validate(CallEnvironment(functions, f3.parameterNames.toVariables()))
            main.validate(CallEnvironment(functions, Variables(emptyMap())))
        }
    }
    "validate fails when there are two functions with the same name and parameter count" {
        every { main.validate(any()) } just Runs
        every { f2Body.validate(any()) } just Runs
        every { f3Body.validate(any()) } just Runs
        val e = shouldThrow<ValidationError> {
            ProgramElement(listOf(f2, f3.copy(name = "f2")), main).validate()
        }
        e shouldHaveMessage "FUNCTIONS NOT DISTINCT f2:3" // ends with :3 because initially f3 was on third line
    }
    "validate fails when main function fails" {
        val e = object : ValidationError("the_cause") {
            override val prefix = "Prefix"
        }
        every { f1Body.validate(any()) } just Runs
        every { f2Body.validate(any()) } just Runs
        every { f3Body.validate(any()) } just Runs
        every { main.validate(any()) } throws e
        val thrown = shouldThrow<ValidationError> {
            ProgramElement(listOf(f1, f2, f3), main).validate()
        }
        thrown shouldHaveMessage "Prefix the_cause:4"
    }
    "invoke calls invoke on main method" {
        every { main(CallEnvironment(functions, Variables(emptyMap()))) } returns 42
        ProgramElement(listOf(f1, f2, f3), main)() shouldBe 42
        verify { main(CallEnvironment(functions, Variables(emptyMap()))) }
    }
})
