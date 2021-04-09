package jvmusin.interpreter.token

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import jvmusin.interpreter.SymbolQueue

class GeneralExpressionTokenReaderTests : StringSpec({
    fun tryRead(s: String) = GeneralExpressionTokenReader.tryRead(SymbolQueue(s))
    "Reads binary expressions" {
        val s = "(a+(x/2))"
        tryRead(s).shouldNotBeNull().toElement().toString() shouldBe s
    }
    "Reads call expressions" {
        val s = "f((a+(x/2)))"
        tryRead(s).shouldNotBeNull().toElement().toString() shouldBe s
    }
    "Reads if expressions" {
        val s = "[(a+(x/2))]?(13):((45/11))"
        tryRead(s).shouldNotBeNull().toElement().toString() shouldBe s
    }
    "Reads constant expressions" {
        val s = "-123"
        tryRead(s).shouldNotBeNull().toElement().toString() shouldBe s
    }
    "Reads identifier expressions" {
        val s = "some_variable_or_functionName"
        tryRead(s).shouldNotBeNull().toElement().toString() shouldBe s
    }
})
