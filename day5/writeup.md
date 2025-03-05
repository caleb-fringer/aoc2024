# Day 5
The first set of inputs gives page ordering rules, X|Y => X must come before Y
for an update to be valid
The second set of inputs is the page numbers of each update

The first task is to determine which updates are being printed (i.e., which
updates correctly follow the page ordering rules
Then, I need to sum the middle number from each correctly ordered update.
- will the lists of inputs always be odd numbered?
- this also indicates we want random access to each update to keep efficient
- retrieval of the middle element

Two ideas for how to solve this: hash-map brute-force and prefix-tree

---
Idea 1) Suppose I first read the rules list into a hashmap, where 

Lets walk through the first example
First input says I want to print pages 75, 47, 61, 53, 29
First, does 75 has any dependencies? Does it appear on the left
hand side of any rules? If so, is that dependency in the list after 75?
The only dependency that 75 has is 97. But, 97 does not appear in the list
So we're okay.
Next, 47. 47 has dependencies of 97, and 75. 97 does not appear in the list,
and 75 does not come after 47.
Next, 61. 61 has 97, 47, and 75. 97 does not appear in the list, and 47, 75
do come after 61.
Next, 53. 53 has 47, 61, and 97. Same as above.
Next, 29. 29 has 75, 97, 53, 61, 47. Same as above
So this one is good.
To recap, for each element i of the list, we had to check for i's dependencies,
which could be O(1) time, and then check if any of those dependencies appeared
in the rest of the list in O(n) comparisons.
Since we have to do this for every n element of the list, this is O(n^2)

Now lets walk thru the input 97, 13, 75, 29, 47.
97 has no deps. cont.
13 has deps 97, 61, 29, 47, 75, 53. 75 comes after 13, as does, 29, and 47.
So actually we have to compare each k dependency n times, or we have to
test each subsequent element for membership in the set which could be O(1)

I suspect there's a way to optimize checking if any of the dependencies
appear afterwards.

What happens if I do this in the reverse order?
Suppose I do the following. What if I make a set A that contains all of the
elements in the list, and a mapping of LHS -> Set(all RHS assignments).
Then, starting from the nth element of the list, I ask "do any of n's deps
occur in the rest of the list? Because if so, this update is not valid"
If the answer is no, I know that all of n's dependencies are satisfied,
I know that if X|Y and Y|Z, X must come before Z.
If X|Y and X|Z it tells us nothing about the order of Y and Z.

Is it possible that we say "All of B's deps are satisfied", but
the presence of B at the end of the list violates some other numbers
deps?
For instance, 1,2,5,4 where 4 4's dependencies of coming before 5,6,7 are satisfied
but 5|4 only checks for the elements before it. But thats okay because by definition,
if 4 doesn't appear in the first n elements in the list, it must come after 5 and the
rule is satisfied.
So I think this approach works

Lets just test rq on the first input
75, 47, 61, 53, 29
Initialize the ruleset to:
{ 47: [53, 13, ]}
I'm gonna write some code to read the input and make it into a hashmap

