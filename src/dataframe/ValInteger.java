package dataframe;

import java.util.Objects;

public class ValInteger extends Value {
    private Integer value;
    private static ValInteger integer = new ValInteger();

    public static ValInteger getInstance(){
        return integer;
    }

    ValInteger(){};

    public ValInteger(final int integer){
        value = integer;
    }

    public Integer getValue() {
        return value;
    }

    @Override
    public String toString() {
        return Integer.toString(value);
    }


    @Override
    public Value add(Value value) throws CustomException {
        if (value instanceof ValInteger){
            return new ValInteger(this.value + ((ValInteger) value).getValue());
        }
        else if (value instanceof ValDouble){
            return new ValInteger((int)(this.value + ((ValDouble) value).getValue()));
        }
        else if (value instanceof ValFloat){
            return new ValInteger((int)(this.value + ((ValFloat) value).getValue()));
        }
        else throw new CustomException("Tried invalid operation + ");

    }

    @Override
    public Value sub(Value value) throws CustomException {
        if (value instanceof ValInteger){
            return new ValInteger(this.value-((ValInteger) value).getValue());
        }
        else if (value instanceof ValDouble){
            return new ValInteger((int)(this.value - ((ValDouble) value).getValue()));
        }
        else if (value instanceof ValFloat){
            return new ValInteger((int)(this.value - ((ValFloat) value).getValue()));
        }
        else throw new CustomException("Tried invalid operation - ");

    }

    @Override
    public Value mul(Value value) throws CustomException {
        if (value instanceof ValInteger){
            return new ValInteger(this.value*((ValInteger) value).getValue());
        }
        else if (value instanceof ValDouble){
            return new ValInteger((int)(this.value * ((ValDouble) value).getValue()));
        }
        else if (value instanceof ValFloat){
            return new ValInteger((int)(this.value * ((ValFloat) value).getValue()));
        }
        else throw new CustomException("Tried invalid operation * ");
    }

    @Override
    public Value div(Value value) throws CustomException {
        if (value instanceof ValInteger){
            return new ValInteger(this.value/((ValInteger) value).getValue());
        }
        else if (value instanceof ValDouble){
            return new ValDouble((this.value / ((ValDouble) value).getValue()));
        }
        else if (value instanceof ValFloat){
            return new ValDouble((this.value / ((ValFloat) value).getValue()));
        }
        else throw new CustomException("Tried invalid operation /");
    }

    @Override
    public Value pow(Value value) throws CustomException {
        if (value instanceof ValInteger){
            return new ValInteger((int)Math.pow((double)this.value,(double)((ValInteger) value).getValue()));
        }
        else if (value instanceof ValDouble){
            return new ValInteger((int)Math.pow((double)this.value,(double)((ValDouble) value).getValue()));
        }
        else if (value instanceof ValFloat){
            return new ValInteger((int)Math.pow((double)this.value,(double)((ValFloat) value).getValue()));
        }
        else throw new CustomException("Tried invalid operation ^");
    }

    @Override
    public boolean eq(Value value) {
        if (value instanceof ValInteger){
            return Objects.equals(this.value, ((ValInteger) value).getValue());
        }
        return false;
    }

    @Override
    public boolean lte(Value value) {
        if (value instanceof ValInteger) {
            return this.value <= ((ValInteger) value).getValue();
        }
        return false;
    }

    @Override
    public boolean gte(Value value) {
        if (value instanceof ValInteger) {
            return this.value >= ((ValInteger) value).getValue();
        }
        return false;
    }

    @Override
    public boolean neq(Value value) {
        if (value instanceof ValInteger){
            return !Objects.equals(this.value, ((ValInteger) value).getValue());
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ValInteger that = (ValInteger) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public Value create(String s) {
        value = Integer.parseInt(s);
        return new ValInteger(value);
    }

    @Override
    public int compareTo(Value o) {
        return value.compareTo(((ValInteger)o).getValue());
    }
}
