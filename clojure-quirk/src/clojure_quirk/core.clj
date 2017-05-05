(ns clojure-quirk.core
  (:require [instaparse.core :as insta])
  )

;;; begin utility functions 
(defn third [aList] (nth aList 2))
(defn fourth [aList] (nth aList 3))
(defn fifth [aList] (nth aList 4)) 
(defn sixth [aList] (nth aList 5))
(defn seventh [aList] (nth aList 6))


(defn CallByLabel [funLabel & args]
  ;; calling the functions by the label :Statement
  (apply (ns-resolve 'clojure-quirk.core (symbol (name funLabel))) args))


(defn ret-print [thingToPrint] 
  ;; for printing
  (println thingToPrint) 
  thingToPrint)


(defn ** [base power]
  ;; exponents (recursive)
  (reduce * (repeat power base)))


;;; end utility functions 


;; <Program> --> <Statement> <Program> | <Statement> 
(defn Program [subtree scope] 
  (ret-print "Program")
  ;; (ret-print subtree)
  ;; (ret-print (count subtree))
  
  (cond(= 2 (count subtree))       
    ;; Program1
    (CallByLabel (first (second subtree)) (second subtree) scope)
    
    ;; Program0
    (= :Program (first (third subtree)))
    ((CallByLabel (first (second subtree)) (second subtree) scope)
      (CallByLabel (first (third subtree)) (third subtree) scope))))


;; <Statement> -> <FunctionDeclaration> | <Assignment> | <Print> 
(defn Statement [subtree scope] 
  ;; (ret-print "Statement")
  ;; (ret-print subtree)
  ;; (ret-print (count subtree))
  (CallByLabel (first (second subtree)) (second subtree) scope))


;; <FunctionDeclaration> -> FUNCTION <Name> PAREN <FunctionParams> LBRACE <FunctionBody> RBRACE
(defn FunctionDeclaration [subtree scope] 
  ;; (ret-print "FunctionDeclaration")
  ;; (ret-print subtree)
  (let [functionName (CallByLabel (first (third subtree)) (third subtree) scope)
        paramNames (CallByLabel (first (fifth subtree)) (fifth subtree) scope)]
    (assoc scope functionName (paramNames (seventh subtree)))))


;; <FunctionParams> -> <NameList> RPAREN | RPAREN 
(defn FunctionParams [subtree scope]
  ;; (ret-print "FunctionParams")
  ;; (ret-print (first (second subtree)))
  ;; (ret-print subtree)
  (cond (= 2 (count subtree))
        (CallByLabel (first (second subtree)) (second subtree) scope)
        
        (= :RPAREN (first (second subtree)))
           (list)))


;; <FunctionBody> -> <Program> <Return> | <Return> 
(defn FunctionBody [subtree scope]
  ;; (ret-print "FunctionBody")
  (cond(= 2 (count subtree))
       ; FunctionBody1 
    (CallByLabel (first (second subtree)) (second subtree) scope)
    
    ;; FunctionBody0
    (= :Program (first (third subtree)))
    ((CallByLabel (first (second subtree)) (second subtree) scope)
      (CallByLabel (first (third subtree)) (third subtree) scope))))


;; <Return> -> RETURN <ParameterList> 
(defn Return [subtree scope]
  ;; (ret-print "Return")
  ;; (ret-print (subtree))
  (CallByLabel (first (third subtree)) (third subtree) scope))


;; <Assignment> - <SingleAssignment> | <MultipleAssignment> 
(defn Assignment [subtree scope]
  ;; (ret-print "Assignment")
  ;; (println subtree)
  ;; (println (count subtree))
  ;; Assignment0 and Assignment1
  (CallByLabel (first (second subtree)) (second subtree) scope))


;; <SingleAssignment> -> VAR <Name> ASSIGN <Expression> 
(defn SingleAssignment [subtree scope] 
  ;; (ret-print "SingleAssignment")
  ;; (println subtree)
  ;; (println (count subtree))
  ;; SingleAssignment0 
  ;; 1. Get the name of the variable 
  ;; 2. Get the value of the <Expression> 
  ;; 3. Bind name to value in scope. 
  (assoc scope (CallByLabel (first (third subtree)) (third subtree) scope) 
         (CallByLabel(first (fifth subtree)) (fifth subtree) scope)))


;; <MultipleAssignment> -> VAR <NameList> ASSIGN <FunctionCall>
(defn MultipleAssignment [subtree scope] 
  ;; (ret-print "MultipleAssignment")
  ;; (ret-print subtree)
  ;; (ret-print (count subtree))
  
  )


;; <Print> -> PRINT <Expression> 
(defn Print [subtree scope]
  ;; (ret-print "Print") 
  ;; (ret-print subtree)
  ;; (ret-print (count subtree))
  (ret-print(CallByLabel (first (third subtree)) (third subtree) scope)))


;; <NameList> -> <Name> COMMA <NameList> | <Name> 
(defn NameList [subtree scope]
  ;; (ret-print "NameList")
  ;; (ret-print subtree)
  ;; (ret-print (count subtree))
  
  )


;; <ParameterList> -> <Parameter COMMA <ParameterList> | <Parameter> 
(defn ParameterList [subtree scope]
  ;; (ret-print "ParameterList")
  ;; (ret-print subtree)
  ;; (ret-print (count subtree))
  
  )


;; <Parameter> -> <Expression> | <Name> 
(defn Parameter [subtree scope] 
  ;; (ret-print "Parameter")
  ;; (ret-print subtree)
  ;; (ret-print (count subtree))
  (CallByLabel (first (second subtree)) (second subtree) scope))


;; <Expression> -> <Term> ADD <Expression> | <Term> SUB <Expression> | <Term> 
(defn Expression [subtree scope]
  ;; (ret-print "Expression")
  ;; (ret-print subtree)
	;; (ret-print (count subtree))
	
	(cond (= 2 (count subtree))
       (CallByLabel (first (second subtree)) (second subtree) scope)
       (= :ADD (first (third subtree)))
       (+ (CallByLabel (first (second subtree)) (second subtree) scope)
          (CallByLabel (first (fourth subtree)) (fourth subtree) scope))
       (= :SUB (first (third subtree)))
       (- (CallByLabel (first (second subtree)) (second subtree) scope)
          (CallByLabel (first (fourth subtree)) (fourth subtree) scope))))


;; <Term> -> <Factor> MULT <Term> | <Factor> DIV <Term> | <Factor> 
(defn Term [subtree scope]
  ;; (ret-print "Term") 
  ;; (ret-print subtree)
  ;; (ret-print (count subtree)) 
  
  (cond (= 2 (count subtree)) 
        (CallByLabel (first (second subtree)) (second subtree) scope) 
        (= :MULT (first (third subtree))) 
        (* (CallByLabel (first (second subtree)) (second subtree) scope) 
           (CallByLabel (first (fourth subtree)) (fourth subtree) scope))
        (= :DIV (first (third subtree)))
        (/ (CallByLabel (first (second subtree)) (second subtree) scope) 
           (CallByLabel(first (fourth subtree)) (fourth subtree) scope)))) 


;; <Factor> -> <SubExpression> EXP <Factor> | <SubExpression> | <FunctionCall> | <Value> EXP <Factor> | <Value> 
(defn Factor [subtree scope] 
  ;; (ret-print "Factor")
  ;; (ret-print subtree)
  ;; (ret-print (count subtree))
  
  (cond (= 2 (count subtree))
        (CallByLabel (first (second subtree)) (second subtree) scope) 
        
        (= :EXP (first (third subtree)))
        (** (CallByLabel (first (second subtree)) (second subtree) scope) 
                   (CallByLabel (first (fourth subtree)) (fourth subtree) scope))))


;; <FunctionCall> -> <Name> LPAREN <FunctionCallParams> COLON <Number> | <Name> LPAREN <FunctionCallParams>
(defn FunctionCall [subtree scope] 
  ;; (ret-print "FunctionCall")
  ;; (ret-print subtree)
  ;; (ret-print (count subtree))
  
  )


;; <FunctionCallParams> ->  <ParameterList> RPAREN | RPAREN
(defn FunctionCallParams [subtree scope]
  ;; (ret-print "FunctionCallParams")
  ;; (ret-print subtree)
  ;; (ret-print (count subtree))
  (CallByLabel(first (second subtree)) (second subtree) scope))


;; <SubExpression> -> LPAREN <Expression> RPAREN
(defn SubExpression [subtree scope]
  ;; (ret-print "SubExpression")
  ;; (ret-print subtree)
  ;; (ret-print (count subtree))
  (CallByLabel(first (third subtree)) (third subtree) scope))


;; <Value> -> <Name> | <Number>
(defn Value [subtree scope] 
  ;; (ret-print "Value")
  ;; (println subtree)
  ;; (println (count subtree))
  ;; Value0 and Value1
  
  (CallByLabel (first (second subtree)) (second subtree) scope))


;; <Name> -> IDENT | SUB IDENT | ADD IDENT
(defn Name [subtree scope]
  ;; (ret-print subtree) 
  ;; (ret-print (count subtree))
  ;; (ret-print "Name")

  (cond
    (= :IDENT (first (second subtree)))
    (get scope (second (second subtree)))
    
    (= :SUB (first (second subtree)))
    (* -1 (get scope (second (second subtree))))
    
    (= :ADD (first (second subtree)))
    (+ (get scope (second (second subtree))))))


;; <Number> -> NUMBER | SUB NUMBER | ADD NUMBER
(defn MyNumber [subtree scope]
  ;; (ret-print "Number")
  ;; (println subtree)
  ;; (println (count subtree))
  ;; (println(first (second subtree)) (second subtree))
  ;; (println(second(second subtree)))  
  ;; (println(Double/parseDouble (second(second subtree))))
  (cond 
    (= :NUMBER (first (second subtree)))
    (Double/parseDouble (second (second subtree)))
    
    (= :SUB (first (second subtree)))
    (* -1 (Double/parseDouble (second (second subtree))))
    
    (= :ADD (first (second subtree)))
    (+ (Double/parseDouble (second(second subtree))))))


;; our cool interpreter 
(defn interpret-quirk [subtree scope] 
  (CallByLabel (first subtree) subtree {}))


;; our cool parser 
 (def parser 
  (insta/parser (slurp "resources/quirk-ebnf-grammar.ebnf")
                :auto-whitespace :standard))


;; this is our main  
(defn -main [& args]
 ;; takes in input
 (def stdin (slurp *in*))
 ;; is there a command for -pt? 
 (if (.equals "-pt" (first *command-line-args*))
   (def SHOW_PARSE_TREE true))
 
 (def parse-tree (parser stdin))
 (def interpreted (interpret-quirk parse-tree {}))

 (cond (= true SHOW_PARSE_TREE)
       (println parse-tree)
       
       :else
       (println interpreted)))

