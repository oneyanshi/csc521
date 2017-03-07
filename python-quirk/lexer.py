import sys
import re

#for the lexer

'''
prog = re.compile(pattern)
result = prog.match(string)
'''


lexemes = ["var", "function", "return", "print",
            "=", "+", "-", "*", "/",
            "^", "(", ")", "{",
            "}", ",",":"]

def SplitSourceByWhiteSpace(source):
    allSplits = []
    for i in range(len(source)):
        thisSplit = source[i].split()
        allSplits += thisSplit
    print(allSplits)
    return allSplits

def Tokenize(source):
    literalNumber = re.compile(r"((\d+(\.\d*)?)|(\.\d+))")
    literalAlpha = re.compile(r"([a-zA-Z]+[a-zA-Z0-9_]*)")
    position = 0
    tokenList = []
    token = ""
    for i in range(len(source)):
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
        if source[i] == ",":
            token = "COMMA"
            tokenList.append(token)
        if source[i] == ":":
            token = "COLON"
            tokenList.append(token)

        #regex
        if literalNumber.match(source[i]):
            token = ("NUMBER", source[i])
            tokenList.append(token)
        if literalAlpha.match(source[i]) and source[i] not in lexemes:
            token = ("IDENT", source[i])
            tokenList.append(token)
    print (tokenList)
    return tokenList

if __name__ == '__main__':
     print ("Starting __main__")
     SplitSourceByWhiteSpace(sys.stdin.readlines())
