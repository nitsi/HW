using System;
using System.Collections.Generic;
using System.Text;
using System.Threading.Tasks;

namespace Reversi
{
    class Program
    {
        static void Main(string[] args)
        {
            ReversiGame i_MainGame = new ReversiGame();
            i_MainGame.Play(true);
        }
    }
}
