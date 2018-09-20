package com.compiler.lexical.analyzer;

import java.io.FileNotFoundException;
import java.io.IOException;

import com.compiler.common.Token;
import com.compiler.common.TokenType;
import com.compiler.lexical.matches.MatchAttrib;
import com.compiler.lexical.matches.MatchBase;
import com.compiler.lexical.matches.MatchIdentifierOrKeyword;
import com.compiler.lexical.matches.MatchLiteral;
import com.compiler.lexical.matches.MatchNumber;
import com.compiler.lexical.matches.MatchRelationalOperator;
import com.compiler.status.StatusManager;
import com.compiler.status.StatusTransfer;

public class LexicalAnalyzer {
    
    private Token tokenBuffer = null;
    private CodeReader codeReader;
    private final char EOF = (char) -1;

    public LexicalAnalyzer(String filePath) throws FileNotFoundException, IOException {
        codeReader = new CodeReader(filePath);
    }

    public Token nextToken() {
        Token token = null;
        MatchBase matcher = null;

        if (hasTokenBuffer()) {
            return pollTokenBuffer();
        }

        char ch = codeReader.readChar();

        try {
            if (isNumber(ch)) {
                matcher = new MatchNumber(codeReader);
            }
            else if (isLiteral(ch)) {
                matcher = new MatchLiteral(codeReader);
            }
            else if (isAttrib(ch)) {
                matcher = new MatchAttrib(codeReader);
            }
            else if (isRelationalOperator(ch)) {
                matcher = new MatchRelationalOperator(codeReader);
            }
            else if (isIdentifierOrKeyword(ch)) {
                matcher = new MatchIdentifierOrKeyword(codeReader);
            }
            else if (isOperator(ch)) {
                token = createOperatorToken(ch);
            }
            else if (Character.isWhitespace(ch)) {
                token = consumeWhiteSpaces();
            }
            else if (isComment(ch)) {
                token = consomeComment();
            }
            else {
                throw new LexicalException(null, LexicalException.ILLEGAL_CHARACTER);
            }

            if (matcher != null) {
                token = matcher.matchToken();
            }
        }
        catch (LexicalException le) {
            StatusManager sm = StatusTransfer.getInstance().getStatusManager();
            sm.appendStatus(codeReader.getFileName() + "(" + String.valueOf(codeReader.getLine()) + ") error '"
                    + codeReader.readPreviousChar() + "' : " + le.getMessage());

            token = nextToken();
        }

        return token;
    }

    public Token storeToken(Token token) {
        Token lastToken = tokenBuffer;

        if (token != null) {
            tokenBuffer = token;
        }

        return lastToken;
    }

    private Token pollTokenBuffer() {
        Token token = tokenBuffer;
        tokenBuffer = null;
        return token;
    }

    private boolean hasTokenBuffer() {
        return tokenBuffer != null;
    }

    private boolean isNumber(char ch) {
        return Character.isDigit(ch);
    }
    
    private boolean isLiteral(char ch) {
        return ch == '\"';
    }

    private boolean isAttrib(char ch) {
        return ch == ':';
    }

    private boolean isRelationalOperator(char ch) {
        return ch == '&' || ch == '!';
    }

    private boolean isOperator(char ch) {
        return TokenType.OPERATORS.indexOf(ch) != -1;
    }

    private boolean isIdentifierOrKeyword(char ch) {
        return Character.isLetter(ch) || ch == '_';
    }

    private boolean isComment(char ch) {
        return ch == '#';
    }

    private Token consumeWhiteSpaces() {
        char ch;

        do ch = codeReader.readChar();
        while (Character.isWhitespace(ch) && ch != EOF);

        if (ch == EOF) {
            return null;
        }

        codeReader.back();
        return nextToken();
    }

    private Token consomeComment() throws LexicalException {
        if (codeReader.readChar() != '-') {
            codeReader.back();
            throw new LexicalException(null, LexicalException.ILLEGAL_CHARACTER);
        }

        char ch;
        boolean readComment = true;

        while (readComment) {
            ch = codeReader.readChar();
            if (ch == '-') {
                ch = codeReader.readChar();
                if (ch == '#') {
                    readComment = false;
                }
                else {
                    codeReader.back();
                }
            }
            else if (ch == EOF) {
                throw new LexicalException(null, LexicalException.COMMENT_NOT_CLOSED);
            }
        }

        return nextToken();
    }

    private Token createOperatorToken(char ch) {
        String lexeme = String.valueOf(ch);
        int type = TokenType.getTypeByLexeme(lexeme);
        return new Token(type, lexeme);
    }
}
