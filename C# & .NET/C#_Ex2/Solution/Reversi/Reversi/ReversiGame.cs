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
        BLACK,
        WHITE,
        EMPTY
    }

    class ReversiGame
    {
        UserPlayer m_PlayerOne;
        UserPlayer m_PlayerTwo;
        int m_BoardSize;

        // make public
        internal void Play(bool i_PlayGameFlag)
        {
            // initiation process
            showWelcomeMessage();
            while (i_PlayGameFlag)
            {
                // Get players with lazy declaration
                m_PlayerOne = new UserPlayer(Colors.BLACK);
                string i_UserChoice = getChoiceFromConsole();

                // TODO:  Should fix this, Currently has no PC
                if (i_UserChoice == GamerTypes.Computer.ToString())
                {
                    // THIS SHOULD BE COMPUTER PLAYER, FIX THIS !
                    m_PlayerTwo = new UserPlayer(Colors.WHITE);
                }
                else
                {
                    m_PlayerTwo = new UserPlayer(Colors.WHITE);
                }

                // Pick board size
                m_BoardSize = getBoardSize();
                //TODO: change i_
                GameBoard i_GameBoard = new GameBoard(m_BoardSize);

                Console.WriteLine("Great! All set-up! \nPress any key to start \n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                Console.ReadLine();

                // Let's clear the screen
                clearScreen();

                // Player one start
                CurrentPlayer m_CurrentPlayer = CurrentPlayer.PlayerOne;
                //TODO: Valid moves should tell us which player has no moves, or even if both does not have moves
                while (i_GameBoard.GotMoreValidMoves(CurrentPlayer.PlayerOne))
                {
                    i_GameBoard.PrintCurrentState();
                    //TODO: Clean double coding
                    if (m_CurrentPlayer == CurrentPlayer.PlayerOne)
                    {
                        Console.WriteLine("Turn of: X");
                        // if first player
                        while (true)
                        {
                            string i_Move = m_PlayerOne.GetMove();
                            if (i_GameBoard.CheckIfValid(i_Move, m_PlayerOne.Color))
                            {
                                i_GameBoard.AppendMove(i_Move,m_PlayerOne.Color);
                                break;
                            }
                            // TODO: Add more lines to the print, as he asked in his file
                            Console.WriteLine("Invalid move!");
                        }
                    }
                    else
                    {
                        // if second
                        while (true)
                        {
                            Console.WriteLine("Turn of: O");
                            string i_Move = m_PlayerTwo.GetMove();
                            if (i_GameBoard.CheckIfValid(i_Move, m_PlayerTwo.Color))
                            {
                                i_GameBoard.AppendMove(i_Move, m_PlayerTwo.Color);
                                break; // Exit loop
                            }
                            // TODO: Add more lines to the print, as he asked in his file
                            Console.WriteLine("Invalid move!");
                        }
                    }

                    // change player
                    m_CurrentPlayer = m_CurrentPlayer == CurrentPlayer.PlayerOne ? CurrentPlayer.PlayerTwo : CurrentPlayer.PlayerOne;
                    clearScreen();
                }

                // This point means someone won
                //TODO: change i_
                Colors i_CalculatedWinner = i_GameBoard.CalculateWinner();

                //TODO: Change strings to eNums
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

        //TODO: Change to 6 or 8 strictly
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
