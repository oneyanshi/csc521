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
(defn CallBylabel [funLabel & args]
  (apply(ns-resolve 'clojure-quirk.core (symbol(name funLabel))) args)
  )
 

; <Program> --> <Statement> <Program> | <Statement> 
(defn Program [subtree scope] 
  (cond 
    ; Program0 
    (= :Program (first (third subtree))) 
    (CallByLabel (first (second subtree)) (second subtree) scope 
      (CallByLabel(first (third subtree)) (third subtree) scope)) 
    
    ; Program1
    :else 
    (CallByLabel (first (second subtree)) (second subtree) scope) 
    )
  )

; <Statement> -> <FunctionDeclaration> | <Assignment> | <Print> 
(defn Statement [subtree scope] 
  (CallByLabel (first (second subtree)) (second subtree) scope)
  )


; <FunctionDeclaration> -> FUNCTION <Name> PAREN <FunctionParams> LBRACE <FunctionBody> RBRACE

; <FunctionParams> -> <NameList> RPAREN | RPAREN 

; <FunctionBody> -> <Program> <Return> | <Return> 

; <Return> -> RETURN <ParameterList> 

; <Assignment> - <SingleAssignment> | <MultipleAssignment> 
(defn Assignment [subtree scope] 
  (CallByLabel (first (second subtree)) (second subtree) scope)
  
  )

; <SingleAssignment> -> VAR <Name> ASSIGN <Expression> 

; <MultipleAssignment> -> VAR <NameList> ASSIGN <FunctionCall>

; <Print> -> PRINT <Expression> 
(defn Print [subtree scope]
  (println(CallByLabel (first (second subtree)) (second subtree) scope))
  )

; <NameList> -> <Name> COMMA <NameList> | <Name> 

; <ParameterList> -> <Parameter COMMA <ParameterList> | <Parameter> 

; <Parameter> -> <Expression> | <Name> 
(defn Parameter [subtree scope] 
  (CallByLabel (first (second subtree)) (second subtree) scope)
  )


; <Expression> -> <Term> ADD <Expression> | <Term> SUB <Expression> | <Term> 
(defn Expression [subtree scope] 
	(cond
   (= 2 (count subtree))
   (CallByLabel (first (second subtree)) (second subtree) scope) 
   (= :Term (first (second subtree)))
		((if (.equals :ADD (first (third subtree)))
			(+ (CallByLabel (first (second subtree))(second subtree) scope)
			(CallByLabel (first (fourth subtree))(fourth subtree) scope)))
		(if (.equals :SUB (first (third subtree)))
			(- (CallByLabel (first (second subtree))(second subtree) scope)
			(CallByLabel (first (fourth subtree))(fourth subtree) scope))))	
	:else
	(CallByLabel (first (second subtree)) (second subtree) scope)
 )
)

; <Term> -> <Factor> MULT <Term> | <Factor> DIV <Term> | <Factor> 
(defn Term [subtree scope] 
  (cond 
    (= 2 (count subtree))
    (CallByLabel (first (second subtree)) (second subtree) scope) 
    (= :Factor (first (second subtree)))
    ((if (.equals :MULT (first (third subtree)))
       (* (CallByLabel (first (second subtree))(second subtree) scope)
          (CallByLabel (first (fourth subtree))(fourth subtree) scope)))
     (if(.equals :DIV (first (third subtree)))
       (- (CallByLabel (first(second subtree))(second subtree) scope)
          (CallByLabel (first (fourth subtree))(fourth subtree) scope))))   
    :else 
    (CallByLabel (first (second subtree)) (second subtree) scope)
    )
  ) 

; <Factor> -> <SubExpression> EXP <Factor> | <SubExpression> | <FunctionCall> | <Value> EXP <Factor> | <Value> 


; <FunctionCall> -> #<FunctionCall> ->  <Name> LPAREN <FunctionCallParams> COLON <Number> | <Name> LPAREN <FunctionCallParams>


; <FunctionCallParams> ->  <ParameterList> RPAREN | RPAREN


; <SubExpression> -> LPAREN <Expression> RPAREN


; <Value> -> <Name> | <Number>
(defn Value [subtree scope] 
  (CallByLabel (first (second subtree)) (second subtree) scope) 
  )


; <Name> -> IDENT | SUB IDENT | ADD IDENT


; <Number> -> NUMBER | SUB NUMBER | ADD NUMBER


   
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

 ; takes in input
 (def stdin (slurp *in*))
 
 ; is there a command for -pt? 
 (if (.equals"-pt"(first *command-line-args*))
   (def SHOW_PARSE_TREE true)
   )
 
 (def parse-tree(parser stdin))
 
 ; if SHOW_PARSE_TREE = true, then print the parse tree
 (if(= true SHOW_PARSE_TREE)
   (println parse-tree)
   ;(interpret-quirk parse-tree {}))
   )
 ) 



