package jvmusin.interpreter.runner

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.*

class BinaryExpressionElementTests : StringSpec({
    "toString returns 'left+right' where '+' is an operation sign" {
        val left = mockk<Element>()
        val right = mockk<Element>()
        val operation = BinaryOperation('#', Int::plus)

        every { left.toString() } returns "op1"
        every { right.toString() } returns "op2"

        BinaryExpressionElement(left, right, operation).toString() shouldBe "(op1#op2)"
    }
    "validate runs validation on left and right operands" {
        val environment = mockk<CallEnvironment>()
        val left = mockk<Element>()
        val right = mockk<Element>()

        every { left.validate(environment) } just Runs
        every { right.validate(environment) } just Runs

        BinaryExpressionElement(left, right, mockk()).validate(environment)

        verifyAll {
            left.validate(environment)
            right.validate(environment)
        }
    }
    "invoke calls operation on left and right operands" {
        val environment = mockk<CallEnvironment>()
        val left = mockk<Element>()
        val right = mockk<Element>()
        val operation = mockk<BinaryOperation>()

        every { left(environment) } returns 1
        every { right(environment) } returns 2
        every { operation(1, 2) } returns 102

        BinaryExpressionElement(left, right, operation)(environment) shouldBe 102

        verifyAll {
            left(environment)
            right(environment)
            operation(1, 2)
        }
    }
})
