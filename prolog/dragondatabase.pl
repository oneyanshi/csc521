dragon. fae. salamander. wyrm. quetzakcoatl.
amphithere. lindworm. lungDragon. drake. hydra.
wyvern. cockatrice. kirin. seaSerpent.


eurodragon(dragon).
eurodragon(fae). 
sizeReduct(fae). 
draganid(salamander).
draganid(X) :- eurodragon(X).

drakoid(wyvern).
drakoid(drake).
drakoid(hydra).

drakid(X) :- serpentoid(X); drakoid(X).

trueSerpent(wyvern).
trueSerpent(quetzakcoatl).
brachioserpent(amphithere).
brachioserpent(lindworm). 
serpentoid(lungDragon).
serpentoid(X) :- trueSerpent(X); brachioserpent(X).

bird(cockatrice).

mammal(kirin).

rayFinnedFish(seaSerpent). 

fourLegs(X) :- mammal(X) ; bird(X) ; drakid(X) ; draganid(X).

wings(dragon).
wings(amphithere).
wings(wyvernn).
wings(X) :- bird(X).

feathers(quetzakcoatl). 
feathers(X) :- bird(X).

elongatedBody(salamander). 
elongatedBody(X) :- serpentoid(X); rayFinnedFish(X).
fur(X) :- mammal(X). 
appendagesLost(X) :- trueSerpent(X) ; brachioserpent(X).
extraAppendages(X) :- draganid(X). 



