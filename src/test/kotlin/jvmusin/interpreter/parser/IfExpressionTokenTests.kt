package jvmusin.interpreter.parser

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe

class IfExpressionTokenTests : BehaviorSpec({
    Given("reader.tryRead()") {
        fun tryRead(s: String) = IfExpressionTokenReader.tryRead(SymbolQueue(s))
        When("string is '[a]?(b):(c)'") {
            Then("reads it") {
                val s = "[a]?(b):(c)"
                val result = tryRead(s)
                result.shouldNotBeNull().toElement().toString() shouldBe s
            }
        }
        When("string is '[(a<2)]?((a+1)):(5)'") {
            Then("reads it") {
                val s = "[(a<2)]?((a+1)):(5)"
                val result = tryRead(s)
                result.shouldNotBeNull().toElement().toString() shouldBe s
            }
        }
    }
})
