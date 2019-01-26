package dataframe;

import java.util.Objects;

public class DoubleHolder extends Value {
   /* private Double value;
    private static DoubleHolder integer = new DoubleHolder();

    public static DoubleHolder getInstance(){
        return integer;
    }

    DoubleHolder(){};

    public DoubleHolder(final double integer){
        value = integer;
    }

    public Double getValue() {
        return value;
    }

    @Override
    public String toString() {
        return java.lang.Double.toString(value);
    }


    @Override
    public Value add(Value value) throws CustomException {
        if (value instanceof DoubleHolder){
            return new DoubleHolder(this.value + ((DoubleHolder) value).getValue());
        }
        else if (value instanceof IntHolder){
            return new DoubleHolder(this.value+((IntHolder) value).getValue());
        }
        else if (value instanceof FloatHolder){
            return new DoubleHolder(this.value+((FloatHolder) value).getValue());
        }
        else throw new CustomException("Tried invalid operation +");
    }

    @Override
    public Value sub(Value value) throws CustomException {
        if (value instanceof DoubleHolder){
            return new DoubleHolder(this.value-((DoubleHolder) value).getValue());
        }
        else if (value instanceof IntHolder){
            return new DoubleHolder(this.value-((IntHolder) value).getValue());
        }
        else if (value instanceof FloatHolder){
            return new DoubleHolder(this.value-((FloatHolder) value).getValue());
        }
        else throw new CustomException("Tried invalid operation -");

    }

    @Override
    public Value mul(Value value) throws CustomException {
        if (value instanceof DoubleHolder){
            return new DoubleHolder(this.value*((DoubleHolder) value).getValue());
        }
        else if (value instanceof IntHolder){
            return new DoubleHolder(this.value*((IntHolder) value).getValue());
        }
        else if (value instanceof FloatHolder){
            return new DoubleHolder(this.value*((FloatHolder) value).getValue());
        }
        else throw new CustomException("Tried invalid operation *");

    }

    @Override
    public Value div(Value value) throws CustomException {
        if (value instanceof DoubleHolder){
            return new DoubleHolder(this.value/((DoubleHolder) value).getValue());
        }
        else if (value instanceof IntHolder){
            return new DoubleHolder(this.value/((IntHolder) value).getValue());
        }
        else if (value instanceof FloatHolder){
            return new DoubleHolder(this.value/((FloatHolder) value).getValue());
        }
        else throw new CustomException("Tried invalid operation /");


    }

    @Override
    public Value pow(Value value) throws CustomException {
        if (value instanceof DoubleHolder){
            return new DoubleHolder(Math.pow((double)this.value,(double)((DoubleHolder) value).getValue()));
        }
        else if (value instanceof IntHolder){
            return new DoubleHolder(Math.pow((double)this.value,(double)((IntHolder) value).getValue()));
        }
        else if (value instanceof FloatHolder){
            return new DoubleHolder(Math.pow((double)this.value,(double)((FloatHolder) value).getValue()));
        }
        else throw new CustomException("Tried invalid operation ^");

    }

    @Override
    public boolean eq(Value value) {
        if (value instanceof DoubleHolder){
            return Objects.equals(this.value, ((DoubleHolder) value).getValue());
        }
        return false;
    }

    @Override
    public boolean lte(Value value) {
        if (value instanceof DoubleHolder) {
            return this.value <= ((DoubleHolder) value).getValue();
        }
        return false;
    }

    @Override
    public boolean gte(Value value) {
        if (value instanceof DoubleHolder) {
            return this.value >= ((DoubleHolder) value).getValue();
        }
        return false;
    }

    @Override
    public boolean neq(Value value) {
        if (value instanceof DoubleHolder){
            return !Objects.equals(this.value, ((DoubleHolder) value).getValue());
        }
        return false;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DoubleHolder valDouble = (DoubleHolder) o;
        return Objects.equals(value, valDouble.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public Value create(String s) {
        value = java.lang.Double.parseDouble(s);
        return new DoubleHolder(value);
    }
    @Override
    public int compareTo(Value o) {
        return value.compareTo(((DoubleHolder)o).getValue());
    }*/
   Double doubleValue;

    public DoubleHolder (double x){
        doubleValue = x;
    }


    DoubleHolder(){};


    public Double getValue() {
        return doubleValue;
    }

    @Override
    public String toString() {
        return java.lang.Double.toString(doubleValue);
    }


    @Override
    public Value add(Value value) {
        if (value instanceof DoubleHolder){
            this.doubleValue += ((DoubleHolder) value).getValue();
        }
        if (value instanceof FloatHolder){
            this.doubleValue =doubleValue + ((FloatHolder) value).getValue();
        }
        if (value instanceof IntHolder){
            this.doubleValue = doubleValue + ((IntHolder) value).getValue();
        }
        return this;
    }

    @Override
    public Value sub(Value value) {
        if (value instanceof DoubleHolder){
            this.doubleValue -= ((DoubleHolder) value).getValue();
        }
        if (value instanceof FloatHolder){
            this.doubleValue =doubleValue - ((FloatHolder) value).getValue();
        }
        if (value instanceof IntHolder){
            this.doubleValue = doubleValue - ((IntHolder) value).getValue();
        }
        return this;

    }

    @Override
    public Value mul(Value value) {
        if (value instanceof DoubleHolder){
            this.doubleValue *= ((DoubleHolder) value).getValue();
        }
        if (value instanceof FloatHolder){
            this.doubleValue =doubleValue * ((FloatHolder) value).getValue();
        }
        if (value instanceof IntHolder){
            this.doubleValue = doubleValue * ((IntHolder) value).getValue();
        }
        return this;

    }

    @Override
    public Value div(Value value) throws CustomException{
        if (value instanceof DoubleHolder){
            if(((DoubleHolder) value).getValue() == 0) throw new CustomException("You can't divide by 0");
            this.doubleValue /= ((DoubleHolder) value).getValue();
        }
        if (value instanceof FloatHolder){
            if(((FloatHolder) value).getValue() == 0) throw new CustomException("You can't divide by 0");
            this.doubleValue =doubleValue / ((FloatHolder) value).getValue();
        }
        if (value instanceof IntHolder){
            if(((IntHolder) value).getValue() == 0) throw new CustomException("You can't divide by 0");
            this.doubleValue = doubleValue / ((IntHolder) value).getValue();
        }
        return this;

    }

    @Override
    public Value pow(Value value) {
        if (value instanceof DoubleHolder){
            this.doubleValue = Math.pow(this.doubleValue,((DoubleHolder) value).getValue());
        }
        return this;

    }

    @Override
    public boolean eq(Value value) {
        if (value instanceof DoubleHolder){
            return Objects.equals(this.doubleValue, ((DoubleHolder) value).getValue());
        }
        return false;
    }

    @Override
    public boolean lte(Value value) {
        if (value instanceof DoubleHolder) {
            return this.doubleValue < ((DoubleHolder) value).getValue();
        }
        if (value instanceof FloatHolder){
            return this.doubleValue < ((FloatHolder)value).getValue();
        }
        if (value instanceof IntHolder){
            return this.doubleValue < ((IntHolder)value).getValue();
        }
        return false;
    }

    @Override
    public boolean gte(Value value) {
        if (value instanceof DoubleHolder) {
            return this.doubleValue > ((DoubleHolder) value).getValue();
        }
        if (value instanceof FloatHolder){
            return this.doubleValue > ((FloatHolder)value).getValue();
        }
        if (value instanceof IntHolder){
            return this.doubleValue > ((IntHolder)value).getValue();
        }
        return false;
    }

    @Override
    public boolean neq(Value value) {
        if (value instanceof DoubleHolder){
            return !Objects.equals(this.doubleValue, ((DoubleHolder) value).getValue());
        }
        return false;
    }

    public DoubleHolder create(String s){
        doubleValue = Double.parseDouble(s);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DoubleHolder)) return false;
        DoubleHolder that = (DoubleHolder) o;
        return Double.compare(that.doubleValue, doubleValue) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(doubleValue);
    }

    @Override
    public int compareTo(Value o) {
        return doubleValue.compareTo(((DoubleHolder)o).getValue());
    }

}
