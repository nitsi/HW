using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Reversi
{
    enum Pions
    {
        BLACK, // marks black
        WHITE, // marks white
        EMPTY
    }

    class GameBoard
    {
        private int m_BoardSize;
        private Pions[,] m_Board;

        private string m_Alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        public GameBoard(int i_BoardSize)
        {
            m_BoardSize = i_BoardSize;
            m_Board = new Pions[m_BoardSize, m_BoardSize];
            initBoard();
        }

        private void initBoard()
        {
            // init the array with empty
            for (int i = 0; i < m_BoardSize; i++)
            {
                for (int j = 0; j < m_BoardSize; j++)
                {
                    m_Board[i, j] = Pions.EMPTY;
                }
            }

            int i_BoardMiddle = m_BoardSize / 2;

            // Append BLACK
            m_Board[i_BoardMiddle, i_BoardMiddle] = Pions.BLACK;
            m_Board[i_BoardMiddle - 1, i_BoardMiddle - 1] = Pions.BLACK;

            // Append WHITE
            m_Board[i_BoardMiddle - 1, i_BoardMiddle] = Pions.WHITE;
            m_Board[i_BoardMiddle, i_BoardMiddle - 1] = Pions.BLACK;
        }

        internal bool GotMoreValidMoves()
        {
            return true;
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
                    generateColumnSeparator();
                }

                generateNewLine();
                generateLineSeparators();
                generateNewLine();
            }
        }

        private void generateCellContent(int i, int j)
        {
            Pions i_TempPionContent = m_Board[i, j];
            if (i_TempPionContent == Pions.EMPTY)
            {
                Console.Write(" ");
            }
            else if (i_TempPionContent == Pions.BLACK)
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
            Console.Write(i + " |");
        }

        private void generateLineSeparators()
        {
            generateBorderSpan();
            for (int i = 0; i < m_BoardSize; i++)
            {
                Console.Write("=======");
            }
        }

        private void generateBorderSpan()
        {
            Console.Write("    ");
        }

        private void generateTopLetters()
        {
            // Generate top letters
            generateBorderSpan();
            for (int i = 0; i < m_BoardSize; i++)
            {
                Console.Write("  " + m_Alphabet[i] + "   ");
            }
        }

        private void generateNewLine()
        {
            Console.WriteLine();
        }

        internal bool CheckIfValid(string i_Move)
        {
            throw new NotImplementedException();
        }

        internal void AppendMove(object p)
        {
            throw new NotImplementedException();
        }

        internal string CalculateWinner()
        {
            return null; // "Black"
        }
    }
}
