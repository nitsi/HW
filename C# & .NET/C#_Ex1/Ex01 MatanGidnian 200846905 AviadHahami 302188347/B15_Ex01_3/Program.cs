namespace B15_Ex01_3
{
    using System;

    public class Program
    {
        private static int m_Number;

        public static void Main()
        {
            getInputAndVerify();
            double number = m_Number;
            for (int i = m_Number; i > Math.Ceiling(number / 2); i--)
            {
                printLineOfClockUp(i);
            }

            for (double i = Math.Ceiling(number / 2); i <= m_Number; i++)
            {
                printLineOfClockUp((int)i);
            }
        }

        private static void getInputAndVerify()
        {
            bool validNum = false;
            string insertedString;
            
            while (!validNum)
            {
                System.Console.WriteLine("Please enter a valid positive number for TimeClock: ");
                insertedString = System.Console.ReadLine();
                validNum = int.TryParse(insertedString, out m_Number);
            }

            if ((int)m_Number % 2 == 0)
            {
                m_Number++;
            }
        }

        private static void printLineOfClockUp(int i_LineNum)
        {
            int spacesInSides = m_Number - i_LineNum;
            int itemsToPrint = m_Number - (spacesInSides * 2);

            for (int i = 0; i < spacesInSides; i++)
            {
                System.Console.Write(" ");
            }

            for (int i = 0; i < itemsToPrint; i++)
            {
                System.Console.Write("*");
            }

            for (int i = 0; i < spacesInSides; i++)
            {
                System.Console.Write(" ");
            }

            System.Console.WriteLine(string.Empty);
        }

        private static void printLineOfClockDown(int i_LineNum)
        {
            int spacesInSides = m_Number - i_LineNum;
            int itemsToPrint = m_Number - (spacesInSides * 2);

            for (int i = spacesInSides; i > 0; i--)
            {
                System.Console.Write(" ");
            }

            for (int i = itemsToPrint; i > 0; i++)
            {
                System.Console.Write("*");
            }

            for (int i = spacesInSides; i < 0; i--)
            {
                System.Console.WriteLine(" ");
            }
        }
    }
}