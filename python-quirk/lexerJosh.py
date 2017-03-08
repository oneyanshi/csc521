import sys
import re

#for the lexer
lexemes = []

unvariedLexemes = ["var", "function", "return", "print",
            "=", "+", "-", "*", "/",
            "^", "(", ")", "{",
            "}", ",",":"]

def SplitSourceByWhitespace(source):
    allSplits = []
    for line in source:
        thisSplit = line.split()
        #for line.split():
        allSplits += thisSplit
    #print(allSplits)
    return allSplits


def SplitByUnvariedLexemes(source):
    i=0
    allSplits = []
    while i< len(source):
        line = source[i]
        unvariedLexemeFound = False;
        for lexeme in unvariedLexemes:
            if(-1 != line.find(lexeme)):
                unvariedLexemeFound = True;
                split = line.split(lexeme)
                allSplits += split
            if not unvariedLexemeFound:
                allSplits += line
        i += 1
    return allSplits


def Tokenize(source):
    '''Tokenize() takes in the return value from SplitbyUnvariedLexemes
    and then iterates over each value to assign an appropriate token'''

    #compile the regular expressions in order to use them later to
    #check for matching within the stream of lexemes
    literalNumber = re.compile(r"((\d+(\.\d*)?)|(\.\d+))")
    literalAlpha = re.compile(r"([a-zA-Z]+[a-zA-Z0-9_]*)")

    i = 0 #beginning of the index
    tokenList = []
    token = ""
    while i < len(source):
        #keywords
        if source[i] == "var":
            token = "VAR"
            tokenList.append(token)
        if source[i] == "function":
            token = "FUNCTION"
            tokenList.append(token)
        if source[i] == "return":
            token = "RETURN"
            tokenList.append(token)
        if source[i] == "print":
            token = "PRINT"
            tokenList.append(token)

        #arithmetic operations
        if source[i] == "=":
            token = "ASSIGN"
            tokenList.append(token)
        if source[i] == "+":
            token = "ADD"
            tokenList.append(token)
        if source[i] == "-":
            token = "SUB"
            tokenList.append(token)
        if source[i] == "*":
            token = "MULT"
            tokenList.append(token)
        if source[i] == "/":
            token = "DIV"
            tokenList.append(token)
        if source[i] == "^":
            token = "EXP"
            tokenList.append(token)
            token = ""
        #misc
        if source[i] == "(":
            token = "LPAREN"
            tokenList.append(token)
        if source[i] == ")":
            token = "RPAREN"
            tokenList.append(token)
        if source[i] == "{":
            token = "LBRACE"
            tokenList.append(source[i])
        if source[i] == "}":
            token = "RBRACE"
            tokenList.append(source[i])
        if source[i] == ",":
            token = "COMMA"
            tokenList.append(token)
        if source[i] == ":":
            token = "COLON"
            tokenList.append(token)

        #regex
        if literalNumber.match(source[i]):
            token = ("NUMBER:" + source[i])
            tokenList.append(token)
            token = ""
        if literalAlpha.match(source[i]) and source[i] not in lexemes:
            token = ("IDENT:" + source[i])
            tokenList.append(token)
            token = ""
        i += 1
    token = "EOF" #by this point, we should have iterated over the program
    tokenList.append(token)
    #for debugging purposes
    print (tokenList)
    return tokenList


if __name__ == '__main__':
    print ("starting __main__")
    Tokenize(SplitByUnvariedLexemes(SplitSourceByWhitespace(sys.stdin.readlines())))
