package model;

import chess.ChessGame;

public record GameData(int GameId, String WhiteUserName, String BlackUserName, ChessGame game) {
}
