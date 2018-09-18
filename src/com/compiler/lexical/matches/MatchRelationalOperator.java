package com.compiler.lexical.matches;

import com.compiler.common.Token;
import com.compiler.common.TokenType;
import com.compiler.lexical.analyzer.CodeReader;
import com.compiler.lexical.analyzer.LexicalException;

public class MatchRelationalOperator extends MatchBase {

    // (\!\=|\&(dif|eq|[gl][te]))

    public MatchRelationalOperator(CodeReader codeReader) {
        super(codeReader);
    }

    public Token matchToken() throws LexicalException {
        Token token = null;
        if (codeReader.readPreviousChar() == '&') {
            setState(Q2);
        }

        while (mustRun()) {
            ch = codeReader.readChar();

            switch (getState()) {
            case Q1: // !
                q1();
                break;

            case Q2: // &
                q2();
                break;

            case Q3: // d
                q3();
                break;

            case Q4: // i
                q4();
                break;

            case Q5: // e
                q5();
                break;

            case Q6: // [gl]
                q6();
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
            setFinalState();
        }
        else {
            codeReader.back();
            throw new LexicalException(createTokenError(), LexicalException.ILLEGAL_CHARACTER);
        }
    }

    private void q2() throws LexicalException {
        if (ch == 'd') {
            setState(Q3);
            appendChar(ch);
        }
        else if (ch == 'e') {
            setState(Q5);
            appendChar(ch);
        }
        else if ((ch == 'g') || (ch == 'l')) {
            setState(Q6);
            appendChar(ch);
        }
        else {
            codeReader.back();
            throw new LexicalException(createTokenError(), LexicalException.ILLEGAL_CHARACTER);
        }
    }

    private void q3() throws LexicalException {
        if (ch == 'i') {
            appendChar(ch);
            setState(Q4);
        }
        else {
            codeReader.back();
            throw new LexicalException(createTokenError(), LexicalException.ILLEGAL_CHARACTER);
        }
    }

    private void q4() throws LexicalException {
        if (ch == 'f') {
            appendChar(ch);
            setFinalState();
        }
        else {
            codeReader.back();
            throw new LexicalException(createTokenError(), LexicalException.ILLEGAL_CHARACTER);
        }
    }

    private void q5() throws LexicalException {
        if (ch == 'q') {
            appendChar(ch);
            setFinalState();
        }
        else {
            codeReader.back();
            throw new LexicalException(createTokenError(), LexicalException.ILLEGAL_CHARACTER);
        }
    }

    private void q6() throws LexicalException {
        if ((ch == 't') || (ch == 'e')) {
            appendChar(ch);
            setFinalState();
        }
        else {
            codeReader.back();
            throw new LexicalException(createTokenError(), LexicalException.ILLEGAL_CHARACTER);
        }
    }

    private Token qf() {
        String lexeme = getLexeme();
        int tokenType = TokenType.getTypeByLexeme(lexeme);

        stopRun();
        return createToken(tokenType);
    }
}
