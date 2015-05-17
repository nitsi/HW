using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Reversi
{
    class UserPlayer
    {
        private string m_PlayerColor;

        public UserPlayer(string i_PlayerColor)
        {
            // TODO: Complete member initialization
            this.m_PlayerColor = i_PlayerColor;
        }

        internal bool HasMoves()
        {
            throw new NotImplementedException();
        }

        internal string GetMove()
        {
            throw new NotImplementedException();
        }

        public string Color
        {
            get
            {
                return this.m_PlayerColor;
            }
        }
    }
}
