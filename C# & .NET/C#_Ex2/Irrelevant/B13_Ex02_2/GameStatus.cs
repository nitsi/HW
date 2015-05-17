using System;
using System.Collections.Generic;
using System.Text;

namespace B13_Ex02_1
{
    public enum eCurrentGameStatus
    {
        GameDidNotEnd = 0,
        CircleWon = 1,
        XWon = 2,
        GameTied = 3,
    }

    public class GameStatus
    {
        /* The game board in which this class checks statuses. */
        private Board m_GameBoard;

        /* The score that a player must get in order to win the game. */
        private int m_MaxPlayerScore;

        /* The tool used to validate piece moves. */
        private ValidateMove m_MoveChecker;

        /*
         * In the case where the player must move again, this method checks to see if his move
         * uses the same piece he used in the previous move.
         */
        public static bool CheckPlayerAteAgain(int i_CurrentPlayerNumber, string i_LastPlayerTurn, string i_GameMove)
        {
            bool newPlayerMoveIsOk = false;

            // Retrieves the old "to" row and column, and the new "from" row and column
            int oldToColumn = ValidateGameInput.GetToColumn(i_LastPlayerTurn);
            int oldToRow = ValidateGameInput.GetToRow(i_LastPlayerTurn);
            int newFromColumn = ValidateGameInput.GetFromColumn(i_GameMove);
            int newFromRow = ValidateGameInput.GetFromRow(i_GameMove);

            // Move is valid only if the previous piece used is the same as the current one
            if (oldToColumn == newFromColumn && oldToRow == newFromRow)
            {
                newPlayerMoveIsOk = true;
            }

            return newPlayerMoveIsOk;
        }

        /* 
         * Class constructor. 
         */
        public GameStatus(Board i_Board)
        {
            m_GameBoard = i_Board;
            m_MaxPlayerScore = (m_GameBoard.BoardSize / 2) * (m_GameBoard.BoardSize - 2) / 2;
        }

        /*
         * Gets the current game status. Either the game has ended (and some piece
         * type has won), the game is tied, or the game can still continue.
         */
        public eCurrentGameStatus GetGameStatus(Board i_Board, ValidateMove i_MoveChecker)
        {
            m_GameBoard = i_Board;
            eCurrentGameStatus gameStatus;
            m_MoveChecker = i_MoveChecker;

            // Check scores for each player
            if (m_GameBoard.XPieces.Count == 0)
            {
                gameStatus = eCurrentGameStatus.CircleWon;
            }
            else if (m_GameBoard.CirclePieces.Count == 0)
            {
                gameStatus = eCurrentGameStatus.XWon;
            }
            else
            {
                // Check if the circle and X pieces can make a move on the board
                bool canCirclePiecesMove = PieceTypeCanMove(eBoardPieces.Circle);
                bool canXPiecesMove = PieceTypeCanMove(eBoardPieces.X);

                if (!canCirclePiecesMove && !canXPiecesMove)
                {
                    // If both player can't move, game is tied
                    gameStatus = eCurrentGameStatus.GameTied;
                }
                else if (!canCirclePiecesMove)
                {
                    // If only the circle pieces can't move, X pieces win
                    gameStatus = eCurrentGameStatus.XWon;
                }
                else if (!canXPiecesMove)
                {
                    // If only the X pieces can't move, circle pieces win
                    gameStatus = eCurrentGameStatus.CircleWon;
                }
                else
                {
                    // Otherwise both players can move, and the game did not end
                    gameStatus = eCurrentGameStatus.GameDidNotEnd;
                }
            }

            return gameStatus;
        }

        /*
         * Checks whether the given piece type can move on the board.
         */
        private bool PieceTypeCanMove(eBoardPieces i_BoardPiece)
        {
            List<PlayerBoardPiece> pieceTypeList;

            if (i_BoardPiece.Equals(eBoardPieces.CircleKing) || i_BoardPiece.Equals(eBoardPieces.CircleKing))
            {
                // Piece is of type circle, get the list of all the circle pieces
                pieceTypeList = m_GameBoard.CirclePieces;
            }
            else
            {
                // Piece is of type X, get the list of all the X pieces
                pieceTypeList = m_GameBoard.XPieces;
            }

            // Flag for whether the piece type can make move on the board
            bool typeCanMove = false;

            foreach (PlayerBoardPiece gamePiece in pieceTypeList)
            {
                // Check each piece if it can make some move
                if (m_MoveChecker.CanMoveOneSpot(gamePiece.m_Row, gamePiece.m_Column) ||
                    m_MoveChecker.CanEatSomePiece(gamePiece.m_Row, gamePiece.m_Column))
                {
                    // Found a piece that can move, return true
                    typeCanMove = true;
                    break;
                }
            }

            return typeCanMove;
        }
    }
}