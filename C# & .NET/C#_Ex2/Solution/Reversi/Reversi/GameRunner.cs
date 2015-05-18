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

    class GameRunner
    {
        int m_BoardSize;
        bool isPlayer2PC = true;
        GameBoard m_GameBoard;


        public void StartGame()
        {
            string userChoice;

            while (true)
            {
                // TODO: Print Welcome Message
                userChoice = getChoiceFromConsole();
                m_BoardSize = getBoardSize();
                m_GameBoard.initBoard(m_BoardSize);

                isPlayer2PC = (userChoice == GamerTypes.Computer.ToString()) ? true : false;

                Play(isPlayer2PC);

                if (!AskForAnotherGame())
                {
                    Console.WriteLine("Thank you for playing ! \nPress any key to leave....");
                    Console.Read();
                    System.Environment.Exit(0);
                }
                else
                {
                    clearScreen();
                }
            }
        }

        private void Play(bool i_IsPlayer2PC)
        {
            Player uPlayer1 = new Player(Colors.BLACK);
            Player uPlayer2 = new Player(Colors.WHITE);

            bool playerOneTurn = true;
            bool hasMovesP1;
            bool hasMovesP2;

            while (true)
            {
                clearScreen();
                m_GameBoard.PrintCurrentState();
                hasMovesP1 = m_GameBoard.GotMoreValidMoves(uPlayer1.Color);
                hasMovesP2 = m_GameBoard.GotMoreValidMoves(uPlayer2.Color);

                if (playerOneTurn)
                {
                    if (!hasMovesP1)
                    {
                        System.Console.WriteLine("P1 turn, but P1 has no moves possible");
                        if (!hasMovesP2)
                        {
                            System.Console.WriteLine("P2 turn, but P2 ALSO has no moves possible");
                            Colors winnerColor = m_GameBoard.CalculateWinner();

                            DeclareWinner(winnerColor);
                            break;
                        }
                    }
                    else
                    {
                        //Player1 logic
                        Console.WriteLine("Turn of P1: X");
                        string nextMove = GetMove(m_BoardSize, uPlayer1.Color);
                        m_GameBoard.AppendMove(nextMove, uPlayer1.Color);
                        playerOneTurn = false;
                    }
                }
                else
                {
                    //Player2 logic
                    Console.WriteLine("Turn of P2: O");

                    string nextMove = (i_IsPlayer2PC) ? uPlayer2.GetMovePC(m_BoardSize, m_GameBoard) : GetMove(m_BoardSize, uPlayer2.Color);
                    m_GameBoard.AppendMove(nextMove, uPlayer2.Color);
                    playerOneTurn = true;
                }

            }
            //End Game
        }

        private void DeclareWinner(Colors i_WinnerColor)
        {
            m_GameBoard.PrintCurrentState();
            System.Console.WriteLine("Winner is: " + i_WinnerColor.ToString());
        }

        private bool AskForAnotherGame()
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

        private void clearScreen()
        {
            Ex02.ConsoleUtils.Screen.Clear();
        }

        private string GetMove(int i_BoardSize, Colors i_PlayerColor)
        {
            string m_Alphabet = "ABCDEFGH";

            while (true)
            {
                Console.WriteLine("Please enter your move : ");
                string i_UserInput = Console.ReadLine();
                if (i_UserInput.Length == 2 && m_Alphabet.IndexOf(i_UserInput[0]) > -1 && (i_UserInput[1] >= 0
                    && (int)Char.GetNumericValue(i_UserInput[1]) <= i_BoardSize)
                    && (m_GameBoard.CheckIfValid(i_UserInput, i_PlayerColor)))
                {
                    //TODO: make if more readeable and throw print lines with proper notes regarding user input
                    return i_UserInput;
                }

                Console.WriteLine("Invalid move!");
            }
        }

        private string GetMovePC(int i_BoardSize, Colors i_PlayerColor)
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
                newMove.Append((char)(randomLetterCell + 65)).Append((char)randomNumCell);

                if (m_GameBoard.CheckIfValid(newMove.ToString(), Colors.WHITE))
                {
                    return newMove.ToString();
                }
                else
                {
                    newMove.Clear();
                }
            }
        }

        private int getBoardSize()
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

        private string getChoiceFromConsole()
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