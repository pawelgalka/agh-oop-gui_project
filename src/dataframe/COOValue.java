package dataframe;

public class COOValue extends Value {
    private final int index;
    private Value value;

    public int getIndex() {
        return index;
    }

    public Value getValue() {
        return value;
    }

    public COOValue(Value value, int index){
        this.value = value;
        this.index = index;
    }

    public void add(Object v){
        if(v instanceof Integer){
            value =new ValInteger((Integer) v);
        }
        if(v instanceof Boolean){
            value =new ValBoolean((Boolean) v);
        }
        if(v instanceof Double){
            value =new ValDouble((Double) v);
        }
        if(v instanceof Float){
            value =new ValFloat((Float) v);
        }
        if(v instanceof String){
            value =new ValString((String) v);
        }
    }

    @Override
    public String toString() {
        return "index "+index+" | value "+value;
    }

    @Override
    public Value add(Value value) throws CustomException {
        return this.value.add(value);
    }

    @Override
    public Value sub(Value value)throws CustomException {
        return this.value.sub(value);
    }

    @Override
    public Value mul(Value value)throws CustomException {
        return this.value.mul(value);
    }

    @Override
    public Value div(Value value)throws CustomException {
        return this.value.div(value);
    }

    @Override
    public Value pow(Value value)throws CustomException {
        return this.value.pow(value);
    }

    @Override
    public boolean eq(Value value) {
        return this.value.eq(value);
    }

    @Override
    public boolean lte(Value value) {
        return this.value.lte(value);
    }

    @Override
    public boolean gte(Value value) {
        return this.value.gte(value);
    }

    @Override
    public boolean neq(Value value) {
        return this.value.neq(value);
    }

    @Override
    public boolean equals(Object other) {
        return this.value.equals(value);
    }

    @Override
    public int hashCode() {
        return this.value.hashCode();
    }

    @Override
    public Value create(String s) {
        return new COOValue(this.value.create(s),getIndex());
    }

    @Override
    public int compareTo(Value o) {
        return value.compareTo(o);
    }
}
