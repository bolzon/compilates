package com.compiler.lexical.matches;

import com.compiler.common.Token;
import com.compiler.common.TokenType;
import com.compiler.lexical.analyzer.CodeReader;
import com.compiler.lexical.analyzer.LexicalException;

public abstract class MatchBase {

    protected static final int FINAL_STATE = 0XFFFF;

    private int state;
    private String lexeme;
    private boolean runMatch;

    protected char ch;
    protected CodeReader codeReader;

    public MatchBase(CodeReader codeReader) {
        setState(0);
        clearLexeme();
        appendChar(codeReader.readPreviousChar());

        this.ch = 0;
        this.runMatch = true;
        this.codeReader = codeReader;
    }

    public abstract Token matchToken() throws LexicalException;

    protected Token createTokenError() {
        return createToken(TokenType.ERROR);
    }

    protected Token createToken(int type) {
        return new Token(type, getLexeme());
    }

    protected void clearLexeme() {
        lexeme = "";
    }

    protected String getLexeme() {
        return lexeme;
    }

    protected void stopRun() {
        runMatch = false;
    }

    protected boolean mustRun() {
        return runMatch;
    }

    protected int getState() {
        return state;
    }

    protected void setState(int state) {
        if (state < 0) {
            state = 0;
        }

        this.state = state;
    }

    protected void setFinalState() {
        codeReader.back();
        state = FINAL_STATE;
    }

    protected void appendChar(char ch) {
        lexeme += ch;
    }

    protected static final int Q1 = 0;
    protected static final int Q2 = 1;
    protected static final int Q3 = 2;
    protected static final int Q4 = 3;
    protected static final int Q5 = 4;
    protected static final int Q6 = 5;
    protected static final int Q7 = 6;
    protected static final int Q8 = 7;
    protected static final int Q9 = 8;
    protected static final int Q10 = 9;
    protected static final int Q11 = 10;
    protected static final int Q12 = 11;
    protected static final int Q13 = 12;
    protected static final int Q14 = 13;
    protected static final int Q15 = 14;
    protected static final int Q16 = 15;
    protected static final int Q17 = 16;
    protected static final int Q18 = 17;
    protected static final int Q19 = 18;
    protected static final int Q20 = 19;
}
