﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Collections;

namespace Reversi
{
    enum Pions
    {
        //TODO: get rif of comments
        BLACK, // marks black
        WHITE, // marks white
        EMPTY
    }

    class GameBoard
    {
        private int m_BoardSize;
        private Pions[,] m_Board;
        private ArrayList m_PlayerOneValidMoves;
        private ArrayList m_PlayerTwoValidMoves;


        //TODO: Lower letters to 8 letters
        private string m_Alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        //TODO: change to io
        public GameBoard(int i_BoardSize)
        {
            m_BoardSize = i_BoardSize;
            m_Board = new Pions[m_BoardSize, m_BoardSize];
            initBoard();
            initMovesTrackers();
        }

        private void initMovesTrackers()
        {
            throw new NotImplementedException();
        }

        private void initBoard()
        {
            // init the array with empty
            for (int i = 0; i < m_BoardSize; i++)
            {
                for (int j = 0; j < m_BoardSize; j++)
                {
                    m_Board[i, j] = Pions.EMPTY;
                }
            }

            //TODO: vhange i
            int i_BoardMiddle = m_BoardSize / 2;

            // Append BLACK
            m_Board[i_BoardMiddle, i_BoardMiddle] = Pions.BLACK;
            m_Board[i_BoardMiddle - 1, i_BoardMiddle - 1] = Pions.BLACK;

            // Append WHITE
            m_Board[i_BoardMiddle - 1, i_BoardMiddle] = Pions.WHITE;
            m_Board[i_BoardMiddle, i_BoardMiddle - 1] = Pions.WHITE;
        }

        internal bool GotMoreValidMoves(CurrentPlayer currentPlayer)
        {
            throw new NotImplementedException();
        }

        //TODO: replace methos writeline with internal C# writeline
        internal void PrintCurrentState()
        {
            generateTopLetters();
            generateNewLine();
            generateLineSeparators();
            generateNewLine();
            for (int i = 0; i < m_BoardSize; i++)
            {
                generateLineNumber(i);
                for (int j = 0; j < m_BoardSize; j++)
                {
                    generateColumnSeparator();
                    generateCellContent(i, j);

                }

                generateNewLine();
                generateLineSeparators();
                generateNewLine();
            }
        }

        //TODO: change to meaningful names
        private void generateCellContent(int i, int j)
        {
            //TODO: change i_
            Pions i_TempPionContent = m_Board[i, j];
            if (i_TempPionContent == Pions.EMPTY)
            {
                Console.Write(" ");
            }
            else if (i_TempPionContent == Pions.BLACK)
            {
                Console.Write("X");
            }
            else
            {
                Console.Write("O");
            }
        }

        private void generateColumnSeparator()
        {
            Console.Write(" | ");
        }

        private void generateLineNumber(int i)
        {
            Console.Write((i + 1) + " )");
        }

        private void generateLineSeparators()
        {
            //TODO: replace with regular print add comment above
            generateBorderSpan();
            for (int i = 0; i < m_BoardSize; i++)
            {
                Console.Write("====");
            }
        }

        private void generateBorderSpan()
        {
            Console.Write("    ");
        }

        private void generateTopLetters()
        {
            // Generate top letters
            //TODO: replace with regular print add comment above
            generateBorderSpan();
            for (int i = 0; i < m_BoardSize; i++)
            {
                Console.Write("  " + m_Alphabet[i] + " ");
            }
        }

        private void generateNewLine()
        {
            Console.WriteLine();
        }

        //Assuming we've recieved in the form of "A1" <Char,Number>

        internal bool CheckIfValid(string i_Move, string i_PlayerMove)
        {
            int[] i_PlayerMoveCoords = new int[2]; // used for coorsdinate

            // Get data from player
            //TODO : change this to object parsing
            i_PlayerMoveCoords[0] = m_Alphabet.IndexOf(i_PlayerMove[0]);
            i_PlayerMoveCoords[1] = i_PlayerMove[1];

            if (m_Board[i_PlayerMoveCoords[0], i_PlayerMoveCoords[1]] != Pions.EMPTY)
            {
                return false;
            }
            else
            {
                return !crawler(i_PlayerMoveCoords[0], i_PlayerMoveCoords[1]) ? false : true;
            }
        }

        internal void AppendMove(object p)
        {
            throw new NotImplementedException();
            // insert given value to array
            // re-eval the m_Board
        }

        internal string CalculateWinner()
        {

            int i_WhitePoints = 0;
            int i_BlackPoints = 0;

            for (int i = 0; i < m_BoardSize; i++)
            {
                for (int j = 0; j < m_BoardSize; j++)
                {
                    if (m_Board[i, j] == Pions.WHITE)
                    {
                        i_WhitePoints++;
                    }
                    else if (m_Board[i, j] == Pions.BLACK)
                    {
                        i_BlackPoints++;
                    }
                }
            }
            return i_BlackPoints > i_WhitePoints ? Pions.BLACK.ToString() : Pions.WHITE.ToString();
        }



    }
}
