using System;
using System.Collections.Generic;
using System.Text;

namespace B13_Ex02_1
{
    public class Player
    {
        /* The name of this player. */
        private string m_PlayerName;

        /* The score of this player. */
        private int m_PlayerScore;

        /* Flag for whether this player is human or not */
        private bool m_IsPlayerHuman;

        /* The type of pieces (Circle or X) this player is using. */
        private eBoardPieces m_BoardPieceType;

        /* An array of all the circle pieces on the board. */
        private List<PlayerBoardPiece> m_PlayerPieces;

        /*
         * Constructor - Create the instance of this player, by given
         * his first name
         */
        public Player(string i_PlayerFirstName, eBoardPieces i_BoardPieceType)
        {
            m_PlayerName = i_PlayerFirstName;
            m_PlayerScore = 0;
            m_IsPlayerHuman = false;
            m_BoardPieceType = i_BoardPieceType;
            m_PlayerPieces = null;
        }

        /*
         * A getter for the player's name.
         */
        public string PlayerName
        {
            get
            {
                return m_PlayerName;
            }
        }

        /*
         * A getter and setter for player's score.
         */
        public int PlayerScore
        {
            get
            {
                return m_PlayerScore;
            }

            set
            {
                m_PlayerScore = value;
            }
        }

        /*
         * Gets whether this player is human or a computer AI.
         */
        public bool IsPlayerHuman
        {
            get
            {
                return m_IsPlayerHuman;
            }

            set
            {
                m_IsPlayerHuman = value;
            }
        }

        /* 
         * Gets the board piece type of this player.
         */
        public eBoardPieces BoardPieceType
        {
            get
            {
                return m_BoardPieceType;
            }
        }

        /* 
         * Gets the board pieces of this player.
         */
        public List<PlayerBoardPiece> PlayerPieces
        {
            get
            {
                return m_PlayerPieces;
            }

            set
            {
                m_PlayerPieces = value;
            }
        }
    }
}
