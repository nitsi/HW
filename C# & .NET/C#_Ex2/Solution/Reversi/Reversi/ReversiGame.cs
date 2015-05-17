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
    enum CurrentPlayer
    {
        PlayerOne,
        PlayerTwo
    }
    class ReversiGame
    {
        UserPlayer m_PlayerOne;
        int m_BoardSize;
        internal void Play(bool i_PlayGameFlag)
        {

            //initiation process
            showWelcomeMessage();
            if (i_PlayGameFlag)
            {
                //Get players with lazy declaration
                m_PlayerOne = new UserPlayer(0);
                String i_UserChoice = getChoiceFromConsole();

                //Should fix this
                if (i_UserChoice == GamerTypes.Computer.ToString())
                {
                    ComputerPlayer io_PlayerTwo = new ComputerPlayer(0);
                }
                else
                {
                    UserPlayer io_PlayerTwo = new UserPlayer(0);
                }

                //Pick board size
                m_BoardSize = getBoardSize();
                GameBoard i_GameBoard = new GameBoard(m_BoardSize);

                Console.WriteLine("Great! All set-up! \nPress any key to start \n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                Console.ReadLine();

                //Let's clear the screen
                clearScreen();
                CurrentPlayer m_CurrentPlayer = CurrentPlayer.PlayerOne;
                while (i_GameBoard.GotMoreValidMoves())
                {
                    i_GameBoard.PrintCurrentState();
                    if (m_CurrentPlayer == CurrentPlayer.PlayerOne)
                    {
                        //if first player
                    }
                    else
                    {
                        //if second
                    }
                }

                //From that point we can play

            }
        }

        private void clearScreen()
        {
            Ex02.ConsoleUtils.Screen.Clear();
        }

        private int getBoardSize()
        {
            Console.WriteLine("Nice, Let's move on !");
            while (true)
            {
                Console.WriteLine("Please pick a board size. Choose 6,8 or higher");
                int i_UserInput;
                if (Int32.TryParse(Console.ReadLine(), out i_UserInput))
                {
                    if (i_UserInput == 6 || i_UserInput >= 8)
                    {
                        return i_UserInput;
                    }

                }
                Console.WriteLine("Please make sure to insert a valid number");
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
