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

    public static Collection<ChessMove> moveKnight(ChessBoard board, ChessPosition myPosition) {
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        Collection<ChessMove> moves = new ArrayList<>();
        int [][] movement = {{2,1},{2,-1},{-2,-1},{-2,1},{1,2},{1,-2},{-1,-2},{-1,2}};
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
            if (occupied != null) {
                if (occupied.getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                    moves.add(new ChessMove(myPosition, focus, null));
                }
                continue;
            }
            moves.add(new ChessMove(myPosition, focus, null));


        }
        return moves;
    }
    public static Collection<ChessMove> moveWhitePawn(ChessBoard board, ChessPosition myPosition) {
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        Collection<ChessMove> moves = new ArrayList<>();
        int [][] movement = {{1,-1},{1,0},{1,1}};
        if (row == 2) {
            ChessPosition jump = new ChessPosition(4, col);
            ChessPosition front = new ChessPosition(3, col);
            ChessPiece occupied2 = board.getPiece(front);
            ChessPiece occupied1 = board.getPiece(jump);
            if (occupied1 == null) {
                if (occupied2 == null){
                    moves.add(new ChessMove(myPosition, jump, null));
                }
            }
        }
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
                    if (r == 8 ){
                        moves.add(new ChessMove(myPosition, focus, ChessPiece.PieceType.QUEEN));
                        moves.add(new ChessMove(myPosition, focus, ChessPiece.PieceType.KNIGHT));
                        moves.add(new ChessMove(myPosition, focus, ChessPiece.PieceType.ROOK));
                        moves.add(new ChessMove(myPosition, focus, ChessPiece.PieceType.BISHOP));
                    }
                    else{
                        moves.add(new ChessMove(myPosition, focus, null));
                    }

                }
                continue;
            }
            if (occupied != null) {
                if (occupied.getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                    if (r == 8 ){
                        moves.add(new ChessMove(myPosition, focus, ChessPiece.PieceType.QUEEN));
                        moves.add(new ChessMove(myPosition, focus, ChessPiece.PieceType.KNIGHT));
                        moves.add(new ChessMove(myPosition, focus, ChessPiece.PieceType.ROOK));
                        moves.add(new ChessMove(myPosition, focus, ChessPiece.PieceType.BISHOP));
                    }
                    else{
                        moves.add(new ChessMove(myPosition, focus, null));
                    }
                }
                continue;
            }




        }
        return moves;
    }

    public static Collection<ChessMove> moveBlackPawn(ChessBoard board, ChessPosition myPosition) {
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        Collection<ChessMove> moves = new ArrayList<>();
        int [][] movement = {{-1,-1},{-1,0},{-1,1}};
        if (row == 7) {
            ChessPosition jump = new ChessPosition(5, col);
            ChessPosition front = new ChessPosition(6, col);
            ChessPiece occupied2 = board.getPiece(front);
            ChessPiece occupied1 = board.getPiece(jump);
            if (occupied1 == null) {
                if (occupied2 == null){
                    moves.add(new ChessMove(myPosition, jump, null));
                }
            }
        }
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
                    if (r == 1 ){
                        moves.add(new ChessMove(myPosition, focus, ChessPiece.PieceType.QUEEN));
                        moves.add(new ChessMove(myPosition, focus, ChessPiece.PieceType.KNIGHT));
                        moves.add(new ChessMove(myPosition, focus, ChessPiece.PieceType.ROOK));
                        moves.add(new ChessMove(myPosition, focus, ChessPiece.PieceType.BISHOP));
                    }
                    else{
                        moves.add(new ChessMove(myPosition, focus, null));
                    }
                }
                continue;
            }
            if (occupied != null) {
                if (occupied.getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                    if (r == 1 ){
                        moves.add(new ChessMove(myPosition, focus, ChessPiece.PieceType.QUEEN));
                        moves.add(new ChessMove(myPosition, focus, ChessPiece.PieceType.KNIGHT));
                        moves.add(new ChessMove(myPosition, focus, ChessPiece.PieceType.ROOK));
                        moves.add(new ChessMove(myPosition, focus, ChessPiece.PieceType.BISHOP));
                    }
                    else{
                        moves.add(new ChessMove(myPosition, focus, null));
                    }
                }

            }



        }
        return moves;
    }

}
