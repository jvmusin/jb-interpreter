package jvmusin.interpreter.parser

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldBeEmpty

class SafeTokenReaderTests : BehaviorSpec({
    Given("readSymbol(symbol)") {
        When("next symbol is good") {
            Then("reads it and does not fail") {
                SymbolQueue("abc").apply {
                    caret = 1
                    readSafely { readSymbol('b') shouldBe 'b'; 'x' } shouldBe 'x'
                    caret shouldBe 2
                }
            }
        }
        When("next symbol is bad") {
            Then("does not read it and fails") {
                SymbolQueue("abc").apply {
                    caret = 1
                    readSafely { readSymbol('c'); 'x' }.shouldBeNull()
                    caret shouldBe 1
                }
            }
        }
    }
    Given("readSymbol(predicate)") {
        When("next symbol is good") {
            Then("reads it and does not fail") {
                SymbolQueue("abc").apply {
                    caret = 1
                    readSafely { readSymbol { it == 'b' } shouldBe 'b'; 'x' } shouldBe 'x'
                    caret shouldBe 2
                }
            }
        }
        When("next symbol is bad") {
            Then("does not read it and fails") {
                SymbolQueue("abc").apply {
                    caret = 1
                    readSafely { readSymbol { it == 'c' }; 'x' }.shouldBeNull()
                    caret shouldBe 1
                }
            }
        }
    }
    Given("tryReadSymbol(symbol)") {
        When("next symbol is good") {
            Then("reads it and does not fail") {
                SymbolQueue("abc").apply {
                    caret = 1
                    readSafely { tryReadSymbol('b') shouldBe 'b'; 'x' } shouldBe 'x'
                    caret shouldBe 2
                }
            }
        }
        When("next symbol is bad") {
            Then("does not read it, but does not fail") {
                SymbolQueue("abc").apply {
                    caret = 1
                    readSafely { tryReadSymbol('c').shouldBeNull(); 'x' } shouldBe 'x'
                    caret shouldBe 1
                }
            }
        }
    }
    Given("tryReadSymbol(predicate)") {
        When("next symbol is good") {
            Then("reads it and does not fail") {
                SymbolQueue("abc").apply {
                    caret = 1
                    readSafely { tryReadSymbol { it == 'b' } shouldBe 'b'; 'x' } shouldBe 'x'
                    caret shouldBe 2
                }
            }
        }
        When("next symbol is bad") {
            Then("does not read it, but does not fail") {
                SymbolQueue("abc").apply {
                    caret = 1
                    readSafely { tryReadSymbol { it == 'c' }.shouldBeNull(); 'x' } shouldBe 'x'
                    caret shouldBe 1
                }
            }
        }
    }
    Given("readString(string)") {
        When("string is empty") {
            Then("reads an empty string and does not fail") {
                SymbolQueue("abcd").apply {
                    caret = 1
                    readSafely { readString("").shouldBeEmpty(); 'x' } shouldBe 'x'
                    caret shouldBe 1
                }
            }
        }
        When("next string is good") {
            Then("reads it and does not fail") {
                SymbolQueue("abcd").apply {
                    caret = 1
                    readSafely { readString("bc") shouldBe "bc"; 'x' } shouldBe 'x'
                    caret shouldBe 3
                }
            }
        }
        When("next string is bad") {
            Then("does not read it and fails") {
                SymbolQueue("abcd").apply {
                    caret = 1
                    readSafely { readString("bq").shouldBeNull(); 'x' }.shouldBeNull()
                    caret shouldBe 1
                }
            }
        }
    }
    Given("readString(predicate)") {
        When("next string is good") {
            Then("reads it and does not fail") {
                SymbolQueue("abcd").apply {
                    caret = 1
                    readSafely { readString { it in "bc" } shouldBe "bc"; 'x' } shouldBe 'x'
                    caret shouldBe 3
                }
            }
        }
        When("one symbol is good") {
            Then("reads it and does not fail") {
                SymbolQueue("abcd").apply {
                    caret = 1
                    readSafely { readString { it in "bq" } shouldBe "b"; 'x' } shouldBe 'x'
                    caret shouldBe 2
                }
            }
        }
        When("no symbols are good") {
            Then("does not read anything and does not fail") {
                SymbolQueue("abcd").apply {
                    caret = 1
                    readSafely { readString { it in "xq" }.shouldBeEmpty(); 'x' } shouldBe 'x'
                    caret shouldBe 1
                }
            }
        }
    }
    Given("readToken") {
        When("token can be read") {
            Then("reads it and moves caret") {
                SymbolQueue("123 identifier 123").apply {
                    readSafely {
                        readToken(ConstantExpressionTokenReader).value shouldBe 123
                        readSymbol(' ') shouldBe ' '
                        readToken(IdentifierTokenReader).name shouldBe "identifier"
                        "qq"
                    } shouldBe "qq"
                    caret shouldBe "123 identifier".length
                }
            }
        }
        When("token can not be read") {
            Then("returns null and does not move the caret") {
                SymbolQueue("123 identifier 123").apply {
                    caret = 1
                    readSafely {
                        readToken(ConstantExpressionTokenReader).value shouldBe 23
                        readSymbol(' ') shouldBe ' '
                        readToken(IdentifierTokenReader) shouldBe "identifier"
                        readToken(IdentifierTokenReader)
                    }.shouldBeNull()
                    caret shouldBe 1
                }
            }
        }
    }
    Given("integration tests") {
        When("reading several tokens one by one") {
            Then("they are read correctly") {
                SymbolQueue("this is 123 text").apply {
                    readSafely {
                        readString("this ") shouldBe "this "
                        tryReadSymbol('s').shouldBeNull()
                        readString("is 12") shouldBe "is 12"
                        readToken(ConstantExpressionTokenReader).value shouldBe 3
                        "12345"
                    } shouldBe "12345"
                    caret shouldBe "this ".length + "is 12".length + "3".length
                }
            }
        }
        When("something is between fails") {
            Then("caret returns back") {
                SymbolQueue("this is 123 text").apply {
                    caret = 2
                    readSafely {
                        readString("is ") shouldBe "is "
                        tryReadSymbol('s').shouldBeNull()
                        readString("is 12") shouldBe "is 12"
                        readToken(ConstantExpressionTokenReader).value shouldBe 3
                        readToken(ConstantExpressionTokenReader).value
                        "12345"
                    }.shouldBeNull()
                    caret shouldBe 2
                }
            }
        }
        When("readSafely calls are nested") {
            Then("they work independently") {
                SymbolQueue("this is 123 text").apply {
                    caret = 2
                    readSafely {
                        readString("is ") shouldBe "is "
                        tryReadSymbol('s').shouldBeNull()
                        readSafely {
                            readString("is 123") shouldBe "is 123"
                            'x'
                        } shouldBe 'x'
                        readSafely { readString("qqq") }.shouldBeNull()
                        readString(" text") shouldBe " text"
                        "qq"
                    } shouldBe "qq"
                    caret shouldBe "this is 123 text".length
                }
            }
        }
    }
})
