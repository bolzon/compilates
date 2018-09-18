package com.compiler.syntactic.analyzer;

import java.io.FileNotFoundException;
import java.io.IOException;

import com.compiler.common.Token;
import com.compiler.common.TokenType;
import com.compiler.lexical.analyzer.LexicalAnalyzer;

public class SyntacticAnalyzer {

    private Token tk;
    private LexicalAnalyzer la;

    public SyntacticAnalyzer(String file) throws FileNotFoundException, IOException {
        la = new LexicalAnalyzer(file);
    }

    public void startAnalysis() throws SyntacticException {
        procS();
    }

    /**
     * RULE 01
     */
    private void procS() throws SyntacticException {
        tk = la.nextToken();
        if (tk.compareType(TokenType.RW_PROGRAM)) {
            tk = la.nextToken();
            if (tk.compareType(TokenType.IDENTIFIER)) {
                tk = la.nextToken();
                if (tk.compareType(TokenType.END_COMMAND)) {
                    procBLOCO();

                    tk = la.nextToken();
                    if (tk.compareType(TokenType.RW_END_PROGRAM)) {
                        tk = la.nextToken();
                        if (tk.compareType(TokenType.END_COMMAND)) {
                            while (la.nextToken() != null);

                            // TODO: ACCEPTED!
                            return;
                        }
                    }
                }
            }
        }

        // TODO: ERRO!
        throw new SyntacticException(tk, SyntacticException.ILLEGAL_TOKEN);
    }

    /**
     * RULE 02 RULE 03
     */
    private void procBLOCO() throws SyntacticException {
        tk = la.nextToken();
        if (tk.compareType(TokenType.RW_BEGIN)) {
            procCMDS();

            tk = la.nextToken();
            if (tk.compareType(TokenType.RW_END)) {
                // TODO: ACCEPTED!
                return;
            }
        }
        else if (tk.compareTypes(
			TokenType.RW_IF, TokenType.RW_FOR,
			TokenType.RW_WHILE, TokenType.RW_DECLARE,
			TokenType.IDENTIFIER
	    )) {
            la.storeToken(tk);
            procCMD();

            // TODO: ACCEPTED!
            return;
        }

        // TODO: ERRO!
        throw new SyntacticException(tk, SyntacticException.ILLEGAL_TOKEN);
    }

    /**
     * RULE 04 RULE 05
     */
    private void procCMDS() throws SyntacticException {
        tk = la.nextToken();
        if (tk.compareTypes(
    		TokenType.RW_IF, TokenType.RW_FOR,
    		TokenType.RW_WHILE, TokenType.RW_DECLARE,
    		TokenType.IDENTIFIER
		)) {
            la.storeToken(tk);
            procCMD();
            procCMDS();
        }
        else { // {empty}
            la.storeToken(tk);
        }

        // TODO: ACCEPTED!
    }

    /**
     * RULE 06 RULE 07 RULE 08 RULE 09 RULE 10
     */
    private void procCMD() throws SyntacticException {
        tk = la.nextToken();
        if (tk.compareType(TokenType.RW_DECLARE)) {
            tk = la.nextToken();
            if (tk.compareType(TokenType.IDENTIFIER)) {
                tk = la.nextToken();
                if (tk.compareType(TokenType.DATA_TYPE)) {
                    tk = la.nextToken();
                    if (tk.compareType(TokenType.END_COMMAND)) {
                        // TODO: ACCEPTED!
                        return;
                    }
                }
            }
        }
        else if (tk.compareType(TokenType.IDENTIFIER)) {
            tk = la.nextToken();
            if (tk.compareType(TokenType.OP_ATTRIB)) {
                procEXP();

                tk = la.nextToken();
                if (tk.compareType(TokenType.END_COMMAND)) {
                    // TODO: ACCEPTED!
                    return;
                }
            }
        }
        else if (tk.compareType(TokenType.RW_FOR)) {
            tk = la.nextToken();
            if (tk.compareType(TokenType.BRACKET_LEFT)) {
                tk = la.nextToken();
                if (tk.compareType(TokenType.IDENTIFIER)) {
                    tk = la.nextToken();
                    if (tk.compareType(TokenType.OP_ATTRIB)) {
                        procVAL_N();
                        procOP_MD();
                        procOP_AS();

                        tk = la.nextToken();
                        if (tk.compareType(TokenType.RW_TO)) {
                            procVAL_N();
                            procOP_MD();
                            procOP_AS();

                            tk = la.nextToken();
                            if (tk.compareType(TokenType.BRACKET_RIGHT)) {
                                procBLOCO();

                                // TODO: ACCEPTED!
                                return;
                            }
                        }
                    }
                }
            }
        }
        else if (tk.compareType(TokenType.RW_IF)) {
            tk = la.nextToken();
            if (tk.compareType(TokenType.BRACKET_LEFT)) {
                procEXP_L();

                tk = la.nextToken();
                if (tk.compareType(TokenType.BRACKET_RIGHT)) {
                    tk = la.nextToken();
                    if (tk.compareType(TokenType.RW_THEN)) {
                        procBLOCO();
                        procCND_2();

                        // TODO: ACCEPTED!
                        return;
                    }
                }
            }
        }
        else if (tk.compareType(TokenType.RW_WHILE)) {
            tk = la.nextToken();
            if (tk.compareType(TokenType.BRACKET_LEFT)) {
                procEXP_L();

                tk = la.nextToken();
                if (tk.compareType(TokenType.BRACKET_RIGHT)) {
                    procBLOCO();

                    // TODO: ACCEPTED!
                    return;
                }
            }
        }

        // TODO: ERRO!
        throw new SyntacticException(tk, SyntacticException.ILLEGAL_TOKEN);
    }

    /**
     * RULE 11 RULE 12
     */
    private void procCND_2() throws SyntacticException {
        tk = la.nextToken();
        if (tk.compareType(TokenType.RW_ELSE)) {
            procBLOCO();
        }
        else { // {empty}
            la.storeToken(tk);
        }

        // TODO: ACCEPTED!
    }

    /**
     * RULE 13 RULE 14 RULE 15 RULE 16
     */
    private void procEXP() throws SyntacticException {
        tk = la.nextToken();
        if (tk.compareType(TokenType.IDENTIFIER)) {
            procOP_T();

            // TODO: ACCEPTED!
            return;
        }
        else if (tk.compareType(TokenType.LOGIC_VALUE)) {
            procOP_LX();

            // TODO: ACCEPTED!
            return;
        }
        else if (tk.compareType(TokenType.NUMBER)) {
            procOP_MD();
            procOP_AS();
            procOP_R();

            // TODO: ACCEPTED!
            return;
        }
        else if (tk.compareType(TokenType.BRACKET_LEFT)) {
            procVAL_N();
            procOP_MD();
            procOP_AS();

            tk = la.nextToken();
            if (tk.compareType(TokenType.BRACKET_RIGHT)) {
                procOP_MD();
                procOP_AS();
                procOP_R();

                // TODO: ACCEPTED!
                return;
            }
        }

        // TODO: ERRO!
        throw new SyntacticException(tk, SyntacticException.ILLEGAL_TOKEN);
    }

    /**
     * RULE 17 RULE 18 RULE 19 RULE 20
     */
    private void procEXP_L() throws SyntacticException {
        tk = la.nextToken();
        if (tk.compareType(TokenType.NUMBER)) {
            procOP_MD();
            procOP_AS();

            tk = la.nextToken();
            if (tk.compareType(TokenType.OP_RELATIONAL)) {
                procVAL_N();
                procOP_MD();
                procOP_AS();
                procOP_LY();

                // TODO: ACCEPTED!
                return;
            }
        }
        else if (tk.compareType(TokenType.IDENTIFIER)) {
            procOP_T();

            // TODO: ACCEPTED!
            return;
        }
        else if (tk.compareType(TokenType.BRACKET_LEFT)) {
            procVAL_N();
            procOP_MD();
            procOP_AS();

            tk = la.nextToken();
            if (tk.compareType(TokenType.BRACKET_RIGHT)) {
                procOP_MD();
                procOP_AS();

                tk = la.nextToken();
                if (tk.compareType(TokenType.OP_RELATIONAL)) {
                    procVAL_N();
                    procOP_MD();
                    procOP_AS();
                    procOP_LY();

                    // TODO: ACCEPTED!
                    return;
                }
            }
        }
        else if (tk.compareType(TokenType.LOGIC_VALUE)) {
            procOP_LX();

            // TODO: ACCEPTED!
            return;
        }

        // TODO: ERRO!
        throw new SyntacticException(tk, SyntacticException.ILLEGAL_TOKEN);
    }

    /**
     * RULE 21 RULE 22 RULE 23
     */
    private void procVAL_N() throws SyntacticException {
        tk = la.nextToken();
        if (tk.compareType(TokenType.IDENTIFIER) || tk.compareType(TokenType.NUMBER)) {
            // TODO: ACCEPTED!
            return;
        }
        else if (tk.compareType(TokenType.BRACKET_LEFT)) {
            procVAL_N();
            procOP_MD();
            procOP_AS();

            tk = la.nextToken();
            if (tk.compareType(TokenType.BRACKET_RIGHT)) {
                // TODO: ACCEPTED!
                return;
            }
        }

        // TODO: ERRO!
        throw new SyntacticException(tk, SyntacticException.ILLEGAL_TOKEN);
    }

    /**
     * RULE 24 RULE 25
     */
    private void procOP_MD() throws SyntacticException {
        tk = la.nextToken();
        if (tk.compareType(TokenType.OP_MULT_DIV)) {
            procVAL_N();
            procOP_MD();
        }
        else // {empty}
        {
            la.storeToken(tk);
        }

        // TODO: ACCEPTED!
    }

    /**
     * RULE 26 RULE 27
     */
    private void procOP_AS() throws SyntacticException {
        tk = la.nextToken();
        if (tk.compareType(TokenType.OP_ADD_SUB)) {
            procVAL_N();
            procOP_MD();
            procOP_AS();
        }
        else { // {empty}
            la.storeToken(tk);
        }

        // TODO: ACCEPTED!
    }

    /**
     * RULE 28 RULE 29
     */
    private void procOP_LX() throws SyntacticException {
        tk = la.nextToken();
        if (tk.compareType(TokenType.OP_LOGIC)) {
            procEXP_L();
        }
        else { // {empty}
            la.storeToken(tk);
        }

        // TODO: ACCEPTED!
    }

    /**
     * RULE 30 RULE 31
     */
    private void procOP_LY() throws SyntacticException {
        tk = la.nextToken();
        if (tk.compareType(TokenType.OP_LOGIC)) {
            procVAL_N();
            procOP_MD();
            procOP_AS();

            tk = la.nextToken();
            if (tk.compareType(TokenType.OP_RELATIONAL)) {
                procVAL_N();
                procOP_MD();
                procOP_AS();

                // TODO: ACCEPTED!
                return;
            }
        }
        else { // {empty}
            la.storeToken(tk);

            // TODO: ACCEPTED!
            return;
        }

        // TODO: ERRO!
        throw new SyntacticException(tk, SyntacticException.ILLEGAL_TOKEN);
    }

    /**
     * RULE 32 RULE 33
     */
    private void procOP_R() throws SyntacticException {
        tk = la.nextToken();
        if (tk.compareType(TokenType.OP_RELATIONAL)) {
            procVAL_N();
            procOP_MD();
            procOP_AS();
            procOP_LY();
        }
        else { // {empty}
            la.storeToken(tk);
        }

        // TODO: ACCEPTED!
    }

    /**
     * RULE 34 RULE 35 RULE 36 RULE 37 RULE 38
     */
    private void procOP_T() throws SyntacticException {
        tk = la.nextToken();
        if (tk.compareType(TokenType.OP_RELATIONAL)) {
            procVAL_N();
            procOP_MD();
            procOP_AS();
            procOP_LY();
        }
        else if (tk.compareType(TokenType.OP_LOGIC)) {
            procEXP_L();
        }
        else if (tk.compareType(TokenType.OP_ADD_SUB)) {
            procVAL_N();
            procOP_MD();
            procOP_AS();
            procOP_R();
        }
        else if (tk.compareType(TokenType.OP_MULT_DIV)) {
            procVAL_N();
            procOP_MD();
            procOP_AS();
            procOP_R();
        }
        else { // {empty}
            la.storeToken(tk);
        }

        // TODO: ACCEPTED!
    }
}
