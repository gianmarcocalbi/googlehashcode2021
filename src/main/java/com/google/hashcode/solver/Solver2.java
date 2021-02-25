package com.google.hashcode.solver;

import com.google.hashcode.Scanner;
import com.google.hashcode.model.Book;
import com.google.hashcode.model.Library;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Solver2 implements Solver {
  @Override
  public Scanner run(Scanner scanner) {
    List<Library> sortedLibraries = scanner.getLibraries().parallelStream().sorted((o1, o2) -> {
      return Integer.compare(o1.getScanPerDay_M(), o2.getScanPerDay_M());
    }).collect(Collectors.toList());
    scanner.setLibraries(sortedLibraries);
    
    return null;
  }
  
  private static void removeBooksFromLibraries(Set<Book> books, List<Library> libraries) {
    libraries.forEach(l -> {
      l.getBooks().removeAll(books);
    });
  }
}
