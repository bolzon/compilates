package com.compiler.lexical.analyzer;
import com.compiler.common.Token;

public class LexicalException extends Exception {

    private int type;
    private Token token;
    private String message;
    private static final long serialVersionUID = 1L;

    public static final int UNKNOWN_EXCEPTION = 0;
    public static final int ILLEGAL_CHARACTER = 1;
    public static final int COMMENT_NOT_CLOSED = 2;
    public static final int EQUAL_EXPECTED = 3;
    public static final int DIGIT_EXPECTED = 4;
    public static final int DIGIT_OR_SIGNAL_EXPECTED = 5;

    public LexicalException(Token token, int type) {
        this.type = type;
        this.token = token;
        setMessageByType(type);
    }

    public LexicalException(Token token) {
        this.token = token;
        type = LexicalException.UNKNOWN_EXCEPTION;
        setMessageByType(type);
    }

    public Token getToken() {
        return token;
    }

    public int getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    private void setMessage(String message) {
        this.message = message;
    }

    private void setMessageByType(int type) {
        switch (type) {
        case LexicalException.ILLEGAL_CHARACTER:
            setMessage("illegal character");
            break;

        case LexicalException.COMMENT_NOT_CLOSED:
            setMessage("comment not closed");
            break;

        case LexicalException.EQUAL_EXPECTED:
            setMessage("'=' expected");
            break;

        case LexicalException.DIGIT_EXPECTED:
            setMessage("digit expected");
            break;

        case LexicalException.DIGIT_OR_SIGNAL_EXPECTED:
            setMessage("digit, '+' or '-' expected");
            break;

        case LexicalException.UNKNOWN_EXCEPTION:
        default:
            setMessage("unknown error");
        }
    }
}
