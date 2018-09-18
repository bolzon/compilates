package com.compiler.common;

import java.util.Arrays;
import java.util.List;

public class Token {

    private String lexeme = "";
    private int type = TokenType.UNKNOWN;

    public Token(int type, String lexeme) {
        setType(type);
        setLexeme(lexeme);
    }

    public boolean compareType(int type) {
        return this.type == type;
    }

    public boolean compareTypes(List<Integer> types) {
    	return types.contains(this.type);
    }

    public boolean compareTypes(Integer... types) {
    	return Arrays.asList(types).contains(this.type);
    }

    public boolean equals(Token token) {
        return type == token.type && lexeme.equals(token.lexeme);
    }

    public String getLexeme() {
        return lexeme;
    }

    public void setLexeme(String lexeme) {
        this.lexeme = lexeme;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        if (type < 0 || type > TokenType.EOF) {
            type = TokenType.UNKNOWN;
        }
        this.type = type;
    }

    public String getStringType() {
        return TokenType.getStringType(getType());
    }
}
