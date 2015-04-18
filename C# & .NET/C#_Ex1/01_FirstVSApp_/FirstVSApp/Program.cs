/// <summary>
/// Console.WriteLine
/// Console.ReadLine()
/// Creating Enumerands (enum)
/// Enum.Parse
/// Enum.ToString
/// float.Parse
/// float.ToString
/// string concatenation
/// </summary>
namespace FirstVSApp
{
    public enum eWeekDays
    {
        Sunday,
        Monday,
        Tuesday,
        Wednesday,
        Thursday,
        Friday
    }

    class Program
    {
        public static void Main()
        {
            System.Console.WriteLine("Hi. Please enter your name (and then press enter):");
            // simple ReadLine operation:
            string name = System.Console.ReadLine();

            System.Console.WriteLine("Please enter your prefered working day (and then press enter):");
            // creating an enum from a string:
            string weekDayStr = System.Console.ReadLine();
            eWeekDays workingDay = (eWeekDays)System.Enum.Parse(typeof(eWeekDays), weekDayStr);

            System.Console.WriteLine("Please enter your prefered salary (and then press enter):");
            // creating an int from a string:
            string salaryStr = System.Console.ReadLine();

            float salary = 0;
            bool goodInput = float.TryParse(salaryStr, out salary); // returns false upon failure
            if (goodInput)
            {
                salary *= 1.1f;
            }

            // string concatenation:
            string msg = "Hi " + name + "!\nYou are working on " + workingDay.ToString() + ".\nYou are going to get $" + salary.ToString() + "hour :)\n";

            // the program's output:
            System.Console.WriteLine(msg);

            // wait for enter
            System.Console.WriteLine("Please press 'Enter' to exit...");
            System.Console.ReadLine();
        }
    }
}
