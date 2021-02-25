package com.google.hashcode.solver;

import com.google.hashcode.Scanner;
import com.google.hashcode.model.Car;
import com.google.hashcode.model.Intersection;
import com.google.hashcode.model.Street;

public class FirstSolver implements Solver {
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

    return scanner;
  }
}
