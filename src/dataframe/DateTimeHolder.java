package dataframe;

import dataframe.exceptions.CustomException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class DateTimeHolder extends Value {
    /*private Date value;
    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static DateTimeHolder integer = new DateTimeHolder();

    public static DateTimeHolder getInstance(){
        return integer;
    }

    DateTimeHolder(){};

    public DateTimeHolder(final Date integer){
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
        if (v instanceof DateTimeHolder){
            return Objects.equals(this.value, ((DateTimeHolder) v).getValue());
        }
        return false;
    }

    public boolean lte(Value v) {
        if (v instanceof DateTimeHolder) {
            return ((DateTimeHolder) v).getValue().before(value);
        }
        return false;
    }

    public boolean gte(Value v){
        if (v instanceof DateTimeHolder) {
            return ((DateTimeHolder) v).getValue().after(value) ;
        }
        return false;
    }

    public boolean neq(Value v){
        if (v instanceof ValBoolean) {
            return !Objects.equals(this.value, ((DateTimeHolder) v).getValue());
        }
        return false;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DateTimeHolder that = (DateTimeHolder) o;
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
        catch (ParseException e){ throw new IllegalArgumentException("Invalid type for date format");}
        return new DateTimeHolder(value);
    }
    @Override
    public int compareTo(Value o) {
        return value.compareTo(((DateTimeHolder)o).getValue());
    }*/
    Date dateValue;


    public DateTimeHolder (Date x){
        dateValue = x;
    }

    DateTimeHolder(){
        dateValue = new Date();
    };

    public Date getValue() {
        return dateValue;
    }

    @Override
    public String toString() {
        return dateValue.toString();
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
        if (value instanceof DateTimeHolder){
            return Objects.equals(this.dateValue, ((DateTimeHolder) value).getValue());
        }
        return false;
    }

    @Override
    public boolean lte(Value value) {
        if (value instanceof DateTimeHolder){
            return dateValue.before( ((DateTimeHolder) value).dateValue);
        }
        return false;
    }

    @Override
    public boolean gte(Value value) {
        if (value instanceof DateTimeHolder){
            return dateValue.after( ((DateTimeHolder) value).dateValue);
        }
        return false;
    }

    @Override
    public boolean neq(Value value) {
        if (value instanceof DateTimeHolder){
            return !Objects.equals(this.dateValue, ((DateTimeHolder) value).getValue());
        }
        return false;
    }

    @Override
    public DateTimeHolder create(String s) throws Exception {
        if(s.matches("\\d{4}-\\d{2}-\\d{2}")){
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            try{
                dateValue = format.parse(s);
            }
            catch (ParseException e){
                e.printStackTrace();
                return null;
            }
            return this;

        }
        throw new CustomException("Wrong format of date change it to: yyyy-mm-dd");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DateTimeHolder)) return false;
        DateTimeHolder that = (DateTimeHolder) o;
        return Objects.equals(dateValue, that.dateValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dateValue);
    }

    @Override
    public int compareTo(Value o) {
        return dateValue.compareTo(((DateTimeHolder)o).getValue());
    }
}
