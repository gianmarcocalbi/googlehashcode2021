package com.google.hashcode.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Street {
  private String name;
  private Intersection fromIntersection;
  private Intersection toIntersection;
  private Integer duration;
  
  @Builder.Default
  private TrafficLight trafficLight = new TrafficLight();
}
