package crs.home;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Main {

//    private void quicksort_int()

  // sort-of generic interface
  private static interface Converter<T> {
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

  private static <T> List<T> readListFromConsole(Converter<T> converter) throws IOException {
    List<T> retList = new ArrayList<T>();
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
    boolean halt = false;
    for (; !halt; ) {
      bw.write("next: ");
      bw.flush();
      String ln = br.readLine();
      if (ln.length() == 0) {
        //bw.newLine();
        //bw.write("empty line, halting ..");
        halt = true;
      } else {
        try {
          T v = converter.get(ln);
          retList.add(v);
        } catch (NumberFormatException ex) {
          halt = true;
        }
      }
    }
    return retList;
  }

  public static void main(String[] args) throws IOException {
    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

    // int
    bw.newLine();
    bw.flush();
    Converter<Integer> convInt = new ConverterInt();
    var listInt = readListFromConsole(convInt);
    StringBuilder sb = new StringBuilder();
    for (var item : listInt) {
      sb.append(item);
      sb.append("  ");
    }
    bw.write(sb.toString());
    bw.flush();

    // string
    bw.newLine();
    bw.flush();
    Converter<String> convStr = new ConverterString();
    var listStr = readListFromConsole(convStr);
    sb.setLength(0);
    for (var item : listStr) {
      sb.append(item);
      sb.append("  ");
    }
    bw.write(sb.toString());
    bw.flush();
  }
}
