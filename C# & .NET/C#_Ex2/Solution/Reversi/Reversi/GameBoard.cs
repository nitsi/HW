﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Collections;

namespace Reversi
{

    class GameBoard
    {
        private int m_BoardSize;
        private Colors[,] m_Board;
        private ArrayList m_PlayerOneValidMoves;
        private ArrayList m_PlayerTwoValidMoves;


        //TODO: Lower letters to 8 letters
        private string m_Alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        //TODO: change to io
        public GameBoard(int i_BoardSize)
        {
            m_BoardSize = i_BoardSize;
            m_Board = new Colors[m_BoardSize, m_BoardSize];
            initBoard();
        }

        private void initBoard()
        {
            // init the array with empty
            for (int i = 0; i < m_BoardSize; i++)
            {
                for (int j = 0; j < m_BoardSize; j++)
                {
                    m_Board[i, j] = Colors.EMPTY;
                }
            }

            //TODO: vhange i
            int i_BoardMiddle = m_BoardSize / 2;

            // Append BLACK
            m_Board[i_BoardMiddle, i_BoardMiddle] = Colors.BLACK;
            m_Board[i_BoardMiddle - 1, i_BoardMiddle - 1] = Colors.BLACK;

            // Append WHITE
            m_Board[i_BoardMiddle - 1, i_BoardMiddle] = Colors.WHITE;
            m_Board[i_BoardMiddle, i_BoardMiddle - 1] = Colors.WHITE;
        }

        internal bool GotMoreValidMoves(CurrentPlayer currentPlayer)
        {
            return true;// throw new NotImplementedException();
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
            Colors i_TempPionContent = m_Board[i, j];
            if (i_TempPionContent == Colors.EMPTY)
            {
                Console.Write(" ");
            }
            else if (i_TempPionContent == Colors.BLACK)
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

        internal bool CheckIfValid(string i_PlayerMove, Colors i_PlayerColor)
        {
            int[] i_PlayerMoveCoords = new int[2]; // used for coorsdinate

            // Get data from player
            //TODO : change this to object parsing

            i_PlayerMoveCoords[0] = m_Alphabet.IndexOf(i_PlayerMove[0]);
            i_PlayerMoveCoords[1] = (int)Char.GetNumericValue(i_PlayerMove[1]) - 1;

            int i_XCords = i_PlayerMoveCoords[1];
            int i_YCords = i_PlayerMoveCoords[0];
            if (verifyEdges(i_PlayerMoveCoords[1], i_PlayerMoveCoords[0]) && m_Board[i_XCords, i_YCords] == Colors.EMPTY)
            {
                Console.WriteLine("Enter crawler");
                return crawler(i_XCords, i_YCords, i_PlayerColor);
            }
            else
            {
                return false;
            }
        }

        private bool verifyEdges(int i_X, int i_Y)
        {
            return ((i_X >= 0 && i_X <= m_BoardSize - 1) && (i_Y >= 0 && i_Y <= m_BoardSize - 1));
        }

        private bool crawler(int i_X, int i_Y, Colors i_PlayerColor)
        {
            int io_TempCounterForCrawlers = 0;
            if (verifyEdges(i_X, i_Y))
            {

                return crawlVertical(i_X, i_Y, i_PlayerColor, io_TempCounterForCrawlers);
            }
            else
            {
                return false;
            }
        }

        private bool crawlVertical(int i_X, int i_Y, Colors i_PlayerColor, int io_TempCounterForCrawlers)
        {
            // so we won't go out of boundries
            if (!verifyEdges(i_X, i_Y)) { return false; }

            Colors i_CurrentColorFromCell = m_Board[i_X, i_Y];
            // Means that's the first iteration 
            if ((i_CurrentColorFromCell == Colors.EMPTY && io_TempCounterForCrawlers == 0) || (i_CurrentColorFromCell != i_PlayerColor && i_CurrentColorFromCell != Colors.EMPTY))
            {
                //go recursive
                return crawlVertical(i_X - 1, i_Y, i_PlayerColor, io_TempCounterForCrawlers + 1) || crawlVertical(i_X - 1, i_Y, i_PlayerColor, io_TempCounterForCrawlers + 1);

            }
            // Means we hit cell from same color, but it's adjacent
            if (i_CurrentColorFromCell == i_PlayerColor && io_TempCounterForCrawlers == 0) { return false; }
            // Means we hit a cell from another color, but we passed other cells on the way
            if (i_CurrentColorFromCell == i_PlayerColor && io_TempCounterForCrawlers > 0) { return true; }
            return false;
        }


        internal void AppendMove(string i_Move, Colors i_GivenColor)
        {
            m_Board[(int)Char.GetNumericValue(i_Move[1]) - 1, m_Alphabet.IndexOf(i_Move[0])] = i_GivenColor;
        }
        internal Colors CalculateWinner()
        {

            int i_WhitePoints = 0;
            int i_BlackPoints = 0;

            for (int i = 0; i < m_BoardSize; i++)
            {
                for (int j = 0; j < m_BoardSize; j++)
                {
                    if (m_Board[i, j] == Colors.WHITE)
                    {
                        i_WhitePoints++;
                    }
                    else if (m_Board[i, j] == Colors.BLACK)
                    {
                        i_BlackPoints++;
                    }
                }
            }
            return i_BlackPoints > i_WhitePoints ? Colors.BLACK : Colors.WHITE;
        }





    }
}
