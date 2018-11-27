package dataframe;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class ValDateTime extends Value {
    private Date value;
    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static ValDateTime integer = new ValDateTime();

    public static ValDateTime getInstance(){
        return integer;
    }

    ValDateTime(){};

    public ValDateTime(final Date integer){
        value = integer;
    }

    public Date getValue() {
        return value;
    }

    @Override
    public String toString() {
        return dateFormat.format(value);
    }

    public Value add(Value v){
        return this;
    }

    public Value sub(Value v){
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

    public boolean eq(Value v){
        if (v instanceof ValDateTime){
            return Objects.equals(this.value, ((ValDateTime) v).getValue());
        }
        return false;
    }

    public boolean lte(Value v) {
        if (v instanceof ValDateTime) {
            return ((ValDateTime) v).getValue().before(value);
        }
        return false;
    }

    public boolean gte(Value v){
        if (v instanceof ValDateTime) {
            return ((ValDateTime) v).getValue().after(value) ;
        }
        return false;
    }

    public boolean neq(Value v){
        if (v instanceof ValBoolean) {
            return !Objects.equals(this.value, ((ValDateTime) v).getValue());
        }
        return false;
    }


    /*@Override
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
        ValDateTime that = (ValDateTime) o;
        return Objects.equals(value, that.value) &&
                Objects.equals(dateFormat, that.dateFormat);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, dateFormat);
    }

    @Override
    public Value create(String s) {
        try {
            value = dateFormat.parse(s);
        }
        catch (ParseException e){}
        return new ValDateTime(value);
    }
    @Override
    public int compareTo(Value o) {
        return value.compareTo(((ValDateTime)o).getValue());
    }
}
