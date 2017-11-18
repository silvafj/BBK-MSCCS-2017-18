# Worksheet on recursion using Python

These exercises are from the "Interactive Python 3 tutorial" but we include the tests in this repository so you 
shouldn't need to resort to using `snakify` directly.

You should attempt (at least) the "Easy" and "Medium" labelled exercises. Place each attempt under the appropriate folder, e.g., exercise one under the folder `ex01`, etc.

1. **The length of the segment** (Easy)

	Given four real numbers representing cartesian coordinates: 
	(x<sub>1</sub>,y<sub>1</sub>),(x<sub>2</sub>,y<sub>2</sub>), write a function `distance(x1, y1, x2, y2)` 
	to compute the distance between the points (x<sub>1</sub>,y<sub>1</sub>) and (x<sub>2</sub>,y<sub>2</sub>). 
	Read four real numbers and print the resulting distance calculated by the function.

	The formula for distance between two points can be found at [Wolfram](http://mathworld.wolfram.com/Distance.html).

2. **Negative exponent** (Easy) 

	Given a positive real number *a* and integer *n*; compute a<sup>n</sup>.
	
	Write a function `power(a, n)` to calculate the results using the function and print the result of the expression.

	**Do not** use the function of the same name from the standard library.	

3. **Uppercase** (Medium)

	Write a function `capitalise(lower_case_word)` that takes the lower case word and returns the word with 
	the first letter capitalised. E.g., `print(capitalize('word'))` should print the word `Word`.
	
	Then, given a line of lowercase ASCII words (text separated by a single space), print the line 
	with the first letter of each word capitalised using the your own function `capitalise()`.

	In Python there is a function `ord(character)`, which returns the character code in the ASCII chart or `character`.
	The function `chr(code)`, which returns the character from ASCII with the value `code`. 

	For example, `ord('a') == 97`, and `chr(97) == 'a'`. 

4. **Exponentiation** (Medium) 

	Given a positive real number *a*  and a non-negative integer *n*, calculate a<sup>n</sup> without using loops, 
	the `**` operator, or the built in function `math.pow()` (or any other *third party* function). 
	Instead, use recursion and the relation a<sup>n</sup> = a * a<sup>n−1</sup>. You should print the result of your 
	computation.
	
	Your function should be of the form `power(a, n)`.
	
5. **Reverse the sequence** (Hard) 

	Given a sequence of integers that end with a `0`, print the sequence in reverse order.
	Don't use lists or other data structures, instead use *recursion*. 
	
6. **Fibonacci numbers** (Hard) 

	Given a non-negative integer *n*, write a function that returns the *n<sup>th</sup>* Fibonacci number. 
	Your function should be called `fibonacci` and have a single argument, `n`.

	Don't use loops; use recursion instead. However, you should think about why the recursive method 
	is much slower than using loops.



