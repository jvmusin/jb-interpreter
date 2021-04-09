package jvmusin.interpreter.token

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import jvmusin.interpreter.SymbolQueue

class SafeTokenReaderTests : StringSpec({
    "Reads strings" {
        val q = SymbolQueue("abcde")
        readTokenSafely(q) {
            readString("a")
            readString("bc")
        }.shouldNotBeNull()
        q.poll { it == 'd' }
    }
    "Does not break caret after reading failure" {
        val q = SymbolQueue("abcdefg")
        readTokenSafely(q) {
            readString("a")
            readString("bc")
        }.shouldNotBeNull()
        readTokenSafely(q) {
            readString("de")
            readString("fq")
        }.shouldBeNull()
        q.poll { it == 'd' }
    }
    "Reads a correct expression" {
        val q = SymbolQueue("(abc+f(123))")
        readTokenSafely(q) {
            readToken(GeneralExpressionTokenReader)
        }.shouldNotBeNull().toElement().toString() shouldBe "(abc+f(123))"
        q.isEmpty().shouldBeTrue()
    }
})
