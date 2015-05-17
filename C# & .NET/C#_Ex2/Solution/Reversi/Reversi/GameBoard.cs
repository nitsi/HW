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

        public bool GotMoreValidMoves { get; set; }
    }
}
