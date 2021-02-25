package com.google.hashcode.solver;

import com.google.hashcode.Scanner;
import com.google.hashcode.model.Book;
import com.google.hashcode.model.Library;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class FinalSolver implements Solver {
  @Override
  public Scanner run(Scanner scanner) {
    List<Library> result = new ArrayList<>();
    int elapsedDays = 0;
    for (int i = 0; i < scanner.getLibraries().size(); i++) {
      final int tempElapsedDays = elapsedDays;
      scanner.setLibraries(scanner.getLibraries().parallelStream().sorted((o1, o2) -> {
        return Integer.compareUnsigned(
            o2.getCoefficient(scanner.getDays_D(), tempElapsedDays),
            o1.getCoefficient(scanner.getDays_D(), tempElapsedDays)
        );
      }).collect(Collectors.toList()));
      Library target = scanner.getLibraries().remove(0);
      result.add(target);
      Integer daysRunning = scanner.getDays_D() - elapsedDays - target.getSignUpTime_T();
      Integer totalSentBooks = daysRunning * target.getScanPerDay_M();
      Integer untilBook = Math.min(
          totalSentBooks > 0 ? totalSentBooks : 0,
          target.getBooks().size()
      );
      removeBooksFromLibraries(target.getBooks()
          .subList(0, untilBook), scanner.getLibraries());
      elapsedDays += target.getSignUpTime_T();
    }
    scanner.setLibraries(result);
    return scanner;
  }

  private static void removeBooksFromLibraries(Collection<Book> books, List<Library> libraries) {
    libraries.forEach(l -> {
      l.getBooks().removeAll(books);
    });
  }
}
