package crs.home;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

class ConsoleHelper {
  ConsoleHelper() {
    // NOTE: using these  I/O constructs to be able to execute code from both the IDE and standalone
    bufferedReader = new BufferedReader(new InputStreamReader(System.in));
    bufferedWriter = new BufferedWriter(new OutputStreamWriter(System.out));
  }

  BufferedReader getReader() {
    return bufferedReader;
  }

  BufferedWriter getWriter() {
    return bufferedWriter;
  }

  private BufferedReader bufferedReader;
  private BufferedWriter bufferedWriter;
}
