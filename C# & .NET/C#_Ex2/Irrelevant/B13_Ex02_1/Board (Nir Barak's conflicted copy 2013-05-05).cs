using System;
using System.Collections.Generic;
using System.Text;

namespace B13_Ex02_1
{
    /*
     *  An enum representing the three possible pieces on the checkers board:
     *  Circle (O), X, or Empty ( ).
     */
    public enum eBoardPieces
    {
        Circle = 0,
        CircleKing = 1,
        X = 2,
        XKing = 3,
        Empty = 4,
    }

    /*
     * An enum representing the valid checker board sizes.
     */
    public enum eBoardSize
    {
        Six = 6,
        Eight = 8,
        Ten = 10,
    }

    /*
     * A struct representing the location of a piece on the board
     */
    public struct PlayerBoardPiece
    {
        public int m_Row;
        public int m_Column;

        public PlayerBoardPiece(int i_Row, int i_Column)
        {
            this.m_Row = i_Row;
            this.m_Column = i_Column;
        }
    }

    /*
     * Builds the actual checkers board on which the game will be played.
     * 
     */
    public class Board
    {
        /* The actual board of pieces representation. */
        private eBoardPieces[,] m_GameBoard;

        /* The size of the board. */
        private int m_BoardSize = 0;

        /* An array of all the circle pieces on the board. */
        private PlayerBoardPiece[] m_CirclePieces;

        /* An array of all the X pieces on the board. */
        private PlayerBoardPiece[] m_XPieces;

        private int m_NumOfPiecesCircle;

        private int m_NumOfPiecesX;

        /*
         * Class constructor. Creates a board of size given, and initializes
         * its pieces to their appropriate starting locations.
         */
        public Board(int i_BoardSize)
        {
            // Create a new board with the given size
            m_GameBoard = new eBoardPieces[i_BoardSize, i_BoardSize];
            m_BoardSize = i_BoardSize;

            // Calculate the number of pieces and create an array of pieces for each type
            int numberOfPieces = (m_BoardSize / 2) * (m_BoardSize - 2) / 2;
            m_CirclePieces = new PlayerBoardPiece[numberOfPieces];
            m_XPieces = new PlayerBoardPiece[numberOfPieces];

            // Put all the pieces on the board in their starting position
            InitializeBoard();
        }

        /*
         * Returns this board
         */
        public eBoardPieces[,] GameBoard
        {
            get
            {
                return m_GameBoard;
            }
        }

        /*
         * Returns this board size.
          */
        public int BoardSize
        {
            get
            {
                return m_BoardSize;
            }
        }

        /* 
         * Gets or sets an array of the circle pieces on the board.
         */
        public PlayerBoardPiece[] CirclePieces
        {
            get
            {
                return m_CirclePieces;
            }

            set
            {
                m_CirclePieces = value;
            }
        }

        /* 
         * Gets or sets an array of the circle pieces on the board.
         */
        public PlayerBoardPiece[] XPieces
        {
            get
            {
                return m_XPieces;
            }

            set
            {
                m_XPieces = value;
            }
        }

        /* 
         * Gets the number of circle pieces left on the board. 
         */
        public int NumOfPiecesCircle
        {
            get
            {
                return m_NumOfPiecesCircle;
            }
        }

        /* 
         * Gets the number of circle pieces left on the board. 
         */
        public int NumOfPiecesX
        {
            get
            {
                return m_NumOfPiecesX;
            }
        }

        /*
         * Returns the type of piece at the given row and column in the board.
         */
        public eBoardPieces GetPieceType(int i_Row, int i_Column)
        {
            return m_GameBoard[i_Row, i_Column];
        }

        /*
         * Sets a piece on the board.
         */
        public void SetPiece(int i_Row, int i_Column, eBoardPieces i_Piece)
        {
            m_GameBoard[i_Row, i_Column] = i_Piece;
        }

        /*
         * Initializes the board, placing all pieces in their starting locations.
         */ 
        public void InitializeBoard()
        {
            // The number of rows in which there will be pieces (circles and X's)
            int numberOfRowsWithPieces = (m_BoardSize - 2) / 2;
            int startingPieceLocation = 1;
            m_NumOfPiecesCircle = 0;
            m_NumOfPiecesX = 0;

            // Fill the upper rows with circles
            for (int row = 0; row < numberOfRowsWithPieces; row++)
            {
                for (int column = 0; column < m_BoardSize; column++)
                {
                    startingPieceLocation++;
                    if (startingPieceLocation % 2 == 0)
                    {
                        m_GameBoard[row, column] = eBoardPieces.Empty;
                    }
                    else
                    {
                        m_GameBoard[row, column] = eBoardPieces.Circle;
                        m_CirclePieces[m_NumOfPiecesCircle].m_Row = row;
                        m_CirclePieces[m_NumOfPiecesCircle].m_Column = column;
                        m_NumOfPiecesCircle++;
                    }
                }

                startingPieceLocation--;
            }

            // Fill the two empty rows
            for (int row = numberOfRowsWithPieces; row < numberOfRowsWithPieces + 2; row++)
            {
                for (int column = 0; column < m_BoardSize; column++)
                {
                    m_GameBoard[row, column] = eBoardPieces.Empty;
                }
            }

            startingPieceLocation = 1;

            // Fill the lower rows with X's.
            for (int row = numberOfRowsWithPieces + 2; row < m_BoardSize; row++)
            {
                for (int column = 0; column < m_BoardSize; column++)
                {
                    startingPieceLocation++;
                    if (startingPieceLocation % 2 == 0)
                    {
                        m_GameBoard[row, column] = eBoardPieces.X;
                        m_XPieces[m_NumOfPiecesX].m_Row = row;
                        m_XPieces[m_NumOfPiecesX].m_Column = column;
                        m_NumOfPiecesX++;
                    }
                    else
                    {
                        m_GameBoard[row, column] = eBoardPieces.Empty;
                    }
                }

                startingPieceLocation--;
            }
        }

        /*
         * Update the location of the game pieces on the board.
         */
        public void UpdatePiecesLists()
        {
            eBoardPieces boardPiece;
            int numOfPieces = (m_BoardSize / 2) * (m_BoardSize - 2) / 2;
            m_CirclePieces = new PlayerBoardPiece[numOfPieces];
            m_XPieces = new PlayerBoardPiece[numOfPieces];
            m_NumOfPiecesCircle = 0;
            m_NumOfPiecesX = 0;

            for (int row = 0; row < m_BoardSize; row++)
            {
                for (int column = 0; column < m_BoardSize; column++)
                {
                    boardPiece = GetPieceType(row, column);

                    if (boardPiece.Equals(eBoardPieces.Circle) || boardPiece.Equals(eBoardPieces.CircleKing))
                    {
                        // Update circle array if piece is of type circle
                        m_CirclePieces[m_NumOfPiecesCircle].m_Row = row;
                        m_CirclePieces[m_NumOfPiecesCircle].m_Column = column;
                        m_NumOfPiecesCircle++;
                    }
                    else if (boardPiece.Equals(eBoardPieces.X) || boardPiece.Equals(eBoardPieces.XKing))
                    {
                        // Update X array if piece is of type X
                        m_XPieces[m_NumOfPiecesX].m_Row = row;
                        m_XPieces[m_NumOfPiecesX].m_Column = column;
                        m_NumOfPiecesX++;
                    }
                }
            }
        }
    }
}
