using System;
using System.Collections.Generic;
using System.Text;
using System.Threading.Tasks;

namespace Reversi
{
    // change neame to something more meaning ful
    class StartUp
    {
        public static void Main(string[] args)
        {
            // change i_MainGame  to correct name
            GameRunner mainGame = new GameRunner();
            mainGame.StartGame();
        }
    }
}