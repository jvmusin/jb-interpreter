package jvmusin.interpreter.token

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import jvmusin.interpreter.SymbolQueue

class IfExpressionTokenTests : BehaviorSpec ({
    Given("reader.tryRead()") {
        When("string is '[a]?(b):(c)'") {
            Then("reads it") {
                val result = IfExpressionTokenReader.tryRead(SymbolQueue("[a]?(b):(c)"))
                result.shouldNotBeNull().toElement().toString() shouldBe "[a]?(b):(c)"
            }
        }
        When("string is '[(a<2)]?((a+1)):(5)'") {
            Then("reads it") {
                val result = IfExpressionTokenReader.tryRead(SymbolQueue("[(a<2)]?((a+1)):(5)"))
                result.shouldNotBeNull().toElement().toString() shouldBe "[(a<2)]?((a+1)):(5)"
            }
        }
    }
})