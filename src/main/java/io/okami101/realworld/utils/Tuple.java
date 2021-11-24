package io.okami101.realworld.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Tuple<K, V> {
  private K first;
  private V second;
}
