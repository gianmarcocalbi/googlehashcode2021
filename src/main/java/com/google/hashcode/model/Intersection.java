package com.google.hashcode.model;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class Intersection {
  private Integer id;

  @Builder.Default
  private List<Street> incomingStreets = new ArrayList<>();

  @Builder.Default
  private List<Street> outgoingStreets = new ArrayList<>();

  public List<TrafficLight> getTrafficLights() {
    return incomingStreets.stream().map(Street::getTrafficLight).collect(Collectors.toList());
  }

  public void step(int t) {
    List<TrafficLight> trafficLights = getTrafficLights();
    if (trafficLights.isEmpty()) {
      return;
    }
    int n = 0;
    TrafficLight tl = trafficLights.get(0);
    for (int i = 0; i < trafficLights.size(); i++) {
      tl = trafficLights.get(i);
      n += tl.getGreenDuration();
      if (t % trafficLights.size() < n) {
        break;
      }
    }
    if (tl.getWaitingCars().isEmpty()) {
      return;
    }
    Car car = tl.getWaitingCars().remove(0);
    car.moveToNextStreet(t);
  }
}
