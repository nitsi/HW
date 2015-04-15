/*
ID: 200846905
Exercise 9

====================== Part 1 -  ======================
1. X = p1(a,b)
2. X = Y = 36
3. Fail, Cannot be matched (b & x) - lower case letters.
4. X = B = p2(9)
5. Fail, Y cannot be binded to more then 1 number (4 or 3)
6. A = 3, C = p3(B) => p3(3), B = 3
7. Fail, Y cannot be binded to more then 1 variable (a or 3)
8. X = p3(Z,8), Z = 9, W = p2(2, X) = p2(2, p3(9,8))
9. A = p2(a,X) => p2(a,a), B = 2, X = a, Y = 3                        
*/

/* ====================== Part 2 -  ======================
   1 */
odd(1) :- !.
odd(2) :- fail.
odd(X) :- X > 2, X1 is X - 2, odd(X1), !.
odd(X) :- X < 1, X1 is X + 2, odd(X1).

/* 2 */
triangularNth(0,0) :- !. 
triangularNth(X,Y) :- X > 0, X1 is X - 1, triangularNth(X1,Y1), Y is X + Y1.


/* 3 */
find_min([R],R) :- !.
find_min([X,Y|L],R) :- X > Y, R is Y, find_min([Y|L],R), !.
find_min([X,Y|L],R) :- X =< Y, R is X, find_min([X|L],R).