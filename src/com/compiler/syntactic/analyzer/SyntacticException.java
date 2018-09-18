package com.compiler.syntactic.analyzer;
import com.compiler.common.Token;

public class SyntacticException extends Exception {

    private int type;
    private Token token;
    private String message;
    private static final long serialVersionUID = 1L;

    public static final int UNKNOWN_EXCEPTION = 0;
    public static final int ILEGAL_TOKEN = 1;

    public SyntacticException(Token token, int type) {
        this.type = type;
        this.token = token;
        setMessageByType(type);
    }

    public SyntacticException(Token token) {
        this.token = token;
        type = SyntacticException.UNKNOWN_EXCEPTION;
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
        case SyntacticException.ILEGAL_TOKEN:
            setMessage("illegal token");
            break;

        case SyntacticException.UNKNOWN_EXCEPTION:
        default:
            setMessage("unknown error");
        }
    }
}
