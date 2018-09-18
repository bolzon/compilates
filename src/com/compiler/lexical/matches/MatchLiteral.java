package com.compiler.lexical.matches;

import com.compiler.common.Token;
import com.compiler.common.TokenType;
import com.compiler.lexical.analyzer.CodeReader;
import com.compiler.lexical.analyzer.LexicalException;

public class MatchLiteral extends MatchBase {

    public MatchLiteral(CodeReader codeReader) {
        super(codeReader);
    }

    public Token matchToken() throws LexicalException {
        clearLexeme();
        Token token = null;

        while (mustRun()) {
            ch = codeReader.readChar();

            switch (getState()) {
            case Q1: // "
                q1();
                break;

            case FINAL_STATE:
            default:
                token = qf();
            }
        }

        return token;
    }

    private void q1() throws LexicalException {
        while (isLiteral(ch)) {
            appendChar(ch);
            ch = codeReader.readChar();
        }

        if (ch == '"') {
            setFinalState();
        }
        else {
            codeReader.back();
            throw new LexicalException(createTokenError(), LexicalException.ILLEGAL_CHARACTER);
        }
    }

    private Token qf() {
        stopRun();
        return createToken(TokenType.LITERAL);
    }

    private boolean isLiteral(char ch) {
        return ch != '\"' && ch != '\n' && ch != '\r';
    }
}
