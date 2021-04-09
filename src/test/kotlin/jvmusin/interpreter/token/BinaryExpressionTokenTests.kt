package jvmusin.interpreter.token

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import jvmusin.interpreter.SymbolQueue

class BinaryExpressionTokenTests : BehaviorSpec({
    Given("reader.tryRead()") {
        fun tryRead(s: String) = BinaryExpressionTokenReader.tryRead(SymbolQueue(s))
        When("expression is good") {
            Then("reads it") {
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
        }
        When("empty parentheses") {
            Then("fails") {
                tryRead("()").shouldBeNull()
            }
        }
        When("no operation") {
            Then("fails") {
                tryRead("((a+b))").shouldBeNull()
            }
        }
    }
})
