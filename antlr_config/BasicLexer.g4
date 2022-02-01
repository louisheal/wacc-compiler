lexer grammar BasicLexer;

//base-type
INT: 'int' ;
BOOL: 'bool' ;
CHAR: 'char' ;
STRING: 'string' ;

//uni-operators
NOT: '!' ;
LEN: 'len' ;
ORD: 'ord' ;
CHR: 'chr' ;

//bin-operators
PLUS: '+' ;
MINUS: '-' ;
MULTIPLY: '*' ;
DIVIDE: '/' ;
MODULO: '%' ;
GREATER_THAN: '>' ;
GREATER_THAN_OR_EQUAL: '>=' ;
LESS_THAN: '<' ;
LESS_THAN_OR_EQUAL: '<=' ;
EQUAL: '==' ;
NOTEQUAL: '!=' ;
AND: '&&' ;
OR: '||' ;

//ident
UNDERSCORE: '' ;
LOWER_CASE: 'a..z' ;
UPPER_CASE: 'A..Z' ;

//brackets
OPEN_PARENTHESES: '(';
CLOSE_PARENTHESES: ')';

//numbers
fragment DIGIT: '0'..'9' ;
INTEGER: DIGIT+ ;

//bool-liter
TRUE: 'true' ;
FALSE: 'false' ;

//escaped-char
ZERO: '0' ;
BACK: 'b' ;
TAB: 't' ;
NEWLINE: 'n' ;

FORM_FEED: 'f' ;
GIVE_A_NAME_FOR_R: 'r' ;
DOUBLE_QUOTE: '"' ;

//pair-liter
NULL: 'null' ;

