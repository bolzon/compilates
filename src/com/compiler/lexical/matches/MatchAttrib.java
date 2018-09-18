package com.compiler.lexical.matches;

import com.compiler.common.Token;
import com.compiler.common.TokenType;
import com.compiler.lexical.analyzer.CodeReader;
import com.compiler.lexical.analyzer.LexicalException;

public class MatchAttrib extends MatchBase {

    public MatchAttrib(CodeReader codeReader) {
        super(codeReader);
    }

    public Token matchToken() throws LexicalException {
        Token token = null;

        while (mustRun()) {
            ch = codeReader.readChar();

            switch (getState()) {
            case Q1: // :
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
        if (ch == '=') {
            appendChar(ch);
        }
        else {
            codeReader.back();
            throw new LexicalException(createTokenError(), LexicalException.EQUAL_EXPECTED);
        }

        setFinalState();
    }

    private Token qf() {
        stopRun();
        return createToken(TokenType.OP_ATTRIB);
    }
}
