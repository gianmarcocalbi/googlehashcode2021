package com.google.hashcode;

import com.google.hashcode.solver.FirstSolver;
import com.google.hashcode.solver.SecondSolver;
import com.google.hashcode.solver.Solver;
import lombok.extern.log4j.Log4j2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Log4j2
public class App {

  private static final String root = "/Users/gcalbi/workspace/google_hash_code_2021/src/main"
      + "/resources/";

  private static Solver getSolver(String name) {
    switch (name) {
      case "1":
        return new FirstSolver();
      case "2":
        return new SecondSolver();
    }
    return null;
  }

  public static void main(String[] args) throws IOException {
    log.info("Program started with args " + Arrays.toString(args));
    String statementLetter = args[0];
    Solver solver = getSolver(args[1]);
    if (statementLetter.equalsIgnoreCase("all")) {
      solveAll(solver);
    } else {
      solve(statementLetter, solver);
    }
    System.exit(0);
  }

  private static void solveAll(Solver solver) throws IOException {
    log.info("Solving all statements at once");
    solve("a", solver);
    solve("b", solver);
    solve("c", solver);
    solve("d", solver);
    solve("e", solver);
    solve("f", solver);
  }

  private static void solve(String letter, Solver solver) throws IOException {
    log.info(String.format(
        "Solving statement %s with solver %s",
        letter,
        solver.getClass().getSimpleName()
    ));
    List<String> lines = fileToStrings("/" + letter + ".txt");
    Path file = Paths.get(
        root + "/output/" + letter + "_" + solver.getClass().getSimpleName() + ".txt");
    List<String> ls = new ArrayList<>();
    Scanner scanner = new Scanner();
    LocalTime t = LocalTime.now();
    log.info("Initializing scanner");
    assert lines != null;
    scanner.init(lines);
    log.info(
        "Scanner initialization completed in " + Duration.between(t, LocalTime.now()).toString());

    t = LocalTime.now();
    log.info("Running solver");
    scanner.run(solver);
    log.info(
        "Solver run in " + Duration.between(t, LocalTime.now()).toString());

    log.warn("Simulation score = " + scanner.computeScore());
    
    t = LocalTime.now();
    log.info("Building output");
    String output = scanner.buildOutput();
    log.info(
        "Output built in " + Duration.between(t, LocalTime.now()).toString());
    ls.add(output);

    try {
      Files.write(file, ls, StandardCharsets.UTF_8);
    } catch (IOException e) {
      log.error(e);
      throw e;
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
