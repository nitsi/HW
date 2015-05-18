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
        private ArrayList m_PlayerOneValidMoves;
        private ArrayList m_PlayerTwoValidMoves;


        //TODO: Lower letters to 8 letters
        private string m_Alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        //TODO: change to io
        public GameBoard(int i_BoardSize)
        {
            m_BoardSize = i_BoardSize;
            m_Board = new Colors[m_BoardSize, m_BoardSize];
            initBoard();
            initMovesTrackers();
        }

        private void initMovesTrackers()
        {
            throw new NotImplementedException();
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

        internal bool GotMoreValidMoves(CurrentPlayer currentPlayer)
        {
            throw new NotImplementedException();
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
            i_PlayerMoveCoords[1] = i_PlayerMove[1];

            if (!verifyEdges(i_PlayerMoveCoords[0], i_PlayerMoveCoords[1]) || m_Board[i_PlayerMoveCoords[0], i_PlayerMoveCoords[1]] != Colors.EMPTY)
            {
                return false;
            }
            else
            {
                return !crawler(i_PlayerMoveCoords[0], i_PlayerMoveCoords[1], i_PlayerColor) ? false : true;
            }
        }

        private bool verifyEdges(int p1, int p2)
        {
            return ((p1 > 0 && p1 <= m_BoardSize) && (p2 > 0 && p2 <= m_BoardSize));
        }

        private bool crawler(int i_X, int i_Y, Colors i_PlayerColor)
        {
            int io_TempCounterForCrawlers = 0;
            if (verifyEdges(i_X, i_Y))
            {
                return crawlVertical(i_X, i_Y, i_PlayerColor, io_TempCounterForCrawlers) || crawlHorizontal(i_X, i_Y, i_PlayerColor, io_TempCounterForCrawlers) || crawlSlash(i_X, i_Y, i_PlayerColor, io_TempCounterForCrawlers) || crawlBackslash(i_X, i_Y, i_PlayerColor, io_TempCounterForCrawlers);
            }
            else
            {
                return false;
            }
        }

        private bool crawlBackslash(int i_X, int i_Y, Colors i_PlayerColor, int io_TempCounterForCrawlers)
        {// If we're out of bound we return false
            if (!verifyEdges(i_X, i_Y)) { return false; }
            // Init temp var for ease
            Colors i_CurrentColorInCell;
            i_CurrentColorInCell = m_Board[i_X, i_Y];
            // If current color in cell is ours, we test if we passed at least one pion. if yes -> return true
            if (i_CurrentColorInCell == i_PlayerColor)
            {
                return io_TempCounterForCrawlers > 0 ? true : false;
            }
            // Else if the cell is empty, means we can't block with it. So we return false
            else if (i_CurrentColorInCell == Colors.EMPTY)
            {
                return false;
            }
            // Else, the color is not ours neither empty hence it's the enemy. we go recursive right and left and increase counter for passed pions.
            else
            {
                return crawlBackslash(i_X + 1, i_Y - 1, i_PlayerColor, io_TempCounterForCrawlers + 1) || crawlBackslash(i_X - 1, i_Y + 1, i_PlayerColor, io_TempCounterForCrawlers + 1);
            }
        }

        private bool crawlSlash(int i_X, int i_Y, Colors i_CurrentPlayerColor, int io_TempCounterForCrawlers)
        {
            // If we're out of bound we return false
            if (!verifyEdges(i_X, i_Y)) { return false; }
            // Init temp var for ease
            Colors i_CurrentColorInCell;
            i_CurrentColorInCell = m_Board[i_X, i_Y];
            // If current color in cell is ours, we test if we passed at least one pion. if yes -> return true
            if (i_CurrentColorInCell == i_CurrentPlayerColor)
            {
                return io_TempCounterForCrawlers > 0 ? true : false;
            }
            // Else if the cell is empty, means we can't block with it. So we return false
            else if (i_CurrentColorInCell == Colors.EMPTY)
            {
                return false;
            }
            // Else, the color is not ours neither empty hence it's the enemy. we go recursive right and left and increase counter for passed pions.
            else
            {
                return crawlSlash(i_X - 1, i_Y - 1, i_CurrentPlayerColor, io_TempCounterForCrawlers + 1) || crawlSlash(i_X + 1, i_Y + 1, i_CurrentPlayerColor, io_TempCounterForCrawlers + 1);
            }
        }

        private bool crawlHorizontal(int i_X, int i_Y, Colors i_CurrentPlayerColor, int io_TempCounterForCrawlers)
        {
            // If we're out of bound we return false
            if (!verifyEdges(i_X, i_Y)) { return false; }
            // Init temp var for ease
            Colors i_CurrentColorInCell;
            i_CurrentColorInCell = m_Board[i_X, i_Y];
            // If current color in cell is ours, we test if we passed at least one pion. if yes -> return true
            if (i_CurrentColorInCell == i_CurrentPlayerColor)
            {
                return io_TempCounterForCrawlers > 0 ? true : false;
            }
            // Else if the cell is empty, means we can't block with it. So we return false
            else if (i_CurrentColorInCell == Colors.EMPTY)
            {
                return false;
            }
            // Else, the color is not ours neither empty hence it's the enemy. we go recursive right and left and increase counter for passed pions.
            else
            {
                return crawlHorizontal(i_X, i_Y + 1, i_CurrentPlayerColor, io_TempCounterForCrawlers + 1) || crawlHorizontal(i_X, i_Y - 1, i_CurrentPlayerColor, io_TempCounterForCrawlers + 1);
            }
        }

        private bool crawlVertical(int i_X, int i_Y, Colors i_CurrentPlayerColor, int io_TempCounterForCrawlers)
        {
            // If we're out of bound we return false
            if (!verifyEdges(i_X, i_Y)) { return false; }
            // Init temp var for ease
            Colors i_CurrentColorInCell;
            i_CurrentColorInCell = m_Board[i_X, i_Y];
            // If current color in cell is ours, we test if we passed at least one pion. if yes -> return true
            if (i_CurrentColorInCell == i_CurrentPlayerColor)
            {
                return io_TempCounterForCrawlers > 0 ? true : false;
            }
            // Else if the cell is empty, means we can't block with it. So we return false
            else if (i_CurrentColorInCell == Colors.EMPTY)
            {
                return false;
            }
            // Else, the color is not ours neither empty hence it's the enemy. we go recursive right and left and increase counter for passed pions.
            else
            {
                return crawlVertical(i_X + 1, i_Y, i_CurrentPlayerColor, io_TempCounterForCrawlers + 1) || crawlVertical(i_X - 1, i_Y, i_CurrentPlayerColor, io_TempCounterForCrawlers + 1);
            }
        }

        internal void AppendMove(object p)
        {
            throw new NotImplementedException();
            // insert given value to array
            // re-eval the m_Board
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
