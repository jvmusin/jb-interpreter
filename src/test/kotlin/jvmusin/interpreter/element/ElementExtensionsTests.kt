package jvmusin.interpreter.element

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.assertions.throwables.shouldThrowAny
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.throwable.shouldHaveMessage
import io.mockk.*

class ElementExtensionsTests : BehaviorSpec({

    val testElement = mockk<Element>()

    beforeEach {
        clearMocks(testElement)
    }

    Given("validateWithLineNumber") {
        When("everything is alright") {
            Then("does not throw any exceptions") {
                every { testElement.validate(any()) } just Runs
                testElement.validateWithLineNumber(CallEnvironment.EMPTY, 1)
            }
        }
        When("element's validate method throws a ValidationError") {
            Then("throws a wrapped validation error") {
                val e = object : ValidationError("cause") {
                    override val prefix = "Prefix"
                }
                every { testElement.validate(any()) } throws e
                shouldThrow<ValidationError> { testElement.validateWithLineNumber(CallEnvironment.EMPTY, 2) }
                    .shouldHaveMessage("Prefix cause:2")
            }
        }
        When("element's validate method throws not a ValidationError for some reason") {
            Then("rethrows the same exception") {
                val e = Exception("123")
                every { testElement.validate(any()) } throws e
                shouldThrowAny { testElement.validateWithLineNumber(CallEnvironment.EMPTY, 2) } shouldBe e
            }
        }
    }

    Given("invokeSafely") {
        When("no exceptions are thrown") {
            Then("returns calculated value") {
                every { testElement.invokeUnsafely(any()) } returns 42
                testElement.invokeSafely { invokeUnsafely(CallEnvironment.EMPTY) } shouldBe 42
            }
        }
        When("an InterpreterRuntimeError is thrown") {
            Then("it rethrows") {
                val e = InterpreterRuntimeError("oops")
                every { testElement.invokeUnsafely(any()) } throws e
                shouldThrowAny { testElement.invokeSafely { invokeUnsafely(CallEnvironment.EMPTY) } } shouldBe e
            }
        }
        When("some throwable but InterpreterRuntimeError is thrown") {
            Then("it wraps into an InterpreterRuntimeError with message equal to element.toString() and rethrows") {
                val e = StackOverflowError()
                every { testElement.invokeUnsafely(any()) } throws e
                every { testElement.toString() } returns "string representation"
                val thrown = shouldThrowExactly<InterpreterRuntimeError> {
                    testElement.invokeSafely { invokeUnsafely(CallEnvironment.EMPTY) }
                }
                thrown.cause shouldBe e
                thrown shouldHaveMessage "string representation"
            }
        }
    }

    Given("invokeSafelyWithLineNumber") {
        When("no exceptions are thrown") {
            Then("returns calculated value") {
                every { testElement.invokeUnsafely(any()) } returns 42
                testElement.invokeSafelyWithLineNumber(11) { invokeUnsafely(CallEnvironment.EMPTY) } shouldBe 42
            }
        }
        When("an unwrapped InterpreterRuntimeError is thrown") {
            Then("it's getting wrapped and rethrown") {
                val e = InterpreterRuntimeError("oops")
                every { testElement.invokeUnsafely(any()) } throws e
                val thrown = shouldThrow<InterpreterRuntimeError> {
                    testElement.invokeSafelyWithLineNumber(11) { invokeUnsafely(CallEnvironment.EMPTY) }
                }
                thrown.cause shouldBe e
                thrown shouldHaveMessage "RUNTIME ERROR oops:11"
            }
        }
        When("a wrapped InterpreterRuntimeError is thrown") {
            Then("it rethrows") {
                val e = InterpreterRuntimeError("oops", wrapped = true)
                every { testElement.invokeUnsafely(any()) } throws e
                shouldThrowAny {
                    testElement.invokeSafelyWithLineNumber(11) { invokeUnsafely(CallEnvironment.EMPTY) }
                } shouldBe e
            }
        }
        When("some Throwable but InterpreterRuntimeError is thrown") {
            Then("it wraps with a cause equal to exception's toString()") {
                val e = Throwable("the cause")
                every { testElement.invokeUnsafely(any()) } throws e
                every { testElement.toString() } returns "string representation"
                val thrown = shouldThrow<InterpreterRuntimeError> {
                    testElement.invokeSafelyWithLineNumber(11) { invokeUnsafely(CallEnvironment.EMPTY) }
                }
                thrown.cause.shouldNotBeNull().cause shouldBe e // it's getting nested because of wrapping
                thrown.message shouldBe "RUNTIME ERROR string representation:11"
            }
        }
    }
})
