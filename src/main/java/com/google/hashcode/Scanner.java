package com.google.hashcode;

import com.google.hashcode.model.Book;
import com.google.hashcode.model.Library;
import com.google.hashcode.solver.Solver;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Log4j2
@Getter
@Setter
public class Scanner {
  private Integer numBooks_B;
  private Integer numLibraries_L;
  private Integer days_D;
  private Map<Integer, Book> books = new HashMap<>();
  private List<Library> libraries = new ArrayList<>();

  public Scanner init(List<String> lines) {
    String[] firstLineTokens = lines.remove(0).split(" ");
    numBooks_B = Integer.parseInt(firstLineTokens[0]);
    numLibraries_L = Integer.parseInt(firstLineTokens[1]);
    days_D = Integer.parseInt(firstLineTokens[2]);

    String[] bookLineTokens = lines.remove(0).split(" ");
    for (int i = 0; i < numBooks_B; i++) {
      books.put(i, Book.builder().id(i).score(Integer.parseInt(bookLineTokens[i])).build());
    }

    log.info(numBooks_B + " Books put into the scanner");

    int libraryCounter = 0;
    while (!lines.isEmpty()) {
      if ("".equals(lines.get(0))) {
        lines.remove(0);
        continue;
      }
      log.info("Scanning library " + libraryCounter + "/" + numLibraries_L);
      String[] metaTokens = lines.remove(0).split(" ");
      String[] bookTokens = lines.remove(0).split(" ");
      Integer numBooks = Integer.parseInt(metaTokens[0]);
      Integer signupDays = Integer.parseInt(metaTokens[1]);
      Integer capacity = Integer.parseInt(metaTokens[2]);

      Library library = Library.builder()
          .signUpTime_T(signupDays)
          .scanPerDay_M(capacity)
          .id(libraryCounter)
          .books(new ArrayList<>())
          .build();

      for (int i = 0; i < numBooks; i++) {
        library.getBooks().add(books.get(Integer.parseInt(bookTokens[i])));
      }
      library.setBooks(library.getBooks().parallelStream().sorted((o1, o2) -> {
        return Integer.compareUnsigned(o2.getScore(), o1.getScore());
      }).collect(Collectors.toList()));
      libraries.add(library);
      libraryCounter++;
    }

    log.warn("Initialization finished");

    return this;
  }

  public Scanner run(Solver solver) {
    return solver.run(this);
  }

  public String buildOutput() {
    StringBuilder sb = new StringBuilder();
    sb.append(libraries.size()).append("\n");

    for (Library library : libraries) {
      if (library.getBooks().isEmpty()) {
        continue;
      }
      sb.append(library.getId()).append(" ").append(library.getBooks().size()).append("\n");
      sb.append(library.getBooks()
          .stream()
          .map(l -> l.getId().toString())
          .collect(Collectors.joining(" ")));
      sb.append("\n");
    }

    return sb.toString();
  }
}
