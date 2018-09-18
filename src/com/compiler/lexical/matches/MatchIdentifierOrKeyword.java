package com.compiler.lexical.matches;

import com.compiler.common.Token;
import com.compiler.common.TokenTable;
import com.compiler.common.TokenType;
import com.compiler.lexical.analyzer.CodeReader;

public class MatchIdentifierOrKeyword extends MatchBase {

    public MatchIdentifierOrKeyword(CodeReader codeReader) {
        super(codeReader);
    }

    public Token matchToken() {
        Token token = null;

        while (mustRun()) {
            ch = codeReader.readChar();

            switch (getState()) {
            case Q1: // letters, digits or underscore
                q1();
                break;

            case FINAL_STATE:
            default:
                token = qf();
            }
        }

        return token;
    }

    private void q1() {
        while (isIdentifier(ch)) {
            appendChar(ch);
            ch = codeReader.readChar();
        }

        setFinalState();
    }

    private Token qf() {
        String lexeme = getLexeme();
        TokenTable tokenTable = TokenTable.getInstance();
        Token token = tokenTable.get(lexeme);

        if (token == null) {
            token = new Token(TokenType.IDENTIFIER, lexeme);
            tokenTable.put(lexeme, token);
        }

        stopRun();
        codeReader.back();

        return token;
    }

    private boolean isIdentifier(char ch) {
        return Character.isLetterOrDigit(ch) || ch == '_';
    }
}
