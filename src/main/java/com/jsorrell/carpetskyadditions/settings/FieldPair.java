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
        name = copy.name;
        value = copy.value;
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
        return name + " " + value;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof FieldPair otherPair)) return false;
        return name.equals(otherPair.name) && value.equals(otherPair.value);
    }

    @Override
    public String toString() {
        return name + ": " + value;
    }
}
