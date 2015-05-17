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

    enum Colors
    {
        Black,
        White
    }

    class ReversiGame
    {
        UserPlayer m_PlayerOne;
        UserPlayer m_PlayerTwo;
        int m_BoardSize;

        internal void Play(bool i_PlayGameFlag)
        {
            // initiation process
            showWelcomeMessage();
            while (i_PlayGameFlag)
            {
                // Get players with lazy declaration
                m_PlayerOne = new UserPlayer(Colors.Black.ToString());
                string i_UserChoice = getChoiceFromConsole();

                // Should fix this, Currently has no PC
                if (i_UserChoice == GamerTypes.Computer.ToString())
                {
                    m_PlayerTwo = new UserPlayer(Colors.White.ToString());
                }
                else
                {
                    m_PlayerTwo = new UserPlayer(Colors.White.ToString());
                }

                // Pick board size
                m_BoardSize = getBoardSize();
                GameBoard i_GameBoard = new GameBoard(m_BoardSize);

                Console.WriteLine("Great! All set-up! \nPress any key to start \n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                Console.ReadLine();

                // Let's clear the screen
                clearScreen();
                CurrentPlayer m_CurrentPlayer = CurrentPlayer.PlayerOne;
                while (i_GameBoard.GotMoreValidMoves())
                {
                    i_GameBoard.PrintCurrentState();
                    if (m_CurrentPlayer == CurrentPlayer.PlayerOne)
                    {
                        // if first player
                        while (true)
                        {
                            string i_Move = m_PlayerOne.GetMove();
                            if (i_GameBoard.CheckIfValid(i_Move))
                            {
                                i_GameBoard.AppendMove(m_PlayerOne.Color);
                                break;
                            }
                            Console.WriteLine("Invalid move!");
                        }
                    }
                    else
                    {
                        // if second
                        while (true)
                        {
                            string i_Move = m_PlayerTwo.GetMove();
                            if (i_GameBoard.CheckIfValid(i_Move))
                            {
                                i_GameBoard.AppendMove(m_PlayerOne.Color);
                                break; // Exit loop
                            }

                            Console.WriteLine("Invalid move!");
                        }
                    }

                    // change player
                    m_CurrentPlayer = m_CurrentPlayer == CurrentPlayer.PlayerOne ? CurrentPlayer.PlayerTwo : CurrentPlayer.PlayerOne;
                    clearScreen();
                }

                // This point means someone won
                string i_CalculatedWinner = i_GameBoard.CalculateWinner();

                if (i_CalculatedWinner == m_PlayerOne.Color)
                {
                    showWinnerMessage("Player One");
                }
                else
                {
                    showWinnerMessage("Player Two");
                }

                clearScreen();
                i_PlayGameFlag = askForAnotherGame();
            }

            if (i_PlayGameFlag == false)
            {
                Console.WriteLine("Thank you for playing ! \nPress any key to leave....");
                Console.Read();
                System.Environment.Exit(0);
            }
        }

        private bool askForAnotherGame()
        {
            while (true)
            {
                Console.WriteLine("Would you like to play again ? (Y/N)");
                string i_UserInput = Console.ReadLine();
                if (i_UserInput == "Y")
                {
                    return true;
                }
                else if (i_UserInput == "N")
                {
                    return false;
                }
                else
                {
                    Console.WriteLine("Please type in Y/N.....");
                }
            }
        }

        private void showWinnerMessage(string i_WinnerName)
        {
            Console.WriteLine("And the winner is " + i_WinnerName + " !!");
            Console.WriteLine("Press any key to continue");
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
                if (int.TryParse(Console.ReadLine(), out i_UserInput))
                {
                    if ((i_UserInput == 6 || i_UserInput >= 8) && i_UserInput <= 25)
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
            string i_TempStringForIOTests;
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
