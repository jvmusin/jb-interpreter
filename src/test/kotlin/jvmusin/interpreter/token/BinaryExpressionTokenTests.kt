package jvmusin.interpreter.token

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import jvmusin.interpreter.SymbolQueue

class BinaryExpressionTokenTests : StringSpec({
    fun tryRead(s: String) = BinaryExpressionTokenReader.tryRead(SymbolQueue(s))
    "Reads good expressions" {
        val expressions = listOf(
            "(1+1)",
            "(a+b)",
            "(a+1)",
            "(first+(second+3))",
            "((a+b)/(c-(e+f)))"
        )
        for (expression in expressions) {
            tryRead(expression).shouldNotBeNull().toElement().toString() shouldBe expression
        }
    }
    "Fails on empty parentheses" { tryRead("()").shouldBeNull() }
    "Fails on no operation inside" { tryRead("((a+b))").shouldBeNull() }
})
