using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Reversi
{
    class UserInteraction
    {
        private static readonly string ERROR_DATA_NOT_VALID = "ERROR: The input data is not valid";

        private static readonly string INFO_NEED_NEW_CELL_INPUT = "Please enter a cell in format Letter,Number. e.g: A3";

        private static string AVAILABLE_LETTERS = "ABCDEFGH";

        public UserInteraction()
        {
        }

        public void TurnOf(string i_Name)
        {
            Console.WriteLine("Turn of " + i_Name);
        }

        public string GetName()
        {
            System.Console.WriteLine("Please enter your name: ");
            string name = Console.ReadLine();
            return name;
        }

        public void PlayerNoMoves(string i_Name)
        {
            string toPrint = string.Format("{0} turn, but {0} has no moves possible", i_Name);
            System.Console.WriteLine(toPrint);
        }

        public string GetAvilableLetters()
        {
            return AVAILABLE_LETTERS;
        }

        public void PrintNeedValidData()
        {
            System.Console.WriteLine(ERROR_DATA_NOT_VALID);
            System.Console.WriteLine(INFO_NEED_NEW_CELL_INPUT);
        }

        public string GetUserMove()
        {
            Console.WriteLine("Please enter your move : ");
            string userInput = Console.ReadLine();
            return userInput;
        }

        public void ClearScreen()
        {
            Ex02.ConsoleUtils.Screen.Clear();
        }

        public int GetBoardSize()
        {
            while (true)
            {
                Console.WriteLine("Please pick a board size. Choose 6 or 8");
                int i_UserInput;
                if (int.TryParse(Console.ReadLine(), out i_UserInput))
                {
                    if (i_UserInput == 6 || i_UserInput == 8)
                    {
                        return i_UserInput;
                    }
                }

                Console.WriteLine("Please choose exactly 6 or 8");
            }
        }

        public string GetChoiceFromConsole()
        {
            string tempStringForIOTests;
            while (true)
            {
                Console.WriteLine("Would you like to play vs a Computer or another User ? (Type Computer or User)");
                tempStringForIOTests = Console.ReadLine();
                if (tempStringForIOTests == "User" || tempStringForIOTests == "Computer")
                {
                    return tempStringForIOTests;
                }

                Console.WriteLine("Please type in the key words as shown on CLI");
            }
        }

        public void GenerateTableCellContet(Colors i_TempPionContent)
        {
            if (i_TempPionContent == Colors.EMPTY)
            {
                Console.Write(" ");
            }
            else if (i_TempPionContent == Colors.BLACK)
            {
                Console.Write("X");
            }
            else
            {
                Console.Write("O");
            }
        }

        public void GenerateTableBorderSpan()
        {
            Console.Write("    ");
        }

        public void GenreateTableColumnSeparator()
        {
            Console.Write(" | ");
        }

        public void GenerateTableLineNumber(int i)
        {
            Console.Write((i + 1) + " )");
        }

        public void GenerateTableLineSeparators(int i_BoardSize)
        {
            GenerateTableBorderSpan();
            for (int i = 0; i < i_BoardSize; i++)
            {
                Console.Write("====");
            }
        }

        public void GenerateTableTopLetters(int i_BoardSize)
        {
            // Generate top letters
            GenerateTableBorderSpan();
            for (int i = 0; i < i_BoardSize; i++)
            {
                Console.Write("  " + GetAvilableLetters()[i] + " ");
            }
        }

        public void GenereateTableNewLine()
        {
            Console.WriteLine();
        }

        internal void printCurrentTableState(int i_BoardSize, Colors[,] i_Board)
        {
            GenerateTableTopLetters(i_BoardSize);
            GenereateTableNewLine();
            GenerateTableLineSeparators(i_BoardSize);
            GenereateTableNewLine();
            for (int i = 0; i < i_BoardSize; i++)
            {
                GenerateTableLineNumber(i);
                for (int j = 0; j < i_BoardSize; j++)
                {
                    GenreateTableColumnSeparator();
                    Colors tempPionContent = i_Board[i, j];
                    GenerateTableCellContet(tempPionContent);
                }

                GenereateTableNewLine();
                GenerateTableLineSeparators(i_BoardSize);
                GenereateTableNewLine();
            }
        }
    }
}
