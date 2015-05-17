using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Reversi
{
    class GameBoard
    {
        private int m_BoardSize;
        private char[,] m_Board;
        public GameBoard(int i_BoardSize)
        {
            m_BoardSize = i_BoardSize;
            m_Board = new char[m_BoardSize,m_BoardSize];
            initBoard();
        }

        private void initBoard()
        {
            int i_BoardMiddle = m_BoardSize / 2;

            m_Board[i_BoardMiddle, i_BoardMiddle] = 2;
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
