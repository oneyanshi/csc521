#python-quirk

>Note to self: PartialParser and PartialInterpreter need to be pushed and fix lexer.py/
Edit the README.md as you update the PartialParser.py and PartialInterpreter.py
When worked on, the names will be changed  

##How to Use:
Please download/clone the repository onto your desktop or laptop. From there, ensure you
are able to use Python 2.7+ (it is coded with 3.0 syntax). Within the terminal, you should
take your Quirk file and do the following:

`python lexer.py < app.q | python parser.py | python interpreter.py > output`

where `app.q` is your Quirk file. The output of the sequence should be the execution within app.q.

So if app.q's contents contain only the code `print 4` what should result is the number 4 being printed
on the terminal.

##lexer.py

##PartialParser.py

##PartialInterpreter.py
