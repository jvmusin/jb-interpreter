package jvmusin.interpreter.runner

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class WrappableErrorTests : StringSpec({

    class TestWrappableError(
        message: String,
        cause: Throwable? = null,
        wrapped: Boolean = false
    ) : WrappableError(message, cause, wrapped) {
        override val prefix = "Prefix"
        fun wrap(lineNumber: Int) = wrap(lineNumber, ::TestWrappableError)
    }

    "Wraps unwrapped exceptions correctly" {
        val e = TestWrappableError("msg")
        val wrapped = e.wrap(42)
        wrapped.cause shouldBe e
        wrapped.message shouldBe "Prefix msg:42"
        wrapped.wrapped shouldBe true
    }

    "Does not wrap already wrapped exceptions" {
        val e = TestWrappableError("msg", wrapped = true)
        e.wrap(42) shouldBe e
    }
})
