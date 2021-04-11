package jvmusin.interpreter.element

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

class VariableElementTests : StringSpec({
    "toString returns variable name" {
        VariableElement("abc").toString() shouldBe "abc"
    }
    "validate accesses environment's variable with the given name" {
        val environment = mockk<CallEnvironment>()
        every { environment.getVariable("x") } returns 1
        VariableElement("x").validate(environment)
        verify { environment.getVariable("x") }
    }
    "invoke returns variable's value from environment" {
        val environment = mockk<CallEnvironment>()
        every { environment.getVariable("x") } returns 1
        VariableElement("x")(environment) shouldBe 1
        verify { environment.getVariable("x") }
    }
})
