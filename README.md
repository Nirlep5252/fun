# Breaking Math

BreakingMath is a simple interpreted programming language.
You can use it to evaluate mathematical expressions (cuz it only has number datatype lol).

It supports:
- [x] Basic mathematical operations (`+`, `-`, `*`, `/`, `**`)
- [ ] Builtin mathematical functions (`sin`, `cos`, `tan`, `log`, `sqrt`)
- [x] Parenthesis
- [x] Unary operators (`-`)
- [x] Print/Show output (`show`)
- [x] Boolean values (`true`, `false`)
- [x] Comparison operators (`==`, `!=`, `<`, `>`, `<=`, `>=`)
- [ ] Logical operators (`and`, `or`)
- [x] Variables 
- [x] Optional mutability of variables
- [x] Variable scoping
- [ ] User defined functions 
- [ ] Control flow (`if`, `else`, `loop`, `break`)
- [ ] User input (`get`)
- [ ] Prepositional logic (premises, validation of statements, rules of inference, etc.)

# References

- Guide: https://craftinginterpreters.com/
- Visitor Pattern: https://en.wikipedia.org/wiki/Visitor_pattern
- BNF: https://en.wikipedia.org/wiki/Backus%E2%80%93Naur_form

# Example program:

```
let a = 6;
let b = 9;

show a + b + a * b; # prints 69 (nice)

let mut x = 1;
x = x + 1;
show x; # prints 2

let y = 1;
y = y + 1; # error! y is not mutable
show y;
```

# Language Grammar

- This is similar to [BNF](https://en.wikipedia.org/wiki/Backus%E2%80%93Naur_form)
- Also inspired from: https://craftinginterpreters.com/representing-code.html

```text
program -> declaration* EOF;
block -> "{" declaration* "}";
declaration -> variableDeclaration | statement;
statement -> expressionStatement | printStatement | ifStatement | block;

variableDeclaration -> "let" ("mut")? IDENTIFIER "=" expression ";";
expressionStatement -> expression ";";
printStatement -> "print" expression ";";
ifStatement -> "if" expression statement ("else" statement)?;

expression -> assignment;
assignment -> (IDENTIFIER "=" assignment) | equality;
equality -> comparison (("==") comparison)*;
comparison -> term ((">" | ">=" | "<" | "<=") term)*;
term -> factor (( "+" | "-" ) factor)*;
factor -> pow (( "/" | "*" ) pow)*;
pow -> unary ("**" unary)*;
unary -> (("-" | NOT) unary) | primary;
primary -> NUMBER | "(" expression ")" | IDENTIFIER | TRUE | FALSE;
```