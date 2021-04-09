package jvmusin.interpreter.token

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import jvmusin.interpreter.SymbolQueue

class OperationTokenTests : BehaviorSpec({
    Given("reader.tryRead") {
        fun tryRead(s: String) = OperationTokenReader.tryRead(SymbolQueue(s))
        When("operation is known") {
            Then("reads it") {
                tryRead("+").shouldNotBeNull().apply { symbolsUsed shouldBe 1 }.function(100, 11) shouldBe 111
                tryRead("-").shouldNotBeNull().apply { symbolsUsed shouldBe 1 }.function(100, 11) shouldBe 89
                tryRead("*").shouldNotBeNull().apply { symbolsUsed shouldBe 1 }.function(100, 11) shouldBe 1100
                tryRead("/").shouldNotBeNull().apply { symbolsUsed shouldBe 1 }.function(100, 11) shouldBe 9
                tryRead("%").shouldNotBeNull().apply { symbolsUsed shouldBe 1 }.function(100, 11) shouldBe 1
                tryRead(">").shouldNotBeNull().apply { symbolsUsed shouldBe 1 }.function(100, 11) shouldBe 1
                tryRead("<").shouldNotBeNull().apply { symbolsUsed shouldBe 1 }.function(100, 11) shouldBe 0
                tryRead("=").shouldNotBeNull().apply { symbolsUsed shouldBe 1 }.function(100, 11) shouldBe 0
                tryRead("=").shouldNotBeNull().apply { symbolsUsed shouldBe 1 }.function(100, 100) shouldBe 1
            }
        }
        When("operation is unknown") {
            Then("fails") {
                tryRead("^").shouldBeNull()
            }
        }
    }
})