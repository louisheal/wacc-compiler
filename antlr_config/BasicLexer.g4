lexer grammar BasicLexer;

//comment
COMMENT: '#'~[\n]* '\n' -> skip ;

//func
IS: 'is' ;

//commands
S_SKIP: 'skip' ;
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

//assign-rhs
CALL: 'call' ;

//numbers
fragment DIGIT: '0'..'9' ;
INTEGER: DIGIT+ ;

//pair-type
PAIR: 'pair' ;
NEW_PAIR: 'newpair' ;

//pair-elem
FST: 'fst' ;
SND: 'snd' ;

//base-type
INT: 'int' ;
BOOL: 'bool' ;
CHAR: 'char' ;
STRING: 'string' ;

//brackets
P_OPEN: '(';
P_CLOSE: ')';

//array-type
SB_OPEN: '[' ;
SB_CLOSE: ']' ;

//uni-operators
NOT: '!' ;
LEN: 'len' ;
ORD: 'ord' ;
CHR: 'chr' ;

//assign
ASSIGN: '=' ;

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

//semicolon
SEMI_COLON: ';' ;

//comma
COMMA: ',' ;

//whitespace
EOL: [ \n\t\r]+ -> skip ;

//escaped-char
fragment ESC_CHAR: '0'
                 | 'b'
                 | 't'
                 | 'n'
                 | 'f'
                 | 'r'
                 | '"'
                 | '\''
                 | '\\' ;

//character
fragment CHARACTER: ~['"]
                  | '\\' ESC_CHAR ;

//char-liter
CHAR_LITER: '\'' CHARACTER '\'' ;


//string-liter
STR_LITER: '"' CHARACTER* '"' ;



//bool-liter
BOOL_LITER: 'true' | 'false' ;

//pair-liter
NULL: 'null' ;

IDENT: [_a-zA-Z] [_a-zA-Z0-9]* ;