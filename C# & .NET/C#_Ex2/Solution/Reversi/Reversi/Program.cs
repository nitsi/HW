﻿using System;
using System.Collections.Generic;
using System.Text;
using System.Threading.Tasks;

namespace Reversi
{
    // change neame to something more meaning ful
    class Program
    {
        static void Main(string[] args)
        {
            //change i_MainGame  to correct name
            ReversiGame i_MainGame = new ReversiGame();
            i_MainGame.Play(true);
        }
    }
}