package model;

import chess.ChessGame;

public record GameData(int gameId, String whiteUserName, String blackUserName, String gameName, ChessGame game) {
}
