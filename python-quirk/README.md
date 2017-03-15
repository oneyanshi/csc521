# python-quirk

## General List
- [ ] get started on interpreter

## Navigation
* [How to Use](#how-to-use)
* [Lexer](#lexerpy)
* [Parser](#parserpy)
* [Interpreter](#interpreterpy)
* [Resources](#resources)

## How to Use:
Please download/clone the repository onto your desktop or laptop. From there, ensure you
are able to use Python 2.7+ (it is coded with 3.0 syntax). Within the terminal, you should
take your Quirk file and do the following:

`python lexer.py < app.q | python parser.py | python interpreter.py > output`

where `app.q` is your Quirk file.

If app.q's contents contain only the code `print 4` what should result is the number 4 being printed
on the terminal. More on specific outputs will be available in the sections below.

If you would like to test the lexer on its own, please uncomment any debugging print lines within the code and type the following within the terminal:

`python lexer.py < app.q`

The parser and the interpreter require inputs of some sort, which are passed in from the lexer.

If you would prefer to check the functionality of them separate from one another, then uncomment the lines that contain either the tokens (for the parser) or the tree (for the interpreter) to use for debugging/testing.

## lexer.py

### Lexer goals
The goal of the lexer is to tokenize the contents of the Quirk file that is passed into it. I utilized the tokens that were defined for us, which can be found in the folder `grammar-rules`.

### Function explanation
First, we define keywords for the lexer to look out for: `var`, `function`, `return`, and `print`.

There are three functions within the program: `SplitSourceByWhiteSpace()`, `SplitSourceByRegex()`, and `Tokenize()`. Each function has a temporary variable in which can be passed in.

The Quirk file's contents will be passed into `SplitSourceByWhiteSpace()` first, where the contents of the file are split based on whitespace, eliminating any unneeded spaces and tabs that we can't properly read. Within the same function, I then utilized Python's `join()` method to make the text a single string, which will be returned and used as an input for `SplitSourceByRegex()`.

`SplitSourceByRegex()` takes in the return value from `SplitSourceByWhiteSpace()` and splits the text into `allSplits`, a list that contains what is within the source Quirk file. This then returns a list that is used in `Tokenize()`, which tokenizes the lexemes that are passed in and thus checks and replaces for any specific keywords.

`Tokenize()` utilizes regex in order to capture all alphabetical and numerical characters that it would otherwise miss--we also account for the keywords here by checking to make sure that a keyword like `var` is not counted as an `IDENT` but instead `VAR`.

### Side Note
I realize that there is probably a better way of solving the problem of characters that were stuck together: how do we properly read and parse through a Quirk file if there are no whitespaces? For example:

```
function SquareDistance(x1, y1, x2, y2) {
  return x1 ^ x2 + y1 ^ y2
  }
var distance = SquareDistance(2, 3, 5, 6)
```

That would be difficult to just split via whitespace as there are places in the code (expectedly) that do not contain whitespaces but needs to be separated from what it is ATTACHED to. We thus need to split the source by white space and then join the text before splitting again based on non-word characters using regular expressions.

### Outcome
An example of what the lexer should be doing:

Input: `print 4`

Output: `["PRINT", "NUMBER:4"]`

This standard output then becomes the standard input for the parser.


## parser.py

### Parser Goals
The parser takes in a sequence of tokens (from the lexer) and then produces a parse tree based on the given grammar rules. The parser is a recursive descent parser.

The parser's standard input takes in a stream of tokens from the lexer (I have left within the program test cases/test tokens for debugging purposes). The parser is responsible for building a parser tree via an algorithm that works iteratively. In other words, functions will call on other functions within itself, thus cutting back on work and code. Thanks to Professor Josh McCoy's base, the parser is relatively easy to understand.

The tree is built based on Quirk's grammar. Each function represents a grammar rule. For example:

`<Program> -> <Statement> <Program> | <Statement>`

is its own function, as seen within line 40 of the code. We are then able to break it down and call upon the Statement() function within the Program() function and so on. In each rule, we test for the success of the query and then continue to move down the tree based upon the functions called.

If we continued, `<Statement>` would result in the Statement() function being called, which thus breaks off into three possibilities: `<FunctionDeclaration>`, `Assignment`, or `Print`. We would work from the bottom up before terminating once we reached the EOF statement in the token stream.

The same pattern emerges for the other grammar rules that are then broken down. A look at the code will reveal said pattern.

### Return values
Each function will return a boolean value that is `True` if the subtree corresponds to the grammar found and `False` if it isn't. The function will also return the position into the list of tokens where the last grammar function left off. And, third, the function will return the parse tree that was generated by the grammar function. It is overall appended to the "larger" tree.

### Outcome
The parser should output a parse tree, whose integrity is preserved by using json capabilities. This output will become the input for the interpreter.

Continuing with the same example as above, what should be provided as the parse tree is the following:

```
[True, 2,
  [u'Program1',
    [u'Statement2',
      [u'Print0', u'PRINT',
        [u'Expression2',
          [u'Term2',
            [u'Factor4',
              [u'Value1',
                [u'Number0', u'NUMBER:4']]]]]]]]]
```

(Formatted on the README for readability, what should actually come out of the parser is what follows below.)

```
[True, 2, [u'Program1', [u'Statement2', [u'Print0', u'PRINT', [u'Expression2', [u'Term2', [u'Factor4', [u'Value1', [u'Number0', u'NUMBER:4']]]]]]]]]
```


## interpreter.py
### TODO
- [ ] literally everything

The interpreter takes in the parser's output, the tree, and then executes the code. This is where the code `print 4` would actually be executed after being ran through the lexer and the parser. The output of the interpreter should be the code's output.

## Resources
[PEP8 Checker](http://pep8online.com/)
