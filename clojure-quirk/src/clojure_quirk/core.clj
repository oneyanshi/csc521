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


; breakdown 


; end breakdown 

; our cool parser 
 (def parser 
  (insta/parser (slurp "resources/quirk-ebnf-grammar.ebnf")
    :auto-whitespace :standard)
  )
 
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


