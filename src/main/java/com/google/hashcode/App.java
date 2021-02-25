package com.google.hashcode;

import com.google.hashcode.solver.DummySolver;
import com.google.hashcode.solver.FinalSolver;
import com.google.hashcode.solver.NewSolver;
import com.google.hashcode.solver.Solver;
import com.google.hashcode.solver.SolverB;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class App {

  private static final String root = "/home/gcalbi/workspace/googlehashcode2020/src/main"
      + "/resources/";

  private static String getStatementName(String letter) {
    switch (letter) {
      case "a":
        return "a_example";
      case "b":
        return "b_read_on";
      case "c":
        return "c_incunabula";
      case "e":
        return "e_so_many_books";
      case "d":
        return "d_tough_choices";
      case "f":
        return "f_libraries_of_the_world";
    }
    return null;
  }

  public static void main(String[] args) {
    solveAll();
  }

  private static void solveSingle() {
    String letter = "e";
    Solver solver = new DummySolver();
    solve(letter, solver);
  }

  private static void solveAll() {
    solve("a", new NewSolver());
    solve("b", new NewSolver());
    solve("c", new NewSolver());
    solve("d", new NewSolver());
    solve("e", new NewSolver());
    solve("f", new NewSolver());
  }

  private static void solve(String letter, Solver solver) {
    String statementName = getStatementName(letter);
    List<String> lines = fileToStrings("/" + statementName + ".txt");
    Path file = Paths.get(
        root + "/output/" + statementName + "_" + solver.getClass().getSimpleName() + ".txt");
    List<String> ls = new ArrayList<>();
    ls.add(new Scanner().init(lines).run(solver).buildOutput());
    try {
      Files.write(file, ls, StandardCharsets.UTF_8);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static List<String> fileToStrings(String filePath) {
    List<String> lines = new ArrayList<>();
    InputStream inputStream = App.class.getResourceAsStream(filePath);
    try {
      try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
        String line;
        while ((line = br.readLine()) != null) {
          lines.add(line);
        }
        return lines;
      } catch (IOException e) {
        e.printStackTrace();
      }
    } finally {
      if (inputStream != null) {
        try {
          inputStream.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    return null;
  }
}
