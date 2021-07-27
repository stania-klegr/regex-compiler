This repo is a FSM bassed regex compiler and an accompanying string searching program. It was done for a COMPX301 assignment working in pairs.


Regexp specification:

- Any symbol that does not have a special meaning (as given below) is a literal that matches itself
- . is a wildcard symbol that matches any literal
- adjacent regexps are concatenated to form a single regexp
- * indicates closure (zero or more occurrences) on the preceding regexp
- + indicates that the preceding regexp can occur one or more times
- ? indicates that the preceding regexp can occur zero or one time
- | is an infix alternation operator such that if r and e are regexps, then r|e is a regexp that matches one of either r or e
- ( and ) may enclose a regexp to raise its precedence in the usual manner; such that if e is a regexp, then (e) is a regexp and is equivalent to e. e cannot be empty.
- \ is an escape character that matches nothing but indicates the symbol immediately following the backslash loses any special meaning and is to be interpreted as a literal symbol
- square brackets "[" and "]" enclose a list of symbols of which one and only one must match (i.e. a shorthand for multi-symbol alternation); all special symbols lose their special meaning within the brackets, and if the closing square bracket is to be a literal then it must be first in the enclosed list, and the list cannot be empty.
operator precedence is as follows (from high to low):
- escaped characters (i.e. symbols preceded by \)
- parentheses (i.e. the most deeply nested regexps have the highest precedence)
- repetition/option operators (i.e. *, + and ?)
- concatenation
- alternation (i.e. | and [ ])
