package com.compiler.lexical.matches;

import com.compiler.common.Token;
import com.compiler.common.TokenType;
import com.compiler.lexical.analyzer.CodeReader;
import com.compiler.lexical.analyzer.LexicalException;

public class MatchNumber extends MatchBase {

    // [0-9]+(\.[0-9]+)?([eE][\+\-]?[0-9]+)?

    public MatchNumber(CodeReader codeReader) {
        super(codeReader);
    }

    public Token matchToken() throws LexicalException {
        Token token = null;

        while (mustRun()) {
            ch = codeReader.readChar();

            switch (getState()) {
            case Q1: // numbers
                q1();
                break;

            case Q2: // commas
                q2();
                break;

            case Q3: // numbers
                q3();
                break;

            case Q4: // cientific notation
                q4();
                break;

            case Q5: // + / -
                q5();
                break;

            case Q6: // numbers
                q6();
                break;

            case FINAL_STATE:
            default:
                token = qf();
            }
        }

        return token;
    }

    private void q1() {
        if (Character.isDigit(ch)) {
            setState(Q1);
            appendChar(ch);
        }
        else if (ch == '.') {
            setState(Q2);
            appendChar(ch);
        }
        else if (Character.toUpperCase(ch) == 'E') {
            setState(Q4);
            appendChar(ch);
        }
        else {
            setFinalState();
        }
    }

    private void q2() throws LexicalException {
        if (Character.isDigit(ch)) {
            setState(Q3);
            appendChar(ch);
        }
        else {
            codeReader.back(2);
            throw new LexicalException(createTokenError(), LexicalException.DIGIT_EXPECTED);
        }
    }

    private void q3() {
        if (Character.isDigit(ch)) {
            setState(Q3);
            appendChar(ch);
        }
        else if (Character.toUpperCase(ch) == 'E') {
            setState(Q4);
            appendChar(ch);
        }
        else {
            setFinalState();
        }
    }

    private void q4() throws LexicalException {
        if ((ch == '+') || (ch == '-')) {
            setState(Q5);
            appendChar(ch);
        }
        else if (Character.isDigit(ch)) {
            setState(Q6);
            appendChar(ch);
        }
        else {
            codeReader.back(2);
            throw new LexicalException(createTokenError(), LexicalException.DIGIT_OR_SIGNAL_EXPECTED);
        }
    }

    private void q5() throws LexicalException {
        if (Character.isDigit(ch)) {
            setState(Q6);
            appendChar(ch);
        }
        else {
            codeReader.back(2);
            throw new LexicalException(createTokenError(), LexicalException.DIGIT_EXPECTED);
        }
    }

    private void q6() {
        if (Character.isDigit(ch)) {
            setState(Q6);
            appendChar(ch);
        }
        else {
            setFinalState();
        }
    }

    private Token qf() {
        stopRun();
        codeReader.back();
        return createToken(TokenType.NUMBER);
    }
}
