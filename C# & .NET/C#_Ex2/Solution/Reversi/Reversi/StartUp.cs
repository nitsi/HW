using System;
using System.Collections.Generic;
using System.Text;
using System.Threading.Tasks;

namespace Reversi
{
    public class StartUp
    {
        public static void Main(string[] args)
        {
            GameRunner mainGame = new GameRunner();
            mainGame.StartGame();
        }
    }
}