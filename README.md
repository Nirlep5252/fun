# Breaking Math

- A simple mathematical evaluator. (please give us marks)
- Please use IntelliJ while running this project (IDK java and how else to run this shit)

# References

- https://craftinginterpreters.com/scanning.html
- https://craftinginterpreters.com/representing-code.html

# Language Grammar

```text
expression -> literal | unary | binary | grouping;
literal -> NUMBER;
grouping -> "(" expression ")";
unary -> "-" expression;
binary -> expression operator expression;
operator -> "+" | "-" | "+" | "*" | "/";
```