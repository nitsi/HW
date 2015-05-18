using System;
using System.Collections.Generic;
using System.Text;

namespace B13_Ex02_1
{
    public class Game
    {
        /* The first player. */
        private Player m_FirstPlayer;

        /* The second player. */
        private Player m_SecondPlayer;

        /* The actual board in which the game is being played. */
        private Board m_GameBoard;

        /* The move input given by the player from the console. */
        private string m_PlayerMoveInput = null;

        /* A flag signifying if this is the first turn in the game. */
        private bool m_firstTurnInGame = true;

        /* The current player that is playing. 1 - first player, 2 - second player. */
        private int m_CurrentPlayerNumber = 1;

        /* The last turn made on the board. */
        private string m_LastPlayerTurn = null;

        /* A tool for validating a players' move. */
        private ValidateMove m_PlayerMoveChecker;

        /* A flag for whether the user wants to exit the game. */
        private bool m_UserWantsToExit = false;

        /*
         * Prompts user for a valid name. Name must be under 20 character, and devoid of spaces.
         */
        private void PromptUserName(out string o_UserName)
        {
            // Read player name
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
         * Get the move of the next player.
         */
        private void GetNextPlayerMove(bool i_LastTurnWasLegal, bool i_KeepLastPlayerTurn)
        {
            // Contain flag if we want to exit
            m_UserWantsToExit = false;

            // Print last player's move if it was legal and not the first move
            if (!m_firstTurnInGame && i_LastTurnWasLegal && !i_KeepLastPlayerTurn)
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

            // Read next move from the current player
            if (m_CurrentPlayerNumber == 1)
            {
                // Read first player input
                Console.Write("{0}'s Turn ({1}) : ", m_FirstPlayer.PlayerName, m_FirstPlayer.BoardPieceType);
                m_PlayerMoveInput = Console.ReadLine();

                // Check if we want to exit
                if (m_PlayerMoveInput.Equals("Q"))
                {
                    m_UserWantsToExit = true;
                }
            }
            else if (m_SecondPlayer.IsPlayerHuman)
            {
                // Read second player input only if player is human
                Console.Write("{0}'s Turn ({1}) : ", m_SecondPlayer.PlayerName, m_SecondPlayer.BoardPieceType);
                m_PlayerMoveInput = Console.ReadLine();

                // Check if the player wants to exit
                if (m_PlayerMoveInput.Equals("Q"))
                {
                    m_UserWantsToExit = true;
                }
            }
            else
            {
                // Player is computer, calculate all possible moves
                m_PlayerMoveChecker.CalculatePossibleMoves(m_SecondPlayer);

                // Get the previous "to" move of the player
                int oldFromColumn = ValidateGameInput.GetToColumn(m_LastPlayerTurn);
                int oldFromRow = ValidateGameInput.GetToRow(m_LastPlayerTurn);
                int newFromColumn;
                int newFromRow;

                if (i_KeepLastPlayerTurn)
                {
                    // Computer has a second turn, make sure it picks the proper piece to move
                    foreach (PossibleMove currentAIMove in m_PlayerMoveChecker.AIMoves)
                    {
                        newFromColumn = currentAIMove.m_FromColumn;
                        newFromRow = currentAIMove.m_FromRow;
                        if (oldFromColumn == newFromColumn && oldFromRow == newFromRow)
                        {
                            // Parse move into a readable string format
                            m_PlayerMoveInput = string.Format(
                                "{0}{1}>{2}{3}",
                                (char)('A' + oldFromColumn),
                                (char)('a' + oldFromRow),
                                (char)('A' + currentAIMove.m_ToColumn),
                                (char)('a' + currentAIMove.m_ToRow));
                            break;
                        }
                    }
                }
                else
                {
                    // Choose a random move for the computer AI to perform from a list of possible moves
                    Random randomNumberMove = new Random();
                    int randomMove = randomNumberMove.Next(m_PlayerMoveChecker.AIMoves.Count);
                    PossibleMove nextMove = m_PlayerMoveChecker.AIMoves[randomMove];

                    // Parse move into a readable string format
                    m_PlayerMoveInput = string.Format(
                        "{0}{1}>{2}{3}",
                        (char)('A' + nextMove.m_FromColumn),
                        (char)('a' + nextMove.m_FromRow),
                        (char)('A' + nextMove.m_ToColumn),
                        (char)('a' + nextMove.m_ToRow));
                }
            }

            // Save the last turn
            if (!i_KeepLastPlayerTurn)
            {
                m_LastPlayerTurn = m_PlayerMoveInput;
            }
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
            int letterValueInASCII = 'A';

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
            letterValueInASCII = 'a';

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
         * Gets the relevant user info on the initial run of the game.
         */
        private void StartFirstTimeNewGame()
        {
            string firstPlayerName;

            // Get the first player name and create player
            PromptUserName(out firstPlayerName);
            m_FirstPlayer = new Player(firstPlayerName, eBoardPieces.Circle);

            // Get board size
            int boardSize;
            PromptBoardSize(out boardSize);

            // Create the second player (either human or AI)
            ConfigureSecondPlayer();

            // Create the board, initialize it, and create a move checker for it
            m_GameBoard = new Board(boardSize);

            // Set to the first turn in game
            m_firstTurnInGame = true;
        }

        /*
         * If the game has ended, checks whether the game was tied or if
         * one of the players won, and updates the player scores accordingly.
         */
        public void UpdatePlayersScore(eCurrentGameStatus i_GameStatus)
        {
            // Check if the game has finished (only if players still wants to play)
            if (!i_GameStatus.Equals(eCurrentGameStatus.GameDidNotEnd))
            {
                if (i_GameStatus.Equals(eCurrentGameStatus.GameTied))
                {
                    // The game has ended in a tie
                    Console.Write("Game finished in a tie (no player can move)!");
                    m_FirstPlayer.PlayerScore++;
                    m_SecondPlayer.PlayerScore++;
                }
                else if (i_GameStatus.Equals(eCurrentGameStatus.CircleWon))
                {
                    // The first player has won
                    Console.Write("{0} won! ", m_FirstPlayer.PlayerName);
                    m_FirstPlayer.PlayerScore++;
                }
                else
                {
                    // The second player has won
                    Console.Write("{0} won! ", m_SecondPlayer.PlayerName);
                    m_SecondPlayer.PlayerScore++;
                }

                // Print the current player scores
                Console.WriteLine(
                    "Current player score: First Player - {0}, Second Player - {1}.\nWould you like to play again? (Y/N)",
                    m_FirstPlayer.PlayerScore,
                    m_SecondPlayer.PlayerScore);
            }
        }

        /*
         * Checks the current player move. If move is illegal, prints to the console
         * the type of error the move made.
         */
        private void CheckAndMakeMove(out bool i_MoveWasLegal, out bool i_KeepPlayer)
        {
            eValidMoveMessages playerMoveMessage;

            // Check the move and set it on the board
            if (m_CurrentPlayerNumber == 1)
            {
                playerMoveMessage = m_PlayerMoveChecker.CheckAndSetMove(
                    m_FirstPlayer,
                    ValidateGameInput.GetFromRow(m_PlayerMoveInput),
                    ValidateGameInput.GetFromColumn(m_PlayerMoveInput),
                    ValidateGameInput.GetToRow(m_PlayerMoveInput),
                    ValidateGameInput.GetToColumn(m_PlayerMoveInput));
            }
            else
            {
                playerMoveMessage = m_PlayerMoveChecker.CheckAndSetMove(
                    m_SecondPlayer,
                    ValidateGameInput.GetFromRow(m_PlayerMoveInput),
                    ValidateGameInput.GetFromColumn(m_PlayerMoveInput),
                    ValidateGameInput.GetToRow(m_PlayerMoveInput),
                    ValidateGameInput.GetToColumn(m_PlayerMoveInput));
            }

            i_MoveWasLegal = false;
            i_KeepPlayer = false;

            // Print a message to player according to move message
            switch (playerMoveMessage)
            {
                case eValidMoveMessages.MoveIsNotInBoard:
                    Console.WriteLine("The move was out of board range! Please try again.");
                    break;
                case eValidMoveMessages.PieceIsNotPlayers:
                    Console.WriteLine("That piece is not yours! Please try again.");
                    break;
                case eValidMoveMessages.PieceMustMove:
                    Console.WriteLine("You must move your piece to different spot! Please try again.");
                    break;
                case eValidMoveMessages.PlayerMustEatEnemy:
                    Console.WriteLine("There exists a move where you can eat an enemy piece. Please do that.");
                    break;
                case eValidMoveMessages.WrongDirection:
                    Console.WriteLine("You are not a king, you cannot go in that direction! Please try again.");
                    break;
                case eValidMoveMessages.MoveIsIllegal:
                    Console.WriteLine("Your move is not legal! Please try again.");
                    break;
                case eValidMoveMessages.ValidMoveKeepPlayerTurn:
                    i_KeepPlayer = true;
                    i_MoveWasLegal = true;
                    m_GameBoard = m_PlayerMoveChecker.GameBoard;
                    break;
                default:
                    i_MoveWasLegal = true;
                    m_GameBoard = m_PlayerMoveChecker.GameBoard;
                    break;
            }
        }

        /*
         * In the case where one player wants to quit the game, the player requesting to quit loses
         * and the other players' score increases.
         */
        private void PlayerWantsToExit()
        {
            if (m_CurrentPlayerNumber == 1)
            {
                // The second player has won
                UpdatePlayersScore(eCurrentGameStatus.XWon);
            }
            else
            {
                // The first player has won
                UpdatePlayersScore(eCurrentGameStatus.CircleWon);
            }
        }

        /* 
         * Starts the actual game. Receives inputs from player, checks move, and reports illegal moves.
         * Ends game when one player has eaten all of the other players' pieces, or if the other player 
         * cannot move. Ties game if no player can move. When game ends, prompts player if they would 
         * like to play again. Starts a new game from scratch with the same players.
         */
        public void StartGame(bool i_IsGameNew)
        {
            if (i_IsGameNew)
            {
                // Game is new, get information from user
                StartFirstTimeNewGame();
            }
            else
            {
                // Players restarted game, initialize board
                m_GameBoard.InitializeBoard();
                m_firstTurnInGame = true;
            }

            GameStatus currentBoardStatus = new GameStatus(m_GameBoard);
            m_PlayerMoveChecker = new ValidateMove(m_GameBoard);

            // Give each player a list of the location of all their pieces
            m_FirstPlayer.PlayerPieces = m_GameBoard.CirclePieces;
            m_SecondPlayer.PlayerPieces = m_GameBoard.XPieces;

            // Start game - initialize required variables
            m_UserWantsToExit = false;
            bool gameOver = false;
            bool lastTurnWasLegal = true;
            bool keepLastPlayerTurn = false;
            eCurrentGameStatus gameStatus = eCurrentGameStatus.GameDidNotEnd;
            bool playerYesOrNoAnswer;

            // Print the initial board and get the first move
            Ex02.ConsoleUtils.Screen.Clear();
            PrintBoard();

            // Starts playing. Game only ends when one player wins, or the game is tied
            while (!gameOver)
            {
                // Get the player move
                GetNextPlayerMove(lastTurnWasLegal, keepLastPlayerTurn);

                // Check if user wants to end game
                if (m_UserWantsToExit)
                {
                    gameOver = true;
                    break;
                }

                // Check if the given input is valid or not. If not, keep asking until it is valid
                while (!ValidateGameInput.CheckPlayerMove(m_PlayerMoveInput, m_GameBoard) && !m_UserWantsToExit)
                {
                    lastTurnWasLegal = false;
                    GetNextPlayerMove(lastTurnWasLegal, keepLastPlayerTurn);

                    // Case user wants to exit
                    if (m_UserWantsToExit)
                    {
                        gameOver = true;
                        break;
                    }
                }

                // Set the last move as legal
                lastTurnWasLegal = true;

                while (lastTurnWasLegal && !m_UserWantsToExit)
                {
                    // Validate player move. If move was illegal, print to the player the reason why
                    CheckAndMakeMove(out lastTurnWasLegal, out keepLastPlayerTurn);

                    // Ask for player turn again until a legal one is given
                    if (!lastTurnWasLegal)
                    {
                        // Move was illegal, try again
                        GetNextPlayerMove(lastTurnWasLegal, keepLastPlayerTurn);

                        // Check if the user want to exit
                        if (m_UserWantsToExit)
                        {
                            gameOver = true;
                            break;
                        }

                        lastTurnWasLegal = true;
                    }
                    else
                    {
                        // Move was valid, exit loop
                        break;
                    }
                }

                // Set the last move as legal
                lastTurnWasLegal = true;
                if (!gameOver)
                {
                    // Print the board with the new move on it
                    Ex02.ConsoleUtils.Screen.Clear();
                    PrintBoard();
                }

                // Update piece lists of both players
                m_FirstPlayer.PlayerPieces = new List<PlayerBoardPiece>(m_GameBoard.CirclePieces);
                m_SecondPlayer.PlayerPieces = new List<PlayerBoardPiece>(m_GameBoard.XPieces);

                // Switch the current player only if we need to (we might need to stay with the same player)
                if (!keepLastPlayerTurn && !m_UserWantsToExit)
                {
                    m_CurrentPlayerNumber = m_CurrentPlayerNumber == 1 ? 2 : 1;
                }

                // Check if game has ended
                if (!m_UserWantsToExit)
                {
                    gameStatus = currentBoardStatus.GetGameStatus(m_GameBoard, m_PlayerMoveChecker);
                    if (!gameStatus.Equals(eCurrentGameStatus.GameDidNotEnd))
                    {
                        // Game has ended, update score
                        gameOver = true;
                        UpdatePlayersScore(gameStatus);
                    }
                }

                // If the player turn did not change, make sure player eats another piece
                if (keepLastPlayerTurn && !m_UserWantsToExit)
                {
                    GetNextPlayerMove(lastTurnWasLegal, keepLastPlayerTurn);

                    // Check if the given input is valid or not. If not, keep asking until it is valid
                    while (!GameStatus.CheckPlayerAteAgain(m_CurrentPlayerNumber, m_LastPlayerTurn, m_PlayerMoveInput) &&
                           ValidateGameInput.CheckPlayerMove(m_PlayerMoveInput, m_GameBoard) && 
                           !m_UserWantsToExit)
                    {
                        Console.WriteLine("Your move was invalid or you need to another enemy piece!");
                        lastTurnWasLegal = false;
                        GetNextPlayerMove(lastTurnWasLegal, keepLastPlayerTurn);

                        // Case user wants to exit
                        if (m_UserWantsToExit)
                        {
                            gameOver = true;
                            break;
                        }
                    }

                    CheckAndMakeMove(out lastTurnWasLegal, out keepLastPlayerTurn);

                    if (lastTurnWasLegal)
                    {
                        // Print the board with the new move on it
                        Ex02.ConsoleUtils.Screen.Clear();
                        PrintBoard();
                        m_CurrentPlayerNumber = m_CurrentPlayerNumber == 1 ? 2 : 1;
                    }
                }

                keepLastPlayerTurn = false;
            }

            // After game ends, check if the user wants to play again
            if (gameOver)
            {
                if (m_UserWantsToExit)
                {
                    // Update user score if a player wanted to quit
                    PlayerWantsToExit();
                }

                ValidateGameInput.CheckAnswer(out playerYesOrNoAnswer);

                if (playerYesOrNoAnswer)
                {
                    // If player said yes, start a new game
                    m_firstTurnInGame = true;
                    gameOver = false;
                    StartGame(false);
                }
            }
        }
    }
}
