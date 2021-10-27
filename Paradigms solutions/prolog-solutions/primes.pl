composite(1).
	prime(2).
	prime(N) :- N > 2, \+ composite(N).

fillAdd(Now, Max, Plus) :-
	Now =< Max,
	assert(composite(Now)),
	NextNow is Now + Plus,
	fillAdd(NextNow, Max, Plus).

fill(Now, Max) :-
	Now =< Max,
	\+ composite(Now),
	NowForAdd is Now * Now,
	NowForAdd =< Max,
	fillAdd(NowForAdd, Max, Now).

fill(Now, Max) :-
	Now =< Max,
	NextNow is Now + 1,
	fill(NextNow, Max).

init(Max) :- fill(2, Max).

getDivs(N, DivNow, [N]) :- DivNow * DivNow > N, !.

getDivs(N, DivNow, [DivNow | T]) :-
	0 is mod(N, DivNow),
	NewN is div(N, DivNow),
	getDivs(NewN, DivNow, T).

getDivs(N, DivNow, [H | T]) :-
	\+ 0 is mod(N, DivNow),
	NewDiv is DivNow + 1,
	getDivs(N, NewDiv, [H | T]).

getNumber(1, _, []).

getNumber(R, Pred, [H | T]) :-
	Pred =< H,
	prime(H),
	getNumber(R1, H, T),
	R is R1 * H.

prime_divisors(1, []) :- !.

prime_divisors(N, Divs) :-
	number(N),
	getDivs(N, 2, Divs).

prime_divisors(N, Divs) :-
	\+ number(N),
	getNumber(N, 2, Divs).

perDivs([], _, 1) :- !.
perDivs(_, [], 1).

perDivs([H | T], [H1 | T1], R) :-
    H = H1,
    perDivs(T, T1, R1),
    R is R1 * H.

perDivs([H | T], [H1 | T1], R) :-
    H \= H1,
    H > H1,
    perDivs([H | T], T1, R).

perDivs([H | T], [H1 | T1], R) :-
    H \= H1,
    H < H1,
    perDivs(T, [H1 | T1], R).

gcd(1, _, 1) :- !.
gcd(_, 1, 1) :- !.

gcd(A, B, GCD) :-
 	prime_divisors(A, RA),
 	prime_divisors(B, RB),
 	perDivs(RA, RB, R),
 	GCD = R.