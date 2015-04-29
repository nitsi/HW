namespace B15_Ex01_1
{
    using System.Text;

    public class Program
    {
        private static int[] m_InsertedNumbersArr = new int[5];
        private static string[] m_InsertedStringsArr = new string[5];
        private static string[] m_NumbersInBinaryArr = new string[5];
        private static double[] m_BooleanDigitCounter = new double[2];

        public static void Main()
        {
            readAndValidateInput();
            printBinaryRepersentation();
            printSeriesInfo();
            printNumbersAverage();
            printBooleanAverage();
        }

        private static void readAndValidateInput()
        {
            string tempStringInput = string.Empty;
            bool isRealNumber;
            int tempIntInput;
            
            System.Console.WriteLine("Please enter 5 three digit numbers:");
            for (int i = 0; i < 5; i++)
            {
                tempStringInput = System.Console.ReadLine();
                isRealNumber = int.TryParse(tempStringInput, out tempIntInput);
                if (!isRealNumber || tempStringInput.Length != 3 || tempIntInput < 0)
                {
                    System.Console.WriteLine("You have entered a bad input, please try again");
                    i--;
                    continue;
                }

                m_InsertedStringsArr[i] = tempStringInput;
                m_InsertedNumbersArr[i] = tempIntInput;
                m_NumbersInBinaryArr[i] = convertToBinary(tempIntInput);
            }
        }

        private static string convertToBinary(int i_numberForConvert)
        {
            StringBuilder binaryRepersentation = new StringBuilder();
            char tempChar = '0';
            
            if (i_numberForConvert == 0)
            {
                binaryRepersentation.Append(tempChar);
            }

            while (i_numberForConvert != 0)
            {
                tempChar = (i_numberForConvert % 2 == 0) ? '0' : '1';
                binaryRepersentation.Append(tempChar);
                i_numberForConvert /= 2;
                m_BooleanDigitCounter[tempChar - '0']++;
            }

            return reverseString(binaryRepersentation.ToString());
        }

        private static string reverseString(string i_StringToReverse)
        {
            char[] tempChars = new char[i_StringToReverse.Length];
            for (int i = 0, j = i_StringToReverse.Length - 1; i <= j; i++, j--)
            {
                tempChars[i] = i_StringToReverse[j];
                tempChars[j] = i_StringToReverse[i];
            }

            return new string(tempChars);
        }

        private static void printBinaryRepersentation()
        {
            for (int i = 0; i < 5; i++)
            {
                System.Console.WriteLine("The number " + m_InsertedNumbersArr[i] + " representation in binary is: " + m_NumbersInBinaryArr[i]);
            }
        }

        private static void printSeriesInfo()
        {
            int counterDec = 0;
            int counterInc = 0;
            string tempString;

            for (int i = 0; i < 5; i++)
            {
                tempString = m_InsertedStringsArr[i];
                counterInc += (tempString[0] <= tempString[1] && tempString[1] <= tempString[2]) ? 1 : 0;
                counterDec += (tempString[2] <= tempString[1] && tempString[1] <= tempString[0]) ? 1 : 0;
            }

            System.Console.WriteLine("The number of Increasing Serieses is: " + counterInc);
            System.Console.WriteLine("The number of Decreasing Serieses is: " + counterDec);
        }

        private static void printNumbersAverage()
        {
            double average = 0;

            for (int i = 0; i < 5; i++)
            {
                average += m_InsertedNumbersArr[i];
            }
            
            average /= 5;
            System.Console.WriteLine("The average of the 5 numbers is: " + average);
        }

        private static void printBooleanAverage()
        {
            double average = (m_BooleanDigitCounter[0] + m_BooleanDigitCounter[1]) / 5;

            System.Console.WriteLine("Average amount of digits in the binary numbers: " + average);
        }
    }
}