# Worksheet on recursion using Python (II)

Place each attempt under the appropriate folder, e.g., exercise one under the folder `ex01`, etc.; you should find the appropriate tests under the respective folder.


As usual, you should (at least) attempt the exercises labelled "Easy" and "Medium". 

1. (Easy) [No provided tests for this one].
	Write a (recursive) function which implements the Pascal's triangle:
	```	
				       1
			        1    1
		          1    2    1
		       1    3    3    1
		     1    4    6    4    1
		 1    5    10    10    5    1

	```	

1. (Easy) We can define the sum from 1 to *x* (i.e., 1 + 2 + ... + *x*) recursively as follows for integer x ≥ 1:

	1, if x = 1 
	
	x + sum from 1 to x-1, if x > 1
	
	Write a function `sum(val)` that computes the sum from 1 to `val` recursively.
	
1. (Easy) The digital sum of a number `n` is the sum of its digits. 
	Write a recursive function `digitalSum(n)` that takes a positive integer `n` and returns its digital sum. 
	For example, `digitalSum(2019)` should return `12` because `2 + 0 + 1 + 9 = 12`.

1. (Medium) We can determine how many digits a positive integer has by repeatedly dividing by 10 
	(without keeping the remainder) until the number is less than 10, consisting of only 1 digit. 
	We add 1 to this value for each time we divided by 10. Here is the recursive algorithm:
		
	1. If n < 10 return 1.
	2. Otherwise, return 1 + the number of digits in
         n/10 (ignoring the fractional part).
    
	Implement this recursive algorithm in Python as the function `how_many(num)`, where `num` represents the positive
	integer value for which we wish to determine the number of digits.

1. (Medium) Define a recursive function `nestedListContains(NL, target)`, that takes a nested list `NL` of 
	integers and an integer value `target`, and indicates whether `target` is contained anywhere in the nested list. 
	Your code should return the boolean value `True` when the specified value is contained in the nested list, 
	and `False` otherwise.
	
	For example, `nestedListContains([1, [2, [3], 4]], 3)` should return `True` and 
	`nestedListContains([1, [2, [3], 4]], 5)` shou reurn `False`.


1. (Hard) Write a recursive Python function `max(lst)` that has a parameter (`lst`) 
	representing a list of integers and returns the maximum stored in the list. 

	Thinking recursively, the maximum is either the first value in the list or the maximum 
	of the rest of the list, whichever is larger. If the list only has one integer, 
	then its maximum is this single value, naturally.
	
1. (Hard) The digital root of a non-negative integer `n` is computed as follows. 

	Begin by summing the digits of `n`. The digits of the resulting number are then summed, and this process 
	is continued until a single-digit number is obtained. For example, the digital root of `2019` is `3` because
	`2 + 0 + 1 + 9 = 12` and `1 + 2 = 3`. 
	
	Write a recursive function `digitalRoot(n)` which returns the digital root of `n`.
	You may use your earlier answer for `digitalSum`.

1. (Hard) The *hailstone sequence* starting at a positive integer `n` is generated by following two simple rules. 

	1. If `n` is even, the next number in the sequence is `n/2`. 
	2. If `n` is odd, the next number in the sequence is `3 * n + 1`. 
	
	Repeating this process, we generate the hailstone sequence. 
	
	Write a recursive function `hailstone(n)` which returns the hailstone sequence beginning at `n` as a list. 
	Stop when the sequence reaches the number `1` (since otherwise, we would loop forever `1, 4, 2, 1, 4, 2, ...`).
	
	For example, when `n = 5`, your program should return the list: `[5, 16, 8, 4, 2, 1]`
	
	Mathematicians believe that every hailstone sequence reaches 1 eventually, no matter what value of n we start 
	with. However, no one has been able to prove this yet.
	
1. (Hard) Write a recursive function in Python for the sieve of Eratosthenes (`sieve`.
	
	The sieve of Eratosthenes is a simple algorithm for finding all prime numbers up to a specified integer. 
	It was created by the ancient Greek mathematician *Eratosthenes*. 

	The algorithm to find all the prime numbers less than or equal to a given integer *n*:

	1. Create a list of integers from two to *n*`: 2, 3, 4, ..., *n*
	2. Start with a counter *i* set to 2, i.e. the first prime number
	3. Starting from *i*+i, count up by *i* and remove those numbers from the list, i.e. 2*i, 3*i, 4*i, ...
	4. Find the first number of the list following *i*. This is the next prime number.
	5. Set *i* to the number found in the previous step
	6. Repeat steps 3 and 4 until *i* is greater than *n*. 
		(As an improvement: It's enough to go to the square root of n)
	7. All the numbers, which are still in the list, are prime numbers

	You can easily see that we would be inefficient, if we strictly used this algorithm, e.g., 
	we will try to remove the multiples of 4, although they have been already removed by the multiples of 2. 
	So it's enough to produce the multiples of all the prime numbers up to the square root of *n*. 
	We can recursively create these sets. 
