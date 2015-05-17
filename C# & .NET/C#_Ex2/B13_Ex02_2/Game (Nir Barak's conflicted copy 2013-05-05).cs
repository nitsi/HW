using System;
using System.Collections.Generic;
using System.Text;
using B13_Ex02_1;

namespace B13_Ex02_2
{
    public class Game
    {
        /* Contain the first player name */
        private string m_FirstPlayerName;

        /* The name of the first player. */
        private Player m_FirstPlayer;

        /* The name of the second player. */
        private Player m_SecondPlayer;

        /* Contain the board it self */
        private Board m_GameBoard;

        /* Contain the user input play */
        private string m_PlayerMoveInput = null;

        /* Contain the turns counter */
        private bool m_firstTurnInGame = true;

        /* Contain the current player that is playing. 1 - first player, 2 - second player */
        private int m_CurrentPlayerNumber = 1;

        /* Contain the last turn that was made */
        private string m_LastPlayerTurn = null;

        /* Contain the maximum score that first player can get */
        private int m_MaximumFirstPlayerScore;

        /* Contain the maximum score that second player can get */
        private int m_MaximumSecondPlayerScore;

        /* Contain the number of player that won */
        private int m_WonPlayerNumber;

        /* Contain the tool for check players moves */
        private ValidateMove m_playerMoveChecker;

        /* 

        /*
         * Prompts user for a valid name. Name must be under 20 character, and devoid of spaces.
         */
        private void PromptUserName(out string o_UserName)
        {
            // Read player name.
            Console.WriteLine("Please enter your name (and then press enter):");
            o_UserName = Console.ReadLine();

            // Repeatedly prompt user for name until a legal name is given
            while (!ValidateGameInput.CheckPlayerName(o_UserName))
            {
                o_UserName = Console.ReadLine();
            }
        }

        /*
        * Prompts user for a board size. Legal sizes are defined as 6, 8, or 10.
        */
        private void PromptBoardSize(out int o_BoardSize)
        {
            // Choose a board size.
            Console.WriteLine("Please choose a board size (6, 8, or 10)");
            bool isValidNumber = int.TryParse(Console.ReadLine(), out o_BoardSize);

            // Validate board size input. If input is invalid, prompt user until a legal value is given.
            while (!ValidateGameInput.CheckBoardSize(isValidNumber, o_BoardSize))
            {
                isValidNumber = int.TryParse(Console.ReadLine(), out o_BoardSize);
            }
        }

        /*
         * Configures whether second player is a human player or computer AI. If player is human,
         * prompts user for a name, otherwise defaults name to 'Computer_AI'.
         */
        private void ConfigureSecondPlayer()
        {
            string secondPlayerName = null;
            bool isSecondPlayerHuman = true;

            Console.WriteLine("Do you want to play with a second human player? (Y/N):");
            ValidateGameInput.CheckAnswer(out isSecondPlayerHuman);

            if (isSecondPlayerHuman)
            {
                // Human player choice
                PromptUserName(out secondPlayerName);
            }
            else
            {
                // Computer AI choice
                secondPlayerName = "Computer_AI";
            }

            m_SecondPlayer = new Player(secondPlayerName, eBoardPieces.X);
            m_SecondPlayer.IsPlayerHuman = isSecondPlayerHuman;
        }

        /* 
         * Update players scores
         */
        private void UpdatePlayerScore()
        {
            // Calculate players scores
            m_FirstPlayer.PlayerScore = m_MaximumFirstPlayerScore - m_GameBoard.NumOfPiecesX;
            m_SecondPlayer.PlayerScore = m_MaximumSecondPlayerScore - m_GameBoard.NumOfPiecesCircle;
            Console.WriteLine("yair {0} nir {1} ", m_FirstPlayer.PlayerScore, m_SecondPlayer.PlayerScore);
        }

        /*
         * Check if first player can make any more moves or not
         */
        private bool FirstPlayerCanMakeMoves()
        {
            // Flag if second player can make any moves or not
            bool firstPlayerCanMove = false;

            foreach (PlayerBoardPiece playerPiece in m_FirstPlayer.PlayerPieces)
            {
                // Check each piece if it can make any move
                if (m_playerMoveChecker.CanMoveOneSpot(playerPiece.m_Row, playerPiece.m_Column) ||
                    m_playerMoveChecker.CanEatSomePiece(playerPiece.m_Row, playerPiece.m_Column))
                {
                    // Means that there is one piece that can make a move
                    firstPlayerCanMove = true;
                    break;
                }
            }

            return firstPlayerCanMove;
        }

        /* 
         * Check if second player can make any more moves or not
         */
        private bool SecondPlayerCanMakeMoves()
        {
            // Flag if second player can make any moves or not
            bool secondPlayerCanMove = false;

            if (m_SecondPlayer.IsPlayerHuman)
            {
                foreach (PlayerBoardPiece playerPiece in m_SecondPlayer.PlayerPieces)
                {
                    // Check each piece if it can make any move
                    if (m_playerMoveChecker.CanMoveOneSpot(playerPiece.m_Row, playerPiece.m_Column) ||
                        m_playerMoveChecker.CanEatSomePiece(playerPiece.m_Row, playerPiece.m_Column))
                    {
                        // Means that there is one piece that can make a move
                        secondPlayerCanMove = true;
                        break;
                    }
                }
            }
            else
            {
                secondPlayerCanMove = true;
                m_playerMoveChecker.CalculateAIMoves(m_SecondPlayer);

                // Case second player is not human, we can check his array of moves
                if (m_playerMoveChecker.AIMoves.Count == 0)
                {
                    secondPlayerCanMove = false;
                }
            }

            return secondPlayerCanMove;
        }

        /*
         * Check if the game is over - means if we got tie(both sides can't make any moves),
         * or one player can't make any more moves.
         */
        private bool CheckGameIsOver(out bool o_gameOver)
        {
            // Initialize winner player number
            o_gameOver = false;
            m_WonPlayerNumber = 0;

            // Check scores for each player
            if (m_FirstPlayer.PlayerScore == m_MaximumFirstPlayerScore)
            {
                o_gameOver = true;
                m_WonPlayerNumber = 1;
            }
            else if (m_SecondPlayer.PlayerScore == m_MaximumSecondPlayerScore)
            {
                o_gameOver = true;
                m_WonPlayerNumber = 2;
            }

            if (!o_gameOver)
            {
                // Check if some player can't make any more moves
                bool firstPlayerCanMove = FirstPlayerCanMakeMoves();
                bool secondPlayerCanMove = SecondPlayerCanMakeMoves();

                if (!firstPlayerCanMove && !secondPlayerCanMove)
                {
                    // If both players can't move, game is tied
                    o_gameOver = true;
                    m_WonPlayerNumber = 3;
                }
                else if (!firstPlayerCanMove)
                {
                    // If only the first player can't move, player 2 wins
                    o_gameOver = true;
                    m_WonPlayerNumber = 2;
                }
                else if (!secondPlayerCanMove)
                {
                    // If only the second player can't move, player 1 wins
                    o_gameOver = true;
                    m_WonPlayerNumber = 1;
                }
            }

            return o_gameOver;
        }

        /*
         * Get the next move from player's turn
         */
        private void GetNextPlayerMove(bool i_LastTurnWasLegal, bool i_KeepLastPlayerTurn)
        {
            // Print last player turn's
            if (!m_firstTurnInGame && i_LastTurnWasLegal)
            {
                if (m_CurrentPlayerNumber == 2 && !i_KeepLastPlayerTurn)
                {
                    Console.Write("{0}'s move was ({1}): {2}", m_FirstPlayer.PlayerName, m_FirstPlayer.BoardPieceType, m_LastPlayerTurn);
                }
                else
                {
                    Console.Write("{0}'s move was ({1}): {2}", m_SecondPlayer.PlayerName, m_SecondPlayer.BoardPieceType, m_LastPlayerTurn);
                }

                Console.WriteLine();
            }
            else
            {
                m_firstTurnInGame = false;
            }

            // Check if second player is human or not
            if (m_SecondPlayer.IsPlayerHuman)
            {
                // Read next move from the current player
                if (m_CurrentPlayerNumber == 1)
                {
                    Console.Write("{0}'s Turn ({1}) : ", m_FirstPlayer.PlayerName, m_FirstPlayer.BoardPieceType);
                    m_PlayerMoveInput = Console.ReadLine();
                }
                else
                {
                    Console.Write("{0}'s Turn ({1}) : ", m_SecondPlayer.PlayerName, m_SecondPlayer.BoardPieceType);
                    m_PlayerMoveInput = Console.ReadLine();
                }
            }
            else
            {
                // Check if we need to take input from user or random input from computer
                if (m_CurrentPlayerNumber == 1)
                {
                    Console.Write("{0}'s Turn ({1}) : ", m_FirstPlayer.PlayerName, m_FirstPlayer.BoardPieceType);
                    m_PlayerMoveInput = Console.ReadLine();
                }
                else
                {
                    int oldFromColumn = ValidateGameInput.GetFromColumn(m_LastPlayerTurn);
                    int oldFromRow = ValidateGameInput.GetToColumn(m_LastPlayerTurn);
                    int newToColumn;
                    int newToRow;
                    
                    // Check if it's a new turn or one that we need to continue
                    if (i_KeepLastPlayerTurn)
                    {
                        foreach (AIMove currentAIMove in m_playerMoveChecker.AIMoves)
                        {
                            newToColumn = currentAIMove.m_ToColumn;
                            newToRow = currentAIMove.m_ToRow;
                            if (!(oldFromColumn == newToColumn && oldFromRow == newToRow))
                            {
                                // Case we found some legal move
                                m_PlayerMoveInput = string.Format(
                                                                  "{0}{1}>{2}{3}",
                                                                  (char)('A' + oldFromColumn),
                                                                  (char)('a' + oldFromRow),
                                                                  (char)('A' + newToColumn),
                                                                  (char)('a' + newToRow));
                                break;
                            }
                        }
                    }
                    else
                    {
                        // We need to choose in random a move for the computer
                        Random randomNumberMove = new Random();
                        int randomMove = randomNumberMove.Next(m_playerMoveChecker.AIMoves.Count - 1);
                        AIMove nextMove = m_playerMoveChecker.AIMoves[randomMove];

                        // Get computer move and parse to string
                        oldFromColumn = nextMove.m_FromColumn;
                        oldFromRow = nextMove.m_FromRow;
                        newToColumn = nextMove.m_ToColumn;
                        newToRow = nextMove.m_ToRow;
                        m_PlayerMoveInput = string.Format(
                                                          "{0}{1}>{2}{3}", 
                                                          (char) ('A' + oldFromColumn),
                                                          (char) ('a' + oldFromRow),
                                                          (char) ('A' + newToColumn),
                                                          (char) ('a' + newToRow));
                    }
                }
            }

            // Save the last turn
            m_LastPlayerTurn = m_PlayerMoveInput;
        }

        /*
         * Prints a line made up of '=' separating each row in the board.
         */
        private void PrintBoardRowSeparator()
        {
            Console.Write(" ");

            // Print the separator line.
            for (int i = 0; i < m_GameBoard.BoardSize * 5; i++)
            {
                Console.Write("=");
            }

            Console.WriteLine();
        }

        /*
         * Prints an ASCII representation of the checkers board onto the terminal.
         */
        public void PrintBoard()
        {
            eBoardPieces[,] gameBoard = m_GameBoard.GameBoard;

            // ASCII representation of an alphabetic letter, starting with 'A'.
            int letterValueInASCII = 65;

            Console.Write(" ");

            // Print the upper row of the board.
            for (int i = 0; i < m_GameBoard.BoardSize; i++)
            {
                // Print the column name.
                Console.Write("  {0}  ", (char)letterValueInASCII);
                letterValueInASCII++;
            }

            Console.WriteLine();
            PrintBoardRowSeparator();

            // Change the ASCII value to that of the letter 'a'.
            letterValueInASCII = 97;

            // Print the actual board
            for (int i = 0; i < m_GameBoard.BoardSize; i++)
            {
                // Print the row name.
                Console.Write((char)letterValueInASCII);
                letterValueInASCII++;

                // Print the pieces.
                for (int j = 0; j < m_GameBoard.BoardSize; j++)
                {
                    switch (gameBoard[i, j])
                    {
                        case eBoardPieces.Circle:
                            Console.Write("| O |");
                            break;
                        case eBoardPieces.CircleKing:
                            Console.Write("| Q |");
                            break;
                        case eBoardPieces.X:
                            Console.Write("| X |");
                            break;
                        case eBoardPieces.XKing:
                            Console.Write("| K |");
                            break;
                        case eBoardPieces.Empty:
                            Console.Write("|   |");
                            break;
                    }
                }

                Console.WriteLine();
                PrintBoardRowSeparator();
            }
        }

        /*
         * In case we want to start a new game, restart all parameters and start again
         */
        private void StartNewGame()
        {
            // reset players scores
            m_FirstPlayer.PlayerScore = 0;
            m_SecondPlayer.PlayerScore = 0;

            // case second player is not human, delete his moves
            if (!m_SecondPlayer.IsPlayerHuman)
            {
                m_playerMoveChecker.AIMoves.Clear();
            }

            // Initalize board
            m_GameBoard.InitializeBoard();

            // Initalize board in move checker!!!
            m_playerMoveChecker = new ValidateMove(m_GameBoard);

            // Set to the first turn in game
            m_firstTurnInGame = true;
        }

        /*
         * Case player need to eat again, check that his new "from" coordinates
         * are the same as his "to" coordinates on his last turn
         */
        private bool CheckPlayerAteAgain(int i_CurrentPlayerNumber, string i_LastPlayerTurn)
        {
            // Flag if new player step is ok
            bool newPlayerMoveIsOk = true;
            int oldFromColumn = ValidateGameInput.GetFromColumn(i_LastPlayerTurn);
            int oldToRow = ValidateGameInput.GetToColumn(i_LastPlayerTurn);
            int newToColumn = ValidateGameInput.GetToRow(m_PlayerMoveInput);
            int newToRow = ValidateGameInput.GetToRow(m_PlayerMoveInput);

            if (!(oldFromColumn == newToColumn && oldToRow == newToRow))
            {
                // only if we dealing with humans players
                if (i_CurrentPlayerNumber == 1 || m_SecondPlayer.IsPlayerHuman)
                {
                    newPlayerMoveIsOk = false;
                }
            }

            return newPlayerMoveIsOk;
        }

        /* 
         * Starts the acual game. Received inputs from player, outputs illegal moves,
         * And increments player scores. Ends game when one player has eaten all of the other
         * players' pieces, or if the other player cannot move. Ties game if no player can move.
         * When game ends, prompts player if they would like to play again.
         */
        public void StartGame()
        {
            // Get the first player name and create player
            PromptUserName(out m_FirstPlayerName);
            m_FirstPlayer = new Player(m_FirstPlayerName, eBoardPieces.Circle);

            // Get board size.
            int boardSize;
            PromptBoardSize(out boardSize);

            // Create the second player (either human or AI).
            ConfigureSecondPlayer();

            // Create the board initialize it, and create a move checker for it
            m_GameBoard = new Board(boardSize);
            m_playerMoveChecker = new ValidateMove(m_GameBoard);

            // Give each player a list of the location of all their pieces
            m_FirstPlayer.PlayerPieces = m_GameBoard.CirclePieces;
            m_SecondPlayer.PlayerPieces = m_GameBoard.XPieces;

            // Calculate maximum score for each player
            m_MaximumFirstPlayerScore = (boardSize / 2) * (boardSize - 2) / 2;
            m_MaximumSecondPlayerScore = (boardSize / 2) * (boardSize - 2) / 2;

            // Start game
            bool gameOver = false;
            bool lastTurnWasLegal = true;
            bool keepLastPlayerTurn = false;
            eValidMoveMessages errorFromCheckingMove;

            // For first turn
            Ex02.ConsoleUtils.Screen.Clear();
            PrintBoard();
            GetNextPlayerMove(lastTurnWasLegal, keepLastPlayerTurn);

            while (!gameOver)
            {
                // Check if the given input is valid or not 
                while (!ValidateGameInput.CheckPlayerMove(m_PlayerMoveInput, m_GameBoard))
                {
                    lastTurnWasLegal = false;

                    // Print board and ask for 
                    GetNextPlayerMove(lastTurnWasLegal, keepLastPlayerTurn);
                }

                lastTurnWasLegal = true;

                while (lastTurnWasLegal)
                {
                    // Check the move and set it on the board
                    if (m_CurrentPlayerNumber == 1)
                    {
                        errorFromCheckingMove = m_playerMoveChecker.CheckAndSetMove(
                                                                              m_FirstPlayer,
                                                                              ValidateGameInput.GetFromRow(m_PlayerMoveInput),
                                                                              ValidateGameInput.GetFromColumn(m_PlayerMoveInput),
                                                                              ValidateGameInput.GetToRow(m_PlayerMoveInput),
                                                                              ValidateGameInput.GetToColumn(m_PlayerMoveInput));
                    }
                    else
                    {
                        errorFromCheckingMove = m_playerMoveChecker.CheckAndSetMove(
                                                                              m_SecondPlayer,
                                                                              ValidateGameInput.GetFromRow(m_PlayerMoveInput),
                                                                              ValidateGameInput.GetFromColumn(m_PlayerMoveInput),
                                                                              ValidateGameInput.GetToRow(m_PlayerMoveInput),
                                                                              ValidateGameInput.GetToColumn(m_PlayerMoveInput));
                    }

                    // Check the error
                    switch (errorFromCheckingMove)
                    {
                        case eValidMoveMessages.MoveIsNotInBoard:
                            lastTurnWasLegal = false;
                            Console.WriteLine("The move was out of board range! Please try again.");
                            break;
                        case eValidMoveMessages.PieceIsNotPlayers:
                            lastTurnWasLegal = false;
                            Console.WriteLine("That piece is not yours! Please try again.");
                            break;
                        case eValidMoveMessages.PieceMustMove:
                            lastTurnWasLegal = false;
                            Console.WriteLine("You must move your piece to different spot! Please try again.");
                            break;
                        case eValidMoveMessages.PlayerMustEatEnemy:
                            lastTurnWasLegal = false;
                            Console.WriteLine("There exists a move where you can eat an enemy piece. Please try again.");
                            break;
                        case eValidMoveMessages.WrongDirection:
                            lastTurnWasLegal = false;
                            Console.WriteLine("You are not a king, you cannot go in that direction! Please try again.");
                            break;
                        case eValidMoveMessages.MoveIsIllegal:
                            lastTurnWasLegal = false;
                            Console.WriteLine("Your move is not legal! Please try again.");
                            break;
                        case eValidMoveMessages.ValidMoveKeepPlayerTurn:
                            keepLastPlayerTurn = true;
                            lastTurnWasLegal = true;
                            break;
                        default:
                            lastTurnWasLegal = true;
                            break;
                    }

                    // Print board and ask again
                    if (!lastTurnWasLegal)
                    {
                        GetNextPlayerMove(lastTurnWasLegal, keepLastPlayerTurn);
                        lastTurnWasLegal = true;
                    }
                    else
                    {
                        break;
                    }
                }

                lastTurnWasLegal = true;
                m_GameBoard = m_playerMoveChecker.ChangedBoard;
                Ex02.ConsoleUtils.Screen.Clear();
                PrintBoard();

                // Change pieces lists of player
                m_GameBoard.CirclePieces.CopyTo(m_FirstPlayer.PlayerPieces, 0);
                m_GameBoard.XPieces.CopyTo(m_SecondPlayer.PlayerPieces, 0);

                // Switch between players only if we need to(case we stay with the same player)
                if (keepLastPlayerTurn)
                {
                    m_CurrentPlayerNumber = m_CurrentPlayerNumber == 1 ? 1 : 2;
                }
                else
                {
                    m_CurrentPlayerNumber = m_CurrentPlayerNumber == 1 ? 2 : 1;
                }

                // Calculate and update players' score
                UpdatePlayerScore();

                // Check if game is over
                if (CheckGameIsOver(out gameOver))
                {
                    // Contain the user choise 
                    bool userChoice;

                    // Check if it's not a tie
                    if (m_WonPlayerNumber == 3)
                    {
                        Console.Write("Game finished in a tie (no player can move)! Would you like to play again (Y/N)? ");
                    }
                    else if (m_WonPlayerNumber == 1)
                    {
                        Console.Write("{0} won! Would you like to play again (Y/N)? ", m_FirstPlayer.PlayerName);
                    }
                    else
                    {
                        Console.Write("{0} won! Would you like to play again (Y/N)? ", m_SecondPlayer.PlayerName);
                    }

                    ValidateGameInput.CheckAnswer(out userChoice);
                    if (userChoice)
                    {
                        StartNewGame();
                    }
                    else
                    {
                        // out of game
                        break;
                    }
                }
                else
                {
                    if (keepLastPlayerTurn)
                    {
                        GetNextPlayerMove(lastTurnWasLegal, keepLastPlayerTurn);
                        while (CheckPlayerAteAgain(m_CurrentPlayerNumber, m_LastPlayerTurn))
                        {
                            Console.WriteLine("You need to eat a another enemy piece!");
                            GetNextPlayerMove(lastTurnWasLegal, keepLastPlayerTurn);
                        }
                    }
                    else
                    {
                        GetNextPlayerMove(lastTurnWasLegal, keepLastPlayerTurn);
                    }

                    keepLastPlayerTurn = false;
                }
            }
        }
    }
}
