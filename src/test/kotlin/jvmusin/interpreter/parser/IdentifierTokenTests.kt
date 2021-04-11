package jvmusin.interpreter.parser

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe

class IdentifierTokenTests : StringSpec({
    fun tryRead(s: String) = IdentifierTokenReader.tryRead(SymbolQueue(s))
    "Reads single letter identifiers" {
        tryRead("a").shouldNotBeNull().name shouldBe "a"
    }
    "Reads several letters identifiers" {
        tryRead("abc").shouldNotBeNull().name shouldBe "abc"
    }
    "Reads lower and upped case letters" {
        tryRead("HelloWorld").shouldNotBeNull().name shouldBe "HelloWorld"
    }
    "Reads underscores" {
        tryRead("Some_text_with_underscores").shouldNotBeNull().name shouldBe "Some_text_with_underscores"
    }
    "Does not accept digits" {
        tryRead("Hello1").shouldNotBeNull().name shouldBe "Hello"
    }
})
