package com.compiler.common;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class TokenTable extends HashMap<String, Token> {

	private static TokenTable instance = null;
    private static final long serialVersionUID = 1L;

    static {
        instance = new TokenTable();
    }

    private TokenTable() {
        clear();
        loadKeywords();
    }

    public static TokenTable getInstance() {
        return instance;
    }

    public void insert(String lexeme) {
        if (!contains(lexeme)) {
            Token token = new Token(TokenType.IDENTIFIER, lexeme);
            put(lexeme, token);
        }
    }

    public boolean contains(String lexeme) {
        return get(lexeme) != null;
    }

    public boolean contains(Token token) {
        return contains(token.getLexeme());
    }

    private void loadKeywords() {
        List<Token> tks = TokenType.getKeywords();
        Iterator<Token> i = tks.iterator();

        while (i.hasNext()) {
            Token token = i.next();
            put(token.getLexeme(), token);
        }
    }
}
