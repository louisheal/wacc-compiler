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
NOT_EQUAL: '!=' ;
AND: '&&' ;
OR: '||' ;

//func
IS: 'is' ;


//commands
S_SKIP: 'skip' ;
ASSIGN: '=' ;
READ: 'read' ;
FREE: 'free' ;
RETURN: 'return' ;
EXIT: 'exit' ;
PRINT: 'print' ;
PRINTLN: 'println' ;
IF: 'if' ;
THEN: 'then' ;
ELSE: 'else' ;
FI: 'fi' ;
WHILE: 'while' ;
DO: 'do' ;
DONE: 'done' ;
BEGIN: 'begin' ;
END: 'end' ;
SEMI_COLON: ';' ;

//assign-rhs
NEW_PAIR: 'newpair' ;
CALL: 'call' ;

//pair-type
PAIR: 'pair' ;

//pair-elem
FIRST: 'fst' ;
SECOND: 'snd' ;

//comma
COMMA: ',' ;

//ident
UNDERSCORE: '_' ;
LOWER_CASE: 'a..z' ;
UPPER_CASE: 'A..Z' ;

//brackets
OPEN_PARENTHESES: '(';
CLOSE_PARENTHESES: ')';

//array-type
OPEN_SQUARE_BRACKETS: '[' ;
CLOSED_SQUARE_BRACKETS: ']' ;

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
SINGLE_QUOTE: '\'';
BACKSLASH: '\\' ;

//pair-liter
NULL: 'null' ;

//character
CHARACTER: [^\\'"] ;

//whitespace
EOL: '\n';

//comment
COMMENT: '#'CHARACTER*NEWLINE ;