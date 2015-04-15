/*
ID: 200846905
Exercise 9 */
/* From class:
tree_insert(X, Tree, New) --- X - value, Tree - old tree, New - new */

tree_insert(X, [], tree([], X, [])).
tree_insert(X, tree(Left, V, Right), tree(NewLeft, V, Right)) :-
	X < V,
	tree_insert(X, Left, NewLeft), !.
tree_insert(X, tree(Left, V, Right), tree(Left, V, NewRight)) :-
	X > V,
	tree_insert(X, Right, NewRight).

/* ====================== Part 1 - Tree Repersentation ====================== */
/*   A */
tree_contains(tree(_, V, _), V).
tree_contains(tree(_, _, R), V) :- tree_contains(R, V).	
tree_contains(tree(L, _, _), V) :- tree_contains(L, V).

/* B */
list_to_tree([], []).
list_to_tree([X | L], T) :- 
	list_to_tree(L, NewTree),
	tree_insert(X, NewTree, T), !.

/* C */
tree_flip([],[]).
tree_flip(tree(L, X, R), tree(L1, X, R1)) :- 
	tree_flip(L, R1), 
	tree_flip(R, L1).

/* D */
tree_inorder([],[]) :- !.
tree_inorder(tree(L, V, R), List) :- 
	tree_inorder(L, Lleft), append(Lleft, [V], NewLi),
	tree_inorder(R, Lright), append(NewLi, Lright ,List).

/* E */
tree_sort(L, R) :- 
	list_to_tree(L, NewTree), 
	tree_inorder(NewTree, R).

/* ====================== Part 2 - More Prolog ====================== */
/* A */
take_min([V], V, []).
take_min([V | K], V, Y) :- take_min(K, KLess, _), V < KLess, !. 
take_min([A | K], V, [A | NewK]) :- take_min(K, V, NewK), !.

/* B */
find_k([X], 0, X).
find_k(L, 0, R) :- take_min(L,R,Nleft), !.
find_k(L, K, R) :- take_min(L,X,Nleft), 
	NewK is K - 1, 
	find_k(Nleft, NewK, R).

/* C */
/* Didnt have time, sorry */
/* Sorry for my grade */