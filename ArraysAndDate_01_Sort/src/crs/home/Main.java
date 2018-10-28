package crs.home;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Main {

  // wikipedia-copied quicksort
  private static <T extends Comparable<T>> void quicksort(List<T> lst, int lo, int hi) {
    if ( lo < hi ) {
      var p = partition(lst, lo, hi);
      quicksort(lst, lo, p - 1);
      quicksort(lst, p + 1, hi);
    }
  }

  private static <T> void swap(List<T> lst, int fir, int sec) {
    assert fir < lst.size();
    assert sec < lst.size();
    assert fir != sec;

    var t = lst.get(fir);
    lst.set(fir, lst.get(sec));
    lst.set(sec, t);
  }

  // wikipedia-copied partition
  private static <T extends Comparable<T>> int partition(List<T> lst, int lo , int hi) {
    var pivot = lst.get(hi);
    var i = lo;
    for ( var j = lo ; j < hi; j++) {
      if ( lst.get(j).compareTo(pivot) < 0) {
        if ( i != j ) {
          swap(lst, i , j);
        }
        i++;
      }
    }
    swap(lst, i, hi);
    return i;
  }

  private static <T> Tuple<T, Boolean> readValue(ConsoleHelper consoleHelper, Converter<T> converter) throws  IOException {
    String s = consoleHelper.readLine();
    if (s.length() > 0) {
      try {
        T v = converter.fromString(s);
        return new Tuple(v, true);
      } catch(NumberFormatException ex) {
        consoleHelper.writeLine("Error: Failed to convert input text...");
        // will return invalid
      }
    }
    return new Tuple(null, false);
  }

  private static <T> List<T> readList(ConsoleHelper consoleHelper, Converter<T> converter, String headerMessage) throws IOException {
    List<T> retList = new ArrayList<T>();
    consoleHelper.writeLine(headerMessage);
    boolean halt = false;
    for (; !halt; ) {
      consoleHelper.write("value: ");
      var t = readValue(consoleHelper, converter);
      if (t.get2()) {
        retList.add(t.get1());
      } else {
        //consoleHelper.writeLine("Failed to convert input text. Ending input iteration..");
        halt = true;
      }
    }
    return retList;
  }

  private static <T> void writeList(ConsoleHelper consoleHelper, List<T> lst) throws IOException {
    StringBuilder sb = new StringBuilder();
    if (lst.size() > 0 ) {
      for (var item : lst) {
        sb.append(item);
        sb.append("  ");
      }
    } else {
      sb.append("<empty list>");
    }
    consoleHelper.writeLine(sb.toString());
  }

  // Write a Java program to sort a numeric array and a string array.
  private static void exercise01SortNumericAndStringArray(ConsoleHelper consoleHelper) throws IOException {
    // int
    var convInt = new ConverterInt();
    var listInteger = readList(consoleHelper, convInt, "Please enter a list of integer values (end by supplying empty input).");
    consoleHelper.writeLine("List of integer values before sorting:");
    writeList(consoleHelper, listInteger);
    quicksort(listInteger, 0 , listInteger.size() - 1);
    consoleHelper.writeLine("List of integer values after sorting:");
    writeList(consoleHelper, listInteger);

    // string
    consoleHelper.writeLine("");
    Converter<String> convStr = new ConverterString();
    var listString = readList(consoleHelper, convStr, "Please enter a list of string values (end by supplying empty input).");
    consoleHelper.writeLine("List of string values before sorting:");
    writeList(consoleHelper, listString);
    quicksort(listString, 0, listString.size() - 1);
    consoleHelper.writeLine("List of string values after sorting:");
    writeList(consoleHelper, listString);
  }

  private static Tuple<Double, Integer> readAndSummListOfDouble(ConsoleHelper consoleHelper) throws IOException {
    var converter = new ConverterDouble();
    var list = readList(consoleHelper, converter, "Please enter a list of numeric values (end by supplying empty input).");
    double s = 0;
    for (var v : list) {
      s += v;
    }
    return new Tuple(s, list.size());
  }

  // Write a Java program to sum values of an array.
  private static void exercise02Sum(ConsoleHelper consoleHelper) throws IOException {
    var t = readAndSummListOfDouble(consoleHelper);
    consoleHelper.writeLine("Summation of the elements is %g.", t.get1());
  }

  // Write a Java program to calculate the average value of array elements.
  private static void exercise03Average(ConsoleHelper consoleHelper) throws  IOException {
    var t = readAndSummListOfDouble(consoleHelper);
    var average
        = (t.get2() == 0)
            ? 0.0
            : (t.get1() / t.get2());
    consoleHelper.writeLine("Average value of the elements is %g.", average);
  }

  private static void exercise04SearchForSpecificValue(ConsoleHelper consoleHelper) throws IOException {
    var converter = new ConverterDouble();
    var list = readList(consoleHelper, converter, "Please enter a list of numeric values (end by supplying empty input).");

    quicksort(list, 0 , list.size() - 1);

    consoleHelper.write("value to search: ");
    var valueTuple = readValue(consoleHelper, converter);
    if (valueTuple.get2()) {
      var value = valueTuple.get1();
      // binary search
      var lo = 0;
      var hi = list.size() - 1;
      while (lo <= hi)  {
        var mid = (lo + hi) / 2;
        if (value < list.get(mid)) {
          hi = mid - 1;
        } else if (value > list.get(mid)) {
          lo = mid + 1;
        } else {
          break;
        }
      }
      consoleHelper.writeLine("Value '%g' was %sfound.", value, (lo <= hi) ? "" : "not ");
    }
  }

  private static void displayMenu(ConsoleHelper consoleHelper) throws IOException{
    consoleHelper.writeLine(" 1. Write a Java program to sort a numeric array and a string array.");
    consoleHelper.writeLine(" 2. Write a Java program to sum values of an array.");
    consoleHelper.writeLine(" 3. Write a Java program to calculate the average value of array elements.");
    consoleHelper.writeLine(" 4. Write a Java program to test if an array contains a specific value.");
    //consoleHelper.writeLine(" 5. Write a Java program to remove a specific element from an array.");
    //consoleHelper.writeLine(" 6. Write a Java program to insert an element (specific position) into an array.");
    //consoleHelper.writeLine(" 7. Write a Java program to find the maximum and minimum value of an array.");
    //consoleHelper.writeLine(" 8. Write a Java program to reverse an array of integer values.");
    //consoleHelper.writeLine(" 9. Write a java program to get the length of a given string.");
    //consoleHelper.writeLine("10. Write a Java program to replace all the &#39;d&#39; characters with &#39;f&#39; characters.");
    //consoleHelper.writeLine("11. Write a Java program to convert all the characters in a string to uppercase.");
    //consoleHelper.writeLine("12. Write a Java program to trim any leading or trailing whitespace from a given string.");
    //consoleHelper.writeLine("13. Write a Java program to get and display information (year, month, day, hour, minute) of a default calendar.");
    consoleHelper.writeLine(" 0. ..will exit.");
    consoleHelper.write("Please choose one of the above: ");
  }

  private static void executeMenu(ConsoleHelper consoleHelper) throws IOException {
    // TODO: after a number of invalid options, just halt
    var halt = false;
    for ( ; !halt; ) {
      displayMenu(consoleHelper);
      String ln = consoleHelper.getReader().readLine();
      if (ln.length() == 0) {
        halt = true;
      } else {
        int option = -1; // invalid option that cause another iteration
        try {
          option = Integer.parseInt(ln);
        } catch (NumberFormatException ex) {
          consoleHelper.writeLine("Failed to validate option..");
        }
        if ( option  >= 0 ) {
          switch (option) {
            case 1 :
              exercise01SortNumericAndStringArray(consoleHelper);
              halt = true;
              break;
            case 2 :
              exercise02Sum(consoleHelper);
              halt = true;
              break;
            case 3 :
              exercise03Average(consoleHelper);
              halt = true;
              break;
            case 4 :
              exercise04SearchForSpecificValue(consoleHelper);
              halt = true;
              break;
            //case 5 :
            //  consoleHelper.writeLine("Info: not implemented yet...");
            //  break;
            case 0:
              halt = true;
              break;
            default:
              consoleHelper.writeLine("Invalid option chosen..");
              // will loop once more
          }
        } else {
          // will loop once more
        }
      }
    }
  }

  public static void main(String[] args) throws IOException {
    ConsoleHelper consoleHelper = new ConsoleHelper();

    executeMenu(consoleHelper);
  }
}
