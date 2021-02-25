package com.google.hashcode.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Book implements Comparable {
  private Integer score;
  private Integer id;

  @Override
  public int compareTo(Object o) {
    return Integer.compareUnsigned(score, ((Book)o).getScore());
  }
}
