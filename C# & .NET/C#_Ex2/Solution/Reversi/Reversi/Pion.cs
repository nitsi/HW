using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Reversi
{
    class Pion
    {
        public Pion(String i_Color)
        {
            m_PionColor = i_Color;

        }

        public string m_PionColor
        {
            get
            {
                return this.m_PionColor;
            }
            set
            {
                this.m_PionColor = value;
            }
        }
    }
}
