namespace B15_Ex01_4
{
    using System;
    using System.Linq;
    using System.Threading;

    public class Program
    {
        private static string m_NumberAsString;
        private static int m_Number;
        private static bool m_IsNumber;
        
        public static void Main()
        {
            readAndValidate();
            isPalindrome();
            printSumOrCapitalLettersAmount();
        }

        private static void readAndValidate()
        {
            System.Console.WriteLine("Please enter an 10 size long sigit or string:");
            
            while (true)
            {
                m_NumberAsString = System.Console.ReadLine();
                bool isNumber = int.TryParse(m_NumberAsString, out m_Number);
                bool isOnlyLetters = m_NumberAsString.All(char.IsLetter);
                bool validSize = m_NumberAsString.Length == 10;
               
                if (isNumber && validSize)
                {
                    m_IsNumber = true;
                    break;
                }
                else if (isOnlyLetters && validSize)
                {
                    m_IsNumber = false;
                    break;
                }
                else
                {
                    System.Console.WriteLine("You have entered a bad input, please try again:");
                }
            }   
        }

        private static void isPalindrome()
        {
            int startIndex = 0;
            int endIndex = m_NumberAsString.Length - 1;
            bool validPalindrome = true;

            while (endIndex - startIndex > 0)
            {
                startIndex++;
                endIndex--;

                if (m_NumberAsString[startIndex] == m_NumberAsString[endIndex])
                {
                    continue;
                }
                
                validPalindrome = false;
            }

            System.Console.WriteLine("Is " + m_NumberAsString + " a palindrome: " + validPalindrome);
        }

        private static void printSumOrCapitalLettersAmount()
        {
            if (m_IsNumber)
            {
                int number = m_Number;
                int sum = 0;
                while (number != 0)
                {
                    sum += number % 10;
                    number /= 10;
                }

                System.Console.WriteLine("The sum of the digits is: " + sum);
            }
            else
            {
                int counter = 0;
                for (int i = 0; i < m_NumberAsString.Length; i++)
                {
                    counter += char.IsUpper(m_NumberAsString[i]) ? 1 : 0;
                }

                System.Console.WriteLine("Amount of upper case letters is: " + counter);
            }
        }
    }
}