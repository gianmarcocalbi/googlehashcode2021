package com.google.hashcode.solver;

import com.google.hashcode.Scanner;
import com.google.hashcode.model.Library;
import lombok.extern.log4j.Log4j2;

import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Log4j2
public class DummySolver implements Solver {
  
  public Scanner run(Scanner scanner) {
    log.info("Sorting libraries");
    List<Library> sortedLibraries = scanner.getLibraries().parallelStream().sorted((o1, o2) -> {
      return Integer.compareUnsigned(o2.getScanPerDay_M(), o1.getScanPerDay_M());
    }).collect(Collectors.toList());
    sortedLibraries.forEach(l -> {
      l.setBooks(l.getBooks().parallelStream().sorted((o1, o2) -> {
        return Integer.compareUnsigned(o2.getScore(), o1.getScore());
      }).collect(Collectors.toList()));
    });
    scanner.setLibraries(sortedLibraries);
    return scanner;
  }
}
