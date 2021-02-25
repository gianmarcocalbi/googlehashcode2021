package com.google.hashcode.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Builder
@Data
public class Library {
  private List<Book> books;
  private Integer signUpTime_T;
  private Integer scanPerDay_M;
  private Integer id;

  public Integer getCoefficient(Integer totDays, Integer elapsedDays) {
    int days = totDays - elapsedDays - signUpTime_T;
    int coeff = 0;
    int countedBooks = 0;
    for (int i = 0; i < days; i++) {
      for (int j = 0; j < scanPerDay_M; j++) {
        if (countedBooks == books.size()) {
          return coeff;
        }
        coeff += books.get(countedBooks++).getScore();
      }
    }
    return coeff;
  }
}
