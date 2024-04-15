package games.easyboop;

import core.AbstractGameState;
import core.AbstractParameters;
import core.components.Component;
import core.components.GridBoard;
import core.components.Token;
import games.GameType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EasyBoopGameState extends AbstractGameState {

    GridBoard<Token> gridBoard;
    int n1; // Player1's pieces on the board
    int n2; // Player2's pieces on the board

    public EasyBoopGameState(AbstractParameters gameParameters, int nPlayers) {
        super(gameParameters, nPlayers);
    }

    @Override
    protected GameType _getGameType() { return GameType.EasyBoop; }

    @Override
    protected List<Component> _getAllComponents() {
        return new ArrayList<Component>() {{
            add(gridBoard);
        }};
    }

    @Override
    protected AbstractGameState _copy(int playerId) {
        EasyBoopGameState copy = new EasyBoopGameState(gameParameters.copy(), getNPlayers());
        copy.gridBoard = gridBoard.copy();
        copy.n1 = n1;
        copy.n2 = n2;

        return copy;
    }

    @Override
    protected double _getHeuristicScore(int playerId) {
        if (isNotTerminal()) {
            // TODO calculate an approximate value
            return 0;
        } else {
            // The game finished, we can instead return the actual result of the game for the given player.
            return getPlayerResults()[playerId].value;
        }
    }

    @Override
    public double getGameScore(int playerId) {
        return 0;
    }

    @Override
    protected boolean _equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EasyBoopGameState)) return false;
        if (!super.equals(o)) return false;
        EasyBoopGameState that = (EasyBoopGameState) o;
        return Objects.equals(gridBoard, that.gridBoard) && n1 == that.n1 && n2 == that.n2;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("    0   1   2   3   4   5\n");
        for (int i = 0; i < gridBoard.getHeight(); i++) {
            sb.append(i).append(" | ");
            for (int j = 0; j < gridBoard.getWidth(); j++) {
                sb.append(gridBoard.getElement(i, j)).append(" | ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}