package jvmusin.interpreter.token

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import jvmusin.interpreter.SymbolQueue

class CallExpressionTokenTests : StringSpec({
    fun tryRead(s: String) = CallExpressionTokenReader.tryRead(SymbolQueue(s))
    "Reads func(1,2,abc,(a+b),(a+foo(1,2,5,e)))" {
        val s = "func(1,2,abc,(a+b),(a+foo(1,2,5,e)))"
        tryRead(s).shouldNotBeNull().toElement().toString() shouldBe s
    }
    "Fails when no arguments used" {
        tryRead("func()").shouldBeNull()
    }
    "Fails when function name contains digits" {
        tryRead("func1(1)").shouldBeNull()
    }
})
