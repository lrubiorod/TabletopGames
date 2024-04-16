package games.easyboop;

import core.AbstractParameters;

import java.util.Objects;

public class EasyBoopGameParameters extends AbstractParameters {

    public int gridSize = 6;
    public int winCount = 3;

    public EasyBoopGameParameters() {
    }

    @Override
    protected AbstractParameters _copy() {
        EasyBoopGameParameters copy = new EasyBoopGameParameters();
        copy.gridSize = gridSize;
        copy.winCount = winCount;

        return copy;
    }

    @Override
    protected boolean _equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        EasyBoopGameParameters that = (EasyBoopGameParameters) o;
        return (gridSize == that.gridSize) && (winCount == that.winCount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), gridSize);
    }
}
