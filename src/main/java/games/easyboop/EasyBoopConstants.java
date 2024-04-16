package games.easyboop;

import core.components.Token;

import java.util.ArrayList;

public class EasyBoopConstants {
    public static final ArrayList<Token> playerMapping = new ArrayList<Token>() {{
        add(new Token("x"));
        add(new Token("o"));
    }};
    public static final String emptyCell = ".";

    public static int getPlayerFromPiece(Token token) {
        return playerMapping.indexOf(token);
    }
}