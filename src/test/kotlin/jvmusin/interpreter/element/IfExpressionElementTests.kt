package jvmusin.interpreter.element

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.*

class IfExpressionElementTests : StringSpec({
    "invoke passes environment to condition, ifTrue and ifFalse" {
        val environment = getTestEnvironment()

        val condition = mockk<Element>()
        val ifTrue = mockk<Element>()
        val ifFalse = mockk<Element>()

        every { condition.invoke(environment) } returnsMany listOf(0, 1)
        every { ifTrue.invoke(environment) } returns 0
        every { ifFalse.invoke(environment) } returns 0

        val element = IfExpressionElement(condition, ifTrue, ifFalse)
        element(environment)
        element(environment)

        verifyAll {
            ifTrue.invoke(environment)
            ifFalse.invoke(environment)
        }
    }
    "Evaluates ifTrue if condition gives not zero and ifFalse otherwise" {
        val condition = mockk<Element>()
        every { condition.invoke(any()) } returnsMany listOf(-1, -100, 0, 1, 0, 100)

        val ifTrue = mockk<Element>()
        val ifFalse = mockk<Element>()

        every { ifTrue.invoke(any()) } returns 0
        every { ifFalse.invoke(any()) } returns 0

        val element = IfExpressionElement(condition, ifTrue, ifFalse)
        repeat(6) { element(CallEnvironment.EMPTY) }

        verifySequence {
            ifTrue.invoke(any())
            ifTrue.invoke(any())
            ifFalse.invoke(any())
            ifTrue.invoke(any())
            ifFalse.invoke(any())
            ifTrue.invoke(any())
        }
    }
    "validate calls validate on condition, ifTrue and ifFalse" {
        val environment = getTestEnvironment()

        val condition = mockk<Element>()
        val ifTrue = mockk<Element>()
        val ifFalse = mockk<Element>()

        every { condition.validate(environment) } just Runs
        every { ifTrue.validate(environment) } just Runs
        every { ifFalse.validate(environment) } just Runs

        IfExpressionElement(condition, ifTrue, ifFalse).validate(environment)

        verifyAll {
            condition.validate(environment)
            ifTrue.validate(environment)
            ifFalse.validate(environment)
        }
    }
    "toString returns string in format '[condition]?(ifTrue):(ifFalse)'" {
        val condition = mockk<Element>()
        val ifTrue = mockk<Element>()
        val ifFalse = mockk<Element>()

        every { condition.toString() } returns "a"
        every { ifTrue.toString() } returns "b"
        every { ifFalse.toString() } returns "c"

        IfExpressionElement(condition, ifTrue, ifFalse).toString() shouldBe "[a]?(b):(c)"
    }
})