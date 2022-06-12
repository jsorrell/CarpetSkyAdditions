package com.jsorrell.carpetskyadditions.settings;

public class FieldPair {
  protected String name;
  protected String value;

  FieldPair(String ruleLine) {
    this(ruleLine.split("\\s+", 2));
  }

  FieldPair(String[] fields) {
    this(fields[0], fields[1]);
  }

  FieldPair(FieldPair copy) {
    this.name = copy.name;
    this.value = copy.value;
  }

  FieldPair(String name, String value) {
    this.name = name;
    this.value = value;
  }

  public String getName() {
    return name;
  }

  public String getValue() {
    return value;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public String asConfigLine() {
    return this.name + " " + this.value;
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof FieldPair otherPair)) return false;
    return this.name.equals(otherPair.name) && this.value.equals(otherPair.value);
  }

  @Override
  public String toString() {
    return this.name + ": " + this.value;
  }
}
