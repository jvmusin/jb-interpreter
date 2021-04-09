package jvmusin.interpreter.token

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import main

class IntegrationTests : StringSpec({
    "Calculator (2+2)" {
        main("(2+2)").toInt() shouldBe 4
    }
    "Calculator (2+((3*4)/5))" {
        main("(2+((3*4)/5))").toInt() shouldBe 4
    }
    "If Expression" {
        main("[((10+20)>(20+10))]?(1):(0)").toInt() shouldBe 0
    }
    "Functions" {
        val input = """
            g(x)={(f(x)+f((x/2)))}
            f(x)={[(x>1)]?((f((x-1))+f((x-2)))):(x)}
            g(10)
        """.trimIndent()
        main(input).toInt() shouldBe 60
    }
    "SYNTAX ERROR" {
        main("1 + 2 + 3 + 4 + 5") shouldBe "SYNTAX ERROR"
    }
    "PARAMETER NOT FOUND" {
        val input = """
            f(x)={y}
            f(10)
        """.trimIndent()
        main(input) shouldBe "PARAMETER NOT FOUND y:1"
    }
    "FUNCTION NOT FOUND" {
        val input = """
            g(x)={f(x)}
            g(10)
        """.trimIndent()
        main(input) shouldBe "FUNCTION NOT FOUND f:1"
    }
    "ARGUMENT NUMBER MISMATCH" {
        val input = """
            g(x)={(x+1)}
            g(10,20)
        """.trimIndent()
        main(input) shouldBe "ARGUMENT NUMBER MISMATCH g:2"
    }
    "RUNTIME ERROR" {
        val input = """
            g(a,b)={(a/b)}
            g(10,0)
        """.trimIndent()
        main(input) shouldBe "RUNTIME ERROR"
    }
})
