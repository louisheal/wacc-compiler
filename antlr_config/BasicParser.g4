parser grammar BasicParser;

options {
  tokenVocab=BasicLexer;
}

// EOF indicates that the program must consume to the end of the input.
prog: BEGIN func* stat END EOF ;

func: type IDENT P_OPEN paramList? P_CLOSE IS stat END ;

paramList: param (COMMA param)* ;

param: type IDENT ;

intLiter: INTEGER ;
signedIntLiter: PLUS INTEGER | MINUS INTEGER ;

boolLiter: BOOL_LITER ;

charLiter: CHAR_LITER ;

stringLiter: STR_LITER ;

stat: S_SKIP                            #skip
    | type IDENT ASSIGN assignRHS       #declaration
    | assignLHS ASSIGN assignRHS        #reassignment
    | READ assignLHS                    #read
    | FREE expr                         #free
    | RETURN expr                       #return
    | EXIT expr                         #exit
    | PRINT expr                        #print
    | PRINTLN expr                      #println
    | IF expr THEN stat ELSE stat FI    #if_then_else_fi
    | WHILE expr DO stat DONE           #while_do_done
    | BEGIN stat END                    #begin_end
    | stat SEMI_COLON stat              #semi_colon 
    ;

assignLHS: IDENT
         | arrayElem
         | pairElem 
         ;

expr: intLiter
    | signedIntLiter
    | boolLiter
    | charLiter
    | stringLiter
    | pairLiter
    | IDENT
    | arrayElem
    | unaryOper expr
    | expr DIVIDE expr
    | expr MULTIPLY expr
    | expr MODULO expr
    | expr (PLUS | MINUS) expr
    | expr (GREATER_THAN | GREATER_THAN_OR_EQUAL | LESS_THAN | LESS_THAN_OR_EQUAL) expr
    | expr (EQUAL | NOT_EQUAL) expr
    | expr AND expr
    | expr OR expr
    | P_OPEN expr P_CLOSE
    ;

assignRHS: expr
         | arrayLiter
         | NEW_PAIR P_OPEN expr COMMA expr P_CLOSE
         | pairElem
         | CALL IDENT P_OPEN argList P_CLOSE 
         ;

argList: expr (COMMA expr)* | ; // second empty in case no args taken

pairElem: FST expr | SND expr ;

type: baseType
    | arrayType
    | pairType 
    ;

baseType: INT
        | BOOL
        | CHAR
        | STRING 
        ;

arrayType: baseType SB_OPEN SB_CLOSE    #baseArrayType
         | pairType SB_OPEN SB_CLOSE    #pairArrayType
         | arrayType SB_OPEN SB_CLOSE   #nestedArrayType 
         ;

pairType: PAIR P_OPEN pairElemType COMMA pairElemType P_CLOSE ;

pairElemType: baseType
            | arrayType
            | PAIR 
            ;

unaryOper: NOT
         | MINUS
         | LEN
         | ORD
         | CHR 
         ;

arrayElem: IDENT (SB_OPEN expr SB_CLOSE)+ ;

arrayLiter: SB_OPEN (expr (COMMA expr)*)? SB_CLOSE ;

pairLiter: NULL ;

comment: COMMENT ;
