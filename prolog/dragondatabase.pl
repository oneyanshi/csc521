/*
 * Prolog for a dragon phylogeny tree
 * Queries related to dragons could return either dragons
 * that are related to that type, e.g. Eurodragons or have
 * a particular characteristic e.g. wings.
 *
 */

dragon. fae. salamander. wyrm. quetzakcoatl.
amphithere. lindworm. lungDragon. drake. hydra.
wyvern. cockatrice. kirin. seaSerpent.

isEurodragon(dragon).
isEurodragon(fae).

isDraganid(salamander).
isDraganid(X) :- eurodragon(X).

isDrakoid(wyvern).
isDrakoid(drake).
isDrakoid(hydra).
isDrakid(X) :- serpentoid(X); drakoid(X).

isTrueSerpent(wyvern).
isTrueSerpent(quetzakcoatl).

isBrachioserpent(amphithere).
isBrachioserpent(lindworm).

isSerpentoid(lungDragon).
isSerpentoid(X) :- trueSerpent(X); brachioserpent(X).

isBird(cockatrice).

isMammal(kirin).

isRayFinnedFish(seaSerpent).

hasFourLegs(X) :- mammal(X) ; bird(X) ; drakid(X) ; draganid(X).

hasWings(dragon).
hasWings(amphithere).
hasWings(wyvernn).
hasWings(X) :- bird(X).

hasFeathers(quetzakcoatl).
hasFeathers(X) :- bird(X).

hasElongatedBody(salamander).
hasElongatedBody(X) :- serpentoid(X); rayFinnedFish(X).

hasFur(X) :- mammal(X).

hasAppendagesLost(X) :- trueSerpent(X) ; brachioserpent(X).

hasExtraAppendages(X) :- draganid(X).

hasSizeReduction(fae).

dragon(draganid, eurodragon, fourLegs, wings).
fae(draganid, eurodragon, fourLegs, sizeReduction).
salamander(draganid, elongatedBody, fourLegs, extraAppendages).
wyvern(drakid, drakoid, fourLegs, wings).
cockatrice(bird, feathers, fourLegs, wings).

lungdragon(serpentoid, fourLegs, elongatedBody).
drake(drakid, fourLegs, drakoid).
hydra(drakid, fourLegs, drakoid).
kirin(mammal, fourLegs, fur).
seaSerpent(rayFinnedFish, elongatedBody).

wyrm(serpentoid, trueSerpent, fourLegs, elongatedBody, appendagesLost).
quetzakcoatl(serpentoid, trueSerpent, fourLegs, elongatedBody, appendagesLost, feathers).
amphithere(serpentoid, brachioserpent, fourLegs, elongatedBody, wings, appendagesLost).
lindworm(serpentoid, brachioserpent, fourLegs, elongatedBody, appendagesLost).
