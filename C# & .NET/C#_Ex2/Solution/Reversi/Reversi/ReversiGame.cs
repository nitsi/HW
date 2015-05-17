using System;
using System.Collections.Generic;
using System.Text;

namespace Reversi
{
    enum GamerTypes
    {
        User,
        Computer
    }

    class ReversiGame
    {
        UserPlayer m_PlayerOne;

        internal void Play(bool i_PlayGameFlag)
        {
            if (i_PlayGameFlag)
            {
                showWelcomeMessage();
                m_PlayerOne = new UserPlayer(0);
                String i_UserChoice = getChoiceFromConsole();
                if (i_UserChoice == GamerTypes.Computer.ToString())
                {
                    ComputerPlayer io_PlayerTwo = new ComputerPlayer(0);
                }
                else
                {
                    UserPlayer io_PlayerTwo = new UserPlayer(0);
                }
            }
        }

        private void showWelcomeMessage()
        {
            Console.WriteLine("Welcome to Reversi by Aviad Hahami && Matan Gidnian ! ");
        }

        private string getChoiceFromConsole()
        {
            String i_TempStringForIOTests;
            while (true)
            {
                Console.WriteLine("Would you like to play vs a Computer or another User ? (Type Computer or User)");
                i_TempStringForIOTests = Console.ReadLine();
                if (i_TempStringForIOTests == "User" || i_TempStringForIOTests == "Computer")
                {
                    return i_TempStringForIOTests;
                }
                Console.WriteLine("Please type in the key words as shown on CLI");
            }
        }
    }
}
