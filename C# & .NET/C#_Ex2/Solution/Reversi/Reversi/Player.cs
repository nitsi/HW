using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Reversi
{
    class Player
    {
        private string m_Name;
        private UserInteraction m_UI = new UserInteraction();
        private Colors m_PlayerColor;

        public Player(Colors o_PlayerColor, string o_Name)
        {
            m_PlayerColor = o_PlayerColor;
            m_Name = o_Name;
        }

        public string GetName()
        {
            return m_Name;
        }

        private bool ValidateInput(string i_UserInput, int i_BoardSize, Colors i_PlayerColor, GameBoard i_GameBoard)
        {
            bool answer = true;

            if (i_UserInput.Equals("Q"))
            {
                System.Environment.Exit(0);
            }

            if (i_UserInput.Length == 2 &&
                    m_UI.GetAvilableLetters().IndexOf(i_UserInput[0]) > -1 && 
                    (i_UserInput[1] >= 0 && (int)char.GetNumericValue(i_UserInput[1]) <= i_BoardSize))
            {
                if (i_GameBoard.CheckIfValid(i_UserInput, i_PlayerColor))
                {
                    answer = false;
                }
                else
                {
                    System.Console.WriteLine("The move is not possible as it's not valid for the current board state");
                }
            }
            else
            {
                m_UI.PrintNeedValidData();
            }

            return !answer;
        }

        public string GetMove(int i_BoardSize, Colors i_PlayerColor, GameBoard i_GameBoard)
        {
            while (true)
            {
                string userInput = m_UI.GetUserMove();
                if (ValidateInput(userInput, i_BoardSize, i_PlayerColor, i_GameBoard))
                {
                    return userInput;
                }
            }
        }

        public string GetMovePC(int i_BoardSize, GameBoard i_GameBoard)
        {
            Random randGenerator;
            List<string> blankCells = i_GameBoard.GetListOfEmptyCells();
            int length = blankCells.Count;
            byte guess;
            string chosenMove;

            while (true)
            {
                randGenerator = new Random();
                guess = (byte)randGenerator.Next(length);
                chosenMove = blankCells.ElementAt(guess);

                if (i_GameBoard.CheckIfValid(chosenMove, Colors.WHITE))
                {
                    return chosenMove;
                }
            }
        }

        public Colors Color
        {
            get
            {
                return this.m_PlayerColor;
            }
        }
    }
}