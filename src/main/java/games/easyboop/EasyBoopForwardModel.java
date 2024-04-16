package games.easyboop;

import core.AbstractGameState;
import core.CoreConstants;
import core.actions.AbstractAction;
import core.actions.ActionSpace;
import core.actions.SetGridValueAction;
import core.components.GridBoard;
import core.components.Token;
import core.forwardModels.SequentialActionForwardModel;
import utilities.Vector2D;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EasyBoopForwardModel extends SequentialActionForwardModel {
    @Override
    protected void _setup(AbstractGameState firstState) {
        EasyBoopGameParameters gp = (EasyBoopGameParameters) firstState.getGameParameters();
        int gridSize = gp.gridSize;
        EasyBoopGameState state = (EasyBoopGameState) firstState;
        state.gridBoard = new GridBoard<Token>(gridSize, gridSize, new Token(EasyBoopConstants.emptyCell));
        state.piecesCounter = new int[state.getNPlayers()];
    }

    @Override
    protected List<AbstractAction> _computeAvailableActions(AbstractGameState gameState) {
        return _computeAvailableActions(gameState, ActionSpace.Default);
    }

    protected List<AbstractAction> _computeAvailableActions(AbstractGameState gameState, ActionSpace actionSpace) {
        EasyBoopGameState state = (EasyBoopGameState) gameState;
        ArrayList<AbstractAction> actions = new ArrayList<>();
        int player = state.getCurrentPlayer();

        if (state.isNotTerminal()){
            // Normal action space
            for (int i = 0; i < state.gridBoard.getWidth(); i++) {
                for (int j = 0; j < state.gridBoard.getHeight(); j++) {
                    if (emptyPosition(i, j, state)){
                        actions.add(putPiece(i, j, player, state));
                    }
                }
            }
        }
        return actions;
    }

    @Override
    protected void _afterAction(AbstractGameState currentState, AbstractAction action) {
        EasyBoopGameState state = (EasyBoopGameState) currentState;
        ArrayList<Vector2D> posToCheck =  new ArrayList<>();

        if (action instanceof SetGridValueAction<?> gridValueAction) {
            Vector2D actionPos = new Vector2D(gridValueAction.getX(), gridValueAction.getY());
            state.piecesCounter[state.getCurrentPlayer()] += 1;
            posToCheck.add(actionPos);

            ArrayList<Vector2D> shiftedPositions = shiftAdjacentPieces(actionPos.getX(), actionPos.getY(), state);
            posToCheck.addAll(shiftedPositions);
        }

        if (checkGameEnd(posToCheck, state)) {
            return;
        }
        super._afterAction(currentState, action);
    }

    private boolean checkGameEnd(ArrayList<Vector2D> movements, EasyBoopGameState state) {
        EasyBoopGameParameters gp = (EasyBoopGameParameters) state.getGameParameters();

        boolean[] winner =  new boolean[state.getNPlayers()];

        if (state.piecesCounter[state.getCurrentPlayer()] == 8) {
            winner[state.getCurrentPlayer()] = true;
        }

        for (Vector2D move : movements) {
            int row = move.getX();
            int col = move.getY();
            Token pieceType = state.gridBoard.getElement(row, col);
            int ownerPlayer = EasyBoopConstants.getPlayerFromPiece(pieceType);

            if (winner[ownerPlayer] || state.piecesCounter[ownerPlayer] < 3 ) {
                continue;
            }

            // Check Horizontal
            if (checkLineWinner(row, Math.max(col - (gp.winCount - 1), 0), 0, 1, pieceType, gp.winCount, state)) {
                winner[ownerPlayer] = true;
            }

            // Check Vertical
            if (checkLineWinner(Math.max(row - (gp.winCount - 1), 0), col, 1, 0, pieceType, gp.winCount, state)) {
                winner[ownerPlayer] = true;
            }

            // Check 2 diagonals
            for (int[] direction : new int[][]{{1, 1}, {1, -1}}) {
                int startRow = row;
                int startCol = col;
                for (int i = 0; i < gp.winCount - 1; i++) {
                    if (isValidPosition(startRow - direction[0], startCol - direction[1], state)) {
                        startRow -= direction[0];
                        startCol -= direction[1];
                    }
                }
                if (checkLineWinner(startRow, startCol, direction[0], direction[1], pieceType, gp.winCount, state)) {
                    winner[ownerPlayer] = true;
                }
            }
        }

        if (winner[0] && winner[1]) {
            registerTie(state);

            return true;
        } else if (winner[0]) {
            registerWinner(state, 0);

            return true;
        } else if (winner[1]) {
            registerWinner(state, 1);

            return true;
        }

        return false;
    }

    public ArrayList<Vector2D> shiftAdjacentPieces(int row, int col, EasyBoopGameState state) {
        ArrayList<Vector2D> shiftedPieces = new ArrayList<>();
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}, {-1, -1}, {-1, 1}, {1, -1}, {1, 1}};

        for (int[] dir : directions) {
            int dr = dir[0];
            int dc = dir[1];
            int adjRow = row + dr;
            int adjCol = col + dc;

            if (isValidPosition(adjRow, adjCol, state) && !emptyPosition(adjRow, adjCol, state)) {
                Vector2D newPosition = shiftPiece(adjRow, adjCol, dr, dc, state);
                if (newPosition != null) {
                    shiftedPieces.add(newPosition);
                }
            }
        }

        return shiftedPieces;
    }

    private Vector2D shiftPiece(int row, int col, int dr, int dc, EasyBoopGameState state) {
        Token shifted_piece = state.gridBoard.getElement(row, col);
        int targetRow = row + dr;
        int targetCol = col + dc;

        // The piece falls
        if (!isValidPosition(targetRow, targetCol, state)) {
            state.gridBoard.setElement(row, col, new Token(EasyBoopConstants.emptyCell));
            state.piecesCounter[EasyBoopConstants.getPlayerFromPiece(shifted_piece)] -= 1;

        // The piece is shifted
        } else if (emptyPosition(targetRow, targetCol, state)) {
            state.gridBoard.setElement(targetRow, targetCol, shifted_piece.copy());
            state.gridBoard.setElement(row, col, new Token(EasyBoopConstants.emptyCell));
            return new Vector2D(targetRow, targetCol);
        }

        return null;
    }

    private boolean checkLineWinner(int startRow, int startCol, int deltaRow, int deltaCol, Token piece, int maxCount, EasyBoopGameState state) {
        int count = 0;
        int row = startRow;
        int col = startCol;
        for (int i = 0; i < maxCount + 2; i++) {
            if (isValidPosition(row, col, state) && state.gridBoard.getElement(row, col).equals(piece)) {
                count++;
                if (count == maxCount) {
                    return true;
                }
            } else {
                count = 0;
            }
            row += deltaRow;
            col += deltaCol;
        }
        return false;
    }

    private boolean isValidPosition(int row, int col, EasyBoopGameState state) {
        int height = state.gridBoard.getHeight();
        int width = state.gridBoard.getWidth();
        return 0 <= row && row < height && 0 <= col && col < width;
    }

    private boolean emptyPosition (int row, int col, EasyBoopGameState state) {
        return state.gridBoard.getElement(row, col).getTokenType().equals(EasyBoopConstants.emptyCell);
    }

    private SetGridValueAction<Token> putPiece(int row, int col, int player, EasyBoopGameState state) {
        return new SetGridValueAction<Token>(state.gridBoard.getComponentID(), row, col, EasyBoopConstants.playerMapping.get(player));
    }

    private void registerWinner(EasyBoopGameState state, int winningPlayer) {
        state.setGameStatus(CoreConstants.GameResult.GAME_END);
        state.setPlayerResult(CoreConstants.GameResult.WIN_GAME, winningPlayer);
        state.setPlayerResult(CoreConstants.GameResult.LOSE_GAME, 1 - winningPlayer);
    }

    private void registerTie(EasyBoopGameState state) {
        state.setGameStatus(CoreConstants.GameResult.DRAW_GAME);
        Arrays.fill(state.getPlayerResults(), CoreConstants.GameResult.DRAW_GAME);
    }
}
