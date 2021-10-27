get_height(null, 0) :- !.

    get_height(node((_, _, H), _, _), H).

    calc_height(null, 0) :- !.

    calc_height(node((_, _, _), L, R), NewH) :-
    get_height(L, LH),
    get_height(R, RH),
    LH > RH,
    NewH is LH + 1.

calc_height(node((_, _, _), L, R), NewH) :-
    get_height(L, LH),
    get_height(R, RH),
    LH =< RH,
	NewH is RH + 1.

get_LChild(null, null) :- !.

get_LChild(node((_, _, _), L, _), L).

get_RChild(null, null) :- !.

get_RChild(node((_, _, _), _, R), R).

getDelMax(node((NodeK, NodeV, NodeH), null, null), null, (NodeK, NodeV)) :- !.

getDelMax(node((NodeK, NodeV, NodeH), node((LK, LV, LH), LL, LR), null), node((LK, LV, LH), LL, LR), (NodeK, NodeV)) :- !.

getDelMax(node((NodeK, NodeV, NodeH), LChild, RChild), Res, Max) :-
	getDelMax(RChild, ResTree, Max),
	calc_height(node((NodeK, NodeV, NodeH), LChild, ResTree), NewNodeH),
	balance(node((NodeK, NodeV, NewNodeH), LChild, ResTree), Res).


del_node(node((NodeK, NodeV, NodeH), null, null), null) :- !.

del_node(node((NodeK, NodeV, NodeH), LChild, null), LChild) :- !.

del_node(node((NodeK, NodeV, NodeH), null, RChild), RChild) :- !.

del_node(node((NodeK, NodeV, NodeH), LChild, RChild), node((ResK, ResV, NodeH), ResTree, RChild)) :-
	getDelMax(LChild, ResTree, (ResK, ResV)).



balance(null, null) :- !.

balance(node((K, V, 1), L, R), node((K, V, 1), L, R)):- !.

balance(node((K, V, 2), L, R), node((K, V, 2), L, R)):- !.

%  SMALL_LEFT
balance(node((NodeK, NodeV, NodeH),
														L,
														node((RK, RV, RH), RL, RR)),
				node((RK, RV, NewNodeH),
														node((NodeK, NodeV, NewRH), L, RL),
														RR)) :-
	get_height(L, HeightL),
	get_height(RL, HeightRL),
	get_height(RR, HeightRR),
	RH is HeightL + 2,
	HeightRL =< HeightRR,
	calc_height(node((NodeK, NodeV, RH), L, RL), NewRH),
	calc_height(node((RK, RV, NodeH),
														node((NodeK, NodeV, NewRH), L, RL),
														RR), NewNodeH),
	!.

%  SMALL_RIGHT
balance(node((NodeK, NodeV, NodeH),
														node((LK, LV, LH), LL, LR),
														R),
				node((LK, LV, NewNodeH),
														LL,
														node((NodeK, NodeV, NewLH), LR, R))) :-
	get_height(R, HeightR),
	get_height(LR, HeightLR),
	get_height(LL, HeightLL),
	LH is HeightR + 2,
	HeightLR =< HeightLL,
	calc_height(node((NodeK, NodeV, LH), LR, R), NewLH),
	calc_height(node((LK, LV, NodeH),
														LL,
														node((NodeK, NodeV, NewLH), LR, R)), NewNodeH),
	!.

%  BIG_LEFT
balance(node((NodeK, NodeV, NodeH),
														L,
														node((RK, RV, RH),
																						node((RLK, RLV, RLH), RLL, RLR),
																						RR)),
				node((RLK, RLV, NewNodeH),
														node((NodeK, NodeV, NewLH), L, RLL),
														node((RK, RV, NewRH), RLR, RR))) :-
	get_height(L, HeightL),
	get_height(RR, HeightRR),
	RH is HeightL + 2,
	RLH > HeightRR,
	calc_height(node((NodeK, NodeV, RLH), L, RLL), NewLH),
	calc_height(node((RK, RV, RLH), RLR, RR), NewRH),
	calc_height(node((RLK, RLV, RH),
														node((NodeK, NodeV, NewLH), L, RLL),
														node((RK, RV, NewRH), RLR, RR)), NewNodeH),
	!.

%  BIG_RIGHT
balance(node((NodeK, NodeV, NodeH),
														node((LK, LV, LH),
																						LL,
																						node((LRK, LRV, LRH), LRL, LRR)),
														R),
				node((LRK, LRV, NewNodeH),
														node((LK, LV, NewLH), LL, LRL),
														node((NodeK, NodeV, NewRH), LRR, R))) :-
	get_height(R, HeightR),
	get_height(LL, HeightLL),
	LH is HeightR + 2,
	LRH > HeightLL,
	calc_height(node((LK, LV, LRH), LL, LRL), NewLH),
	calc_height(node((NodeK, NodeV, LRH), LRR, R), NewRH),
	calc_height(node((LRK, LRV, LH),
														node((LK, LV, NewLH), LL, LRL),
														node((NodeK, NodeV, NewRH), LRR, R)), NewNodeH),
	!.

balance(Node, Node).


% GET BY KEY
map_get_key(node((NodeK, NodeV, NodeH), LChild, RChild), NodeK, _) :- !.

map_get_key(node((NodeK, NodeV, NodeH), LChild, RChild), Key, Val) :-
	NodeK < Key,
	map_get_key(RChild, Key, Val).

map_get_key(node((NodeK, NodeV, NodeH), LChild, RChild), Key, Val) :-
	NodeK > Key,
	map_get_key(LChild, Key, Val).


%  GET
map_get(node((NodeK, NodeV, NodeH), LChild, RChild), NodeK, NodeV) :- !.

map_get(node((NodeK, NodeV, NodeH), LChild, RChild), Key, Val) :-
	NodeK < Key,
	map_get(RChild, Key, Val).

map_get(node((NodeK, NodeV, NodeH), LChild, RChild), Key, Val) :-
	NodeK > Key,
	map_get(LChild, Key, Val).



%  INSERT
map_insert(null, Key, Val, node((Key, Val, 1), null, null)).

map_insert(node((NodeK, NodeV, NodeH), LChild, RChild), Key, Val, Res) :-
	NodeK > Key,
    map_insert(LChild, Key, Val, TreeMapIns),
    calc_height(node((NodeK, NodeV, NodeH), TreeMapIns, RChild), NewNodeH),
    balance(node((NodeK, NodeV, NewNodeH), TreeMapIns, RChild), Res).

        map_insert(node((NodeK, NodeV, NodeH), LChild, RChild), Key, Val, Res) :-
    NodeK < Key,
    map_insert(RChild, Key, Val, TreeMapIns),
    calc_height(node((NodeK, NodeV, NodeH), LChild, TreeMapIns), NewNodeH),
    balance(node((NodeK, NodeV, NewNodeH), LChild, TreeMapIns), Res).



        %  REMOVE
map_remove(null, _, null) :- !.

    map_remove(node((NodeK, NodeV, NodeH), LChild, RChild), Key, Res) :-
    NodeK > Key,
    map_remove(LChild, Key, NewLChild),
    calc_height(node((NodeK, NodeV, NodeH), NewLChild, RChild), NewNodeH),
    balance(node((NodeK, NodeV, NewNodeH), NewLChild, RChild), Res).

        map_remove(node((NodeK, NodeV, NodeH), LChild, RChild), Key, Res) :-
    NodeK < Key,
    map_remove(RChild, Key, NewRChild),
    calc_height(node((NodeK, NodeV, NodeH), LChild, NewRChild), NewNodeH),
    balance(node((NodeK, NodeV, NewNodeH), LChild, NewRChild), Res).

        map_remove(node((NodeK, NodeV, NodeH), LChild, RChild), NodeK, Res) :-
    del_node(node((NodeK, NodeV, NodeH), LChild, RChild), DelTree),
    balance(DelTree, Res).



        %  PUT
map_put(TreeMap, Key, Val, Res) :-
    map_get_key(TreeMap, Key, Val),
    map_remove(TreeMap, Key, TreeMapRem),
    map_insert(TreeMapRem, Key, Val, Res),
    !.

        map_put(TreeMap, Key, Val, Res) :-
    map_insert(TreeMap, Key, Val, Res).



    %  BUILD
map_build([], null) :- !.

    map_build([(Key, Val) | T], TreeMap) :-
    map_build(T, BuiltTree),
    map_put(BuiltTree, Key, Val, TreeMap).

        map_getLast(TreeMap, (Key, Val)) :-
    getDelMax(TreeMap, _, (Key, Val)).

    map_removeLast(null, null).

    map_removeLast(TreeMap, Result) :-
    getDelMax(TreeMap, Result, _).

