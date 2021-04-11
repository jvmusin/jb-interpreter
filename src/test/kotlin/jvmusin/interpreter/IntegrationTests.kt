package jvmusin.interpreter

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldMatch
import main

class IntegrationTests : StringSpec({
    val negFunction = "neg(x)={(0-x)}"
    val absFunction = "abs(x)={[(x<0)]?(neg(x)):(x)}"
    val gcdFunction = "gcd(a,b)={[(b=0)]?(a):(gcd(b,(a%b)))}"
    val lcmFunction = "lcm(a,b)={((a*b)/gcd(a,b))}"
    val powFunction = "pow(n,k)={[(k=0)]?(1):((pow(n,(k-1))*n))}"
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
    "FUNCTIONS NOT DISTINCT" {
        val f1 = "f(a)={a}"
        val f2 = "f(c)={c}"
        runProgram("f(1)", f1, f2) shouldBe "FUNCTIONS NOT DISTINCT f:2"
    }

    "Deep recursive function fails on stack overflow" {
        val f = "f(x)={[(x=0)]?(0):((f((x-1))+x))}"
        fun sum(x: Int) = x * (x + 1) / 2
        runProgram("f(5)", f).toInt() shouldBe sum(5)
        runProgram("f(100)", f).toInt() shouldBe sum(100)
        runProgram("f(1000000000)", f) shouldMatch "^RUNTIME ERROR .*:1$".toRegex()
    }

    "Functions with the same name and different argument numbers are valid" {
        val min1 = "min(x,y)={[(x<y)]?(x):(y)}"
        val min2 = "min(x,y,z)={min(min(x,y),z)}"
        val min3 = "min(x,y,z,w)={min(min(x,y,z),w)}"
        runProgram("min(6,2,5,3)", min1, min2, min3).toInt() shouldBe 2
    }

    "neg function positive arg" { runProgram("neg(5)", negFunction).toInt() shouldBe -5 }
    "neg function negative arg" { runProgram("neg(-5)", negFunction).toInt() shouldBe 5 }
    "abs function negative arg" { runProgram("abs(-12)", absFunction, negFunction).toInt() shouldBe 12 }
    "abs function positive arg" { runProgram("abs(12)", absFunction, negFunction).toInt() shouldBe 12 }
    "gcd function" { runProgram("gcd(3120,1352)", gcdFunction).toInt() shouldBe 104 }
    "lcm function" { runProgram("lcm(3120,1352)", lcmFunction, gcdFunction).toInt() shouldBe 40560 }
    "pow function" { runProgram("pow(3,5)", powFunction).toInt() shouldBe 3 * 3 * 3 * 3 * 3 }
})
