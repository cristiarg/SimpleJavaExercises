package crs.home;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Main {

  private static class ConsoleHelper {
    public ConsoleHelper() {
      // NOTE: using these  I/O constructs to be able to execute code from both the IDE and standalone
      bufferedReader = new BufferedReader(new InputStreamReader(System.in));
      bufferedWriter = new BufferedWriter(new OutputStreamWriter(System.out));
    }

    public BufferedReader getReader() {
      return bufferedReader;
    }

    public BufferedWriter getWriter() {
      return bufferedWriter;
    }

    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
  }

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

  // sort-of generic interface
  private interface Converter<T> {
    T get(String s) throws NumberFormatException;
  }

  // int specialization
  private static class ConverterInt implements Converter<Integer> {
    public Integer get(String s) throws NumberFormatException {
      int res = Integer.parseInt(s);
      return res;
    }
  }

  // nop specialization
  private static class ConverterString implements Converter<String> {
    public String get(String s) throws NumberFormatException {
      // nop
      return s;
    }
  }

  private static void writeLine(BufferedWriter bufferedWriter, String message) throws IOException {
    bufferedWriter.write(message);
    bufferedWriter.newLine();
    bufferedWriter.flush();
  }

  private static void write(BufferedWriter bufferedWriter, String message) throws IOException {
    bufferedWriter.write(message);
    bufferedWriter.flush();
  }

  private static <T> List<T> readList(ConsoleHelper consoleHelper, Converter<T> converter, String headerMessage) throws IOException {
    List<T> retList = new ArrayList<T>();
    writeLine(consoleHelper.getWriter(), headerMessage);
    boolean halt = false;
    for (; !halt; ) {
      write(consoleHelper.getWriter(), "value: ");
      String ln = consoleHelper.getReader().readLine();
      if (ln.length() == 0) {
        halt = true;
      } else {
        try {
          T v = converter.get(ln);
          retList.add(v);
        } catch (NumberFormatException ex) {
          writeLine(consoleHelper.getWriter(), "Failed to convert input text. Ending input iteration..");
          halt = true;
        }
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
    writeLine(consoleHelper.getWriter(), sb.toString());
  }

  // Write a Java program to sort a numeric array and a string array.
  private static void exercise01ArrayAndDate(ConsoleHelper consoleHelper) throws IOException {
    // int
    var convInt = new ConverterInt();
    var listInteger = readList(consoleHelper, convInt, "Please enter a list of integer values (end by supplying empty input).");
    writeLine(consoleHelper.getWriter(), "List of integer values before sorting:");
    writeList(consoleHelper, listInteger);
    quicksort(listInteger, 0 , listInteger.size() - 1);
    writeLine(consoleHelper.getWriter(), "List of integer values after sorting:");
    writeList(consoleHelper, listInteger);

    // string
    writeLine(consoleHelper.getWriter(), "");
    Converter<String> convStr = new ConverterString();
    var listString = readList(consoleHelper, convStr, "Please enter a list of string values (end by supplying empty input).");
    writeLine(consoleHelper.getWriter(), "List of string values before sorting:");
    writeList(consoleHelper, listString);
    quicksort(listString, 0, listString.size() - 1);
    writeLine(consoleHelper.getWriter(), "List of string values after sorting:");
    writeList(consoleHelper, listString);
  }

  private static void displayMenu(ConsoleHelper consoleHelper) throws IOException{
    var bw = consoleHelper.getWriter();
    writeLine(bw, " 1. Write a Java program to sort a numeric array and a string array.");
    writeLine(bw, " 2. Write a Java program to sum values of an array.");
    writeLine(bw, " 3. Write a Java program to calculate the average value of array elements.");
    writeLine(bw, " 4. Write a Java program to test if an array contains a specific value.");
    writeLine(bw, " 5. Write a Java program to remove a specific element from an array.");
    //writeLine(bw, " 6. Write a Java program to insert an element (specific position) into an array.");
    //writeLine(bw, " 7. Write a Java program to find the maximum and minimum value of an array.");
    //writeLine(bw, " 8. Write a Java program to reverse an array of integer values.");
    //writeLine(bw, " 9. Write a java program to get the length of a given string.");
    //writeLine(bw, "10. Write a Java program to replace all the &#39;d&#39; characters with &#39;f&#39; characters.");
    //writeLine(bw, "11. Write a Java program to convert all the characters in a string to uppercase.");
    //writeLine(bw, "12. Write a Java program to trim any leading or trailing whitespace from a given string.");
    //writeLine(bw, "13. Write a Java program to get and display information (year, month, day, hour, minute) of a default calendar.");
    writeLine(bw, " 0. ..will exit.");
    write(consoleHelper.getWriter(), "Please choose one of the above: ");
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
          writeLine(consoleHelper.getWriter(), "Failed to validate option..");
        }
        if ( option  >= 0 ) {
          switch (option) {
            case 1 :
              exercise01ArrayAndDate(consoleHelper);
              halt = true;
              break;
            case 2 :
            case 3 :
            case 4 :
            case 5 :
              writeLine(consoleHelper.getWriter(), "Info: not implemented yet...");
              break;
            case 0:
              halt = true;
              break;
            default:
              writeLine(consoleHelper.getWriter(), "Invalid option chosen..");
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
