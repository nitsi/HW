using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Reversi
{
    class UserPlayer
    {
        private int m_AmountOfPoints;
        private int p1;
        private string m_PlayerColor;
               

        public UserPlayer(int p,string i_PlayerColor)
        {
            // TODO: Complete member initialization
            this.m_AmountOfPoints = p;
            this.m_PlayerColor = i_PlayerColor;
        }

        internal bool HasMoves()
        {
            throw new NotImplementedException();
        }

        internal String GetMove()
        {
            throw new NotImplementedException();
        }
    }
}
