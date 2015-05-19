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
        GameBoard m_GameBoard = new GameBoard();
        UserInteraction UI = new UserInteraction();

        public void StartGame()
        {
            string userChoice;

            while (true)
            {
                userChoice = UI.GetChoiceFromConsole();
                m_BoardSize = UI.GetBoardSize();
                m_GameBoard.initBoard(m_BoardSize);

                isPlayer2PC = (userChoice == GamerTypes.Computer.ToString()) ? true : false;

                Play(isPlayer2PC);

                if (!AskForAnotherGame())
                {
                    Console.WriteLine("Thank you for playing ! \nPress any key to exit....");
                    Console.Read();
                    System.Environment.Exit(0);
                }
                else
                {
                    UI.ClearScreen();
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
                UI.ClearScreen();
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
                        // Player1 logic
                        Console.WriteLine("Turn of P1: X");
                        string nextMove = uPlayer1.GetMove(m_BoardSize, uPlayer1.Color, m_GameBoard);
                        m_GameBoard.AppendMove(nextMove, uPlayer1.Color);
                        playerOneTurn = false;
                    }
                }
                else
                {
                    // Player2 logic
                    Console.WriteLine("Turn of P2: O");

                    if (!hasMovesP2)
                    {
                        playerOneTurn = true;
                        Console.WriteLine("No Possible mooves for P2, Returning to P1");
                        continue;
                    }

                    string nextMove = i_IsPlayer2PC ? uPlayer2.GetMovePC(m_BoardSize, m_GameBoard) : uPlayer2.GetMove(m_BoardSize, uPlayer2.Color, m_GameBoard);
                    m_GameBoard.AppendMove(nextMove, uPlayer2.Color);
                    playerOneTurn = true;
                }
            }

            // End Game
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
                    Console.WriteLine("Please type Y or N");
                }
            }
        }
    }
}