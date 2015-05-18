using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Reversi
{
    class UserPlayer
    {
        private Colors m_PlayerColor;
        // ????? private List<int[]> m_PivotPosition;
        private string m_Alphabet = "ABCDEFGH";

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
            while (true)
            {
                Console.WriteLine("Please enter your move : ");
                string i_UserInput = Console.ReadLine();
                if (i_UserInput.Length == 2 && m_Alphabet.IndexOf(i_UserInput[0]) > -1 && (i_UserInput[1] >= 0))
                {
                    return i_UserInput;
                }
                Console.WriteLine("Please enter proper moves");
            }
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
