package com.google.hashcode.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class TrafficLight {
  private List<Car> waitingCars = new ArrayList<>();
  private Integer greenDuration;
  private Street street;
}
