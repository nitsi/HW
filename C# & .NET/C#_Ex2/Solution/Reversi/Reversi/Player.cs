using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Reversi
{
    class Player
    {
        private Colors m_PlayerColor;

        public Player(Colors o_PlayerColor)
        {
            this.m_PlayerColor = o_PlayerColor;
        }

        private bool ValidateInput(string i_UserInput, int i_BoardSize, Colors i_PlayerColor, GameBoard i_GameBoard)
        {
            string m_Alphabet = "ABCDEFGH";
            bool answer = true;

            if(i_UserInput.Equals("Q"))
            {
                System.Environment.Exit(0);
            }

            if (i_UserInput.Length == 2 &&
                    m_Alphabet.IndexOf(i_UserInput[0]) > -1 && 
                    (i_UserInput[1] >= 0 && (int)Char.GetNumericValue(i_UserInput[1]) <= i_BoardSize))
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
                System.Console.WriteLine("The input data is not valid");
                System.Console.WriteLine("Please enter a cell in format Letter,Number. e.g: A3");
            }
            return !answer;
        }

        public string GetMove(int i_BoardSize, Colors i_PlayerColor, GameBoard i_GameBoard)
        {
            while (true)
            {
                Console.WriteLine("Please enter your move : ");
                string i_UserInput = Console.ReadLine();
                if (ValidateInput(i_UserInput, i_BoardSize, i_PlayerColor, i_GameBoard))
                {
                    return i_UserInput;
                }

                Console.WriteLine("Invalid move!");
            }
        }

        public string GetMovePC(int i_BoardSize, GameBoard gameB)
        {
            Random randGenerator;
            byte randomLetterCell;
            byte randomNumCell;
            StringBuilder newMove = new StringBuilder();

            while (true)
            {
                randGenerator = new Random();
                randomLetterCell = (byte)randGenerator.Next(i_BoardSize);
                randomNumCell = (byte)randGenerator.Next(i_BoardSize);
                newMove.Append((char)(randomLetterCell + 65)).Append((char)(randomNumCell + 48));

                if (gameB.CheckIfValid(newMove.ToString(), Colors.WHITE))
                {
                    return newMove.ToString();
                }
                else
                {
                    newMove.Clear();
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