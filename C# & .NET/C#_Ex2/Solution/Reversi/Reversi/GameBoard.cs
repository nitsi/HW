using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Reversi
{
    class GameBoard
    {
        private int m_BoardSize;

        public GameBoard(int m_BoardSize)
        {
            this.m_BoardSize = m_BoardSize;
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
