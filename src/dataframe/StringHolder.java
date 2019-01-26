package dataframe;

import java.util.Objects;

public class StringHolder extends Value {
  /*  private String value;
    static StringHolder integer = new StringHolder();

    public static StringHolder getInstance(){
        return integer;
    }

    StringHolder(){};

    public StringHolder(final String integer){
        value = integer;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }


    @Override
    public Value add(Value value) { return this; }

    @Override
    public Value sub(Value value) {
        return this;
    }

    @Override
    public Value mul(Value value) {
        return this;
    }

    @Override
    public Value div(Value value) {
        return this;
    }

    @Override
    public Value pow(Value value) {
        return this;
    }

    @Override
    public boolean eq(Value value) {
        if (value instanceof StringHolder){
            return Objects.equals(this.value, ((StringHolder) value).getValue());
        }
        return false;
    }

    @Override
    public boolean lte(Value value) {
        if (value instanceof StringHolder) {
            return this.value.length() <= ((StringHolder) value).getValue().length();
        }
        return false;
    }

    @Override
    public boolean gte(Value value) {
        if (value instanceof StringHolder) {
            return this.value.length() >= ((StringHolder) value).getValue().length();
        }
        return false;
    }

    @Override
    public boolean neq(Value value) {
        if (value instanceof StringHolder){
            return !Objects.equals(this.value, ((StringHolder) value).getValue());
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StringHolder valString = (StringHolder) o;
        return Objects.equals(value, valString.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public Value create(String s) {
        value = s;
        return new StringHolder(value);
    }
    @Override
    public int compareTo(Value o) {
        return value.compareTo(((StringHolder)o).getValue());
    }*/
  String stringValue;

    public StringHolder (String x){
        stringValue = x;
    }

    StringHolder(){}

    public String getValue() {
        return stringValue;
    }

    @Override
    public String toString() {
        return stringValue;
    }

    @Override
    public Value add(Value value) {
        return null;
    }

    @Override
    public Value sub(Value value) {
        return null;
    }

    @Override
    public Value mul(Value value) {
        return null;
    }

    @Override
    public Value div(Value value) {
        return null;
    }

    @Override
    public Value pow(Value value) {
        return null;
    }

    @Override
    public boolean eq(Value value) {
        if (value instanceof StringHolder){
            return Objects.equals(this.stringValue, ((StringHolder) value).getValue());
        }
        return false;
    }

    @Override
    public boolean lte(Value value) {
        return false;
    }

    @Override
    public boolean gte(Value value) {
        return false;
    }

    @Override
    public boolean neq(Value value) {
        if (value instanceof StringHolder){
            return !Objects.equals(this.stringValue, ((StringHolder) value).getValue());
        }
        return false;
    }

    @Override
    public StringHolder create(String s) {
        stringValue = s;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StringHolder)) return false;
        StringHolder that = (StringHolder) o;
        return Objects.equals(stringValue, that.stringValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stringValue);
    }

    @Override
    public int compareTo(Value o) {
        return stringValue.compareTo(((StringHolder)o).getValue());
    }
}
