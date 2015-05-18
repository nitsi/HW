using System;
using System.Collections.Generic;
using System.Text;

namespace B13_Ex02_1
{
    /*
     * En enum containing all of the possible 
     * outcomes resulting from the player move.
     */
    public enum eValidMoveMessages
    {
        // Invalid messages
        MoveIsNotInBoard = 0,
        PieceIsNotPlayers = 1,
        WrongDirection = 2,
        SpotIsNotEmpty = 3,
        MoveIsIllegal = 4,
        PieceMustMove = 5,
        PlayerMustEatEnemy = 6,

        // Valid messages
        ValidMoveMoveToNextPlayer = 7,
        ValidMoveKeepPlayerTurn = 8,
    }

    /*
     * A struct representing a possible move that 
     * can be made by the AI computer.
     */
    public struct PossibleMove
    {
        public int m_FromRow;
        public int m_FromColumn;
        public int m_ToRow;
        public int m_ToColumn;

        public PossibleMove(int i_FromRow, int i_FromColumn, int i_ToRow, int i_ToColumn)
        {
            this.m_FromRow = i_FromRow;
            this.m_FromColumn = i_FromColumn;
            this.m_ToRow = i_ToRow;
            this.m_ToColumn = i_ToColumn;
        }
    }

    public class ValidateMove
    {
        /* The game board. */
        private Board m_GameBoard;

        /* The player making the move. */
        private Player m_Player;

        /* An array of pieces of this player on the board. */
        private List<PlayerBoardPiece> m_PlayerPieces;

        /* A list of all the possible move that can be done by the AI. */
        private List<PossibleMove> m_AIMoves;

        /* The current row location of the piece. */
        private int m_FromRow;

        /* The current column location of the piece. */
        private int m_FromColumn;

        /* The desired row move of the piece. */
        private int m_ToRow;

        /* The desired column row of the piece. */
        private int m_ToColumn;

        /* A copy of the game board for calculating chained eating. */
        private Board m_BoardCopy;

        /* A flag for whether the player should keep playing. */
        private bool m_KeepPlayerTurn;

        /*
         * Class constructor.
         */
        public ValidateMove(Board i_BoardGame)
        {
            // Initialize all values of this move
            m_GameBoard = i_BoardGame;
            m_FromRow = -1;
            m_FromColumn = -1;
            m_ToRow = -1;
            m_ToColumn = -1;
            m_BoardCopy = i_BoardGame;
        }

        /*
         * Gets a list of all the possible AI moves.
         */
        public List<PossibleMove> AIMoves
        {
            get
            {
                return m_AIMoves;
            }
        }

        /*
         * Gets a list of all the possible AI moves.
         */
        public Board GameBoard
        {
            get
            {
                return m_GameBoard;
            }
        }

        /*
         * Sets a new move on the board, and returns a message on whether
         * the move was valid or not. If not, returns the error type.
         */
        public eValidMoveMessages CheckAndSetMove(Player i_Player, int i_FromRow, int i_FromColumn, int i_ToRow, int i_ToColumn)
        {
            // Set values given for move
            m_Player = i_Player;
            m_PlayerPieces = i_Player.PlayerPieces;
            m_FromRow = i_FromRow;
            m_FromColumn = i_FromColumn;
            m_ToRow = i_ToRow;
            m_ToColumn = i_ToColumn;
            m_KeepPlayerTurn = false;

            // 'Reset' the copied board and copy the current board
            m_GameBoard = m_BoardCopy;
            m_BoardCopy = new Board(m_GameBoard.BoardSize);
            CopyGameBoard();

            // Initialize AI move list
            m_AIMoves = new List<PossibleMove>();

            // Check move validity
            return IsMoveValid();
        }

        /*
         * Copies the game board to a local member boad in this class.
         */
        private void CopyGameBoard()
        {
            for (int row = 0; row < m_GameBoard.BoardSize; row++)
            {
                for (int column = 0; column < m_GameBoard.BoardSize; column++)
                {
                    m_BoardCopy.SetPiece(row, column, m_GameBoard.GetPieceType(row, column));
                }
            }
        }

        /*
         * Check if the move is inside the boundaries of the checkers board.
         */
        private bool MoveIsInBoard()
        {
            // Check all four coordinates are inside the board.
            return m_FromRow < m_GameBoard.BoardSize &&
                   m_FromColumn < m_GameBoard.BoardSize &&
                   m_ToRow < m_GameBoard.BoardSize &&
                   m_ToColumn < m_GameBoard.BoardSize;
        }

        /*
         * Checks if the piece being moved is of the type of the person currently playing.
         */
        private bool PieceIsPlayers()
        {
            bool pieceIsPlayers;
            eBoardPieces pieceType = m_GameBoard.GetPieceType(m_FromRow, m_FromColumn);

            if (m_Player.BoardPieceType.Equals(eBoardPieces.Circle))
            {
                // The Player uses circles; check the piece is either a circle, or a king circle
                pieceIsPlayers = pieceType.Equals(eBoardPieces.Circle) || pieceType.Equals(eBoardPieces.CircleKing);
            }
            else
            {
                // The Player uses X's; check the piece is either a X, or a king X
                pieceIsPlayers = pieceType.Equals(eBoardPieces.X) || pieceType.Equals(eBoardPieces.XKing);
            }

            return pieceIsPlayers;
        }

        /*
         * Checks if the piece movement went in the right direction on the board.
         */
        private bool RightPieceDirection()
        {
            bool rightPieceDirection = false;

            switch (m_BoardCopy.GetPieceType(m_FromRow, m_FromColumn))
            {
                case eBoardPieces.Circle:

                    // Circles must go from top to bottom
                    rightPieceDirection = m_FromRow < m_ToRow;
                    break;
                case eBoardPieces.X:

                    // X's must go from bottom to top
                    rightPieceDirection = m_FromRow > m_ToRow;
                    break;
                case eBoardPieces.XKing:
                case eBoardPieces.CircleKing:

                    // Kings can go wherever they want
                    rightPieceDirection = true;
                    break;
            }

            return rightPieceDirection;
        }

        /*
         * Checks if the requested spot is empty.
         */
        private bool SpotIsEmpty(int i_BoardRow, int i_BoardColumn, Board i_Board)
        {
            return i_Board.GetPieceType(i_BoardRow, i_BoardColumn).Equals(eBoardPieces.Empty);
        }

        /*
         * Checks if the given piece is an enemy type from that of this player.
         */
        private bool IsPieceAnEnemy(eBoardPieces i_enemyPiece)
        {
            bool isPieceAnEnemy;

            if (m_Player.BoardPieceType.Equals(eBoardPieces.Circle))
            {
                // Player uses circles; check enemy piece is a X or a X king
                isPieceAnEnemy = i_enemyPiece.Equals(eBoardPieces.X) ||
                                 i_enemyPiece.Equals(eBoardPieces.XKing);
            }
            else
            {
                // Player uses X; check enemy piece is a circle or circle king
                isPieceAnEnemy = i_enemyPiece.Equals(eBoardPieces.Circle) ||
                                 i_enemyPiece.Equals(eBoardPieces.CircleKing);
            }

            return isPieceAnEnemy;
        }

        /*
         * Checks that the piece moves only diagonally, and that it moves either one spot 
         * up/down, or that it moves 3 spots and there's an enemy piece in the middle, or
         * that there is a legal chain of eating pieces of the enemy player.
         */
        private bool MoveIsLegal()
        {
            bool moveIsLegal;

            // The distance moved in the row and column coordinates
            int rowMovement = Math.Abs(m_ToRow - m_FromRow);
            int columnMovement = Math.Abs(m_ToColumn - m_FromColumn);

            // The player moved only a single spot
            if (rowMovement == 1)
            {
                // Check player move was diagonal, the piece landed on an empty spot, 
                // and no other pieces of this player can 'eat' a piece
                moveIsLegal = rowMovement == columnMovement &&
                              SpotIsEmpty(m_ToRow, m_ToColumn, m_GameBoard) &&
                              !CanPlayerEatSomeEnemyPiece(m_PlayerPieces);
            }
            else
            {
                // The player skipped a spot and 'ate' piece
                if (rowMovement == 2)
                {
                    // Get the piece that was 'eaten' by the player
                    int eatenPieceRow = m_ToRow - m_FromRow < 0 ? m_FromRow - 1 : m_FromRow + 1;
                    int eatenPieceCol = m_ToColumn - m_FromColumn < 0 ? m_FromColumn - 1 : m_FromColumn + 1;
                    eBoardPieces eatenPiece = m_GameBoard.GetPieceType(eatenPieceRow, eatenPieceCol);

                    // Make sure player move was diagonal, player 'ate' an enemy piece, 
                    // piece landed on an empty spot, and the piece cannot chain and eat more pieces
                    moveIsLegal = rowMovement == columnMovement &&
                                  IsPieceAnEnemy(eatenPiece) &&
                                  SpotIsEmpty(m_ToRow, m_ToColumn, m_GameBoard);

                    if (moveIsLegal)
                    {
                        // If move was legal, remove the 'eaten' piece from the board
                        m_BoardCopy.SetPiece(eatenPieceRow, eatenPieceCol, eBoardPieces.Empty);

                        // If the piece can chain another move, keep the players' turn
                        if (CanEatSomePiece(m_ToRow, m_ToColumn))
                        {
                            moveIsLegal = false;
                        }
                    }
                }
                else
                {
                    // Illegal move
                    moveIsLegal = false;
                }
            }

            return moveIsLegal;
        }

        /* 
         * Checks to see if there exists an enemy piece which the current piece can eat legally.
         */
        public bool CanEatSomePiece(int i_PieceRow, int i_PieceColumn)
        {
            bool borderBoundaryCheck, canEatAPiece = false;
            eBoardPieces playerPiece = m_GameBoard.GetPieceType(i_PieceRow, i_PieceColumn);
            eBoardPieces eatenPiece;

            // If we're checking a whether the player can eat another piece
            // Make sure we are looking at the right piece type
            if (i_PieceRow != m_FromRow)
            {
                playerPiece = m_BoardCopy.GetPieceType(m_FromRow, m_FromColumn);
            }

            switch (playerPiece)
            {
                // Circle pieces can only move down on the board
                case eBoardPieces.Circle:

                    borderBoundaryCheck = i_PieceRow + 2 < m_GameBoard.BoardSize && i_PieceColumn - 2 > -1;
                    if (borderBoundaryCheck)
                    {
                        // Check if player can eat down-left
                        eatenPiece = m_GameBoard.GetPieceType(i_PieceRow + 1, i_PieceColumn - 1);
                        canEatAPiece = IsPieceAnEnemy(eatenPiece) && SpotIsEmpty(i_PieceRow + 2, i_PieceColumn - 2, m_GameBoard);

                        if (canEatAPiece)
                        {
                            // Add move to list of possible computer moves
                            m_AIMoves.Add(new PossibleMove(i_PieceRow, i_PieceColumn, i_PieceRow + 2, i_PieceColumn - 2));
                        }
                    }

                    borderBoundaryCheck = i_PieceRow + 2 < m_GameBoard.BoardSize && i_PieceColumn + 2 < m_GameBoard.BoardSize;
                    if (!canEatAPiece && borderBoundaryCheck)
                    {
                        // Check if player can eat down-right
                        eatenPiece = m_GameBoard.GetPieceType(i_PieceRow + 1, i_PieceColumn + 1);
                        canEatAPiece = IsPieceAnEnemy(eatenPiece) && SpotIsEmpty(i_PieceRow + 2, i_PieceColumn + 2, m_GameBoard);

                        if (canEatAPiece)
                        {
                            // Add move to list of possible computer moves
                            m_AIMoves.Add(new PossibleMove(i_PieceRow, i_PieceColumn, i_PieceRow + 2, i_PieceColumn + 2));
                        }
                    }

                    break;

                // X pieces can only move up on the board
                case eBoardPieces.X:

                    borderBoundaryCheck = i_PieceRow - 2 > -1 && i_PieceColumn + 2 < m_GameBoard.BoardSize;
                    if (borderBoundaryCheck)
                    {
                        // Check if player can eat up-right
                        eatenPiece = m_GameBoard.GetPieceType(i_PieceRow - 1, i_PieceColumn + 1);
                        canEatAPiece = IsPieceAnEnemy(eatenPiece) && SpotIsEmpty(i_PieceRow - 2, i_PieceColumn + 2, m_GameBoard);

                        if (canEatAPiece)
                        {
                            // Add move to list of possible computer moves
                            m_AIMoves.Add(new PossibleMove(i_PieceRow, i_PieceColumn, i_PieceRow - 2, i_PieceColumn + 2));
                        }
                    }

                    borderBoundaryCheck = i_PieceRow - 2 > -1 && i_PieceColumn - 2 > -1;
                    if (!canEatAPiece && borderBoundaryCheck)
                    {
                        // Check if player can eat up-left
                        eatenPiece = m_GameBoard.GetPieceType(i_PieceRow - 1, i_PieceColumn - 1);
                        canEatAPiece = IsPieceAnEnemy(eatenPiece) && SpotIsEmpty(i_PieceRow - 2, i_PieceColumn - 2, m_GameBoard);

                        if (canEatAPiece)
                        {
                            // Add move to list of possible computer moves
                            m_AIMoves.Add(new PossibleMove(i_PieceRow, i_PieceColumn, i_PieceRow - 2, i_PieceColumn - 2));
                        }
                    }

                    break;

                // Kings can go both up or down on the board
                case eBoardPieces.XKing:
                case eBoardPieces.CircleKing:

                    borderBoundaryCheck = i_PieceRow - 2 > -1 && i_PieceColumn - 2 > -1;
                    if (borderBoundaryCheck)
                    {
                        // Check if player can eat up-left
                        eatenPiece = m_GameBoard.GetPieceType(i_PieceRow - 1, i_PieceColumn - 1);
                        canEatAPiece = IsPieceAnEnemy(eatenPiece) && SpotIsEmpty(i_PieceRow - 2, i_PieceColumn - 2, m_GameBoard);

                        if (canEatAPiece) 
                        {
                            // Add move to list of possible computer moves
                            m_AIMoves.Add(new PossibleMove(i_PieceRow, i_PieceColumn, i_PieceRow - 2, i_PieceColumn - 2));
                        }
                    }

                    borderBoundaryCheck = i_PieceRow + 2 < m_GameBoard.BoardSize && i_PieceColumn - 2 > -1;
                    if (!canEatAPiece && borderBoundaryCheck)
                    {
                        // Check if player can eat down-left
                        eatenPiece = m_GameBoard.GetPieceType(i_PieceRow + 1, i_PieceColumn - 1);
                        canEatAPiece = IsPieceAnEnemy(eatenPiece) && SpotIsEmpty(i_PieceRow + 2, i_PieceColumn - 2, m_GameBoard);

                        if (canEatAPiece) 
                        {
                            // Add move to list of possible computer moves
                            m_AIMoves.Add(new PossibleMove(i_PieceRow, i_PieceColumn, i_PieceRow + 2, i_PieceColumn - 2));
                        }
                    }

                    borderBoundaryCheck = i_PieceRow - 2 > -1 && i_PieceColumn + 2 < m_GameBoard.BoardSize;
                    if (!canEatAPiece && borderBoundaryCheck)
                    {
                        // Check if player can eat up-right
                        eatenPiece = m_GameBoard.GetPieceType(i_PieceRow - 1, i_PieceColumn + 1);
                        canEatAPiece = IsPieceAnEnemy(eatenPiece) && SpotIsEmpty(i_PieceRow - 2, i_PieceColumn + 2, m_GameBoard);

                        if (canEatAPiece)
                        {
                            // Add move to list of possible computer moves
                            m_AIMoves.Add(new PossibleMove(i_PieceRow, i_PieceColumn, i_PieceRow - 2, i_PieceColumn + 2));
                        }
                    }

                    borderBoundaryCheck = i_PieceRow + 2 < m_GameBoard.BoardSize && i_PieceColumn + 2 < m_GameBoard.BoardSize;
                    if (!canEatAPiece && borderBoundaryCheck)
                    {
                        // Check if player can eat down-right
                        eatenPiece = m_GameBoard.GetPieceType(i_PieceRow + 1, i_PieceColumn + 1);
                        canEatAPiece = IsPieceAnEnemy(eatenPiece) && SpotIsEmpty(i_PieceRow + 2, i_PieceColumn + 2, m_GameBoard);

                        if (canEatAPiece)
                        {
                            // Add move to list of possible computer moves
                            m_AIMoves.Add(new PossibleMove(i_PieceRow, i_PieceColumn, i_PieceRow + 2, i_PieceColumn + 2));
                        }
                    }

                    break;
            }

            m_KeepPlayerTurn = canEatAPiece;
            return canEatAPiece;
        }

        /* 
         * Checks if a given piece can move a single spot. Meant for caluclating AI moves.
         */
        public bool CanMoveOneSpot(int i_PieceRow, int i_PieceColumn)
        {
            bool canMoveOneSpot = false;
            eBoardPieces playerPiece = m_GameBoard.GetPieceType(i_PieceRow, i_PieceColumn);

            switch (playerPiece)
            {
                // Circle pieces can only move down on the board
                case eBoardPieces.Circle:

                    if (i_PieceRow + 1 < m_GameBoard.BoardSize && i_PieceColumn - 1 > -1 &&
                        SpotIsEmpty(i_PieceRow + 1, i_PieceColumn - 1, m_GameBoard))
                    {
                        // AI can move down-left, add to possible moves
                        m_AIMoves.Add(new PossibleMove(i_PieceRow, i_PieceColumn, i_PieceRow + 1, i_PieceColumn - 1));
                        canMoveOneSpot = true;
                    }

                    if (i_PieceRow + 1 < m_GameBoard.BoardSize && i_PieceColumn + 1 < m_GameBoard.BoardSize &&
                        SpotIsEmpty(i_PieceRow + 1, i_PieceColumn + 1, m_GameBoard))
                    {
                        // AI can move down-right, add to possible moves
                        m_AIMoves.Add(new PossibleMove(i_PieceRow, i_PieceColumn, i_PieceRow + 1, i_PieceColumn + 1));
                        canMoveOneSpot = true;
                    }

                    break;

                // X pieces can only move up on the board
                case eBoardPieces.X:

                    if (i_PieceRow - 1 > -1 && i_PieceColumn + 1 < m_GameBoard.BoardSize &&
                        SpotIsEmpty(i_PieceRow - 1, i_PieceColumn + 1, m_GameBoard))
                    {
                        // AI can move up-right, add to possible moves
                        m_AIMoves.Add(new PossibleMove(i_PieceRow, i_PieceColumn, i_PieceRow - 1, i_PieceColumn + 1));
                        canMoveOneSpot = true;
                    }

                    if (i_PieceRow - 1 > -1 && i_PieceColumn - 1 > -1 &&
                        SpotIsEmpty(i_PieceRow - 1, i_PieceColumn - 1, m_GameBoard))
                    {
                        // AI can move up-left, add to possible moves
                        m_AIMoves.Add(new PossibleMove(i_PieceRow, i_PieceColumn, i_PieceRow - 1, i_PieceColumn - 1));
                        canMoveOneSpot = true;
                    }

                    break;

                // Kings can go both up or down on the board
                case eBoardPieces.XKing:
                case eBoardPieces.CircleKing:

                    if (i_PieceRow - 1 > -1 && i_PieceColumn - 1 > -1 &&
                        SpotIsEmpty(i_PieceRow - 1, i_PieceColumn - 1, m_GameBoard))
                    {
                        // AI can move up-left, add to possible moves
                        m_AIMoves.Add(new PossibleMove(i_PieceRow, i_PieceColumn, i_PieceRow - 1, i_PieceColumn - 1));
                        canMoveOneSpot = true;
                    }

                    if (i_PieceRow + 1 < m_GameBoard.BoardSize && i_PieceColumn - 1 > -1 &&
                        SpotIsEmpty(i_PieceRow + 1, i_PieceColumn - 1, m_GameBoard))
                    {
                        // AI can move down-left, add to possible moves
                        m_AIMoves.Add(new PossibleMove(i_PieceRow, i_PieceColumn, i_PieceRow + 1, i_PieceColumn - 1));
                        canMoveOneSpot = true;
                    }

                    if (i_PieceRow - 1 > -1 && i_PieceColumn + 1 < m_GameBoard.BoardSize &&
                        SpotIsEmpty(i_PieceRow - 1, i_PieceColumn + 1, m_GameBoard))
                    {
                        // AI can move up-right, add to possible moves
                        m_AIMoves.Add(new PossibleMove(i_PieceRow, i_PieceColumn, i_PieceRow - 1, i_PieceColumn + 1));
                        canMoveOneSpot = true;
                    }

                    if (i_PieceRow + 1 < m_GameBoard.BoardSize && i_PieceColumn + 1 < m_GameBoard.BoardSize &&
                        SpotIsEmpty(i_PieceRow + 1, i_PieceColumn + 1, m_GameBoard))
                    {
                        // AI can move down-right, add to possible moves
                        m_AIMoves.Add(new PossibleMove(i_PieceRow, i_PieceColumn, i_PieceRow + 1, i_PieceColumn + 1));
                        canMoveOneSpot = true;
                    }

                    break;
            }

            return canMoveOneSpot;
        }

        /*
        * Checks if there exists an enemy piece on the board which some player piece can eat.
        */
        private bool CanPlayerEatSomeEnemyPiece(List<PlayerBoardPiece> i_PlayerPieces)
        {
            bool canPlayerEatEnemy = false;
            int pieceNumber = 0;
            int numOfPieces = i_PlayerPieces.Count;

            while (!canPlayerEatEnemy && pieceNumber < numOfPieces)
            {
                canPlayerEatEnemy = CanEatSomePiece(
                                                    i_PlayerPieces[pieceNumber].m_Row,
                                                    i_PlayerPieces[pieceNumber].m_Column);
                pieceNumber++;
            }

            return canPlayerEatEnemy;
        }

        /*
         * Checks all possible moves where a player piece can move to some spot on the board.
         */
        public void CalculatePossibleMoves(Player i_ComputerPlayer)
        {
            m_GameBoard = m_BoardCopy;
            m_Player = i_ComputerPlayer;
            m_AIMoves = new List<PossibleMove>();
            int numOfPieces = i_ComputerPlayer.PlayerPieces.Count;

            foreach (PlayerBoardPiece playerPiece in i_ComputerPlayer.PlayerPieces)
            {
                m_FromRow = playerPiece.m_Row;
                m_FromColumn = playerPiece.m_Column;

                // Check what the piece can eat
                CanEatSomePiece(playerPiece.m_Row, playerPiece.m_Column);
            }

            // Count other move only if no enemy piece can be eaten
            if (AIMoves.Count != 0)
            {
                return;
            }

            foreach (PlayerBoardPiece playerPiece in i_ComputerPlayer.PlayerPieces)
            {
                m_FromRow = playerPiece.m_Row;
                m_FromColumn = playerPiece.m_Column;

                // Check where the piece can move to
                CanMoveOneSpot(playerPiece.m_Row, playerPiece.m_Column);
            }
        }

        /*
         * Combines all of the tests in this class, and returns a final answer
         * on whether or not the move was valid.
         */
        private eValidMoveMessages IsMoveValid()
        {
            eValidMoveMessages validMoveMessage = eValidMoveMessages.MoveIsIllegal;

            if (m_FromRow == m_ToRow && m_FromColumn == m_ToColumn)
            {
                // Move must move to a different spot on the board
                validMoveMessage = eValidMoveMessages.PieceMustMove;
            }
            else if (!MoveIsInBoard())
            {
                // Player has exceeded board range
                validMoveMessage = eValidMoveMessages.MoveIsNotInBoard;
            }
            else if (!PieceIsPlayers())
            {
                // Piece must be of the type of the player
                validMoveMessage = eValidMoveMessages.PieceIsNotPlayers;
            }
            else if (!RightPieceDirection())
            {
                // Piece must move in the direction of its type
                validMoveMessage = eValidMoveMessages.WrongDirection;
            }
            else if (Math.Abs(m_ToRow - m_FromRow) == 1 &&
                     Math.Abs(m_ToColumn - m_FromColumn) == 1 &&
                     CanPlayerEatSomeEnemyPiece(m_PlayerPieces))
            {
                // Player must eat an enemy piece if he can
                validMoveMessage = eValidMoveMessages.PlayerMustEatEnemy;
            }
            else if (!MoveIsLegal())
            {
                if (!m_KeepPlayerTurn)
                {
                    // Player performed an illegal move
                    validMoveMessage = eValidMoveMessages.MoveIsIllegal;
                }
                else
                {
                    // Move was valid, update the board
                    UpdateBoardAndLists();

                    // Player can eat this piece but must continue eating
                    validMoveMessage = eValidMoveMessages.ValidMoveKeepPlayerTurn;
                }
            }
            else
            {
                // Move was valid, update the board
                UpdateBoardAndLists();

                if (Math.Abs(m_ToRow - m_FromRow) == 1 && Math.Abs(m_ToColumn - m_FromColumn) == 1)
                {
                    // The move is valid, change players
                    validMoveMessage = eValidMoveMessages.ValidMoveMoveToNextPlayer;
                }
                else
                {
                    // The move is valid, change players
                    validMoveMessage = eValidMoveMessages.ValidMoveMoveToNextPlayer;
                }
            }

            return validMoveMessage;
        }

        /* 
         * Updates the board and the list of pieces if the piece move was legal. 
         */
        private void UpdateBoardAndLists()
        {
            // The move was valid, move player piece to its destination and update lists
            m_BoardCopy.SetPiece(m_FromRow, m_FromColumn, eBoardPieces.Empty);

            if (m_ToRow == m_GameBoard.BoardSize - 1 || m_ToRow == 0)
            {
                // Player has reached a border on the board, get king type and change piece to king
                eBoardPieces kingType = m_Player.BoardPieceType.Equals(eBoardPieces.Circle) ? eBoardPieces.CircleKing :
                                                                                              eBoardPieces.XKing;
                
                // Set the piece at its destination
                m_BoardCopy.SetPiece(m_ToRow, m_ToColumn, kingType);
            }
            else
            {
                // Set the piece at its destination
                m_BoardCopy.SetPiece(m_ToRow, m_ToColumn, m_GameBoard.GetPieceType(m_FromRow, m_FromColumn));
            }

            // Update the piece array lists
            m_BoardCopy.UpdatePiecesLists();

            // Update the original board
            m_GameBoard = m_BoardCopy;
        }
    }
}