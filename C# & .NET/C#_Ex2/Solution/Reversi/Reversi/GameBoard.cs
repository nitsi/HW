using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Collections;

namespace Reversi
{
    class GameBoard
    {
        private const int k_Zero = 0;
        private int m_BoardSize;
        private Colors[,] m_Board;
        private UserInteraction m_UI;

        private string m_Alphabet = "ABCDEFGH";

        public GameBoard()
        {
        }

        public void initBoard(int i_BoardSize)
        {
            m_BoardSize = i_BoardSize;
            m_Board = new Colors[m_BoardSize, m_BoardSize];

            // Initialize UI
            m_UI = new UserInteraction();

            // init the array with empty
            for (int i = 0; i < m_BoardSize; i++)
            {
                for (int j = 0; j < m_BoardSize; j++)
                {
                    m_Board[i, j] = Colors.EMPTY;
                }
            }

            // TODO: vhange i
            int boardMiddle = m_BoardSize / 2;

            // Append BLACK
            m_Board[boardMiddle, boardMiddle] = Colors.BLACK;
            m_Board[boardMiddle - 1, boardMiddle - 1] = Colors.BLACK;

            // Append WHITE
            m_Board[boardMiddle - 1, boardMiddle] = Colors.WHITE;
            m_Board[boardMiddle, boardMiddle - 1] = Colors.WHITE;
        }

        internal bool GotMoreValidMoves(Colors i_currentPlayerColor)
        {
            bool validMovesFlag = false;
            for (int i = 0; i < m_BoardSize; i++)
            {
                for (int j = 0; j < m_BoardSize; j++)
                {
                    if (m_Board[i, j] != Colors.EMPTY)
                    {
                        continue;
                    }

                    validMovesFlag = crawler(i, j, i_currentPlayerColor);
                    if (validMovesFlag)
                    {
                        return true;
                    }
                }
            }

            return false;
        }

        internal void PrintCurrentState()
        {
            generateTopLetters();
            generateNewLine();
            generateLineSeparators();
            generateNewLine();
            for (int i = 0; i < m_BoardSize; i++)
            {
                generateLineNumber(i);
                for (int j = 0; j < m_BoardSize; j++)
                {
                    generateColumnSeparator();
                    generateCellContent(i, j);
                }

                generateNewLine();
                generateLineSeparators();
                generateNewLine();
            }
        }

        private void generateCellContent(int i, int j)
        {
            // TODO: change i_
            Colors o_TempPionContent = m_Board[i, j];
            m_UI.GenerateTableCellContet(o_TempPionContent);
        }

        private void generateColumnSeparator()
        {
            m_UI.genreateTableColumnSeparator();
        }

        private void generateLineNumber(int i)
        {
            m_UI.GenerateTableLineNumber(i);
        }

        private void generateLineSeparators()
        {
            m_UI.GenerateTableLineSeparators(m_BoardSize);
        }

        private void generateTopLetters()
        {
            m_UI.GenerateTableTopLetters(m_BoardSize);
        }

        private void generateNewLine()
        {
            m_UI.GenereateTableNewLine();
        }

        // Assuming we've recieved in the form of "A1" <Char,Number>
        public bool CheckIfValid(string i_PlayerMove, Colors i_PlayerColor)
        {
            // used for coorsdinate
            int[] io_PlayerMoveCoords = new int[2];

            // Get data from player
            // TODO : change this to object parsing
            io_PlayerMoveCoords[0] = m_Alphabet.IndexOf(i_PlayerMove[0]);
            io_PlayerMoveCoords[1] = (int)char.GetNumericValue(i_PlayerMove[1]) - 1;

            int io_XCords = io_PlayerMoveCoords[1];
            int io_YCords = io_PlayerMoveCoords[0];
            if (verifyEdges(io_PlayerMoveCoords[1], io_PlayerMoveCoords[0]) && m_Board[io_XCords, io_YCords] == Colors.EMPTY)
            {
                return crawler(io_XCords, io_YCords, i_PlayerColor);
            }
            else
            {
                return false;
            }
        }

        private bool verifyEdges(int i_X, int i_Y)
        {
            return (i_X >= 0 && i_X <= m_BoardSize - 1) && (i_Y >= 0 && i_Y <= m_BoardSize - 1);
        }

        private bool crawler(int io_X, int io_Y, Colors io_PlayerColor)
        {
            if (verifyEdges(io_X, io_Y))
            {
                return crawlHorizontal(io_X, io_Y, io_PlayerColor) || crawlVertical(io_X, io_Y, io_PlayerColor) || crawlCross(io_X, io_Y, io_PlayerColor);
            }
            else
            {
                return false;
            }
        }

        private bool crawlCross(int io_X, int io_Y, Colors io_PlayerColor)
        {
            return validityCrawler(io_X - 1, -1, io_Y + 1, 1, io_PlayerColor, k_Zero)
                || validityCrawler(io_X + 1, 1, io_Y - 1, -1, io_PlayerColor, k_Zero)
                || validityCrawler(io_X + 1, 1, io_Y + 1, 1, io_PlayerColor, k_Zero)
                || validityCrawler(io_X - 1, -1, io_Y - 1, -1, io_PlayerColor, k_Zero);
        }

        private bool crawlVertical(int io_X, int io_Y, Colors i_PlayerColor)
        {
            return validityCrawler(io_X + 1, 1, io_Y, 0, i_PlayerColor, k_Zero) || validityCrawler(io_X - 1, -1, io_Y, 0, i_PlayerColor, k_Zero);
        }

        private bool crawlHorizontal(int i_X, int i_Y, Colors i_PlayerColor)
        {
            return validityCrawler(i_X, 0, i_Y + 1, 1, i_PlayerColor, k_Zero) || validityCrawler(i_X, 0, i_Y - 1, -1, i_PlayerColor, k_Zero);
        }

        private bool validityCrawler(int io_X, int io_XFactor, int io_Y, int io_YFactor, Colors io_PlayerColor, int io_Count)
        {
            // Check in bounds
            if (!verifyEdges(io_X, io_Y))
            {
                return false;
            }

            // Init color
            Colors i_CurrentColorFromCell = m_Board[io_X, io_Y];

            // If we hit empty cell
            if (i_CurrentColorFromCell == Colors.EMPTY)
            {
                return false;
            }

            if (i_CurrentColorFromCell == io_PlayerColor)
            {
                return io_Count == 0 ? false : true;
            }
            else
            {
                // Means it's the opponent color
                return validityCrawler(io_X + io_XFactor, io_XFactor, io_Y + io_YFactor, io_YFactor, io_PlayerColor, io_Count + 1);
            }
        }

        internal void AppendMove(string i_Move, Colors io_GivenColor)
        {
            int io_tempX = (int)char.GetNumericValue(i_Move[1]) - 1;
            int io_tempY = m_Alphabet.IndexOf(i_Move[0]);

            // Inject new data to array
            m_Board[io_tempX, io_tempY] = io_GivenColor;

            updateFromLocation(io_tempX, io_tempY, io_GivenColor);
        }

        private void updateFromLocation(int io_X, int io_Y, Colors io_GivenColor)
        {
            bool dummyBooleanHolder = false;

            // Update vertical
            dummyBooleanHolder = propagateTable(io_X + 1, 1, io_Y, 0, io_GivenColor);
            dummyBooleanHolder = propagateTable(io_X - 1, -1, io_Y, 0, io_GivenColor);

            // Update Horizontal
            dummyBooleanHolder = propagateTable(io_X, 0, io_Y + 1, 1, io_GivenColor);
            dummyBooleanHolder = propagateTable(io_X, 0, io_Y - 1, -1, io_GivenColor);

            // Update slash
            dummyBooleanHolder = propagateTable(io_X - 1, -1, io_Y + 1, 1, io_GivenColor);
            dummyBooleanHolder = propagateTable(io_X + 1, 1, io_Y - 1, -1, io_GivenColor);

            // Update backslash
            dummyBooleanHolder = propagateTable(io_X + 1, 1, io_Y + 1, 1, io_GivenColor);
            dummyBooleanHolder = propagateTable(io_X - 1, -1, io_Y - 1, -1, io_GivenColor);
        }

        private bool propagateTable(int io_X, int io_XFactor, int io_Y, int io_YFactor, Colors io_GivenColor)
        {
            // Check edges
            if (!verifyEdges(io_X, io_Y))
            {
                return false;
            }

            // Get current color
            Colors currentColorFromCell = m_Board[io_X, io_Y];

            // check if empty
            if (currentColorFromCell == Colors.EMPTY)
            {
                return false;
            }

            if (currentColorFromCell == io_GivenColor)
            {
                return true;
            }

            if (propagateTable(io_X + io_XFactor, io_XFactor, io_Y + io_YFactor, io_YFactor, io_GivenColor))
            {
                m_Board[io_X, io_Y] = io_GivenColor;
                return true;
            }
            else
            {
                return false;
            }
        }

        internal Colors CalculateWinner()
        {
            int io_WhitePoints = 0;
            int io_BlackPoints = 0;

            for (int i = 0; i < m_BoardSize; i++)
            {
                for (int j = 0; j < m_BoardSize; j++)
                {
                    if (m_Board[i, j] == Colors.WHITE)
                    {
                        io_WhitePoints++;
                    }
                    else if (m_Board[i, j] == Colors.BLACK)
                    {
                        io_BlackPoints++;
                    }
                }
            }

            return io_BlackPoints > io_WhitePoints ? Colors.BLACK : Colors.WHITE;
        }

        public List<string> GetListOfEmptyCells()
        {
            List<string> io_ListOfEmptyCells = new List<string>();
            for (int i = 0; i < m_BoardSize; i++)
            {
                for (int j = 0; j < m_BoardSize; j++)
                {
                    if (m_Board[i, j] == Colors.EMPTY)
                    {
                        io_ListOfEmptyCells.Add(string.Empty + (i + 1) + string.Empty + m_UI.GetAvilableLetters()[j]);
                    }
                }
            }

            return io_ListOfEmptyCells;
        }
    }
}