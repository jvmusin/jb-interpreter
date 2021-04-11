package jvmusin.interpreter.element

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.mockk

class CallEnvironmentTests : BehaviorSpec({
    Given("Functions") {
        When("no functions are provided") {
            Then("on any function name and parameters throws FunctionNotFoundError") {
                val functions = buildFunctions(emptyList())
                shouldThrow<FunctionNotFoundError> { functions["abc", 1] }
            }
        }
        When("all function names are distinct") {
            val body = mockk<Element>()
            val f1 = FunctionElement(1, "f1", listOf("x"), body)
            val f2 = FunctionElement(2, "f2", listOf("x", "y"), body)
            val f3 = FunctionElement(3, "f3", listOf("a", "b"), body)
            val functions = buildFunctions(listOf(f1, f2, f3))
            Then("returns functions") {
                functions["f1", 1] shouldBe f1
                functions["f2", 2] shouldBe f2
                functions["f3", 2] shouldBe f3
            }
            Then("throws FunctionNotFoundError on unknown functions") {
                shouldThrow<FunctionNotFoundError> { functions["f4", 1] }
            }
            Then("throws ArgumentNumberMismatchError on known functions, but unknown parameter count") {
                shouldThrow<ArgumentNumberMismatchError> { functions["f2", 1] }
            }
        }
        When("all functions are distinct, but some have same name") {
            val body = mockk<Element>()
            val f1 = FunctionElement(1, "f1", listOf("x"), body)
            val f2 = FunctionElement(2, "f1", listOf("x", "y"), body)
            val f3 = FunctionElement(3, "f3", listOf("a", "b"), body)
            val functions = buildFunctions(listOf(f1, f2, f3))
            Then("returns functions") {
                functions["f1", 1] shouldBe f1
                functions["f1", 2] shouldBe f2
                functions["f3", 2] shouldBe f3
            }
            Then("throws FunctionNotFoundError on unknown functions") {
                shouldThrow<FunctionNotFoundError> { functions["f4", 1] }
            }
            Then("throws ArgumentNumberMismatchError on known functions, but unknown parameter count") {
                shouldThrow<ArgumentNumberMismatchError> { functions["f1", 3] }
            }
        }
    }

    Given("buildFunctions") {
        When("some functions have same name and parameter count") {
            val body = mockk<Element>()
            val f1 = FunctionElement(1, "f1", listOf("x"), body)
            val f2 = FunctionElement(2, "f1", listOf("a"), body)
            val f3 = FunctionElement(3, "f3", listOf("a", "b"), body)
            val functions = buildFunctions(listOf(f1, f2, f3))
            Then("only the last one is stored") {
                functions["f1", 1] shouldBe f2
            }
        }
    }

    Given("Variables") {
        When("a variable exist") {
            Then("returns its value") {
                val variables = Variables(mapOf("a" to 1, "b" to 2))
                variables["a"] shouldBe 1
                variables["b"] shouldBe 2
            }
        }
        When("a variable does not exist") {
            Then("throws ParameterNotFoundError") {
                val variables = Variables(mapOf("a" to 1, "b" to 2))
                shouldThrow<ParameterNotFoundError> { variables["c"] }
            }
        }
    }
})
