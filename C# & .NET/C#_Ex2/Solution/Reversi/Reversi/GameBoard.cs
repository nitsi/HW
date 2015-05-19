using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Collections;

namespace Reversi
{
    class GameBoard
    {
        private int m_BoardSize;
        private Colors[,] m_Board;
        private UserInteraction UI;

        private string m_Alphabet = "ABCDEFGH";

        public GameBoard()
        {
        }

        public void initBoard(int o_BoardSize)
        {
            m_BoardSize = o_BoardSize;
            m_Board = new Colors[m_BoardSize, m_BoardSize];

            // Initialize UI
            UI = new UserInteraction();

            // init the array with empty
            for (int i = 0; i < m_BoardSize; i++)
            {
                for (int j = 0; j < m_BoardSize; j++)
                {
                    m_Board[i, j] = Colors.EMPTY;
                }
            }

            //TODO: vhange i
            int i_BoardMiddle = m_BoardSize / 2;

            // Append BLACK
            m_Board[i_BoardMiddle, i_BoardMiddle] = Colors.BLACK;
            m_Board[i_BoardMiddle - 1, i_BoardMiddle - 1] = Colors.BLACK;

            // Append WHITE
            m_Board[i_BoardMiddle - 1, i_BoardMiddle] = Colors.WHITE;
            m_Board[i_BoardMiddle, i_BoardMiddle - 1] = Colors.WHITE;
        }

        internal bool GotMoreValidMoves(Colors i_currentPlayerColor)
        {
            bool i_ValidMovesTestFlag = false;
            for (int i = 0; i < m_BoardSize; i++)
            {
                for (int j = 0; j < m_BoardSize; j++)
                {
                    if (m_Board[i, j] != Colors.EMPTY) { continue; }
                    i_ValidMovesTestFlag = crawler(i, j, i_currentPlayerColor);
                    if (i_ValidMovesTestFlag) { return true; }
                }

            }
            return false;
        }

        //TODO: replace methos writeline with internal C# writeline
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

        //TODO: change to meaningful names
        private void generateCellContent(int i, int j)
        {
            //TODO: change i_
            Colors i_TempPionContent = m_Board[i, j];
            UI.GenerateTableCellContet(i_TempPionContent);
        }

        private void generateColumnSeparator()
        {
            Console.Write(" | ");
        }

        private void generateLineNumber(int i)
        {
            Console.Write((i + 1) + " )");
        }

        private void generateLineSeparators()
        {
            //TODO: replace with regular print add comment above
            UI.generateTableBorderSpan();
            for (int i = 0; i < m_BoardSize; i++)
            {
                Console.Write("====");
            }
        }

        private void generateTopLetters()
        {
            UI.GenerateTableTopLetters(m_BoardSize);
            // Generate top letters
            //TODO: replace with regular print add comment above
            UI.generateTableBorderSpan();
            for (int i = 0; i < m_BoardSize; i++)
            {
                Console.Write("  " + m_Alphabet[i] + " ");
            }
        }

        private void generateNewLine()
        {
            Console.WriteLine();
        }

        //Assuming we've recieved in the form of "A1" <Char,Number>

        public bool CheckIfValid(string i_PlayerMove, Colors i_PlayerColor)
        {
            int[] i_PlayerMoveCoords = new int[2]; // used for coorsdinate

            // Get data from player
            //TODO : change this to object parsing

            i_PlayerMoveCoords[0] = m_Alphabet.IndexOf(i_PlayerMove[0]);
            i_PlayerMoveCoords[1] = (int)Char.GetNumericValue(i_PlayerMove[1]) - 1;

            int i_XCords = i_PlayerMoveCoords[1];
            int i_YCords = i_PlayerMoveCoords[0];
            if (verifyEdges(i_PlayerMoveCoords[1], i_PlayerMoveCoords[0]) && m_Board[i_XCords, i_YCords] == Colors.EMPTY)
            {
                //Console.WriteLine("Enter crawler");
                return crawler(i_XCords, i_YCords, i_PlayerColor);
            }
            else
            {
                return false;
            }
        }

        private bool verifyEdges(int i_X, int i_Y)
        {
            return ((i_X >= 0 && i_X <= m_BoardSize - 1) && (i_Y >= 0 && i_Y <= m_BoardSize - 1));
        }

        private bool crawler(int i_X, int i_Y, Colors i_PlayerColor)
        {
            if (verifyEdges(i_X, i_Y))
            {
                // OLD version
                //return crawlBackSlash_old(i_X, i_Y, i_PlayerColor) || crawlSlash_old(i_X, i_Y, i_PlayerColor) || crawlVertical_old(i_X, i_Y, i_PlayerColor) || crawlHorizontal_old(i_X, i_Y, i_PlayerColor);
                // New version
                return crawlHorizontal(i_X, i_Y, i_PlayerColor) || crawlVertical(i_X, i_Y, i_PlayerColor) || crawlCross(i_X, i_Y, i_PlayerColor);
            }
            else
            {
                //Console.WriteLine("Main crawler, failed on edges");
                return false;
            }
        }

        private bool crawlCross(int i_X, int i_Y, Colors i_PlayerColor)
        {
            int i_Zero = 0;
            return validityCrawler(i_X - 1, -1, i_Y + 1, 1, i_PlayerColor, i_Zero)
                || validityCrawler(i_X + 1, 1, i_Y - 1, -1, i_PlayerColor, i_Zero)
                || validityCrawler(i_X + 1, 1, i_Y + 1, 1, i_PlayerColor, i_Zero)
                || validityCrawler(i_X - 1, -1, i_Y - 1, -1, i_PlayerColor, i_Zero);

        }

        private bool crawlVertical(int i_X, int i_Y, Colors i_PlayerColor)
        {
            int i_Zero = 0;
            return validityCrawler(i_X + 1, 1, i_Y, 0, i_PlayerColor, i_Zero) || validityCrawler(i_X - 1, -1, i_Y, 0, i_PlayerColor, i_Zero);
        }

        private bool crawlHorizontal(int i_X, int i_Y, Colors i_PlayerColor)
        {
            int i_Zero = 0;
            return validityCrawler(i_X, 0, i_Y + 1, 1, i_PlayerColor, i_Zero) || validityCrawler(i_X, 0, i_Y - 1, -1, i_PlayerColor, i_Zero);
        }

        private bool validityCrawler(int i_X, int i_XFactor, int i_Y, int i_YFactor, Colors i_PlayerColor, int i_Count)
        {
            // Check in bounds
            if (!verifyEdges(i_X, i_Y)) { return false; }
            // Init color
            Colors i_CurrentColorFromCell = m_Board[i_X, i_Y];
            // If we hit empty cell
            if (i_CurrentColorFromCell == Colors.EMPTY) { return false; }
            if (i_CurrentColorFromCell == i_PlayerColor)
            {
                return i_Count == 0 ? false : true;
            }
            else // Means it's the opponent color
            {
                return validityCrawler(i_X + i_XFactor, i_XFactor, i_Y + i_YFactor, i_YFactor, i_PlayerColor, i_Count + 1);
            }

        }
        internal void AppendMove(string i_Move, Colors i_GivenColor)
        {
            int i_tempX = (int)Char.GetNumericValue(i_Move[1]) - 1;
            int i_tempY = m_Alphabet.IndexOf(i_Move[0]);
            // Inject new data to array
            m_Board[i_tempX, i_tempY] = i_GivenColor;

            updateFromLocation(i_tempX, i_tempY, i_GivenColor);
        }

        private void updateFromLocation(int i_X, int i_Y, Colors i_GivenColor)
        {
            Console.WriteLine("updating stuff");
            bool dummy = false;
            // Update vertical
            dummy = propagateTable(i_X + 1, 1, i_Y, 0, i_GivenColor);
            dummy = propagateTable(i_X - 1, -1, i_Y, 0, i_GivenColor);
            // Update Horizontal
            dummy = propagateTable(i_X, 0, i_Y + 1, 1, i_GivenColor);
            dummy = propagateTable(i_X, 0, i_Y - 1, -1, i_GivenColor);
            //// Update slash
            dummy = propagateTable(i_X - 1, -1, i_Y + 1, 1, i_GivenColor);
            dummy = propagateTable(i_X + 1, 1, i_Y - 1, -1, i_GivenColor);
            //// Update backslash
            dummy = propagateTable(i_X + 1, 1, i_Y + 1, 1, i_GivenColor);
            dummy = propagateTable(i_X - 1, -1, i_Y - 1, -1, i_GivenColor);
        }

        private bool propagateTable(int i_X, int i_XFactor, int i_Y, int i_YFactor, Colors i_GivenColor)
        {
            // Check edges
            if (!verifyEdges(i_X, i_Y)) { return false; }

            // Get current color
            Colors i_CurrentColorFromCell = m_Board[i_X, i_Y];

            // check if empty
            if (i_CurrentColorFromCell == Colors.EMPTY) { return false; }

            if (i_CurrentColorFromCell == i_GivenColor) { return true; }

            if (propagateTable(i_X + i_XFactor, i_XFactor, i_Y + i_YFactor, i_YFactor, i_GivenColor))
            {
                m_Board[i_X, i_Y] = i_GivenColor;
                return true;
            }
            else
            {
                return false;
            }

        }



        internal Colors CalculateWinner()
        {

            int i_WhitePoints = 0;
            int i_BlackPoints = 0;

            for (int i = 0; i < m_BoardSize; i++)
            {
                for (int j = 0; j < m_BoardSize; j++)
                {
                    if (m_Board[i, j] == Colors.WHITE)
                    {
                        i_WhitePoints++;
                    }
                    else if (m_Board[i, j] == Colors.BLACK)
                    {
                        i_BlackPoints++;
                    }
                }
            }
            return i_BlackPoints > i_WhitePoints ? Colors.BLACK : Colors.WHITE;
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
                        io_ListOfEmptyCells.Add(string.Empty + (i + 1) + string.Empty + UI.GetAvilableLetters()[j]);
                    }
                }
            }

            return io_ListOfEmptyCells;
        }
    }
}
