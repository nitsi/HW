using System;
using System.Collections.Generic;
using System.Text;
using System.Text.RegularExpressions;

namespace B13_Ex02_1
{
    public class ValidateGameInput
    {
        /*
         * Checks if the given player name is maximum 20 characters long,
         * and without any white spaces
         */
        public static bool CheckPlayerName(string i_PlayerName)
        {
            // Contain the result of this check
            bool playerNameCheckResult = true;

            // Repeatedly prompt user for name until a legal name is given
            if (i_PlayerName.Length > 20 || i_PlayerName.Contains(" ") || i_PlayerName.Equals(string.Empty))
            {
                playerNameCheckResult = false;
                Console.WriteLine("Error: invalid name. Please enter a name under 20 characters without spaces:");
            }

            return playerNameCheckResult;
        }

        /*
         * Checks if the given board size is legal or not (means given input
         * is 6, 8 or 10)
         */
        public static bool CheckBoardSize(bool i_IsValidNumber, int i_BoardSize)
        {
            // Contain the result of this check
            bool boardSizeCheckResult = true;

            // Validate board size input. If input is invalid, prompt user until a legal value is given.
            if (!i_IsValidNumber || !Enum.IsDefined(typeof(eBoardSize), i_BoardSize))
            {
                boardSizeCheckResult = false;
                Console.WriteLine("Error: invalid board size. Please enter a valid size (6, 8 or 10)");
            }

            return boardSizeCheckResult;
        }

        /*
         * Checks if the given move is legal or not by parsing the input and analyzing
         * the result. The player move pattern should be - COLrow>COLrow.
         */
        public static bool CheckPlayerMove(string i_PlayerMove, Board i_GameBoard)
        {
            // Contain the move pattern
            Match movePattern = Regex.Match(i_PlayerMove, @"^[A-Z][a-z]>[A-Z][a-z]$");

            // Contain a flag for the results of the move until now
            bool playerMoveCheckResult = true;

            // Check if we got a success match
            if (!movePattern.Success)
            {
                playerMoveCheckResult = false;

                // Print the board and the error
                Console.WriteLine("Error: the pattern of your move should be - COLrow>COLrow");
            }

            // Check that move is not out of board range, only if there was not error
            if (playerMoveCheckResult)
            {
                // Start to parse the given move
                int fromMoveColumn = GetFromColumn(i_PlayerMove);
                int fromMoveRow = GetFromRow(i_PlayerMove);
                int toMoveColumn = GetToColumn(i_PlayerMove);
                int toMoveRow = GetToRow(i_PlayerMove);
                int boardSize = i_GameBoard.BoardSize;

                if ((fromMoveColumn >= boardSize) || (fromMoveRow >= boardSize) ||
                    (toMoveColumn >= boardSize) || (toMoveRow >= boardSize))
                {
                    playerMoveCheckResult = false;
                    Console.WriteLine("Error: the given move is out of range");
                }
            }

            return playerMoveCheckResult;
        }

        /*
         * Checks the input from user for "Y/N" questions
         */
        public static void CheckAnswer(out bool o_UserAnswer)
        {
            o_UserAnswer = false;
            string userInput = null;
            userInput = Console.ReadLine();

            while (!o_UserAnswer)
            {
                if (userInput.Equals("Y"))
                {
                    o_UserAnswer = true;
                    break;
                }
                else if (userInput.Equals("N"))
                {
                    o_UserAnswer = false;
                    break;
                }
                else
                {
                    // Invalid choice (neither Y nor N)
                    Console.WriteLine("Error: invalid choice. Please answer (Y/N): ");
                    userInput = Console.ReadLine();
                }
            }
        }

        /*
         * Get the "from" column of the player move.
         */
        public static int GetFromColumn(string i_PlayerMove)
        {
            return i_PlayerMove[0] - 'A';
        }

        /*
        * Get the "from" row of the player move.
        */
        public static int GetFromRow(string i_PlayerMove)
        {
            return i_PlayerMove[1] - 'a';
        }

        /*
        * Get the "to" column of the player move.
        */
        public static int GetToColumn(string i_PlayerMove)
        {
            return i_PlayerMove[3] - 'A';
        }

        /*
        * Get the "to" row of the player move.
        */
        public static int GetToRow(string i_PlayerMove)
        {
            return i_PlayerMove[4] - 'a';
        }
    }
}
