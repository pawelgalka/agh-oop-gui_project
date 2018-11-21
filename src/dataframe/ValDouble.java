package dataframe;

import java.util.Objects;

public class ValDouble extends Value {
    private Double value;
    private static ValDouble integer = new ValDouble();

    public static ValDouble getInstance(){
        return integer;
    }

    ValDouble(){};

    public ValDouble(final double integer){
        value = integer;
    }

    public Double getValue() {
        return value;
    }

    @Override
    public String toString() {
        return Double.toString(value);
    }


    @Override
    public Value add(Value value) throws CustomException {
        if (value instanceof ValDouble){
            return new ValDouble(this.value + ((ValDouble) value).getValue());
        }
        else if (value instanceof ValInteger){
            return new ValDouble(this.value+((ValInteger) value).getValue());
        }
        else if (value instanceof ValFloat){
            return new ValDouble(this.value+((ValFloat) value).getValue());
        }
        else throw new CustomException("Tried invalid operation +");
    }

    @Override
    public Value sub(Value value) throws CustomException {
        if (value instanceof ValDouble){
            return new ValDouble(this.value-((ValDouble) value).getValue());
        }
        else if (value instanceof ValInteger){
            return new ValDouble(this.value-((ValInteger) value).getValue());
        }
        else if (value instanceof ValFloat){
            return new ValDouble(this.value-((ValFloat) value).getValue());
        }
        else throw new CustomException("Tried invalid operation -"+value.getClass());

    }

    @Override
    public Value mul(Value value) throws CustomException {
        if (value instanceof ValDouble){
            return new ValDouble(this.value*((ValDouble) value).getValue());
        }
        else if (value instanceof ValInteger){
            return new ValDouble(this.value*((ValInteger) value).getValue());
        }
        else if (value instanceof ValFloat){
            return new ValDouble(this.value*((ValFloat) value).getValue());
        }
        else throw new CustomException("Tried invalid operation *");

    }

    @Override
    public Value div(Value value) throws CustomException {
        if (value instanceof ValDouble){
            return new ValDouble(this.value/((ValDouble) value).getValue());
        }
        else if (value instanceof ValInteger){
            return new ValDouble(this.value/((ValInteger) value).getValue());
        }
        else if (value instanceof ValFloat){
            return new ValDouble(this.value/((ValFloat) value).getValue());
        }
        else throw new CustomException("Tried invalid operation /");


    }

    @Override
    public Value pow(Value value) throws CustomException {
        if (value instanceof ValDouble){
            return new ValDouble(Math.pow((double)this.value,(double)((ValDouble) value).getValue()));
        }
        else if (value instanceof ValInteger){
            return new ValDouble(Math.pow((double)this.value,(double)((ValInteger) value).getValue()));
        }
        else if (value instanceof ValFloat){
            return new ValDouble(Math.pow((double)this.value,(double)((ValFloat) value).getValue()));
        }
        else throw new CustomException("Tried invalid operation ^");

    }

    @Override
    public boolean eq(Value value) {
        if (value instanceof ValDouble){
            return Objects.equals(this.value, ((ValDouble) value).getValue());
        }
        return false;
    }

    @Override
    public boolean lte(Value value) {
        if (value instanceof ValDouble) {
            return this.value <= ((ValDouble) value).getValue();
        }
        return false;
    }

    @Override
    public boolean gte(Value value) {
        if (value instanceof ValDouble) {
            return this.value >= ((ValDouble) value).getValue();
        }
        return false;
    }

    @Override
    public boolean neq(Value value) {
        if (value instanceof ValDouble){
            return !Objects.equals(this.value, ((ValDouble) value).getValue());
        }
        return false;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ValDouble valDouble = (ValDouble) o;
        return Objects.equals(value, valDouble.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public Value create(String s) {
        value = Double.parseDouble(s);
        return new ValDouble(value);
    }
    @Override
    public int compareTo(Value o) {
        return value.compareTo(((ValDouble)o).getValue());
    }

}
