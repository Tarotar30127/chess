package chess.movement;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;

public abstract class MoveOnce implements MovementCalculator {
    public Collection<ChessMove> moveOneSpot(ChessBoard board, ChessPosition myPosition, int[][] movement){
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        Collection<ChessMove> moves = new ArrayList<>();
        for (int[] move : movement) {
            int r =row;
            int c = col;
            r += move[0];
            c += move[1];

            boolean baseCase = (r < 1 )||(r > 8) || (c < 1) || (c > 8);
            if(baseCase == true) {
                continue;
            }
            ChessPosition focus = new ChessPosition(r, c);
            ChessPiece occupied = board.getPiece(focus);
            if(occupied != null){
                if (occupied.getTeamColor() != board.getPiece(myPosition).getTeamColor()){
                    moves.add(new ChessMove(myPosition, focus, null));
                }
                continue;
            }
            moves.add(new ChessMove(myPosition, focus, null ));


        }
        return moves;
    }

}
