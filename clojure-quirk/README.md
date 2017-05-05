# :cherry_blossom: :star2: clojure-quirk :star2: :cherry_blossom: 

A parser and interpreter written in Clojure for an imaginary grammar/language called Quirk. 

## TODO:
- [x]  Parser works just fine, able to slurp in an outside .q file. 
- [ ] Need to work on interpreter
- [x] Example 1 
- [x] Example 2 
- [ ] Example 3 
- [ ] Example 4 
- [ ] Example 5  
- [ ] Clojure correctness ; coding standards check? 
- [ ] Flesh out README 

## Usage

You must have an environment where you are able to run leiningen. If you cannot run leiningen, go [here for more information](https://leiningen.org/) on how to install it, etc. Recommended that you use Eclipse and download Counterclockwise in order to use write in Clojure within Eclipse. There are other ways to get this environment in text editors like Atom, Sublime, etc., and other programs like IntelliJ. I personally used Eclipse and Counterclockwise and was pretty pleased with the flow that it provided. 

If you already have leiningen, please follow these directions within your terminal/command prompt. 

You should be able to run `lein run [-pt] <[filename.q]`. 

The command `-pt` will display the parse tree within the terminal. Without it, the result will be the interpreted code.

That is, `print 5` will be interpreted as `5`. 

## Parser 

For the parser, we were able to use [instaparse](https://github.com/Engelberg/instaparse) to create the trees required for interpretation. You can find in the `resources` folder a file called `quirk-ebnf-grammar.ebnf`. This is where we defined our grammar and what insta/parse will be checking with the input file given. If not already done, you will also need to install the leiningen dependency for instaparser, version 1.4.5. The dependency should install on its on upon using this project, but just in case, when debugging check to see whether or not the dependency had been installed. 

In order to read this quirk grammar file, don't forget to use slurp! 

If you'd like to see the parse tree within terminal, type the following commands: 

`lein run -pt <[foldername/filename.q]` 

and a parse tree will happen! :sparkles: 

## Interpreter 

Currently, the interpreter evaluates up to example 2. 

The interpreter is very similar to the python interpreter located in the `python-quirk` folder. I used the python interpreter as a template in order to write the Clojure version. It uses the output from the parser in order to interpret and provide results. As per requirements, in order to run code, omit the `-pt` flag and write in terminal: 

`lein run < [filename.q]` 

The parse tree will not be displayed but the interpreted result from the code will be, assuming that there are no errors! 

## Final Thoughts 
:scream: :scream: :scream: :scream: 

In all seriousness, this was an incredibly challenging assignment, and I'm proud of what I have been able to complete for learning a language on the fly and understanding the syntax and base code provided to us. What I would've changed was my own workflow, and while I may not get as far as my peers, I felt like I learned enough that I was able to help other people get to the point that I am at now as well through Piazza :pizza: .