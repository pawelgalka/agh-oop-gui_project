package dataframe;

import java.util.ArrayList;

public class Column {
    private String name;
    private Class<? extends Value> type;
    private ArrayList<Value> arrayList;

    public Column(String name, Class<? extends Value> clazz) {
        arrayList = new ArrayList<>();
        this.name = name;
        this.type = clazz;
    }

    public Column(Column kol){
        this.name = kol.getName();
        this.type = kol.getType();
        this.arrayList = kol.getArrayList();
    }

    public void addElement(Value element) {
        if(type.isInstance(element)) {
            arrayList.add(element);
        }
        else {
            throw new IllegalArgumentException("Invalid type");
        }
    }

    Value getCOOElement(int index, Value hide){
        for(int i=0; i<arrayList.size(); ++i){
            COOValue value = (COOValue)arrayList.get(i);
            if (value.getIndex()==index){
                return value.getValue();
            }
        }
        return hide;
    }

    void add(Value value) throws CustomException {
        for (int i=0; i<arrayList.size(); ++i){
            try {
                arrayList.set(i, arrayList.get(i).add(value));
            } catch (CustomException e){
                throw new InvalidTypeOperation(e.getMessage(),i,getName());
            }
        }
    }

    void mul(Value value) throws CustomException {
        for (int i=0; i<arrayList.size(); ++i){
            try {
                arrayList.set(i, arrayList.get(i).mul(value));
            } catch (CustomException e){
                throw new InvalidTypeOperation(e.getMessage(),i,getName());
            }
        }
    }

    void div(Value value) throws CustomException {
        for (int i=0; i<arrayList.size(); ++i){
            try {
                arrayList.set(i, arrayList.get(i).div(value));
            } catch (CustomException e){
                throw new InvalidTypeOperation(e.getMessage(),i,getName());
            }
        }
    }

    void add(Column column) throws InvalidColumnSizeException, CustomException {
        if (column.size()!=size()) throw new InvalidColumnSizeException(getName());
        for (int i=0; i<size(); ++i){
            try {
                arrayList.set(i,arrayList.get(i).add(column.getArrayList().get(i)));
            } catch (CustomException e){
                throw new InvalidTypeOperation(e.getMessage(),i,getName());
            }
        }
    }

    void mul(Column column) throws InvalidColumnSizeException, CustomException {
        if (column.size()!=size()) throw new InvalidColumnSizeException(getName());
        for (int i=0; i<size(); ++i){
            try {
                arrayList.set(i,arrayList.get(i).mul(column.getArrayList().get(i)));
            } catch (CustomException e){
                throw new InvalidTypeOperation(e.getMessage(),i,getName());
            }
        }
    }

    void div(Column column) throws InvalidColumnSizeException, CustomException {
        if (column.size()!=size()) throw new InvalidColumnSizeException(getName());
        for (int i=0; i<size(); ++i){
            try {
                arrayList.set(i,arrayList.get(i).div(column.getArrayList().get(i)));
            } catch (CustomException e){
                throw new InvalidTypeOperation(e.getMessage(),i,getName());
            }
        }
    }


    int size(){
        return arrayList.size();
    }

    String getName(){
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    Class<? extends Value> getType(){
        return type;
    }

    public ArrayList<Value> getArrayList() {
        return arrayList;
    }

    String print(){
        return "name: "+name+" type: "+type+" content: "+arrayList;
    }
}
