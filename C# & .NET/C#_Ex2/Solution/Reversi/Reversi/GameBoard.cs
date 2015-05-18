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
            if (verifyEdges(i_X, i_Y))
            {
                return crawlRight(i_X, i_Y, i_PlayerColor) || crawlHorizontal(i_X, i_Y, i_PlayerColor) || crawlSlash(i_X, i_Y, i_PlayerColor) || crawlBackslash(i_X, i_Y, i_PlayerColor);
            }
            else
            {
                return false;
            }
        }

        private bool crawlRight(int i_X, int i_Y, Colors i_CurrentPlayerColor)
        {
            int i_TempXCordsHolder = i_X + 1;
            int i_TempYCordsHolder = i_Y;
            int i_EnemyUnitsCounter = 0;
            Colors i_CurrentColorInCell;
            // While in range
            while (verifyEdges(i_TempXCordsHolder, i_TempYCordsHolder))
            {
                i_CurrentColorInCell = m_Board[i_TempXCordsHolder, i_TempYCordsHolder];
                // Test for enemy spots
                if (i_CurrentColorInCell != i_CurrentPlayerColor && i_CurrentColorInCell != Colors.EMPTY)
                {
                    //Move right & increase count
                    i_EnemyUnitsCounter++;
                    i_TempXCordsHolder++;

                    continue;
                }

                else if (i_CurrentColorInCell == i_CurrentPlayerColor)
                {
                    return i_EnemyUnitsCounter == 0 ? false : true;
                }
                else // else means the cell is empty
                {
                    return false;
                }
            }
            // If we reached this it means we're out of boundries for the edges -> means no room
            return false;
        }

        internal void AppendMove(object p)
        {
            throw new NotImplementedException();
            // insert given value to array
            // re-eval the m_Board
        }

        internal string CalculateWinner()
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
            return i_BlackPoints > i_WhitePoints ? Colors.BLACK.ToString() : Colors.WHITE.ToString();
        }



    }
}
