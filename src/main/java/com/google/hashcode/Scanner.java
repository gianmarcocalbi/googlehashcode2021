package com.google.hashcode;

import com.google.hashcode.model.Car;
import com.google.hashcode.model.Intersection;
import com.google.hashcode.model.Street;
import com.google.hashcode.model.TrafficLight;
import com.google.hashcode.solver.Solver;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Log4j2
@Getter
@Setter
public class Scanner {
  private Integer duration_D;
  private Integer numIntersections_I;
  private Integer numStreets_S;
  private Integer numCars_V;
  private Integer bonus_F;
  private Map<String, Street> streets = new HashMap<>();
  private List<Car> cars = new ArrayList<>();
  private Map<Integer, Intersection> intersections = new HashMap<>();

  public Scanner reset() {
    for (Street st : streets.values()) {
      st.getTrafficLight().getWaitingCars().clear();
    }
    for (Car c : cars) {
      c.setCurrStreetIndex(0);
      c.setStreetDurationLeft(1);
      c.setTimeOfArrival(-1);
    }
    return this;
  }

  public Scanner init(List<String> lines) {
    String[] firstLineTokens = lines.remove(0).split(" ");
    duration_D = Integer.parseInt(firstLineTokens[0]);
    numIntersections_I = Integer.parseInt(firstLineTokens[1]);
    numStreets_S = Integer.parseInt(firstLineTokens[2]);
    numCars_V = Integer.parseInt(firstLineTokens[3]);
    bonus_F = Integer.parseInt(firstLineTokens[4]);

    for (int i = 0; i < numIntersections_I; i++) {
      intersections.put(i, Intersection.builder().id(i).build());
    }

    for (int i = 0; i < numStreets_S; i++) {
      String[] streetTokens = lines.remove(0).split(" ");
      Intersection fromIntersection = intersections.get(Integer.parseInt(streetTokens[0]));
      Intersection toIntersection = intersections.get(Integer.parseInt(streetTokens[1]));
      Street street = Street.builder()
          .fromIntersection(fromIntersection)
          .toIntersection(toIntersection)
          .name(streetTokens[2])
          .duration(Integer.parseInt(streetTokens[3]))
          .build();
      fromIntersection.getOutgoingStreets().add(street);
      toIntersection.getIncomingStreets().add(street);
      streets.put(street.getName(), street);
      street.getTrafficLight().setStreet(street);
    }

    for (int i = 0; i < numCars_V; i++) {
      String[] carTokens = lines.remove(0).split(" ");
      int p = Integer.parseInt(carTokens[0]);
      assert carTokens.length == p + 1;

      Car car = Car.builder()
          .id(i)
          .path(Arrays.asList(carTokens)
              .subList(1, p + 1)
              .stream()
              .map(street -> streets.get(street))
              .collect(Collectors.toList()))
          .build();
      cars.add(car);
    }

    return this;
  }

  public Scanner run(Solver solver) {
    return solver.run(this);
  }

  public String buildOutput() {
    // compute score
    //todo

    StringBuilder sb = new StringBuilder();
    sb.append(intersections.size()).append("\n");

    for (Intersection it : intersections.values()) {
      int tl = 0;
      StringBuilder sb2 = new StringBuilder();
      for (TrafficLight trafficLight : it.getTrafficLights()) {
        if (trafficLight.getGreenDuration() > 0) {
          sb2.append(trafficLight.getStreet().getName())
              .append(" ")
              .append(trafficLight.getGreenDuration())
              .append("\n");
          tl += 0;
        }
      }

      if (tl > 0) {
        sb.append(it.getId()).append("\n");
        sb.append(it.getIncomingStreets().size()).append("\n");
        sb.append(sb2);
      }
    }

    return sb.toString();
  }

  public int computeScore() {
    return computeScore(duration_D);
  }

  public int computeScore(int t) {
    int score = 0;
    for (Car car : cars) {
      if (car.hasArrived()) {
        score += bonus_F + t - car.getTimeOfArrival();
      }
    }
    return score;
  }
}
