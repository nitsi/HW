using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Collections;

namespace Reversi
{
    /*
     * TEST 
     */
    class GameBoard
    {
        private int m_BoardSize;
        private Colors[,] m_Board;


        private string m_Alphabet = "ABCDEFGH";

        public GameBoard(int io_BoardSize)
        {
            m_BoardSize = io_BoardSize;
            m_Board = new Colors[m_BoardSize, m_BoardSize];
            initBoard();
        }

        private void initBoard()
        {
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
            if (i_TempPionContent == Colors.EMPTY)
            {
                Console.Write(" ");
            }
            else if (i_TempPionContent == Colors.BLACK)
            {
                Console.Write("X");
            }
            else
            {
                Console.Write("O");
            }
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
            generateBorderSpan();
            for (int i = 0; i < m_BoardSize; i++)
            {
                Console.Write("====");
            }
        }

        private void generateBorderSpan()
        {
            Console.Write("    ");
        }

        private void generateTopLetters()
        {
            // Generate top letters
            //TODO: replace with regular print add comment above
            generateBorderSpan();
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

        internal bool CheckIfValid(string i_PlayerMove, Colors i_PlayerColor)
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
                Console.WriteLine("Enter crawler");
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

                return crawlBackSlash(i_X, i_Y, i_PlayerColor) || crawlSlash(i_X, i_Y, i_PlayerColor) || crawlVertical(i_X, i_Y, i_PlayerColor) || crawlHorizontal(i_X, i_Y, i_PlayerColor);
            }
            else
            {
                Console.WriteLine("Main crawler, failed on edges");
                return false;
            }
        }

        private bool crawlSlash(int i_X, int i_Y, Colors i_PlayerColor)
        {
            // Iterative implementation
            Colors i_CurrentColorFromCell;
            int i_TempCounterForCrawlers = 0;
            int i_TempX = i_X;
            int i_TempY = i_Y;
            //move back. formula is [x+1,y-1]
            //13 is the maximal value by pithagoras
            for (int i = 0; i < 13; i++)
            {
                if (!verifyEdges(i_TempX, i_TempY)) { break; }
                i_CurrentColorFromCell = m_Board[i_TempX, i_TempY];
                // Means we hit cell from same color, but it's adjacent
                if (i_CurrentColorFromCell == i_PlayerColor && i_TempCounterForCrawlers == 0) { return false; }
                // Means we hit a cell from another color, but we passed other cells on the way
                if (i_CurrentColorFromCell == i_PlayerColor && i_TempCounterForCrawlers > 0) { return true; }
                if (i_CurrentColorFromCell != i_PlayerColor && i_CurrentColorFromCell != Colors.EMPTY)
                {
                    i_TempCounterForCrawlers++;
                }
                i_TempX++;
                i_TempY--;
            }
            i_TempCounterForCrawlers = 0;
            i_TempX = i_X;
            i_TempY = i_Y;
            for (int i = 0; i < 13; i++)
            {
                if (!verifyEdges(i_TempX, i_TempY)) { break; }
                i_CurrentColorFromCell = m_Board[i_TempX, i_TempY];
                // Means we hit cell from same color, but it's adjacent
                if (i_CurrentColorFromCell == i_PlayerColor && i_TempCounterForCrawlers == 0) { return false; }
                // Means we hit a cell from another color, but we passed other cells on the way
                if (i_CurrentColorFromCell == i_PlayerColor && i_TempCounterForCrawlers > 0) { return true; }
                if (i_CurrentColorFromCell != i_PlayerColor && i_CurrentColorFromCell != Colors.EMPTY)
                {
                    i_TempCounterForCrawlers++;
                }
                i_TempX--;
                i_TempY++;
            }
            // If all failed
            return false;
        }

        private bool crawlBackSlash(int i_X, int i_Y, Colors i_PlayerColor)
        {
            // Iterative implementation
            Colors i_CurrentColorFromCell;
            int i_TempCounterForCrawlers = 0;
            int i_TempX = i_X;
            int i_TempY = i_Y;
            //move back. formula is [x+1,y+1]
            //13 is the maximal value by pithagoras
            for (int i = 0; i < 13; i++)
            {
                if (!verifyEdges(i_TempX, i_TempY)) { break; }
                i_CurrentColorFromCell = m_Board[i_TempX, i_TempY];
                // Means we hit cell from same color, but it's adjacent
                if (i_CurrentColorFromCell == i_PlayerColor && i_TempCounterForCrawlers == 0) { return false; }
                // Means we hit a cell from another color, but we passed other cells on the way
                if (i_CurrentColorFromCell == i_PlayerColor && i_TempCounterForCrawlers > 0) { return true; }
                if (i_CurrentColorFromCell != i_PlayerColor && i_CurrentColorFromCell != Colors.EMPTY)
                {
                    i_TempCounterForCrawlers++;
                }
                i_TempX++;
                i_TempY++;
            }
            i_TempCounterForCrawlers = 0;
            i_TempX = i_X;
            i_TempY = i_Y;
            for (int i = 0; i < 13; i++)
            {
                if (!verifyEdges(i_TempX, i_TempY)) { break; }
                i_CurrentColorFromCell = m_Board[i_TempX, i_TempY];
                // Means we hit cell from same color, but it's adjacent
                if (i_CurrentColorFromCell == i_PlayerColor && i_TempCounterForCrawlers == 0) { return false; }
                // Means we hit a cell from another color, but we passed other cells on the way
                if (i_CurrentColorFromCell == i_PlayerColor && i_TempCounterForCrawlers > 0) { return true; }
                if (i_CurrentColorFromCell != i_PlayerColor && i_CurrentColorFromCell != Colors.EMPTY)
                {
                    i_TempCounterForCrawlers++;
                }
                i_TempX--;
                i_TempY--;
            }
            // If all failed
            return false;
        }

        private bool crawlHorizontal(int i_X, int i_Y, Colors i_PlayerColor)
        {
            // Iterative implementation
            Colors i_CurrentColorFromCell;
            int i_TempCounterForCrawlers = 0;
            for (int i = i_Y + 1; i < m_BoardSize; i++)
            {
                i_CurrentColorFromCell = m_Board[i_X, i];
                // Means we hit cell from same color, but it's adjacent
                if (i_CurrentColorFromCell == i_PlayerColor && i_TempCounterForCrawlers == 0) { return false; }
                // Means we hit a cell from another color, but we passed other cells on the way
                if (i_CurrentColorFromCell == i_PlayerColor && i_TempCounterForCrawlers > 0) { return true; }
                if (i_CurrentColorFromCell != i_PlayerColor && i_CurrentColorFromCell != Colors.EMPTY)
                {
                    i_TempCounterForCrawlers++;
                }
            }
            i_TempCounterForCrawlers = 0;
            for (int i = i_Y; i > -1; i--)
            {
                i_CurrentColorFromCell = m_Board[i_X, i];
                // Means we hit cell from same color, but it's adjacent
                if (i_CurrentColorFromCell == i_PlayerColor && i_TempCounterForCrawlers == 0) { return false; }
                // Means we hit a cell from another color, but we passed other cells on the way
                if (i_CurrentColorFromCell == i_PlayerColor && i_TempCounterForCrawlers > 0) { return true; }
                if (i_CurrentColorFromCell != i_PlayerColor && i_CurrentColorFromCell != Colors.EMPTY)
                {
                    i_TempCounterForCrawlers++;
                }
            }
            // If all failed
            return false;
        }

        private bool crawlVertical(int i_X, int i_Y, Colors i_PlayerColor)
        {
            // Iterative implementation
            Colors i_CurrentColorFromCell;
            int i_TempCounterForCrawlers = 0;
            for (int i = i_X + 1; i < m_BoardSize; i++)
            {
                if (!verifyEdges(i_X, i_Y)) { return false; }
                i_CurrentColorFromCell = m_Board[i, i_Y];
                // Means we hit cell from same color, but it's adjacent
                if (i_CurrentColorFromCell == i_PlayerColor && i_TempCounterForCrawlers == 0) { return false; }
                // Means we hit a cell from another color, but we passed other cells on the way
                if (i_CurrentColorFromCell == i_PlayerColor && i_TempCounterForCrawlers > 0) { return true; }
                if (i_CurrentColorFromCell != i_PlayerColor && i_CurrentColorFromCell != Colors.EMPTY)
                {
                    i_TempCounterForCrawlers++;
                }
            }
            i_TempCounterForCrawlers = 0;
            for (int i = i_X; i > -1; i--)
            {
                if (!verifyEdges(i_X, i_Y)) { return false; }

                i_CurrentColorFromCell = m_Board[i, i_Y];
                // Means we hit cell from same color, but it's adjacent
                if (i_CurrentColorFromCell == i_PlayerColor && i_TempCounterForCrawlers == 0) { return false; }
                // Means we hit a cell from another color, but we passed other cells on the way
                if (i_CurrentColorFromCell == i_PlayerColor && i_TempCounterForCrawlers > 0) { return true; }
                if (i_CurrentColorFromCell != i_PlayerColor && i_CurrentColorFromCell != Colors.EMPTY)
                {
                    i_TempCounterForCrawlers++;
                }
            }
            // If all failed
            return false;
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

            // Update vertical
            propagatePositiveVertical(i_X, i_Y, i_GivenColor);
            propagateNegativeVertical(i_X, i_Y, i_GivenColor);
            // Update Horizontal
            //propagatePositiveHorizontal(i_X, i_Y, i_GivenColor);
            //propagatePositiveHorizontal(i_X, i_Y, i_GivenColor);
            //// Update slash
            //propagatePositiveSlash(i_X, i_Y, i_GivenColor);
            //propagateNegativeSlash(i_X, i_Y, i_GivenColor);
            //// Update backslash
            //propagatePositiveBackslash(i_X, i_Y, i_GivenColor);
            //propagateNegativeBackslash(i_X, i_Y, i_GivenColor);
        }

        private void propagateNegativeVertical(int i_X, int i_Y, Colors i_GivenColor)
        {
            //throw new NotImplementedException();
        }

        private void propagatePositiveVertical(int i_X, int i_Y, Colors i_GivenColor)
        {
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
    }
}
