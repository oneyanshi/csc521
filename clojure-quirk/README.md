# :cherry_blossom: :star2: clojure-quirk :star2: :cherry_blossom: 

A parser and interpreter written in Clojure for an imaginary grammar/language called Quirk. 

## TODO:
- [x]  Parser works just fine, able to slurp in an outside .q file. 
- [ ] Need to work on interpreter 
- [ ] Clojure correctness ; coding standards check? 
- [ ] Flesh out README 

## Usage

You must have an environment where you are able to run leiningen. If you cannot run leiningen, go [here for more information](https://leiningen.org/) on how to install it, etc. Recommended to use Eclipse and download Counterclockwise in order to use write in Clojure within Eclipse. There are other ways to get this environment in text editors like Atom, Sublime, etc., and other programs like IntelliJ. I personally used Eclipse and Counterclockwise and was pretty pleased with the flow that it provided. 

If you already have leiningen, please follow these directions within your terminal/command prompt. 

You should be able to run `lein run [-pt] <[filename.q]`. 

The command `-pt` will display the parse tree within the terminal. Without it, the result will be the interpreted code.

That is, `print 5` will be interpreted as `5`. 

## Parser 

For the parser, we were able to use [instaparse](https://github.com/Engelberg/instaparse) to create the trees required for interpretation. You can find in the `resources` folder a file called `quirk-ebnf-grammar.ebnf`. This is where we defined our grammar and what insta/parse will be checking with the input file given. If not already done, you will also need to install the leiningen dependency for instaparser, version 1.4.5. The dependency should install on its on upon using this project, but just in case, when debugging check to see whether or not the dependency had been installed. 

In order to read this quirk grammar file, don't forget to use slurp! 

If you'd like to see the parse tree within terminal, type the following commands: 

`lein run -pt<[filename.q]` 

and a parse tree will happen! :sparkle: 

## Interpreter 

## Final Thoughts 
:scream: :scream: :scream: :scream: 