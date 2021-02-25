package com.google.hashcode.model;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class Car {
  private Integer id;
  @Builder.Default
  private List<Street> path = new ArrayList<>();
  private int currStreetIndex;

  @Builder.Default
  private int streetDurationLeft = 1;

  @Builder.Default
  private int timeOfArrival = -1;

  public boolean hasArrived() {
    return timeOfArrival >= 0;
  }

  public Street getCurrentStreet() {
    return path.get(currStreetIndex);
  }

  public void step() {
    if (hasArrived()) {
      return;
    }
    streetDurationLeft -= 1;

    if (streetDurationLeft == 0) {
      getCurrentStreet().getTrafficLight().getWaitingCars().add(this);
    }
  }

  public void moveToNextStreet(int t) {
    currStreetIndex += 1;
    if (currStreetIndex < path.size()) {
      streetDurationLeft = path.get(currStreetIndex).getDuration();
      return;
    }
    timeOfArrival = t;
  }

}
