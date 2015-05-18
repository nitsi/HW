using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Reversi
{
    class UserPlayer
    {
        private Colors m_PlayerColor;
        private List<int[]> m_PivotPosition;

        //TODO: change io_
        public UserPlayer(Colors i_PlayerColor)
        {
            // TODO: Complete member initialization
            this.m_PlayerColor = i_PlayerColor;
            //this.m_PivotPosition = new List<int[]>;
        }

        internal bool HasMoves()
        {
            throw new NotImplementedException();
        }

        internal string GetMove()
        {
            throw new NotImplementedException();
        }

        public Colors Color
        {
            get
            {
                return this.m_PlayerColor;
            }
        }

        public void AddPosition()
        {

        }

        public void RemovePosition()
        {

        }
    }
}
