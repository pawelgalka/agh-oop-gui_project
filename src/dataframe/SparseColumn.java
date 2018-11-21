package dataframe;

import java.util.ArrayList;

public class SparseColumn extends Column {

    private ArrayList<COOValue> cooValueArrayList;

    public SparseColumn(String name, Class<? extends Value> type){
        super(name,type);
        cooValueArrayList = new ArrayList<>();
    }

    public SparseColumn(SparseColumn kol) {
        super(kol.getName(),kol.getType());
        cooValueArrayList = kol.getCooArrayList();
    }

    public COOValue getElement(int index){
        return cooValueArrayList.get(index);
    }

    @Override
    public void addElement(Value element) {
        super.addElement(element);
    }

    @Override
    int size() {
        return cooValueArrayList.size();
    }

    public ArrayList<COOValue> getCooArrayList() {
        return cooValueArrayList;
    }

    @Override
    String print() {
        return "name: "+getName()+" type: "+getType()+" content: "+cooValueArrayList;
    }
}
