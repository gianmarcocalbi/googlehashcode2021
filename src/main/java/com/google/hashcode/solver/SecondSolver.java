package com.google.hashcode.solver;

import com.google.hashcode.Scanner;
import com.google.hashcode.model.Car;
import com.google.hashcode.model.Intersection;
import com.google.hashcode.model.Street;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class SecondSolver implements Solver {
  @Override
  public Scanner run(Scanner scanner) {
    for (Street st : scanner.getStreets().values()) {
      st.getTrafficLight().setGreenDuration(1);
    }

    for (int i = 0; i < scanner.getDuration_D(); i++) {
      for (Car car : scanner.getCars()) {
        car.step();
      }

      for (Intersection it : scanner.getIntersections().values()) {
        it.step(i);
      }
    }

    Set<String> goodStreets = new HashSet<>();

    scanner.getCars().stream().filter(Car::hasArrived).forEach(car ->
        goodStreets.addAll(car.getPath().stream().map(Street::getName).collect(Collectors.toList()))
    );

    scanner.reset();

    for (Street st : scanner.getStreets().values()) {
      if (goodStreets.contains(st.getName())) {
        st.getTrafficLight().setGreenDuration(1);
      } else {
        st.getTrafficLight().setGreenDuration(0);
      }
    }

    return scanner;
  }
}
