package crs.home;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.IOException;

class ConsoleHelper {
  ConsoleHelper() {
    // NOTE: using these  I/O constructs to be able to execute code from both the IDE and standalone
    bufferedReader = new BufferedReader(new InputStreamReader(System.in));
    bufferedWriter = new BufferedWriter(new OutputStreamWriter(System.out));
  }

  BufferedReader getReader() {
    return bufferedReader;
  }

  //BufferedWriter getWriter() {
  //  return bufferedWriter;
  //}

  String readLine() throws IOException {
    String s = bufferedReader.readLine();
    return s;
  }

  void write(boolean newLineAfter, String message) throws IOException {
    bufferedWriter.write(message);
    if (newLineAfter) {
      bufferedWriter.newLine();
    }
    bufferedWriter.flush();
  }

  void write(String message) throws IOException {
    write(false, message);
  }

  void write(String format, Object ... arguments) throws IOException {
    var s = String.format(format, arguments);
    write(false, s);
  }

  void writeLine(String message) throws IOException {
    write(true, message);
  }

  void writeLine(String format, Object ... arguments) throws IOException {
    var s = String.format(format, arguments);
    write(true, s);
  }

  private BufferedReader bufferedReader;
  private BufferedWriter bufferedWriter;
}
