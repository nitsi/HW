namespace B15_Ex01_2
{
    using System;

    public class Program
    {
        public static void Main()
        {
            string clock = string.Format("*****{0} *** {0}  *  {0} *** {0}*****", Environment.NewLine);
            System.Console.WriteLine(clock);
        } 
    }
}