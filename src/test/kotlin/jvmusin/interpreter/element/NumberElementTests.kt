package jvmusin.interpreter.element

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.mockk

class NumberElementTests : StringSpec({
    "toString returns the number in string" {
        NumberElement(-1).toString() shouldBe "-1"
        NumberElement(0).toString() shouldBe "0"
        NumberElement(1).toString() shouldBe "1"
        NumberElement(Int.MIN_VALUE).toString() shouldBe "-2147483648"
        NumberElement(Int.MAX_VALUE).toString() shouldBe "2147483647"
    }
    "validate does not touch environment" {
        NumberElement(0).validate(mockk())
    }
    "invoke returns the number" {
        val environment = mockk<CallEnvironment>()
        NumberElement(-1)(environment) shouldBe -1
        NumberElement(0)(environment) shouldBe 0
        NumberElement(1)(environment) shouldBe 1
        NumberElement(Int.MIN_VALUE)(environment) shouldBe Int.MIN_VALUE
        NumberElement(Int.MAX_VALUE)(environment) shouldBe Int.MAX_VALUE
    }
})
