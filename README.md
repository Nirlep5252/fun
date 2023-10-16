# Breaking Math

- A simple mathematical evaluator. (please give us marks)
- Please use IntelliJ while running this project (IDK java and how else to run this shit)

# References

- scanner: https://craftinginterpreters.com/scanning.html
- parser: https://craftinginterpreters.com/parsing-expressions.html

# Language Grammar

- This is similar to [BNF](https://en.wikipedia.org/wiki/Backus%E2%80%93Naur_form)
- Also inspired from: https://craftinginterpreters.com/representing-code.html

```text
expression -> term;
term -> factor (( "+" | "-" ) factor)*;
factor -> unary (( "/" | "*" ) unary)*;
unary -> ("-" unary) | primary;
primary -> NUMBER | "(" expression ")";
```