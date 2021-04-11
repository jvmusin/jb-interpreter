package jvmusin.interpreter.parser

import io.kotest.assertions.throwables.shouldThrowAny
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe

class SymbolQueueTests : BehaviorSpec({
    Given("isEmpty") {
        When("string is empty") {
            Then("returns true") {
                SymbolQueue("").isEmpty().shouldBeTrue()
            }
        }
        When("string is not empty") {
            Then("returns false") {
                SymbolQueue("abc").isEmpty().shouldBeFalse()
            }
            And("all symbols were read") {
                Then("returns true") {
                    SymbolQueue("abc").apply {
                        poll { it == 'a' }
                        poll { it == 'b' }
                        poll { it == 'c' }
                        isEmpty().shouldBeTrue()
                    }
                }
            }
        }
    }
    Given("caret") {
        When("reading a symbol") {
            Then("it moves") {
                SymbolQueue("abc").apply {
                    caret shouldBe 0
                    poll { it == 'a' }
                    poll { it == 'b' }
                    caret shouldBe 2
                    poll { it == 'c' }
                    caret shouldBe 3
                }
            }
        }
        When("moving it to correct position") {
            Then("it moves") {
                SymbolQueue("abc").apply {
                    poll { it == 'a' }
                    poll { it == 'b' }
                    caret = 0
                    poll { it == 'a' }
                    isEmpty().shouldBeFalse()
                    caret = 3
                    isEmpty().shouldBeTrue()
                }
            }
        }
        When("moving caret to the end of a string") {
            Then("it moves") {
                SymbolQueue("abc").apply {
                    caret shouldBe 0
                    isEmpty().shouldBeFalse()
                    caret = 3
                    isEmpty().shouldBeTrue()
                    tryPoll { true }.shouldBeNull()
                }
            }
        }
        When("moving caret to negative position") {
            Then("fails to move") {
                SymbolQueue("abc").apply {
                    poll { it == 'a' }
                    caret shouldBe 1
                    shouldThrowAny { run { caret = -1 } }
                    caret shouldBe 1
                    poll { it == 'b' }
                }
            }
        }
        When("moving caret too far away") {
            Then("fails to move") {
                SymbolQueue("abc").apply {
                    shouldThrowAny { run { caret = 4 } }
                    shouldThrowAny { run { caret = 5 } }
                    shouldThrowAny { run { caret = 6 } }
                    caret shouldBe 0
                }
            }
        }
    }
    Given("tryPoll") {
        When("one symbol can be read") {
            Then("reads it") {
                SymbolQueue("abc").tryPoll { it == 'a' } shouldBe 'a'
            }
            And("poll predicate fails") {
                Then("does not move caret") {
                    SymbolQueue("abc").apply {
                        tryPoll { it == 'q' }.shouldBeNull()
                        tryPoll { it == 'a' } shouldBe 'a'
                    }
                }
            }
        }
        When("several symbols can be read") {
            Then("reads them") {
                SymbolQueue("abc").apply {
                    tryPoll { it == 'a' } shouldBe 'a'
                    tryPoll { it == 'b' } shouldBe 'b'
                    tryPoll { it == 'c' } shouldBe 'c'
                }
            }
            And("all of them are read") {
                Then("queue becomes empty") {
                    SymbolQueue("abc").apply {
                        tryPoll { it == 'a' }
                        tryPoll { it == 'b' }
                        tryPoll { it == 'c' }
                        isEmpty().shouldBeTrue()
                        tryPoll { true }.shouldBeNull()
                    }
                }
            }
        }
    }
    Given("poll") {
        When("predicate gives true") {
            Then("returns symbols") {
                SymbolQueue("abc").apply {
                    poll { it == 'a' } shouldBe 'a'
                    poll { it == 'b' } shouldBe 'b'
                    poll { it == 'c' } shouldBe 'c'
                }
            }
        }
        When("predicate gives false") {
            Then("fails") {
                SymbolQueue("abc").apply {
                    poll { it == 'a' } shouldBe 'a'
                    shouldThrowAny { poll { it == 'c' } }
                }
            }
        }
        When("queue becomes empty") {
            Then("fails") {
                SymbolQueue("abc").apply {
                    poll { it == 'a' }
                    poll { it == 'b' }
                    poll { it == 'c' }
                    shouldThrowAny { poll { it == 'c' } }
                }
            }
        }
    }
})
