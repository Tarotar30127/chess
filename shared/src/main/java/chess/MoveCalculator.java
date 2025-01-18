package chess;

import java.util.ArrayList;
import java.util.Collection;

public class MoveCalculator {
    public static Collection<ChessMove> moveBishop(ChessBoard board, ChessPosition myPosition){
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        Collection<ChessMove> moves = new ArrayList<>();
        int [][] movement = {{1,1},{-1,1},{-1,-1},{1,-1}};
        for (int[] move : movement) {
            int r =row;
            int c = col;
            while(true){
                r += move[0];
                c += move[1];

                boolean baseCase = (r < 1)||(r > 8) || (c < 1) || (c > 8);
                if(baseCase == true) {
                    break;
                }
                ChessPosition focus = new ChessPosition(r, c);
                ChessPiece occupied = board.getPiece(focus);
                if(occupied != null){
                    if (occupied.getTeamColor() != board.getPiece(myPosition).getTeamColor()){
                        moves.add(new ChessMove(myPosition, focus, null));
                    }
                    break;
                }
                moves.add(new ChessMove(myPosition, focus, null ));

            }
        }
        return moves;
    }
    public static Collection<ChessMove> moveKing(ChessBoard board, ChessPosition myPosition){
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        Collection<ChessMove> moves = new ArrayList<>();
        int [][] movement = {{1,1},{-1,1},{1,-1},{-1,-1},{0,1},{0,-1},{1,0},{-1,0}};
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
    public static Collection<ChessMove> moveQueen(ChessBoard board, ChessPosition myPosition) {
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        Collection<ChessMove> moves = new ArrayList<>();
        int [][] movement = {{1,1},{-1,1},{1,-1},{-1,-1},{0,1},{0,-1},{1,0},{-1,0}};
        for (int[] move : movement) {
            int r = row;
            int c = col;
            while (true) {
                r += move[0];
                c += move[1];

                boolean baseCase = (r < 1) || (r > 8) || (c < 1) || (c > 8);
                if (baseCase == true) {
                    break;
                }
                ChessPosition focus = new ChessPosition(r, c);
                ChessPiece occupied = board.getPiece(focus);
                if (occupied != null) {
                    if (occupied.getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                        moves.add(new ChessMove(myPosition, focus, null));
                    }
                    break;
                }
                moves.add(new ChessMove(myPosition, focus, null));

            }
        }
        return moves;
    }
    public static Collection<ChessMove> moveRook(ChessBoard board, ChessPosition myPosition) {
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        Collection<ChessMove> moves = new ArrayList<>();
        int [][] movement = {{0,1},{0,-1},{1,0},{-1,0}};
        for (int[] move : movement) {
            int r = row;
            int c = col;
            while (true) {
                r += move[0];
                c += move[1];

                boolean baseCase = (r < 1) || (r > 8) || (c < 1) || (c > 8);
                if (baseCase == true) {
                    break;
                }
                ChessPosition focus = new ChessPosition(r, c);
                ChessPiece occupied = board.getPiece(focus);
                if (occupied != null) {
                    if (occupied.getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                        moves.add(new ChessMove(myPosition, focus, null));
                    }
                    break;
                }
                moves.add(new ChessMove(myPosition, focus, null));

            }
        }
        return moves;
    }
    public static Collection<ChessMove> moveWhitePawn(ChessBoard board, ChessPosition myPosition) {
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        Collection<ChessMove> moves = new ArrayList<>();
        int [][] movement = {{1,-1},{1,0},{1,1}};
        for (int[] move : movement) {
            int r = row;
            int c = col;

            r += move[0];
            c += move[1];

            boolean baseCase = (r < 1) || (r > 8) || (c < 1) || (c > 8);
            if (baseCase == true) {
                continue;
            }

            ChessPosition focus = new ChessPosition(r, c);
            ChessPiece occupied = board.getPiece(focus);
            if (col == c){
                if (occupied == null) {
                    moves.add(new ChessMove(myPosition, focus, null));
                }
                continue;
            }
            if (occupied != null) {
                if (occupied.getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                    moves.add(new ChessMove(myPosition, focus, null));
                }
                continue;
            }

            if (row == 2){
                moves.add(new ChessMove(myPosition, focus, null));
            }


        }
        return moves;
    }

    public static Collection<ChessMove> moveBlackPawn(ChessBoard board, ChessPosition myPosition) {
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        Collection<ChessMove> moves = new ArrayList<>();
        int [][] movement = {{-1,-1},{-1,0},{-1,1},{2,0}};
        for (int[] move : movement) {
            int r = row;
            int c = col;

            r += move[0];
            c += move[1];

            boolean baseCase = (r < 1) || (r > 8) || (c < 1) || (c > 8);
            if (baseCase == true) {
                continue;
            }

            ChessPosition focus = new ChessPosition(r, c);
            ChessPiece occupied = board.getPiece(focus);
            if (col == c){
                if (occupied == null) {
                    moves.add(new ChessMove(myPosition, focus, null));
                }
                continue;
            }
            if (occupied != null) {
                if (occupied.getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                    moves.add(new ChessMove(myPosition, focus, null));
                }
                continue;
            }

            if (row == 7){
                moves.add(new ChessMove(myPosition, focus, null));
            }



        }
        return moves;
    }

}
