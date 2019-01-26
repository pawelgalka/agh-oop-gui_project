package dataframe;


import java.util.Objects;

public class FloatHolder extends Value {
  /*  private Float value;
    private static FloatHolder integer = new FloatHolder();

    public static FloatHolder getInstance(){
        return integer;
    }

    FloatHolder(){};

    public FloatHolder(final float integer){
        value = integer;
    }

    public Float getValue() {
        return value;
    }

    @Override
    public String toString() {
        return java.lang.Float.toString(value);
    }


    @Override
    public Value add(Value value) {
        if (value instanceof FloatHolder){
            return new FloatHolder(this.value + ((FloatHolder) value).getValue());
        }
        else if (value instanceof DoubleHolder){
            return new FloatHolder((float) (this.value + ((DoubleHolder) value).getValue()));
        }
        else if (value instanceof IntHolder){
            return new FloatHolder((float)(this.value + ((IntHolder) value).getValue()));
        }
        else System.out.println("Tried invalid operation +");
        return this;
    }

    @Override
    public Value sub(Value value) {
        if (value instanceof FloatHolder){
            return new FloatHolder(this.value-((FloatHolder) value).getValue());
        }
        else if (value instanceof DoubleHolder){
            return new FloatHolder((float) (this.value - ((DoubleHolder) value).getValue()));
        }
        else if (value instanceof IntHolder){
            return new FloatHolder((float)(this.value - ((IntHolder) value).getValue()));
        }
        else System.out.println("Tried invalid operation -");
        return this;

    }

    @Override
    public Value mul(Value value) {
        if (value instanceof FloatHolder){
            return new FloatHolder(this.value*((FloatHolder) value).getValue());
        }
        else if (value instanceof DoubleHolder){
            return new FloatHolder((float) (this.value * ((DoubleHolder) value).getValue()));
        }
        else if (value instanceof IntHolder){
            return new FloatHolder((float)(this.value * ((IntHolder) value).getValue()));
        }
        else System.out.println("Tried invalid operation *");
        return this;
    }

    @Override
    public Value div(Value value) {
        if (value instanceof FloatHolder){
            return new FloatHolder(this.value/((FloatHolder) value).getValue());
        }
        else if (value instanceof DoubleHolder){
            return new FloatHolder((float) (this.value / ((DoubleHolder) value).getValue()));
        }
        else if (value instanceof IntHolder){
            return new FloatHolder((float)(this.value / ((IntHolder) value).getValue()));
        }
        else System.out.println("Tried invalid operation /");
        return this;
    }

    @Override
    public Value pow(Value value) {
        if (value instanceof FloatHolder){
            return new FloatHolder((int) Math.pow((double)this.value,(double)((FloatHolder) value).getValue()));
        }
        else if (value instanceof DoubleHolder){
            return new FloatHolder((float) Math.pow((double)this.value,(double)((DoubleHolder) value).getValue()));
        }
        else if (value instanceof IntHolder){
            return new FloatHolder((float) Math.pow((double)this.value,(double)((IntHolder) value).getValue()));
        }
        else System.out.println("Tried invalid operation ^");
        return this;
    }

    @Override
    public boolean eq(Value value) {
        if (value instanceof FloatHolder){
            return Objects.equals(this.value, ((FloatHolder) value).getValue());
        }
        return false;
    }

    @Override
    public boolean lte(Value value) {
        if (value instanceof FloatHolder) {
            return this.value <= ((FloatHolder) value).getValue();
        }
        return false;
    }

    @Override
    public boolean gte(Value value) {
        if (value instanceof FloatHolder) {
            return this.value >= ((FloatHolder) value).getValue();
        }
        return false;
    }

    @Override
    public boolean neq(Value value) {
        if (value instanceof FloatHolder){
            return !Objects.equals(this.value, ((FloatHolder) value).getValue());
        }
        return false;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FloatHolder valFloat = (FloatHolder) o;
        return Objects.equals(value, valFloat.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public Value create(String s) {
        value = java.lang.Float.parseFloat(s);
        return new FloatHolder(value);
    }
    @Override
    public int compareTo(Value o) {
        return value.compareTo(((FloatHolder)o).getValue());
    }*/
  Float floatValue;

    public FloatHolder (float x){
        floatValue = x;
    }


    FloatHolder(){};


    public Float getValue() {
        return floatValue;
    }

    @Override
    public String toString() {
        return java.lang.Float.toString(floatValue);
    }


    @Override
    public Value add(Value value) {
        if (value instanceof FloatHolder){
            this.floatValue += ((FloatHolder) value).getValue();
        }
        if (value instanceof DoubleHolder){
            this.floatValue = (float)(floatValue + ((DoubleHolder) value).getValue());
        }
        if (value instanceof IntHolder){
            this.floatValue = floatValue + ((IntHolder) value).getValue();
        }
        return this;
    }

    @Override
    public Value sub(Value value) {
        if (value instanceof FloatHolder){
            this.floatValue -= ((FloatHolder) value).getValue();
        }
        if (value instanceof DoubleHolder){
            this.floatValue = (float)(floatValue - ((DoubleHolder) value).getValue());
        }
        if (value instanceof IntHolder){
            this.floatValue = floatValue - ((IntHolder) value).getValue();
        }
        return this;

    }

    @Override
    public Value mul(Value value) {
        if (value instanceof FloatHolder){
            this.floatValue *= ((FloatHolder) value).getValue();
        }
        if (value instanceof DoubleHolder){
            this.floatValue = (float)(floatValue * ((DoubleHolder) value).getValue());
        }
        if (value instanceof IntHolder){
            this.floatValue = floatValue * ((IntHolder) value).getValue();
        }
        return this;

    }

    @Override
    public Value div(Value value) throws CustomException{
        if (value instanceof FloatHolder){
            if(((FloatHolder) value).getValue() == 0) throw new CustomException("You can't divide by 0");
            this.floatValue /= ((FloatHolder) value).getValue();
        }
        if (value instanceof DoubleHolder){
            if(((DoubleHolder) value).getValue() == 0) throw new CustomException("You can't divide by 0");
            this.floatValue = (float)(floatValue / ((DoubleHolder) value).getValue());
        }
        if (value instanceof IntHolder){
            if(((IntHolder) value).getValue() == 0) throw new CustomException("You can't divide by 0");
            this.floatValue = floatValue / ((IntHolder) value).getValue();
        }
        return this;

    }

    @Override
    public Value pow(Value value) {
        if (value instanceof FloatHolder){
            this.floatValue = (float)Math.pow(this.floatValue,((FloatHolder) value).getValue());
        }
        if (value instanceof DoubleHolder){
            this.floatValue = (float)Math.pow((double)this.floatValue,((DoubleHolder) value).getValue());
        }
        return this;

    }

    @Override
    public boolean eq(Value value) {
        if (value instanceof FloatHolder){
            return Objects.equals(this.floatValue, ((FloatHolder) value).getValue());
        }
        return false;
    }

    @Override
    public boolean lte(Value value) {
        if (value instanceof FloatHolder) {
            return this.floatValue < ((FloatHolder) value).getValue();
        }
        if (value instanceof DoubleHolder){
            return this.floatValue < ((DoubleHolder)value).getValue();
        }
        if (value instanceof IntHolder){
            return this.floatValue < ((IntHolder)value).getValue();
        }
        return false;
    }

    @Override
    public boolean gte(Value value) {
        if (value instanceof FloatHolder) {
            return this.floatValue > ((FloatHolder) value).getValue();
        }
        if (value instanceof DoubleHolder){
            return this.floatValue > ((DoubleHolder)value).getValue();
        }
        if (value instanceof IntHolder){
            return this.floatValue > ((IntHolder)value).getValue();
        }
        return false;
    }

    @Override
    public boolean neq(Value value) {
        if (value instanceof FloatHolder){
            return !Objects.equals(this.floatValue, ((FloatHolder) value).getValue());
        }
        return false;
    }

    public FloatHolder create(String s){
        floatValue = Float.parseFloat(s);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FloatHolder)) return false;
        FloatHolder that = (FloatHolder) o;
        return Float.compare(that.floatValue, floatValue) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(floatValue);
    }

    @Override
    public int compareTo(Value o) {
        return floatValue.compareTo(((FloatHolder)o).getValue());
    }
}
