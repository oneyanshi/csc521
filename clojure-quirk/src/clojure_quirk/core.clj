(ns clojure-quirk.core
  (:require [instaparse.core :as insta])
  )

; begin utility functions 
(defn third [aList] (nth aList 2))
(defn fourth [aList] (nth aList 3))
(defn fifth [aList] (nth aList 4)) 
(defn sixth [aList] (nth aList 5))

; calling the functions by the label :Statement
(defn CallByLabel [funLabel & args]
  (apply(ns-resolve 'clojure-quirk.core (symbol(name funLabel))) args)
  )

; for printing
(defn ret-print [thingToPrint] 
  (println thingToPrint)
  thingToPrint
  )

; exponents 
(defn ** [base power]
  (reduce * (repeat base power)))

; end utility functions 

; <Program> --> <Statement> <Program> | <Statement> 
(defn Program [subtree scope] 
  (println "Program")
  ;(println subtree)
  ;(println (count subtree))
  
  (cond(= 2 (count subtree))       
    ; Program1
    (CallByLabel (first (second subtree)) (second subtree) scope)
    
    ; Program0
    (= :Program (first (third subtree)))
    ((CallByLabel (first (second subtree)) (second subtree) scope)
      (CallByLabel (first (third subtree)) (third subtree) scope))
    )
  )

; <Statement> -> <FunctionDeclaration> | <Assignment> | <Print> 
(defn Statement [subtree scope] 
  (println "Statement")
  ;(println subtree)
  ;(println (count subtree))
  (CallByLabel (first (second subtree)) (second subtree) scope)
  )


; <FunctionDeclaration> -> FUNCTION <Name> PAREN <FunctionParams> LBRACE <FunctionBody> RBRACE

; <FunctionParams> -> <NameList> RPAREN | RPAREN 

; <FunctionBody> -> <Program> <Return> | <Return> 

; <Return> -> RETURN <ParameterList> 

; <Assignment> - <SingleAssignment> | <MultipleAssignment> 
(defn Assignment [subtree scope]
  (println "Assignment")
  ;(println subtree)
  ;(println (count subtree))
  ; Assignment0 and Assignment1
  (CallByLabel (first (second subtree)) (second subtree) scope)
  )
  

 <SingleAssignment> -> VAR <Name> ASSIGN <Expression> 
(defn SingleAssignment [subtree scope] 
  (println "SingleaAssignment")
  ;(println subtree)
  ;(println (count subtree))
  
  ; SingleAssignment0 
  ; 1. Get the name of the variable 
  ; 2. Get the value of the <Expression> 
  ; 3. Bind name to value in scope. 
  
  
  )

; <MultipleAssignment> -> VAR <NameList> ASSIGN <FunctionCall>
(defn MultipleAssignment [subtree scope] 
  (println "MultipleAssignment")
  ;(println subtree)
  ;(println (count subtree))
  
  )

; <Print> -> PRINT <Expression> 
(defn Print [subtree scope]
  (println "Print") 
  ;(println subtree)
  ;(println (count subtree))
  (ret-print(CallByLabel (first (third subtree)) (third subtree) scope))
  )

; <NameList> -> <Name> COMMA <NameList> | <Name> 
(defn NameList [subtree scope]
  (println "NameList")
  ;(println subtree)
  ;(println (count subtree))
  
  )

; <ParameterList> -> <Parameter COMMA <ParameterList> | <Parameter> 
(defn ParameterList [subtree scope]
  (println "ParameterList")
  ;(println subtree)
  ;(println (count subtree))
  
  )

; <Parameter> -> <Expression> | <Name> 
(defn Parameter [subtree scope] 
  (println "Parameter")
  ;(println subtree)
  ;(println (count subtree))
  (CallByLabel (first (second subtree)) (second subtree) scope)
  )


; <Expression> -> <Term> ADD <Expression> | <Term> SUB <Expression> | <Term> 
(defn Expression [subtree scope]
  (println "Expression")
  ;(println subtree)
	;(println (count subtree))
	
	(cond (= 2 (count subtree))
       (CallByLabel (first (second subtree)) (second subtree) scope)
       (= :ADD (first (third subtree)))
       (+ (CallByLabel (first (second subtree)) (second subtree) scope)
          (CallByLabel (first (fourth subtree)) (fourth subtree) scope))
       (= :SUB (first (third subtree)))
       (- (CallByLabel (first (second subtree)) (second subtree) scope)
          (CallByLabel (first (fourth subtree)) (fourth subtree) scope)))		
)

; <Term> -> <Factor> MULT <Term> | <Factor> DIV <Term> | <Factor> 
(defn Term [subtree scope]
  (println "Term") 
  ;(println subtree)
  ;(println (count subtree)) 
  
  (cond (= 2 (count subtree)) 
        (CallByLabel (first (second subtree)) (second subtree) scope) 
        (= :MULT (first (third subtree))) 
        (* (CallByLabel (first (second subtree)) (second subtree) scope) 
           (CallByLabel (first (fourth subtree)) (fourth subtree) scope))
        (= :DIV (first (third subtree)))
        (/ (CallByLabel (first (second subtree)) (second subtree) scope) 
           (CallByLabel(first (fourth subtree)) (fourth subtree) scope)))
  ) 

; <Factor> -> <SubExpression> EXP <Factor> | <SubExpression> | <FunctionCall> | <Value> EXP <Factor> | <Value> 
(defn Factor [subtree scope] 
  (println "Factor")
  ;(println subtree)
  ;(println (count subtree))
  
  (cond (= 2 (count subtree))
        (CallByLabel (first (second subtree)) (second subtree) scope) 
        
        (= :EXP (first (third subtree)))
        (** (CallByLabel (first (second subtree)) (second subtree) scope) 
                   (CallByLabel (first (fourth subtree)) (fourth subtree) scope)))
  )


; <FunctionCall> -> <Name> LPAREN <FunctionCallParams> COLON <Number> | <Name> LPAREN <FunctionCallParams>
(defn FunctionCall [subtree scope] 
  (println "FunctionCall")
  ;(println subtree)
  ;(println (count subtree))
  
  )

; <FunctionCallParams> ->  <ParameterList> RPAREN | RPAREN
; QUESTION: do i need to check for the number of subtrees? 
(defn FunctionCallParams [subtree scope]
  (println "FunctionCallParams")
  ;(println subtree)
  ;(println (count subtree))
  
  )

; <SubExpression> -> LPAREN <Expression> RPAREN
; QUESTION: do i need to check for the number of subtrees? 
(defn SubExpression [subtree scope]
  (println "SubExpression")
  ;(println subtree)
  ;(println (count subtree))
  
  )

; <Value> -> <Name> | <Number>
(defn Value [subtree scope] 
  (println "Value")
  ;(println subtree)
  ;(println (count subtree))
  ; Value0 and Value1
  (CallByLabel (first (second subtree)) (second subtree) scope) 
  )


; <Name> -> IDENT | SUB IDENT | ADD IDENT
(defn Name [subtree scope]
  (println "Name")
  ;(println subtree)
  ;(println (count subtree))
  
  )

; <Number> -> NUMBER | SUB NUMBER | ADD NUMBER
(defn MyNumber [subtree scope]
  (println "Number")
  (println subtree)
  (println (count subtree))
  
  )

   
; our cool interpreter 
(defn interpret-quirk [subtree scope] 
  (CallByLabel (first subtree) subtree {})
  )


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



