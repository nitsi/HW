using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Reversi
{
    class ComputerPlayer
    {
        private Colors m_PlayerColor;
        private string m_Alphabet = "ABCDEFGH";

        public ComputerPlayer()
        {
            m_PlayerColor = Colors.WHITE;
        }

        internal bool HasMoves()
        {
            throw new NotImplementedException();
        }

        internal string GetMove(int i_BoardSize)
        {
            Random randGenerator = new Random();
            byte randomLetterCell = (byte)randGenerator.Next(i_BoardSize);
            byte randomNumCell = (byte)randGenerator.Next(i_BoardSize);
            StringBuilder newMove = new StringBuilder();
            newMove.Append((char)(randomLetterCell + 65)).Append((char)randomNumCell);

            return newMove.ToString();
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
