# Breaking Math

- A simple mathematical evaluator. (please give us marks)
- Please use IntelliJ while running this project (IDK java and how else to run this shit)

# References

- Guide: https://craftinginterpreters.com/
- Visitor Pattern: https://en.wikipedia.org/wiki/Visitor_pattern
- BNF: https://en.wikipedia.org/wiki/Backus%E2%80%93Naur_form

# Language Grammar

- This is similar to [BNF](https://en.wikipedia.org/wiki/Backus%E2%80%93Naur_form)
- Also inspired from: https://craftinginterpreters.com/representing-code.html

```text
program -> statement* EOF;
statement -> expressionStatement | printStatement;
expressionStatement -> expression ";";
printStatement -> "print" expression ";";

expression -> term;
term -> factor (( "+" | "-" ) factor)*;
factor -> unary (( "/" | "*" ) unary)*;
unary -> ("-" unary) | primary;
primary -> NUMBER | "(" expression ")";
```