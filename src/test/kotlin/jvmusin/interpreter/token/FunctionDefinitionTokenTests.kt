package jvmusin.interpreter.token

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import jvmusin.interpreter.SymbolQueue

class FunctionDefinitionTokenTests : BehaviorSpec({
    Given("reader.tryRead()") {
        fun tryRead(s: String) = FunctionDefinitionTokenReader.tryRead(SymbolQueue(s))
        When("good definition") {
            Then("reads it") {
                val definitions = listOf(
                    "f(x)={1}",
                    "g(x)={(f(x)+f((x/2)))}",
                    "f(a,b)={c}",
                    "functionName(condition,first,second)={[condition]?(first):(second)}"
                )
                for (definition in definitions) {
                    val function = tryRead(definition)
                    function.shouldNotBeNull().toElement(0).toString() shouldBe definition
                }
            }
        }
        When("no args") {
            Then("fails") {
                tryRead("f()={1}").shouldBeNull()
            }
        }
    }
})
