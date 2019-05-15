grammar IQL;

@header {
package iql.engine.antlr;
}

statement
    : (sql ender)*
    ;

sql
    : ('load'|'LOAD') format '.'? path ('where' | 'WHERE')? expression? booleanExpression*  'as' tableName
    | ('save'|'SAVE') (overwrite | append | errorIfExists | ignore | update)* tableName 'as' format '.' path ('where' | 'WHERE')? expression? booleanExpression* ('partitionBy' col)? ('coalesce' numPartition)?
    | ('select'|'SELECT') ~(';')* 'as'? tableName
    | ('insert'|'INSERT') ~(';')*
    | ('create'|'CREATE') ~(';')*
    | ('drop'|'DROP') ~(';')*
    | ('refresh'|'REFRESH') ~(';')*
    | ('set'|'SET') ~(';')*
    | ('train'|'TRAIN') tableName 'as' format '.' path ('where' | 'WHERE')? expression? booleanExpression*
    | ('register'|'REGISTER') format '.' path ('where' | 'WHERE')? expression? booleanExpression*
    | ('show'|'SHOW') ~(';')*
    | ('describe'|'DESCRIBE') ~(';')*
    | ('import'|'IMPORT'|'include'|'INCLUDE') ~(';')*
    | ('explain'|'EXPLAIN') ~(';')*
    |  SIMPLE_COMMENT
    ;

overwrite
    : ('overwrite' | 'OVERWRITE')
    ;

append
    : ('append' | 'APPEND')
    ;

errorIfExists
    : 'errorIfExists'
    ;

ignore
    : ('ignore' | 'IGNORE')
    ;

update
    : ('update' | 'UPDATE')
    ;

booleanExpression
    : ('and' | 'AND') expression
    ;

expression
    : identifier '=' STRING
    ;

ender
    :';'
    ;

format
    : identifier
    ;

path
    : quotedIdentifier | identifier
    ;

db
    :qualifiedName | identifier
    ;

tableName
    : identifier
    ;

functionName
    : identifier
    ;

col
    : identifier
    ;

qualifiedName
    : identifier ('.' identifier)*
    ;

identifier
    : strictIdentifier
    ;

strictIdentifier
    : IDENTIFIER
    | quotedIdentifier
    ;

quotedIdentifier
    : BACKQUOTED_IDENTIFIER
    ;

STRING
    : '\'' ( ~('\''|'\\') | ('\\' .) )* '\''
    | '"' ( ~('"'|'\\') | ('\\' .) )* '"'
    | BLOCK_STRING
    ;

BLOCK_STRING
    : '```' ~[+] .*? '```'
    ;

IDENTIFIER
    : (LETTER | DIGIT | '_')+
    ;

BACKQUOTED_IDENTIFIER
    : '`' ( ~'`' | '``' )* '`'
    ;

numPartition
    : DIGIT+
    ;

fragment DIGIT
    : [0-9]
    ;

fragment LETTER
    : [a-zA-Z]
    ;

SIMPLE_COMMENT
    : '--' ~[\r\n]* '\r'? '\n'? -> channel(HIDDEN)
    ;

BRACKETED_EMPTY_COMMENT
    : '/**/' -> channel(HIDDEN)
    ;

BRACKETED_COMMENT
    : '/*' ~[+] .*? '*/' -> channel(HIDDEN)
    ;

WS
    : [ \r\n\t]+ -> channel(HIDDEN)
    ;

// Catch-all for anything we can't recognize.
// We use this to be able to ignore and recover all the text
// when splitting statements with DelimiterLexer
UNRECOGNIZED
    : .
    ;