package jvmusin.interpreter.token

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import jvmusin.interpreter.SymbolQueue

class ConstantExpressionTokenTests : BehaviorSpec({
    Given("reader.tryRead()") {
        fun tryRead(s: String) = ConstantExpressionTokenReader.tryRead(SymbolQueue(s))
        When("string is a good number") {
            Then("reads it") {
                val numbers = listOf("123", "0", "-32")
                for (number in numbers) {
                    tryRead(number).shouldNotBeNull().toElement().toString() shouldBe number
                }
            }
        }
        When("string is 'a12'") {
            Then("fails") {
                tryRead("a12").shouldBeNull()
            }
        }
        When("string is empty") {
            Then("fails") {
                tryRead("").shouldBeNull()
            }
        }
    }
})
