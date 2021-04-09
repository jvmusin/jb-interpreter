package jvmusin.interpreter.token

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import jvmusin.interpreter.SymbolQueue

class ProgramTokenTests : StringSpec({
    fun tryRead(s: String) = ProgramTokenReader.tryRead(SymbolQueue(s))
    "Reads program without functions" {
        val s = "[((10+20)>(20/a))]?(1):(0)"
        tryRead(s).shouldNotBeNull().toElement().toString() shouldBe s
    }
    "Reads program with functions" {
        val s = """
            f(x)={(1+2)}
            g(x)={[(x/5)]?(1):(8)}
            f(g(x))
        """.trimIndent()
        tryRead(s).shouldNotBeNull().toElement().toString() shouldBe s
    }
    "Fails when there are extra symbols in the end" {
        val s = """
            f(x)={(1+2)}
            g(x)={[(x/5)](1):(8)}
            f(g(x))
            
        """.trimIndent()
        tryRead(s).shouldBeNull()
    }
})
