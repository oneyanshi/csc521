#python-quirk

##General List
- [ ] heavy lifting for parsing
- [ ] also for interpreter
- [ ] update readme.md as you go and w/ explanations of functions 
- [ ] at least three test cases
- [x] rename files if needed
- [x] keep only necessary files within repository
- [x] add quirk-grammar folder later for easier reference

##How to Use:
Please download/clone the repository onto your desktop or laptop. From there, ensure you
are able to use Python 2.7+ (it is coded with 3.0 syntax). Within the terminal, you should
take your Quirk file and do the following:

`python lexer.py < app.q | python parser.py | python interpreter.py > output`

where `app.q` is your Quirk file. The output of the sequence should be the execution within app.q.

So if app.q's contents contain only the code `print 4` what should result is the number 4 being printed
on the terminal.

##lexer.py
### Lexer Grocery List
- [ ] add in functionality / ability to split by lexemes
- [ ] try at least three test cases

lexer.py reads in a Quirk file, splits the text by white space before splitting once
more to ensure that lexemes are not stuck together. It is then tokenized based upon the Quirk tokens.

The output for the lexer.py will be a fully tokenized version of the program in the Quirk file. The output will then be used in the parser discussed below.

An example of what lexer.py should be doing:

Input: `var q = 2`

Output: `["VAR", "IDENT:q", "ASSIGN", "NUMBER:2"]`

##parser.py



##interpreter.py
