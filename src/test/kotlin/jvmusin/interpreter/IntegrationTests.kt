package jvmusin.interpreter

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import main

class IntegrationTests : StringSpec({
    val gcdFunction = "gcd(a,b)={[(b=0)]?(a):(gcd(b,(a%b)))}"
    val lcmFunction = "lcm(a,b)={((a*b)/gcd(a,b))}"
    val absFunction = "abs(x)={[(x<0)]?((0-x)):(x)}"
    fun buildProgram(program: String, vararg functions: String) = (functions.toList() + program).joinToString("\n")
    fun runProgram(program: String, vararg functions: String) = main(buildProgram(program, *functions))

    "Calculator (2+2)" {
        runProgram("(2+2)").toInt() shouldBe 4
    }
    "Calculator (2+((3*4)/5))" {
        runProgram("(2+((3*4)/5))").toInt() shouldBe 4
    }
    "If Expression" {
        runProgram("[((10+20)>(20+10))]?(1):(0)").toInt() shouldBe 0
    }
    "Functions" {
        val g = "g(x)={(f(x)+f((x/2)))}"
        val f = "f(x)={[(x>1)]?((f((x-1))+f((x-2)))):(x)}"
        runProgram("g(10)", g, f).toInt() shouldBe 60
    }
    "SYNTAX ERROR" {
        runProgram("1 + 2 + 3 + 4 + 5") shouldBe "SYNTAX ERROR"
    }
    "PARAMETER NOT FOUND" {
        val f = "f(x)={y}"
        runProgram("f(10)", f) shouldBe "PARAMETER NOT FOUND y:1"
    }
    "FUNCTION NOT FOUND" {
        val g = "g(x)={f(x)}"
        runProgram("g(10)", g) shouldBe "FUNCTION NOT FOUND f:1"
    }
    "ARGUMENT NUMBER MISMATCH" {
        val g = "g(x)={(x+1)}"
        runProgram("g(10,20)", g) shouldBe "ARGUMENT NUMBER MISMATCH g:2"
    }
    "RUNTIME ERROR" {
        val g = "g(a,b)={(a/b)}"
        runProgram("g(10,0)", g) shouldBe "RUNTIME ERROR (a/b):1"
    }
    "ARGUMENT NAMES NOT DISTINCT" {
        val f = "f(a,b,a)={b}"
        runProgram("f(1,2,1)", f) shouldBe "ARGUMENT NAMES NOT DISTINCT f:1"
    }
    "FUNCTION NAMES NOT DISTINCT" {
        val f1 = "f(a)={a}"
        val f2 = "f(c)={c}"
        runProgram("f(1)", f1, f2) shouldBe "FUNCTION NAMES NOT DISTINCT f:2"
    }

    "gcd function" { runProgram("gcd(3120,1352)", gcdFunction).toInt() shouldBe 104 }
    "lcm function" { runProgram("lcm(3120,1352)", lcmFunction, gcdFunction).toInt() shouldBe 40560 }
    "abs function" { runProgram("abs(-12)", absFunction).toInt() shouldBe 12 }
})
