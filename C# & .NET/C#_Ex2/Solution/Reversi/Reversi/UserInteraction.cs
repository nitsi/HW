using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Reversi
{
    class UserInteraction
    {
        static string AVAILABLE_LETTERS = "ABCDEFGH";
        static readonly string ERROR_ = "ERROR: ";
        static readonly string ERROR_DATA_NOT_VALID = "ERROR: The input data is not valid";

        static readonly string INFO_NEED_NEW_CELL_INPUT = "Please enter a cell in format Letter,Number. e.g: A3";
        static readonly string INFO_ = "INFO: ";

        public UserInteraction()
        {
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

                Console.WriteLine("Please choose 6 or 8 only!");
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

        public void generateTableBorderSpan()
        {
            Console.Write("    ");
        }

        public void genreateTableColumnSeparator()
        {
            Console.Write(" | ");
        }

        public void GenerateTableLineNumber(int i)
        {
            Console.Write((i + 1) + " )");
        }

        public void GenerateTableLineSeparators(int i_BoardSize)
        {
            generateTableBorderSpan();
            for (int i = 0; i < i_BoardSize; i++)
            {
                Console.Write("====");
            }
        }

        public void GenerateTableTopLetters(string m_Alphabet, int i_BoardSize)
        {
            // Generate top letters
            //TODO: replace with regular print add comment above
            generateTableBorderSpan();
            for (int i = 0; i < i_BoardSize; i++)
            {
                Console.Write("  " + m_Alphabet[i] + " ");
            }
        }

        public void GenereateTableNewLine()
        {
            Console.WriteLine();
        }
    }
}
