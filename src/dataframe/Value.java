package dataframe;

import java.lang.reflect.InvocationTargetException;

public abstract class Value implements Cloneable,Comparable<Value>{
    public abstract String toString();
    public abstract Value add(Value value) throws CustomException;
    public abstract Value sub(Value value) throws CustomException;
    public abstract Value mul(Value value) throws CustomException;
    public abstract Value div(Value value) throws CustomException;
    public abstract Value pow(Value value) throws CustomException;
    public abstract boolean eq(Value value);
    public abstract boolean lte(Value value);
    public abstract boolean gte(Value value);
    public abstract boolean neq(Value value);
    public abstract boolean equals(Object other);
    public abstract int hashCode();
    public abstract Value create(String s) throws Exception;
    public abstract Object getValue();


    public abstract int compareTo(Value o);
    public static ValueBuilder builder(Class<? extends Value> c) {
        return new ValueBuilder(c);
    }
    public static class ValueBuilder {
        Class<? extends Value> typ;

        ValueBuilder(Class<? extends Value> c) {
            typ = c;
        }

        public Value build(String data) {
            try {
                return (Value) typ.getMethod("create", String.class).invoke(typ.newInstance(), data);
            } catch (IllegalAccessException | InvocationTargetException | InstantiationException | NoSuchMethodException e) {
                e.printStackTrace();
            }
            throw new RuntimeException();
        }
    }
}
