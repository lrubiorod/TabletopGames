package games.easyboop;

import core.AbstractParameters;

import java.util.Objects;

public class EasyBoopGameParameters extends AbstractParameters {

    public int gridSize = 6;

    public EasyBoopGameParameters() {
    }

    @Override
    protected AbstractParameters _copy() {
        EasyBoopGameParameters copy = new EasyBoopGameParameters();
        copy.gridSize = gridSize;

        return copy;
    }

    @Override
    protected boolean _equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        EasyBoopGameParameters that = (EasyBoopGameParameters) o;
        return gridSize == that.gridSize;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), gridSize);
    }
}
