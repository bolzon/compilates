package com.compiler.common;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public abstract class TokenType {

    public static final int NUMBER = 0; // 123, 4.8, 123E+10, 4.8E+10
    public static final int LITERAL = 1; // "String"
    public static final int IDENTIFIER = 2; // valor, _soma, res12

    public static final int OP_LOGIC = 3; // not | and | or
    public static final int OP_ATTRIB = 4; // :=
    public static final int OP_ADD_SUB = 5; // + | -
    public static final int OP_MULT_DIV = 6; // * | /
    public static final int OP_RELATIONAL = 7; // &lt | &le | &gt | &ge | &eq | &dif | !=

    public static final int END_COMMAND = 8; // ;
    public static final int BRACKET_LEFT = 9; // (
    public static final int BRACKET_RIGHT = 10; // )
    public static final int LOGIC_VALUE = 11; // true | false
    public static final int DATA_TYPE = 12; // bool | string | num

    public static final int RW_PROGRAM = 13; // program
    public static final int RW_END_PROGRAM = 14; // endprog
    public static final int RW_BEGIN = 15; // begin
    public static final int RW_END = 16; // end
    public static final int RW_IF = 17; // if
    public static final int RW_THEN = 18; // then
    public static final int RW_ELSE = 19; // else
    public static final int RW_FOR = 20; // for
    public static final int RW_WHILE = 21; // while
    public static final int RW_DECLARE = 22; // declare
    public static final int RW_TO = 23; // to

    public static final int EOF = 24; // EOF
    public static final int ERROR = 25; // Erro
    public static final int UNKNOWN = 0xffffff;

    private static List<Token> keywords = null;
    public static final String OPERATORS = ";()+-*/";

    static {

        keywords = Arrays.asList(new Token[] {
            new Token(TokenType.OP_ADD_SUB, "+"),
            new Token(TokenType.OP_ADD_SUB, "-"), new Token(TokenType.OP_MULT_DIV, "*"),
            new Token(TokenType.OP_MULT_DIV, "/"), new Token(TokenType.OP_ATTRIB, ":="),
            new Token(TokenType.OP_RELATIONAL, "!="), new Token(TokenType.OP_LOGIC, "or"),
            new Token(TokenType.OP_LOGIC, "not"), new Token(TokenType.OP_LOGIC, "and"),
            new Token(TokenType.OP_RELATIONAL, "&lt"), new Token(TokenType.OP_RELATIONAL, "&le"),
            new Token(TokenType.OP_RELATIONAL, "&gt"), new Token(TokenType.OP_RELATIONAL, "&ge"),
            new Token(TokenType.OP_RELATIONAL, "&eq"), new Token(TokenType.OP_RELATIONAL, "&dif"),

            new Token(TokenType.END_COMMAND, ";"), new Token(TokenType.BRACKET_LEFT, "("),
            new Token(TokenType.BRACKET_RIGHT, ")"), new Token(TokenType.DATA_TYPE, "num"),
            new Token(TokenType.DATA_TYPE, "bool"), new Token(TokenType.DATA_TYPE, "string"),
            new Token(TokenType.LOGIC_VALUE, "true"), new Token(TokenType.LOGIC_VALUE, "false"),

            new Token(TokenType.RW_IF, "if"), new Token(TokenType.RW_TO, "to"), new Token(TokenType.RW_FOR, "for"),
            new Token(TokenType.RW_END, "end"), new Token(TokenType.RW_THEN, "then"),
            new Token(TokenType.RW_ELSE, "else"), new Token(TokenType.RW_BEGIN, "begin"),
            new Token(TokenType.RW_WHILE, "while"), new Token(TokenType.RW_DECLARE, "declare"),
            new Token(TokenType.RW_PROGRAM, "program"), new Token(TokenType.RW_END_PROGRAM, "endprog")
        });
    }

    public static List<Token> getKeywords() {
        return keywords;
    }

    public static int getTypeByLexeme(String lexeme) {
        Iterator<Token> i = keywords.iterator();
        while (i.hasNext()) {
            Token token = i.next();
            if (token.getLexeme().equals(lexeme)) {
                return token.getType();
            }
        }
        return TokenType.UNKNOWN;
    }

    public static String getStringType(int type) {
        Field[] f = TokenType.class.getFields();
        for (int i = 0; i < f.length; i++) {
            try {
                if (f[i].getInt(f[i]) == type) {
                    return f[i].getName();
                }
            }
            catch (Exception e) {
            }
        }
        return "";
    }
}
