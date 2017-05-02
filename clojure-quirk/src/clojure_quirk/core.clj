(ns clojure-quirk.core
  (:require [instaparse.core :as insta]))

; begin utility functions 
(defn third [aList] (nth aList 2))
(defn fourth [aList] (nth aList 3))
(defn fifth [aList] (nth aList 4)) 
(defn sixth [aList] (nth aList 5))

; calling the functions by the label :Statement
(defn CallByLabel [funLabel & args]
  (apply(ns-resolve 'clojure-quirk.core (symbol(name funLabel))) args)
  )

(defn ret-print [thingToPrint] 
  (println thingToPrint)
  thingToPrint
  )
; end utility functions 

; <Program> --> <Statement> <Program> | <Statement> 
(defn Program [subtree scope] 
  (println "Program")
  (println subtree)
  (cond
    ;Program0
    (= :Program (first (third subtree)))
    ((CallByLabel (first (second subtree)) (second subtree) scope)
      (CallByLabel (first (third subtree)) (third subtree) scope))
    
    ;Program1
    :else
    (CallByLabel (first (second subtree)) (second subtree) scope)
    )
  )

; <Statement> -> <FunctionDeclaration> | <Assignment> | <Print> 
(defn Statement [subtree scope] 
  (println "Statement")
  (println subtree)
  ; Statement0 and Statement1 and Statement2
  (CallByLabel (first (second subtree)) (second subtree) scope)
  )


; <FunctionDeclaration> -> FUNCTION <Name> PAREN <FunctionParams> LBRACE <FunctionBody> RBRACE

; <FunctionParams> -> <NameList> RPAREN | RPAREN 

; <FunctionBody> -> <Program> <Return> | <Return> 

; <Return> -> RETURN <ParameterList> 

; <Assignment> - <SingleAssignment> | <MultipleAssignment> 
(defn Assignment [subtree scope] 
  ; Assignment0 and Assignment1
  (CallByLabel (first (second subtree)) (second subtree) scope)
  )
  

; <SingleAssignment> -> VAR <Name> ASSIGN <Expression> 
;(defn SingleAssignment [subtree scope] 
;  ; SingleAssignment0 
;  ; 1. Get the name of the variable 
;  ; 2. Get the value of the <Expression> 
;  ; 3. Bind name to value in scope. 
;  
;  
;  )

; <MultipleAssignment> -> VAR <NameList> ASSIGN <FunctionCall>

; <Print> -> PRINT <Expression> 
(defn Print [subtree scope]
  (ret-print(CallByLabel (first (second subtree)) (second subtree) scope))
  )

; <NameList> -> <Name> COMMA <NameList> | <Name> 

; <ParameterList> -> <Parameter COMMA <ParameterList> | <Parameter> 

; <Parameter> -> <Expression> | <Name> 
(defn Parameter [subtree scope] 
  (CallByLabel (first (second subtree)) (second subtree) scope)
  )


; <Expression> -> <Term> ADD <Expression> | <Term> SUB <Expression> | <Term> 
(defn Expression [subtree scope]
	(println "Expression")
	;(println  (count subtree))
	
	(cond (= 2 (count subtree))
       (CallByLabel (first (second subtree))(second subtree) scope)
       (= :ADD (first (third subtree)))
       (+ (CallByLabel (first (second subtree))(second subtree) scope)
          (CallByLabel (first (fourth subtree))(fourth subtree) scope))
       (= :SUB (first (third subtree)))
       (- (CallByLabel (first (second subtree))(second subtree) scope)
          (CallByLabel (first (fourth subtree))(fourth subtree) scope)))
 	
 ;(println "Done Expression")
			
)

; <Term> -> <Factor> MULT <Term> | <Factor> DIV <Term> | <Factor> 
(defn Term [subtree scope]
  (println "Term") 
  ;(println (count subtree)) 
  
  (cond (= 2 (count subtree)) 
        (CallByLabel (first (second subtree))(second subtree) scope) 
        (= :MULT (first (third subtree))) 
        (* (CallByLabel (first (second subtree))(second subtree) scope) 
           (CallByLabel (first (fourth subtree))(fourth subtree) scope))
        (= :DIV (first (third subtree)))
        (/ (CallByLabel (first (second subtree))(second subtree) scope) 
           (CallByLabel(first (fourth subtree))(fourth subtree) scope)))
  ;(println "Done Term); 
  ) 

; <Factor> -> <SubExpression> EXP <Factor> | <SubExpression> | <FunctionCall> | <Value> EXP <Factor> | <Value> 


; <FunctionCall> -> #<FunctionCall> ->  <Name> LPAREN <FunctionCallParams> COLON <Number> | <Name> LPAREN <FunctionCallParams>


; <FunctionCallParams> ->  <ParameterList> RPAREN | RPAREN


; <SubExpression> -> LPAREN <Expression> RPAREN


; <Value> -> <Name> | <Number>
(defn Value [subtree scope] 
  ; Value0 and Value1
  (CallByLabel (first (second subtree)) (second subtree) scope) 
  )


; <Name> -> IDENT | SUB IDENT | ADD IDENT


; <Number> -> NUMBER | SUB NUMBER | ADD NUMBER
(defn MyNumber [subtree scope] 
  )

   
; function for interpretation 
(defn interpret-quirk [subtree scope] 
  (CallByLabel (first subtree) subtree {})
  )
; end breakdown 

; our cool parser 
 (def parser 
  (insta/parser (slurp "resources/quirk-ebnf-grammar.ebnf")
    :auto-whitespace :standard)
  )

; this is our main  
(defn -main [& args]

 ; takes in input
; (def stdin (slurp *in*))
 
 ; is there a command for -pt? 
 (if (.equals"-pt"(first *command-line-args*))
   (def SHOW_PARSE_TREE true)
   )
 
 (def parse-tree (parser "print 1 + 4 - 3"))
 (def interpreted (interpret-quirk parse-tree {}))
 
 ; if SHOW_PARSE_TREE = true, then print the parse tree
 (if(= true SHOW_PARSE_TREE)
   (println parse-tree)
   )
    (println interpreted)
 ) 



