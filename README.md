# Breaking Math

BreakingMath is a simple interpreted mathematical language. You can use it to evaluate mathematical expressions.

It supports:
- [x] Basic mathematical operations (+, -, *, /, **)
- [ ] Builtin mathematical functions (sin, cos, tan, log, sqrt)
- [x] Parenthesis
- [x] Unary operators (-)
- [x] Print/Show output (`show`)
- [ ] Boolean values (true, false)
- [ ] Comparison operators (==, !=, <, >, <=, >=)
- [ ] Logical operators (and, or) 
- [x] Variables 
- [ ] User defined functions 
- [ ] Control flow (if, else, while) 
- [ ] User input (`get`)

# References

- Guide: https://craftinginterpreters.com/
- Visitor Pattern: https://en.wikipedia.org/wiki/Visitor_pattern
- BNF: https://en.wikipedia.org/wiki/Backus%E2%80%93Naur_form

# Language Grammar

- This is similar to [BNF](https://en.wikipedia.org/wiki/Backus%E2%80%93Naur_form)
- Also inspired from: https://craftinginterpreters.com/representing-code.html

```text
program -> declaration* EOF;
declaration -> variableDeclaration | statement;
variableDeclaration -> "let" IDENTIFIER "=" expression ";";

statement -> expressionStatement | printStatement;
expressionStatement -> expression ";";
printStatement -> "print" expression ";";

expression -> term;
term -> factor (( "+" | "-" ) factor)*;
factor -> pow (( "/" | "*" ) pow)*;
pow -> unary ("**" unary)*;
unary -> ("-" unary) | primary;
primary -> NUMBER | "(" expression ")" | IDENTIFIER
```