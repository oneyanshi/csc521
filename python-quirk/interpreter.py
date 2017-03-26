
from __future__ import print_function
import sys
import pprint


pp = pprint.PrettyPrinter(indent=1, depth=100)

tree = ['Value0', ['Name1', 'SUB', 'IDENT:x']]
tree = ['FunctionDeclaration0',
    'FUNCTIION',
    ['Name0', 'IDENT:FooFunk'],
    'LPAREN',
    ['FunctionParams0',
        ['NameList0',
            ['Name0',"IDENT:a"],
            'COMMA',
            ['NameList1', ['Name0', "IDENT:b"]]],
        'RPAREN'],
    'LBRACE',
    ['FunctionBody1',
        ['Return0',
            ['NameList0',
                ['Name0',"IDENT:y"],
                'COMMA',
                ['NameList1', ['Name0', "IDENT:z"]]]

        ]],
    'RBRACE']

'''Scope example:
var x = 4 #{'x':4}
function foo(z) {
    foo(z/2)
    return z + x
}
foo(14)
Inside of foo(14), the scope looks like this:
{
 'z':7,
 "__parent__": {
    'z':14,
    "__parent__": {
        'x':4
    }
 }
}
'''

'''For testing function calls. The source:
FooFunk(x - 4, z)
'''
tree_with_function_call = ["FunctionCall0",
    ['Name0', "IDENT:FooFunk"],
    'LPAREN',
    ['FunctionCallParams0',
        ["ParameterList0",
            ["Parameter0",
                ['Expression1',
                   ['Term2', ['Factor4', ['Value0', ['Name0', 'IDENT:x']]]],
                   'SUB',
                   ['Expression2',
                        ['Term2', ['Factor4', ['Value1', ['Number0', 'NUMBER:4']]]]
                   ]
                ]
            ],
            'COMMA',
            ['ParameterList1',
                ['Parameter1', ['Name0', "IDENT:z"]]
            ]
        ],
        'RPAREN'
    ]
]

scope_with_function = {
        'x':100,
        'z':101,
        'FooFunk': [['a', 'b'],
             ['FunctionBody1',
              ['Return0',
               ['NameList0',
                ['Name0', 'IDENT:y'],
                'COMMA',
                ['NameList1', ['Name0', 'IDENT:z']]]]]]}

#start utilities
def eprint(msg):
	'''Prints to stderr.
	'''
	print(msg, file=sys.stderr)


def lookup_in_scope_stack(name, scope):
    '''Returns values (including declared functions!) from the scope.
    name - A string value holding the name of a bound variable or function.
    scope - The scope that holds names to value binding for variables and
        functions.
    returns - the value associated with the name in scope.
    '''
    #turn this on for better debugging
    eprint("lookup_in_scope_stack() "+ str(name))

    if name in scope:
        return scope[name]
    else:
        if "__parent__" in scope:
            eprint("not found in scope. Looking at __parent__")
            return lookup_in_scope_stack(name, scope["__parent__"])


def get_name_from_ident(tok):
    '''Returns the string lexeme associated with an IDENT token, tok.
    '''
    eprint("get_name_from_ident() " + tok)
    colon_index = tok.find(":")
    return tok[colon_index+1:]


def get_number_from_ident(tok):
    '''Returns the float lexeme associated with an NUMBER token, tok.
    '''
    eprint("get_number_from_ident() " + tok)
    colon_index = tok.find(":")
    return float(tok[colon_index+1:])


def func_by_name(*args):
    '''Calls a function whos name is given as a parameter. It requires the parse
        tree associated with that point in the grammar traversal and the current
        scope.
    *args is interpreted as
        name = args[0] -- the name of the function to call
        pt = args[1] -- the subtree of the parse tree associated with the name
        scope = args[2] -- the scope the subtree should use
    return - Pass through the return value of the called function.
    '''
    name = args[0]
    pt = args[1]
    scope = args[2]

    returnval = globals()[name](pt, scope)
    eprint("calfunc_by_name()) " + name + " " + str(returnval))
    return returnval
#end utilities


# <Program> -> <Statement> <Program> | <Statement>
def Program0(pt, scope):
    # <Statement> <Program>
    func_by_name(pt[1][0], pt[1], scope)
    func_by_name(pt[2][0], pt[2], scope)


def Program1(pt, scope):
    # <Statement>
    func_by_name(pt[1][0], pt[1], scope)


# <Statement> -> <FunctionDeclaration> | <Assignment> | <Print>
def Statement0(pt, scope):
    # <FunctionDeclaration>
    func_by_name(pt[1][0], pt[1], scope)

def Statement1(pt, scope):
    # <Assignment>
    func_by_name(pt[1][0], pt[1], scope)


def Statement2(pt, scope):
    # <Print>
    func_by_name(pt[1][0], pt[1], scope)


# <FunctionDeclaration> -> FUNCTION <Name> LPAREN <FunctionParams> LBRACE <FunctionBody> RBRACE
def FunctionDeclaration0(pt, scope):
    '''
    1. Get function name.
    2. Get names of parameters.
    3. Get reference to function body subtree.
    4. In scope, bind the function's name to the following list:
        "foo": [['p1', 'p2', 'p3'], [FunctionBodySubtree]]
        where foo is the function names, p1, p2, p2 are the parameters and
        FunctionBodySubtree represents the partial parse tree that holds the
        FunctionBody0 expansion. This would correspond to the following code:
        function foo(p1, p2, p3) { [the function body] }
    #Bonus: check for return value length at declaration time
    '''
    name = func_by_name(pt[2][0], pt[2], scope)[1]
    params = func_by_name(pt[4][0], pt[4], scope)
    scope[name] = [params, pt[6]]


# <FunctionParams> -> <NameList> RPAREN | RPAREN
# should return a list of values
def FunctionParams0(pt, scope):
    # <NameList> RPAREN
    return func_by_name(pt[1][0], pt[1], scope)


def FunctionParams1(pt, scope):
    # RPAREN
    return []


# <FunctionBody> -> <Program> <Return> | <Return>
def FunctionBody1(pt, scope):
    func_by_name(pt[1][0], pt[1], scope)
    func_by_name(pt[2][0], pt[2], scope)


# <Return> -> RETURN <ParameterList>
def Return0(pt, scope):
    return func_by_name(pt[2][0], pt[2], scope)


# <Assignment> -> <SingleAssignment> | <MultipleAssignment>
def Assignment0(pt, scope):
    # <SingleAssignment>
    func_by_name(pt[1][0], pt[1], scope)


def Assignment1(pt, scope):
    # <MultipleAssignment>
    func_by_name(pt[1][0], pt[1], scope)


# <SingleAssignment> -> VAR <Name> ASSIGN <Expression>
def SingleAssignment0(pt, scope):
    #1. Get name of the variable.
    #2. Get value of <Expression>
    #3. Bind name to value in scope.
    #Bonus: error if the name already exists in scope -- no rebinding
    name = func_by_name(pt[2][0], pt[2], scope)[1]
    scope[name] = func_by_name(pt[4][0], pt[4], scope)


# <MultipleAssignment> -> VAR <NameList> ASSIGN <FunctionCall>
def MultipleAssignment0(pt, scope):
    #1. Get list of variable names
    #2. Get the values returned from the fuction call
    #Bonus: error if any name already exists in scope -- no rebinding
    #Bonus: error if the number of variable names does not match the number of values
    name = [func_by_name(pt[2][0], pt[2], scope)[1]]
    scope[name] = func_by_name([4][0], pt[4], scope)


# <Print> -> PRINT <Expression>
def Print0(pt, scope):
	print(str(func_by_name(pt[2][0], pt[2], scope)))


# <NameList> -> <Name> COMMA <NameList> | <Name>
def NameList0(pt, scope):
    # <Name> COMMA <NameList>
    param_name = func_by_name(pt[1][0], pt[1], scope)[1]
    return [param_name] + func_by_name(pt[3][0], pt[3], scope)


def NameList1(pt, scope):
    # <Name>
    # getting the [1] of the return value for name as it returns a [val, name]
    return [func_by_name(pt[1][0], pt[1], scope)[1]]


# <ParameterList> -> <Parameter> COMMA <ParameterList> | <Parameter>
#should return a a list of values.
def ParameterList0(pt, scope):
    # <Parameter> COMMA <ParameterList>
    return func_by_name(pt[1][0], pt[1], scope) + func_by_name(pt[3][0], pt[3], scope)

def ParameterList1(pt, scope):
    # <Parameter>
    return func_by_name(pt[1][0], pt[1], scope)


# <Parameter> -> <Expression> | <Name>
def Parameter0(pt, scope):
    # <Expression>
    return func_by_name(pt[1][0], pt[1], scope)


def Parameter1(pt, scope):
    # <Name>
    # pull value out of [value,name]
    return func_by_name(pt[1][0], pt[1], scope)[0]


#<Expression> -> <Term> ADD <Expression> | <Term> SUB <Expression> | <Term>
def Expression0(pt, scope):
    #<Term> ADD <Expression>
    left_value = func_by_name(pt[1][0], pt[1], scope)
    right_value = func_by_name(pt[3][0], pt[3], scope)
    return left_value + right_value


def Expression1(pt, scope):
	#<Term> SUB <Expression>
	left_value = func_by_name(pt[1][0], pt[1], scope)
	right_value = func_by_name(pt[3][0], pt[3], scope)
	return left_value - right_value


def Expression2(pt, scope):
	#<Term>
	return func_by_name(pt[1][0], pt[1], scope)


#<Term> -> <Factor> MULT <Term> | <Factor> DIV <Term> | <Factor>
def Term0(pt, scope):
    # <Factor> MULT <Term>
    left_value = func_by_name(pt[1][0], pt[1], scope)
    right_value = func_by_name(pt[3][0], pt[3], scope)
    return left_value * right_value


def Term1(pt, scope):
    # <Factor> DIV <Term>
    left_value = func_by_name(pt[1][0], pt[1], scope)
    right_value = func_by_name(pt[3][0], pt[3], scope)
    return left_value / right_value


def Term2(pt, scope):
    # <Factor>
    return func_by_name(pt[1][0], pt[1], scope)


#<Factor> -> <SubExpression> EXP <Factor> | <SubExpression> | <FunctionCall> | <Value> EXP <Factor> | <Value>
def Factor0(pt, scope):
    # <SubExpression> EXP <Factor>
    left_value = func_by_name(pt[1][0], pt[1], scope)
    right_value = func_by_name(pt[3][0], pt[3], scope)
    return left_value ** right_value

def Factor1(pt, scope):
    # <SubExpression>
    return func_by_name(pt[1][0], pt[1], scope)


def Factor2(pt, scope):
    # <FunctionCall>
    return func_by_name(pt[1][0], pt[1], scope)


def Factor3(pt, scope):
    # <Value> EXP <Factor>
    left_value = func_by_name(pt[1][0], pt[1], scope)
    right_value = func_by_name(pt[3][0], pt[3], scope)
    return left_value ** right_value


def Factor4(pt, scope):
    # <Value>
    return func_by_name(pt[1][0], pt[1], scope)


#<FunctionCall> ->  <Name> LPAREN <FunctionCallParams> COLON <Number> | <Name> LPAREN <FunctionCallParams>
def FunctionCall0(pt, scope):
    # <Name> LPAREN <FunctionCallParams> COLON <Number>
    '''
    This is the most complex part of the interpreter as it involves executing a
    a partial parsetree that is not its direct child.
    1. Get the function name.
    2. Retrieve the stored function information from scope.
    3. Make a new scope with old scope as __parent__
    4. Get the list of parameter values.
    5. Bind parameter names to parameter values in the new function scope.
    6. Run the FunctionBody subtree that is part of the stored function information.
    7. Get the index return number.
    8. Return one value from the list of return values that corresponds to the index number.
    Bonus: Flag an error if the index value is greater than the number of values returned by the function body.
    '''
    name = func_by_name(pt[1][0], pt[1], scope)[1]
    return -1


def FunctionCall1(pt, scope):
    # <Name> LPAREN <FunctionCallParams>
    '''
    This is the most complex part of the interpreter as it involves executing a
    a partial parsetree that is not its direct child.
    1. Get the function name.
    2. Retrieve the stored function information from scope.
    3. Make a new scope with old scope as __parent__
    4. Get the list of parameter values.
    5. Bind parameter names to parameter values in the new function scope.
    6. Run the FunctionBody subtree that is part of the stored function information.
    7. Return the list of values generated by the <FunctionBody>
    '''
    # name = func_by_name(pt[1][0], pt[1], scope)[1]
    # pt = lookup_in_scope_stack(name, scope)[1]
    # scope["__parent__"] = scope.pop(name)
    # param_values = lookup_in_scope_stack(name, scope)[1]
    # print(param_values)
    #
    # pp.pprint(scope)
    return -1


#<FunctionCallParams> ->  <ParameterList> RPAREN | RPAREN
def FunctionCallParams0(pt, scope):
    # <ParameterList> RPAREN
    return func_by_name(pt[1][0], pt[1], scope)


def FunctionCallParams1(pt, scope):
    # RPAREN
    return[]


#<SubExpression> -> LPAREN <Expression> RPAREN
def SubExpression0(pt, scope):
    return func_by_name(pt[2][0], pt[2], scope)


#<Value> -> <Name> | <Number>
def Value0(pt, scope):
    # <Name>
	return func_by_name(pt[1][0], pt[1], scope)[0]


def Value1(pt, scope):
    # <Number>
	return func_by_name(pt[1][0], pt[1], scope)


#<Name> -> IDENT | SUB IDENT | ADD IDENT
def Name0(pt, scope):
    # IDENT
	name = get_name_from_ident(pt[1])
	return [lookup_in_scope_stack(name, scope), name]


def Name1(pt, scope):
    # SUB IDENT
	name = get_name_from_ident(pt[2])
	return [-lookup_in_scope_stack(name, scope), name]


def Name2(pt, scope):
    # ADD IDENT
	name = get_name_from_ident(pt[2])
	return [lookup_in_scope_stack(name, scope), name]


#<Number> -> NUMBER | SUB NUMBER | ADD NUMBER
def Number0(pt, scope):
    # NUMBER
	return get_number_from_ident(pt[1])


def Number1(pt, scope):
    # SUB NUMBER
	return -get_number_from_ident(pt[2])


def Number2(pt, scope):
    # ADD NUMBER
	return get_number_from_ident(pt[2])


#corresponds to: print 1 + 4 - 3
e1tree = ['Program1',
 ['Statement2',
  ['Print0',
   'PRINT',
   ['Expression0',
    ['Term2', ['Factor4', ['Value1', ['Number0', 'NUMBER:1']]]],
    'ADD',
    ['Expression1',
     ['Term2', ['Factor4', ['Value1', ['Number0', 'NUMBER:4']]]],
     'SUB',
     ['Expression2',
      ['Term2', ['Factor4', ['Value1', ['Number0', 'NUMBER:3']]]]]]]]]]

# corresponds to: print 6 / 2
e2tree = ["Program1",
            ["Statement2",
                ["Print0",
                "PRINT",
                ["Expression2",
                    ["Term1", ["Factor4", ["Value1", ["Number0", "NUMBER:6"]]],
                    "DIV",
                    ["Term2", ["Factor4", ["Value1", ["Number0", "NUMBER:2"]]]]]]]]]

# corresponds to print 4 ^ 2
e3tree = ["Program1",
            ["Statement2",
                ["Print0",
                "PRINT",
                ["Expression2",
                ["Term2", ["Factor3", ["Value1", ["Number0", "NUMBER:4"]],
                    "EXP",
                ["Factor4", ["Value1", ["Number0", "NUMBER:2"]]]]]]]]]

'''
function foo_func(){
  return 2 ^ 8
}

print foo_func()

out: 256.0
'''

e5tree = ['Program0',
 ['Statement0',
  ['FunctionDeclaration0',
   'FUNCTION',
   ['Name0', 'IDENT:foo_func'],
   'LPAREN',
   ['FunctionParams1', 'RPAREN'],
   'LBRACE',
   ['FunctionBody1',
    ['Return0',
     'RETURN',
     ['ParameterList1',
      ['Parameter0',
       ['Expression2',
        ['Term2',
         ['Factor3',
          ['Value1', ['Number0', 'NUMBER:2']],
          'EXP',
          ['Factor4', ['Value1', ['Number0', 'NUMBER:8']]]]]]]]]],
   'RBRACE']],
 ['Program1',
  ['Statement2',
   ['Print0',
    'PRINT',
    ['Expression2',
     ['Term2',
      ['Factor2',
       ['FunctionCall1',
        ['Name0', 'IDENT:foo_func'],
        'LPAREN',
        ['FunctionCallParams1', 'RPAREN']]]]]]]]]

''' var x = (5 * 2) / 5
    print x
    out: 2.0'''
e6tree = ['Program0',
 ['Statement1',
  ['Assignment0',
   ['SingleAssignment0',
    'VAR',
    ['Name0', 'IDENT:x'],
    'ASSIGN',
    ['Expression2',
     ['Term1',
      ['Factor1',
       ['SubExpression0',
        'LPAREN',
        ['Expression2',
         ['Term0',
          ['Factor4', ['Value1', ['Number0', 'NUMBER:5']]],
          'MULT',
          ['Term2', ['Factor4', ['Value1', ['Number0', 'NUMBER:2']]]]]],
        'RPAREN']],
      'DIV',
      ['Term2', ['Factor4', ['Value1', ['Number0', 'NUMBER:5']]]]]]]]],
 ['Program1',
  ['Statement2',
   ['Print0',
    'PRINT',
    ['Expression2',
     ['Term2', ['Factor4', ['Value0', ['Name0', 'IDENT:x']]]]]]]]]


example4 = ['Program0',
 ['Statement0',
  ['FunctionDeclaration0',
   'FUNCTION',
   ['Name0', 'IDENT:baz_func'],
   'LPAREN',
   ['FunctionParams0',
    ['NameList0',
     ['Name0', 'IDENT:a'],
     'COMMA',
     ['NameList1', ['Name0', 'IDENT:b']]],
    'RPAREN'],
   'LBRACE',
   ['FunctionBody0',
    ['Program0',
     ['Statement1',
      ['Assignment0',
       ['SingleAssignment0',
        'VAR',
        ['Name0', 'IDENT:y'],
        'ASSIGN',
        ['Expression1',
         ['Term2', ['Factor4', ['Value0', ['Name0', 'IDENT:a']]]],
         'SUB',
         ['Expression2',
          ['Term2',
           ['Factor4', ['Value0', ['Name2', 'ADD', 'IDENT:b']]]]]]]]],
     ['Program1',
      ['Statement1',
       ['Assignment0',
        ['SingleAssignment0',
         'VAR',
         ['Name0', 'IDENT:z'],
         'ASSIGN',
         ['Expression1',
          ['Term2', ['Factor4', ['Value0', ['Name0', 'IDENT:a']]]],
          'SUB',
          ['Expression2',
           ['Term2',
            ['Factor4', ['Value0', ['Name1', 'SUB', 'IDENT:b']]]]]]]]]]],
    ['Return0',
     'RETURN',
     ['ParameterList0',
      ['Parameter0',
       ['Expression2',
        ['Term2', ['Factor4', ['Value0', ['Name0', 'IDENT:y']]]]]],
      'COMMA',
      ['ParameterList1',
       ['Parameter0',
        ['Expression2',
         ['Term2', ['Factor4', ['Value0', ['Name0', 'IDENT:z']]]]]]]]]],
   'RBRACE']],
 ['Program0',
  ['Statement1',
   ['Assignment1',
    ['MultipleAssignment0',
     'VAR',
     ['NameList0',
      ['Name0', 'IDENT:v'],
      'COMMA',
      ['NameList1', ['Name0', 'IDENT:w']]],
     'ASSIGN',
     ['FunctionCall1',
      ['Name0', 'IDENT:baz_func'],
      'LPAREN',
      ['FunctionCallParams0',
       ['ParameterList0',
        ['Parameter0',
         ['Expression2',
          ['Term2',
           ['Factor4', ['Value1', ['Number1', 'SUB', 'NUMBER:5']]]]]],
        'COMMA',
        ['ParameterList1',
         ['Parameter0',
          ['Expression2',
           ['Term2',
            ['Factor4', ['Value1', ['Number2', 'ADD', 'NUMBER:2']]]]]]]],
       'RPAREN']]]]],
  ['Program0',
   ['Statement2',
    ['Print0',
     'PRINT',
     ['Expression2',
      ['Term2', ['Factor4', ['Value0', ['Name0', 'IDENT:v']]]]]]],
   ['Program1',
    ['Statement2',
     ['Print0',
      'PRINT',
      ['Expression2',
       ['Term2', ['Factor4', ['Value0', ['Name0', 'IDENT:w']]]]]]]]]]]
if __name__ == '__main__':
    # choose a parse tree and initial scope
    tree = e5tree
    scope = {}
    # execute the program starting at the top of the tree
    func_by_name(tree[0], tree, scope)
    # Uncomment to see the final scope after the program has executed.
    # pp.pprint(scope)
