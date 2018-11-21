package dataframe;

import java.util.Objects;

public class ValFloat extends Value {
    private Float value;
    private static ValFloat integer = new ValFloat();

    public static ValFloat getInstance(){
        return integer;
    }

    ValFloat(){};

    public ValFloat(final float integer){
        value = integer;
    }

    public Float getValue() {
        return value;
    }

    @Override
    public String toString() {
        return Float.toString(value);
    }


    @Override
    public Value add(Value value) {
        if (value instanceof ValFloat){
            return new ValFloat(this.value + ((ValFloat) value).getValue());
        }
        else if (value instanceof ValDouble){
            return new ValFloat((float) (this.value + ((ValDouble) value).getValue()));
        }
        else if (value instanceof ValInteger){
            return new ValFloat((float)(this.value + ((ValInteger) value).getValue()));
        }
        else System.out.println("Tried invalid operation +");
        return this;
    }

    @Override
    public Value sub(Value value) {
        if (value instanceof ValFloat){
            return new ValFloat(this.value-((ValFloat) value).getValue());
        }
        else if (value instanceof ValDouble){
            return new ValFloat((float) (this.value - ((ValDouble) value).getValue()));
        }
        else if (value instanceof ValInteger){
            return new ValFloat((float)(this.value - ((ValInteger) value).getValue()));
        }
        else System.out.println("Tried invalid operation -");
        return this;

    }

    @Override
    public Value mul(Value value) {
        if (value instanceof ValFloat){
            return new ValFloat(this.value*((ValFloat) value).getValue());
        }
        else if (value instanceof ValDouble){
            return new ValFloat((float) (this.value * ((ValDouble) value).getValue()));
        }
        else if (value instanceof ValInteger){
            return new ValFloat((float)(this.value * ((ValInteger) value).getValue()));
        }
        else System.out.println("Tried invalid operation *");
        return this;
    }

    @Override
    public Value div(Value value) {
        if (value instanceof ValFloat){
            return new ValFloat(this.value/((ValFloat) value).getValue());
        }
        else if (value instanceof ValDouble){
            return new ValFloat((float) (this.value / ((ValDouble) value).getValue()));
        }
        else if (value instanceof ValInteger){
            return new ValFloat((float)(this.value / ((ValInteger) value).getValue()));
        }
        else System.out.println("Tried invalid operation /");
        return this;
    }

    @Override
    public Value pow(Value value) {
        if (value instanceof ValFloat){
            return new ValFloat((int)Math.pow((double)this.value,(double)((ValFloat) value).getValue()));
        }
        else if (value instanceof ValDouble){
            return new ValFloat((float)Math.pow((double)this.value,(double)((ValDouble) value).getValue()));
        }
        else if (value instanceof ValInteger){
            return new ValFloat((float)Math.pow((double)this.value,(double)((ValInteger) value).getValue()));
        }
        else System.out.println("Tried invalid operation ^");
        return this;
    }

    @Override
    public boolean eq(Value value) {
        if (value instanceof ValFloat){
            return Objects.equals(this.value, ((ValFloat) value).getValue());
        }
        return false;
    }

    @Override
    public boolean lte(Value value) {
        if (value instanceof ValFloat) {
            return this.value <= ((ValFloat) value).getValue();
        }
        return false;
    }

    @Override
    public boolean gte(Value value) {
        if (value instanceof ValFloat) {
            return this.value >= ((ValFloat) value).getValue();
        }
        return false;
    }

    @Override
    public boolean neq(Value value) {
        if (value instanceof ValFloat){
            return !Objects.equals(this.value, ((ValFloat) value).getValue());
        }
        return false;
    }

   /* @Override
    public boolean equals(Object other) {
        return this.eq((Value)other);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }*/

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ValFloat valFloat = (ValFloat) o;
        return Objects.equals(value, valFloat.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public Value create(String s) {
        value = Float.parseFloat(s);
        return new ValFloat(value);
    }
    @Override
    public int compareTo(Value o) {
        return value.compareTo(((ValFloat)o).getValue());
    }
}
