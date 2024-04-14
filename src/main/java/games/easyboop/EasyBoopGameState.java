package games.easyboop;

import core.AbstractGameState;
import core.AbstractParameters;
import core.components.Component;
import core.components.GridBoard;
import core.components.Token;
import games.GameType;

import java.util.List;

public class EasyBoopGameState extends AbstractGameState {

    GridBoard<Token> gridBoard;

    public EasyBoopGameState(AbstractParameters gameParameters, int nPlayers) {
        super(gameParameters, nPlayers);
    }

    @Override
    protected GameType _getGameType() { return GameType.EasyBoop; }

    @Override
    protected List<Component> _getAllComponents() {
        return List.of();
    }

    @Override
    protected AbstractGameState _copy(int playerId) {
        return null;
    }

    @Override
    protected double _getHeuristicScore(int playerId) {
        return 0;
    }

    @Override
    public double getGameScore(int playerId) {
        return 0;
    }

    @Override
    protected boolean _equals(Object o) {
        return false;
    }
}
