grammar Rubric;

options {
   output=AST;
   ASTLabelType=CommonTree;
}

tokens {
  ACCEPT   =  'Accept';
  DISCARD  =  'Discard';
  SKIP     =  'Skip';
  AFTER    =  'after';
  BEFORE   =  'before';
  DAY      =  'day';
  DAYS     =  'days';
  HOUR     =  'hour';
  HOURS    =  'hours';
  WEEK     =  'week';
  WEEKS    =  'weeks';
  BUSINESS =  'business';
}

@header {
/*
    This file is part of Sarasvati.

    Sarasvati is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    Sarasvati is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public
    License along with Sarasvati.  If not, see <http://www.gnu.org/licenses/>.

    Copyright 2008 Paul Lorenz
*/

package com.googlecode.sarasvati.rubric.lang;

import com.googlecode.sarasvati.GuardResponse;
import com.googlecode.sarasvati.SkipNodeGuardResponse;
import com.googlecode.sarasvati.util.SvUtil;
import com.googlecode.sarasvati.rubric.lang.*;

}

@lexer::header {
/*
    This file is part of Sarasvati.

    Sarasvati is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    Sarasvati is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public
    License along with Sarasvati.  If not, see <http://www.gnu.org/licenses/>.

    Copyright 2008 Paul Lorenz
*/

package com.googlecode.sarasvati.rubric.lang;

import com.googlecode.sarasvati.rubric.lang.*;
}

program returns [RubricStmt value]
         :  stmt EOF { $value = $stmt.value; }
         ;

stmt returns [RubricStmt value]
         :  ('if'|'IF')^ e=orExpr ('then'|'THEN')! ifStmt=stmt ('else'|'ELSE')! elseStmt=stmt
            {$value = new RubricStmtIf( $e.value, $ifStmt.value, $elseStmt.value ); }
         |  result { $value = $result.value; }
         ;

orExpr returns [RubricExpr value]
         :  left=andExpr { $value = $left.value; } ( ('or'|'OR')^ right=andExpr { $value = new RubricExprOr( $value, $right.value ); } )*
         ;

andExpr returns [RubricExpr value]
         :  left=notExpr { $value = $left.value; } ( ('and'|'AND')^ right=notExpr { $value = new RubricExprAnd( $value, $right.value ); } )*
         ;

notExpr returns [RubricExpr value]
         :  ('not'|'NOT')^ expr { $value = new RubricExprNot( $expr.value ); }
         |  expr { $value = $expr.value; }
         ;

expr returns [RubricExpr value]
         :  ID { $value = new RubricExprSymbol( $ID.text ); }
         |  '('! orExpr ')'! { $value = $orExpr.value; }
         ;

result returns [RubricStmt value]
         :  guardResult { $value = new RubricStmtResult( $guardResult.value ); }
         |  NUMBER      { $value = new RubricStmtResult( Integer.parseInt( $NUMBER.text ) ); }
         |  STRING      { $value = new RubricStmtResult( SvUtil.normalizeQuotedString( $STRING.text ) ); }
         |  dateResult  { $value = $dateResult.value; }
         ;

dateResult returns [RubricStmt value]
         :  '('! dateSpec ')'! { $value = $dateSpec.value; }
         ;

dateSpec returns [RubricStmt value]
@init {
  boolean business = false;
}
         :  ID { $value = new RubricStmtDateSymbol( $ID.text ); }
         |  NUMBER ( BUSINESS { business = true; } )? unit=(HOUR|HOURS|DAY|DAYS|WEEK|WEEKS) type=(BEFORE|AFTER) ID
            { $value = new RubricStmtRelativeDate( Integer.parseInt( $NUMBER.text ), business, $unit.text, $type.text, $ID.text ); }
         ;

guardResult returns [GuardResponse value]
         :  ACCEPT   { $value = GuardResponse.ACCEPT_TOKEN_RESPONSE; }
         |  DISCARD  { $value = GuardResponse.DISCARD_TOKEN_RESPONSE; }
         |  SKIP^ ID { $value = new SkipNodeGuardResponse( $ID.text ); }
         |  SKIP     { $value = SkipNodeGuardResponse.DEFAULT_ARC_SKIP_NODE_RESPONSE; }
         ;

// ===========================================================================
// ================   LEXER   ================================================
// ===========================================================================

STRING   :  '"' ( '\\\"' | ~( '"' ) )* '"'
         ;

ID       :  LETTER ( LETTER | DIGIT | '.' | '_' )*
         ;

NUMBER   :  '-'? DIGIT+
         ;

fragment LETTER
         : LOWER | UPPER
         ;

fragment LOWER
         : 'a'..'z'
         ;

fragment UPPER
         : 'A'..'Z'
         ;

fragment DIGIT
         : '0'..'9'
         ;

NEWLINE  :   ('\r'? '\n')+ { $channel=HIDDEN; }
         ;

WHITESPACE
         : ( '\t' | ' ' | '\r' | '\n'| '\u000C' )+ { $channel=HIDDEN; }
         ;

SINGLE_COMMENT
         : '//' ~('\r' | '\n')* NEWLINE { $channel=HIDDEN; }
         ;

MULTI_COMMENT options { greedy = false; }
         : '/*' .* '*/' NEWLINE? { $channel=HIDDEN; }
         ;
