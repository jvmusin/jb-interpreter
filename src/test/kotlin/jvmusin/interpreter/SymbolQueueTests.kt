package jvmusin.interpreter

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
                And("then one symbol was rolled back") {
                    Then("returns false") {
                        SymbolQueue("abc").apply {
                            poll { it == 'a' }
                            poll { it == 'b' }
                            poll { it == 'c' }
                            rollback(1)
                            isEmpty().shouldBeFalse()
                        }
                    }
                }
            }
        }
    }
    Given("rollback") {
        When("rolling back to negative indices") {
            Then("fails") {
                shouldThrowAny { SymbolQueue("abc").rollback(100) }
            }
        }
        When("rolling back negative value") {
            Then("fails") {
                shouldThrowAny { SymbolQueue("abc").rollback(-100) }
                shouldThrowAny { SymbolQueue("abc").apply { rollback(1); rollback(-1) } }
            }
        }
        When("after polling one symbol and rolling it back") {
            Then("doesn't change anything") {
                SymbolQueue("abc").apply {
                    poll { it == 'a' }
                    rollback(1)
                    poll { it == 'a' } shouldBe 'a'
                }
            }
        }
        When("after polling several symbols and rolling back by some") {
            Then("rolls back correctly") {
                SymbolQueue("abcde").apply {
                    poll { it == 'a' } shouldBe 'a'
                    poll { it == 'b' } shouldBe 'b'
                    poll { it == 'c' } shouldBe 'c'
                    poll { it == 'd' } shouldBe 'd'
                    rollback(2)
                    poll { it == 'c' } shouldBe 'c'
                    poll { it == 'd' } shouldBe 'd'
                    poll { it == 'e' } shouldBe 'e'
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
