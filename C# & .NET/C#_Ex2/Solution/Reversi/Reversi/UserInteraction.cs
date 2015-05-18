using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Reversi
{
    class UserInteraction
    {
        static readonly string ERROR_ = "ERROR: ";
        static readonly string ERROR_ = "ERROR: ";
        static readonly string ERROR_ = "ERROR: ";
        static readonly string ERROR_ = "ERROR: ";
        static readonly string ERROR_ = "ERROR: ";
        static readonly string ERROR_ = "ERROR: ";
        static readonly string ERROR_DATA_NOT_VALID = "ERROR: The input data is not valid";

        static readonly string INFO_NEED_NEW_CELL_INPUT = "INFO: Please enter a cell in format Letter,Number. e.g: A3";

        public UserInteraction()
        {
        }

        public void PrintNeedValidData()
        {
            System.Console.WriteLine(ERROR_DATA_NOT_VALID);
            System.Console.WriteLine(INFO_NEED_NEW_CELL_INPUT);
        }

        public void ClearScreen()
        {
            Ex02.ConsoleUtils.Screen.Clear();
        }

        public int GetBoardSize()
        {
            while (true)
            {
                Console.WriteLine("Please pick a board size. Choose 6,8 or higher");
                int i_UserInput;
                if (int.TryParse(Console.ReadLine(), out i_UserInput))
                {
                    if (i_UserInput == 6 || i_UserInput == 8)
                    {
                        return i_UserInput;
                    }
                }

                Console.WriteLine("Please make sure to insert a valid number");
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
    }
}
