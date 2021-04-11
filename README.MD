# Интерпретатор

[![Gradle CI](https://github.com/jvmusin/jb-interpreter/actions/workflows/gradle-build.yml/badge.svg)](https://github.com/jvmusin/jb-interpreter/actions/workflows/gradle-build.yml)

Проект является решением задания на стажировку в JetBrains 2021 на проект
["Inspections for new IDE platform SDK"](https://internship.jetbrains.com/projects/920/).

Запускается с помощью `./gradlew -q run --console=plain <<< "<your_program_here>"`. Также можно подсунуть файл или
какой-то поток, главное — закрыть ввод после его окончания. Вводить программу с клавиатуры не выйдет.

## Задание

Необходимо реализовать интерпретатор языка, описанного ниже. На стандартный вход подается программа на данном языке.
Необходимо вывести значение последнего выражения в программе в десятичной системе счисления

Язык задан следующей грамматикой:

```
<character>   ::= "A" | "B" | "C" | "D" | "E" | "F" | "G" | "H" | "I" | "J" | "K" | "L" | "M" | "N" | "O" | "P" | "Q" | "R" | "S" | "T" | "U" | "V" | "W" | "X" | "Y" | "Z" | "a" | "b" | "c" | "d" | "e" | "f" | "g" | "h" | "i" | "j" | "k" | "l" | "m" | "n" | "o" | "p" | "q" | "r" | "s" | "t" | "u" | "v" | "w" | "x" | "y" | "z" | "_"
<digit>       ::= "0" | "1" | "2" | "3" | "4" | "5" | "6" | "7" | "8" | "9"
<number>      ::= <digit> | <digit> <number>
<identifier>  ::= <character> | <identifier> <character>
<operation>   ::= "+" | "-" | "*" | "/" | "%" | ">" | "<" | "="

<constant-expression> ::= "-" <number> | <number>
<binary-expression>   ::= "(" <expression> <operation> <expression> ")"
<argument-list>       ::= <expression> | <expression> "," <argument-list>
<call-expression>     ::= <identifier> "(" <argument-list> ")"
<if-expression>       ::= "[" <expression> "]?(" <expression> "):(" <expression> ")"


<expression> ::= <identifier>
                  | <constant-expression>
                  | <binary-expression>
                  | <if-expression>
                  | <call-expression>

<parameter-list> ::= <identifier> | <identifier> "," <parameter-list>

<function-definition> ::= <identifier>"(" <parameter_list> ")" "={" <expression> "}"

<function-definition-list> : ""
                             | <function-definition> <EOL>
                             | <function-definition> <EOL> <function-definition-list>

<program> ::= <function-definition-list> <expression>
```

`<EOL>` - символ перевода строки - `\n`, программа не содержит других пробельных символов (пробел, табуляция, и т.д.);

Семантика языка задается следующим образом:

* Все переменные имеют тип `32-битный Integer`;
* Гарантируется, что вычисления не приводят к переполнению;
* Все арифметические операции аналогичны соответствующим операциям для `32-битного int` в языка `Java`;
* Операции сравнения возвращают `1` если сравнение истинно и `0` если ложно;
* `<if-expression>` исполняет второе выражение, если первое выражение не равно `0`; иначе исполняет третье;
* `<call-expression>` вызывает функцию с соответствующим именем
* Выражения вычисляются слева направо.

### Выполнение задания будет оцениваться по следующим пунктам:

#### Калькулятор:

На вход подается корректная программа без `<if-expression>`, у которой `<function-definition-list>` пустой.

Пример:

```
(2+2)
```

Ответ:

```
4
```

Пример:

```
(2+((3*4)/5))
```

Ответ:

```
4
```

#### Поддержка `<if-expression>`: в программе присутствуют `<if-expression>`

Пример:

```
[((10+20)>(20+10))]?(1):(0)
```

Ответ:

```
0
```

#### Поддержка функций: `<function-definition-list>` не пустой

Пример:

```
g(x)={(f(x)+f((x/2)))} f(x)={[(x>1)]?((f((x-1))+f((x-2)))):(x)}
g(10)
```

Ответ:

```
60
```

#### Обработка ошибок:

Если программа не соответствует грамматике необходимо вывести:

```
SYNTAX ERROR
```

Если в программе используется неопределенная переменная необходимо вывести:

```
PARAMETER NOT FOUND <name>:<line>
```

Здесь и далее `<name>` и `<line>` это ошибочное имя и номер строки на которой произошла ошибка.

Если программа вызывает функцию с неизвестным именем, то необходимо вывести:

```
FUNCTION NOT FOUND <name>:<line>
```

Если программа вызывает функцию с неверным числом аргументов, то необходимо вывести:

```
ARGUMENT NUMBER MISMATCH <name>:<line>
```

Если произошла ошибка выполнения необходимо, то вывести:

```
RUNTIME ERROR <expression>:<line>
```

`<expression>` - выражение в котором произошла ошибка.

Пример:

```
1 + 2 + 3 + 4 + 5
```

Ответ:

```
SYNTAX ERROR
```

Пример:

```
f(x)={y}
f(10)
```

Ответ:

```
PARAMETER NOT FOUND y:1
```

Пример:

```
g(x)={f(x)}
g(10)
```

Ответ:

```
FUNCTION NOT FOUND f:1
```

Пример:

```
g(x)={(x+1)}
g(10,20)
```

Ответ:

```
ARGUMENT NUMBER MISMATCH
g:2
```

Пример:

```
g(a,b)={(a/b)}
g(10,0)
```

Ответ:

```
RUNTIME ERROR (a/b):1
```

## Решение

Интерпретатор разделён на две основные части — Парсер и Интерпретатор.

### Парсер

Парсер и всё для него находится в пакете `jvmusin.interpreter.parser`.

Для большей части сущностей существует свой тип токена в этом пакете.

Токены описывают то, как соответствующие им сущности выглядят в грамматике и позволяют парсить их.

Для парсинга токенов используется очередь из символов
[SymbolQueue](src/main/kotlin/jvmusin/interpreter/parser/SymbolQueue.kt), которая позволяет считывать очередной символ,
если он подходит под некоторый предикат. Например, можно запросить символ, который является цифрой. Тогда, либо вернётся
следующий символ, если он является цифрой, либо вернётся `null`.

Сама очередь имеет очень ограниченный функционал, с ней удобнее всего работать через
[queue.readSafely](src/main/kotlin/jvmusin/interpreter/parser/SafeQueueReader.kt).

Если программу не удалось спарсить, то выбрасывается
[SyntaxError](src/main/kotlin/jvmusin/interpreter/parser/SyntaxError.kt).

После парсинга токенов, они переводятся в вид [Element](src/main/kotlin/jvmusin/interpreter/element/Element.kt), на
которых уже происходит валидация и выполнение программы.

### Интерпретатор

Интерпретатор находится в пакете `jvmusin.interpreter.element`.

Метод `Element.validate` позволяет проверять на корректность всю программу, чтобы не было вызовов неизвестных функций
или других логических ошибок при выполнении программы. Если они находятся, то выбрасывается
[ValidationError](src/main/kotlin/jvmusin/interpreter/element/InterpreterErrors.kt).

Метод `Element.invoke` позволяет запускать выполнение элемента. Он принимает на вход
[CallEnvironment](src/main/kotlin/jvmusin/interpreter/element/CallEnvironment.kt), который содержит все локальные
переменные и все известные функции. Чтобы отлавливать ошибки во время выполнения кода, этот метод должен
запускать `Element.invokeUnsafely`, который уже делает всю работу по запуску элемента. Вызов `Element.invokeUnsafely`
всегда оборачивается в `invokeSafely` или `invokeSafelyWithLineNumber` из файла
[ElementExtensions](src/main/kotlin/jvmusin/interpreter/element/ElementExtensions.kt). Эти методы позволяют запускать
код, который потенциально может словить исключение, так, чтобы это исключение не потерялось, а было возвращено
вызывающей стороне в отформатированном виде.

Элемент, отвечающий за всю программу целиком, лежит в
[ProgramElement](src/main/kotlin/jvmusin/interpreter/element/ProgramElement.kt). Соответственно, для запуска программы,
необходимо сначала спарсить её с помощью [ProgramToken](src/main/kotlin/jvmusin/interpreter/parser/ProgramToken.kt),
затем конвертировать его в `ProgramElement`, затем валидировать программу, и уже после этого запускать её, вызвав
метод `invoke`.

## Разное

В файле [IntegrationTests](src/test/kotlin/jvmusin/interpreter/IntegrationTests.kt) есть некоторые примеры программ,
такие как вычисление НОД `gcd`, вычисление НОК `lcm` и ещё некоторые.

**Поддерживается перегрузка методов**. Можно определить несколько методов с одним и тем же именем, но с разным числом
аргументов. Пример программы:

```
min(x,y)={[(x<y)]?(x):(y)}
min(x,y,z)={min(min(x,y),z)}
min(x,y,z,w)={min(min(x,y,z),w)}
min(6,2,5,3)
```

Ответ:

```
2
```

Валидатор также отлавливает ситуации, такие как:

* В параметрах функции есть несколько параметров с одинаковым именем — выбрасывается `ArgumentNamesNotDistinctError` с
  сообщением `ARGUMENT NAMES NOT DISTINCT <functionName>:<line>`;
* Существуют две функции с одинаковым именем и количеством аргументов — выбрасывается `FunctionsNotDistinctError` с
  сообщением `FUNCTIONS NOT DISTINCT <functionName>:<line>`.

## Хотелки

Хочется покрыть юнит-тестами пакет `jvmusin.interpreter.element`.
