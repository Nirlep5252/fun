# BreakingMath

BreakingMath is a simple interpreted programming language inspired by [Lox](https://github.com/munificent/craftinginterpreters).
Now, I know the name has no relation to the language, well that's because it is what it is bro. happens.

It supports:

- [x] Basic mathematical operations (`+`, `-`, `*`, `/`, `**`)
- [x] Builtin mathematical functions (`sin`, `cos`, `tan`, `log`)
- [x] Parenthesis
- [x] Unary operators (`-`)
- [x] Print/Show output (`show`)
- [x] Boolean values (`true`, `false`)
- [x] Comparison operators (`==`, `<`, `>`, `<=`, `>=`)
- [x] Logical operators (`and`, `or`)
- [x] Variables
- [x] Optional mutability of variables
- [x] Variable scoping
- [x] User defined functions
- [ ] `return` statements in functions
- [x] Control flow (`if`, `else`, `for`, `while`)
- [ ] `break` and `continue` statements
- [x] User input (`get`)
- [ ] Importing other files (`use`)
- [ ] Prepositional logic (premises, validation of statements, rules of inference, etc.) (real shit)

## How to run the interpreter?

- Install [Java 21](https://openjdk.org/projects/jdk/21/) or above.
- Get the `meth.jar` file from the [releases](https://github.com/Nirlep5252/BreakingMath/releases)
- Run the following command:

    ```bash
    java -jar meth.jar <path_to_your_code_file>
    ```

## Examples

1. Basic stuff

    ```python
    let a = 6;
    let b = 9;
    show a + b + a * b;

    show (2 + -3) * (5 + 4) ** 2; # u can use parenthesis or the `**` operator for exponentiation
    ```

    Output:

    ```text
    69
    -81
    ```

2. Boolean values and comparisons and logical operations

    ```python
    let a = 123;
    let b = 456;

    show a == b;
    show not (a == b); # there is no `!=`
    show a >= b;
    show a > b;
    show a <= b;
    show a < b;

    let x = true;
    let y = untrue; # same as false lol

    show x or y; # true
    show x and y; # false
    ```

    Output

    ```text
    false
    true
    false
    false
    true
    true
    true
    false
    ```

3. Variable scope

    ```python
    let a = 123;

    {
        let a = 456;
        show a; # 456
        let b = 69;
    }

    show a; # 123
    show b; # error! `b` is not defined
    ```

    Output

    ```text
    456
    123
    [line 10] ERROR: Variable `b` is not defined.
    ```

4. Variable mutability

    ```python
    let a = 123;
    let mut b = 456;

    b = b + 1;
    show b; # 457

    a = a + 1; # error! `a` is not mutable
    ```

    Output

    ```text
    457
    [line 7] ERROR: Variable `a` is not mutable.
    ```

5. Control flow

    ```python
    let a = 123;
    let b = 456;

    if a > b {
        show 1;
    } else {
        show 2;
    }

    let mut i = 0;
    while i <= 5 {
        show i;
        i = i + 1;
    }

    # for loop is just a while loop with a fancy syntax
    for i from 0 to 5 {
        show i;
    }

    # exponential for loop
    for i from 1 to 10000 by i {
        show i;
    }
    ```

    Output

    ```text
    2
    0
    1
    2
    3
    4
    5
    0
    1
    2
    3
    4
    5
    1
    2
    4
    8
    16
    32
    64
    128
    256
    512
    1024
    2048
    4096
    8192
    ```

6. User input

    ```python
    let a = get; # Gets a number from the user (value is NULL if user entered a non-number)
    show a;

    if a == NULL {
        show -1;
    } else {
        show 1;
    }
    ```

7. Functions

    ```python
    fn tower_of_hanoi (count, start, mid, end) {
        if count == 1 {
            show start, end;
        } else {
            tower_of_hanoi(count - 1, start, end, mid);
            tower_of_hanoi(1, start, mid, end);
            tower_of_hanoi(count - 1, mid, start, end);
        }
    }

    tower_of_hanoi(get, 1, 2, 3);
    ```

    Output

    ```text
    3 (input)
    1 3
    1 2
    3 2
    1 3
    2 1
    2 3
    1 3
    ```

## Language Grammar

- This is similar to [BNF](https://en.wikipedia.org/wiki/Backus%E2%80%93Naur_form)
- Also inspired from: [https://craftinginterpreters.com/representing-code.html](https://craftinginterpreters.com/representing-code.html)

```text
program -> declaration* EOF;
block -> "{" declaration* "}";
declaration -> functionDeclaration | variableDeclaration | statement;
statement -> expressionStatement | printStatement | ifStatement | whileStatement | forStatement | block;

functionDeclaration -> "fn" function ";";
function -> IDENTIFIER "(" parameters? ")" block;
variableDeclaration -> "let" ("mut")? IDENTIFIER "=" expression ";";
expressionStatement -> expression ";";
printStatement -> "print" expression ( "," expression )* ";";
ifStatement -> "if" expression statement ("else" statement)?;
whileStatement -> "while" expression statement;
forStatement -> "for" IDENTIFIER "from" expression "to" expression ("by" expression)? statement;

expression -> assignment;
assignment -> (IDENTIFIER "=" assignment) | logic_or;
logic_or -> logic_and ("or" logic_and)*;
logic_and -> equality ("and" equality)*;
equality -> comparison (("==") comparison)*;
comparison -> term ((">" | ">=" | "<" | "<=") term)*;
term -> factor (( "+" | "-" ) factor)*;
factor -> pow (( "/" | "*" ) pow)*;
pow -> unary ("**" unary)*;
unary -> (("-" | NOT) unary) | call;
call -> primary ("(" arguments? ")")*;
arguments -> expression ("," expression)*
primary -> NUMBER | "(" expression ")" | IDENTIFIER | TRUE | FALSE | GET | NULL;
```

## References

- Guide Followed: <https://craftinginterpreters.com/> OR <https://github.com/munificent/craftinginterpreters>
- Visitor Pattern: <https://en.wikipedia.org/wiki/Visitor_pattern>
- BNF: <https://en.wikipedia.org/wiki/Backus%E2%80%93Naur_form>
