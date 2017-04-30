(ns clojure-quirk.core
  (:require [instaparse.core :as insta]))

; the goal is to do lein run -pt < simple.q 
; the goal is to do lein run < simple.q 
; sample for reading all stdin 
; (def stdin (slurp *in*)) 

; begin utility functions 
(defn third [aList] (nth aList 2))
(defn fourth [aList] (nth aList 3))
(defn fifth [aList] (nth aList 4)) 
(defn sixth [aList] (nth aList 5))

; calling the functions by the label :Statement
(defn CallbyLabel [funLabel & args]
  (apply(resolve(symbol(name funLabel))) args))
 
; end utility functions 

; <Program> --> <Statement> <Program> | <Statement> 
(defn Program [subtree scope]
  (cond
    (= :Program (first(third subtree)))
    ((CallByLabel (first(second subtree)) (second subtree) (scope)
                  (CallByLabel (first(third subtree)) (third subtree) scope))
      :else
      (CallByLabel (first(second subtree)) (second subtree) scope)
      )
    )
  )

; <Statement> -> <FunctionDeclaration> | <Assignment> | <Print> 


; <FunctionDeclaration> -> FUNCTION <Name> PAREN <FunctionParams> LBRACE <FunctionBody> RBRACE


; <FunctionParams> -> <NameList> RPAREN | RPAREN 

; <FunctionBody> -> <Program> <Return> | <Return> 

; <Return> -> RETURN <ParameterList> 

; <Assignment> - <SingleAssignment> | <MultipleAssignment> 

; <SingleAssignment> -> VAR <Name> ASSIGN <Expression> 

; <MultipleAssignment> -> VAR <NameList> ASSIGN <FunctionCall>

; <Print> -> PRINT <Expression> 

; <NameList> -> <Name> COMMA <NameList> | <Name> 

; <ParameterList> -> <Parameter COMMA <ParameterList> | <Parameter> 

; <Parameter> -> <Expression> | <Name> 

; <Expression> -> <Term> ADD <Expression> | <Term> SUB <Expression> | <Term> 

; <Term> -> <Factor> MULT <Term> | <Factor> DIV <Term> | <Factor> 

; <Factor> -> <SubExpression> EXP <Factor> | <SubExpression> | <FunctionCall> | <Value> EXP <Factor> | <Value> 

; <FunctionCall> -> #<FunctionCall> ->  <Name> LPAREN <FunctionCallParams> COLON <Number> | <Name> LPAREN <FunctionCallParams>

; <FunctionCallParams> ->  <ParameterList> RPAREN | RPAREN


; <SubExpression> -> LPAREN <Expression> RPAREN

; <Value> -> <Name> | <Number>

; <Name> -> IDENT | SUB IDENT | ADD IDENT

; <Number> -> NUMBER | SUB NUMBER | ADD NUMBER


(defn Number [subtree scope]
  (cond
    (= :Number(
      ) 
    )
  
    )
  )

; returns a map with local variable bindings when calling a function 
; needs to have a list of values 
(def VarBindLoop [paramList valueList] 
  (if(= 1 (count paramList))
    (assoc {} scope (first paramList) (first valueLIst))
    (merge ( assoc {} (first paramlist) (first valuelist)) 
      (VarBindLoop 
        (rest paramList) 
        (rest valueList) 
        )
      )
    )
 )
   


; function for interpretation 
(defn interpret-quirk [subtree scope] (CallByLabel (first subtree) subtree {}))


; end breakdown 

; our cool parser 
 (def parser 
  (insta/parser (slurp "resources/quirk-ebnf-grammar.ebnf")
    :auto-whitespace :standard)
  )



; this is our main  
(defn -main [& args]
 ;(println(first *command-line-args*))
 
 ; takes in input
 (def stdin (slurp *in*))
 
 ; is there a command for -pt? 
 (if (.equals"-pt"(first *command-line-args*))
   (def SHOW_PARSE_TREE true)
   )
 
 (def parse-tree(parser stdin))
 
 ; if SHOW_PARSE_TREE = true, then print the parse tree
 (if(= true SHOW_PARSE_TREE)
   (println parse-tree))
)

;(println(parser "var x = (5 * 2) / 5 print x"))


