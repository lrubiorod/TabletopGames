package games.easyboop;

import core.AbstractGameState;
import core.AbstractParameters;
import core.components.Component;
import core.components.GridBoard;
import core.components.Token;
import core.interfaces.IGridGameState;
import core.interfaces.IPrintable;
import games.GameType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EasyBoopGameState extends AbstractGameState implements IPrintable, IGridGameState<Token> {

    GridBoard<Token> gridBoard;
    int[] piecesCounter;

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
        copy.piecesCounter = piecesCounter.clone();

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
        return playerResults[playerId].value;
    }

    @Override
    protected boolean _equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EasyBoopGameState)) return false;
        if (!super.equals(o)) return false;
        EasyBoopGameState that = (EasyBoopGameState) o;
        return Objects.equals(gridBoard, that.gridBoard) && Objects.equals(piecesCounter, that.piecesCounter);
    }

    @Override
    public String toString() {
        return Objects.hash(gameParameters) + "|" +
                Objects.hash(getAllComponents()) + "|" +
                Objects.hash(gameStatus) + "|" +
                Objects.hash(gamePhase) + "|*|" +
                Objects.hash(gridBoard);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), gridBoard);
    }

    @Override
    public GridBoard<Token> getGridBoard() {
        return gridBoard;
    }

    @Override
    public void printToConsole() {
        System.out.println(gridBoard.toString());
    }
}