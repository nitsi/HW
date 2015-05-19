using System;
using System.Collections.Generic;
using System.Text;

namespace Reversi
{
    private enum GamerTypes
    {
        User,
        Computer
    }

    private enum Colors
    {
        BLACK,
        WHITE,
        EMPTY
    }

    public class GameRunner
    {
        private GameBoard m_GameBoard = new GameBoard();
        private UserInteraction m_UI = new UserInteraction();
        private int m_BoardSize;
        private bool m_IsPlayer2PC = true;

        public void StartGame()
        {
            string userChoice;

            while (true)
            {
                userChoice = m_UI.GetChoiceFromConsole();
                m_BoardSize = m_UI.GetBoardSize();
                m_GameBoard.initBoard(m_BoardSize);

                m_IsPlayer2PC = (userChoice == GamerTypes.Computer.ToString()) ? true : false;

                Play(m_IsPlayer2PC);

                if (!AskForAnotherGame())
                {
                    Console.WriteLine("Thank you for playing ! \nPress any key to exit....");
                    Console.Read();
                    System.Environment.Exit(0);
                }
                else
                {
                    m_UI.ClearScreen();
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
                m_UI.ClearScreen();
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