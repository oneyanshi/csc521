import sys
import re

# keywords for the lexer
lexemes = ["var", "function", "return", "print"]


def SplitSourceByWhiteSpace(source):
    '''Splits the source by white space and then joins again'''
    allSplits = []
    for i in range(len(source)):
        thisSplit = source[i].split()
        allSplits += thisSplit
    finalString = " "
    # debugging
    # print finalString.join(allSplits)
    # returns a string of the joined splits by whitespace
    return finalString.join(allSplits)


def SplitSourceByRegex(source):
    '''Splits finalString from SplitSourceByWhiteSpace using a
    regex expression that captures any non-word character.'''
    allSplits = re.split('(\W)', source)
    # debugging
    # print(allSplits)
    return allSplits


def Tokenize(source):
    '''Tokenize() takes in the return value from SplitSourceByWhiteSpace
    and then iterates over each value to assign an appropriate token'''

    # Compile the regular expressions for use later
    literalNumber = re.compile(r"((\d+(\.\d*)?)|(\.\d+))")
    literalAlpha = re.compile(r"([a-zA-Z]+[a-zA-Z0-9_]*)")

    position = 0    # beginning of the index
    tokenList = []
    token = ""
    for i in range(len(source)):
        # keywords
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

        # arithmetic operations
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
        # misc
        if source[i] == "{":
            token = "LBRACE"
            tokenList.append(token)
        if source[i] == "}":
            token = "RBRACE"
            tokenList.append(token)
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
        # regex
        if literalNumber.match(source[i]):
            token = ("NUMBER:" + source[i])
            tokenList.append(token)
        if literalAlpha.match(source[i]) and source[i] not in lexemes:
            token = ("IDENT:" + source[i])
            tokenList.append(token)

    token = "EOF"
    # add EOF
    tokenList.append(token)
    # debugging purposes
    # print (tokenList)
    return tokenList

if __name__ == '__main__':
    tokens = Tokenize(SplitSourceByRegex(
                SplitSourceByWhiteSpace(sys.stdin.readlines())))
    for i in range(len(tokens)):
        sys.stdout.write(str(tokens[i]) + "\n")
