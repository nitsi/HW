using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Reversi
{
    enum Pions
    {
        BLACK, //marks black
        WHITE, //marks white
        EMPTY
    }
    class GameBoard
    {

        private int m_BoardSize;
        private Pions[,] m_Board;
        public GameBoard(int i_BoardSize)
        {
            m_BoardSize = i_BoardSize;
            m_Board = new Pions[m_BoardSize, m_BoardSize];
            initBoard();
        }

        private void initBoard()
        {
            //init the array with empty
            for (int i = 0; i < m_BoardSize; i++)
            {
                for (int j = 0; j < m_BoardSize; j++)
                {
                    m_Board[i, j] = Pions.EMPTY;
                }

            }

            int i_BoardMiddle = m_BoardSize / 2;
            //Append BLACK
            m_Board[i_BoardMiddle, i_BoardMiddle] = Pions.BLACK;
            m_Board[i_BoardMiddle - 1, i_BoardMiddle - 1] = Pions.BLACK;

            //Append WHITE
            m_Board[i_BoardMiddle - 1, i_BoardMiddle] = Pions.WHITE;
            m_Board[i_BoardMiddle, i_BoardMiddle - 1] = Pions.BLACK;
        }

        internal bool GotMoreValidMoves()
        {
            return false;
        }

        internal void PrintCurrentState()
        {
            throw new NotImplementedException();
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
            return "Black";
        }
    }
}
