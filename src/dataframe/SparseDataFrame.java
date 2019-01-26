package dataframe;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Scanner;

public class SparseDataFrame extends DataFrame {
    private Value hideval;
    private int size;


    public SparseDataFrame(String[] namesOfColumns, Class<? extends Value>[] typesOfColumns, Value hide) throws Exception{
        super(namesOfColumns, typesOfColumns);
        hideval = hide;
    }

    public SparseDataFrame(DataFrame dataFrame, Value hide) throws Exception{
        super(dataFrame.columns,dataFrame.types);
        hideval=hide;
        for (int i=0; i<dataFrame.size(); ++i){
            add(dataFrame.getRecord(i));
        }

    }

    public SparseDataFrame(String filename, Class<? extends Value>[] types, Value hide) throws Exception{
        this(filename,types,true,hide);
    }

    public SparseDataFrame(String filename, Class<? extends Value>[] types, boolean header, Value hide) throws Exception{
        super(new String[types.length],types);
        columns = new String[types.length];
        hideval = hide;

        FileInputStream fstream = new FileInputStream(filename);
        BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

        if (header) {
            columns = br.readLine().split(",");
        }
        else {
            System.out.println("Enter names of columns");
            Scanner scanner = new Scanner(System.in);
            for (int i = 0; i < columns.length; ++i) {
                columns[i] = scanner.next();
            }
        }

        for (int i=0; i<dataframe.size(); ++i){
            dataframe.get(i).setName(columns[i]);
        }

        String strLine;

        Value[] values = new Value[dataframe.size()];
        Value.ValueBuilder[] builders = new Value.ValueBuilder[dataframe.size()];
        for (int i = 0; i < builders.length; i++) {
            builders[i] = Value.builder(types[i]);
           // System.out.println(builders[i]);
        }
        while ((strLine = br.readLine()) != null) {
            String[] str = strLine.split(",");

            for (int i = 0; i < str.length; i++) {
               /*if (types[i] == IntHolder.class){
                    values[i] = IntHolder.getInstance().create(str[i]);
                }
                if (types[i] == DoubleHolder.class){
                    values[i] = DoubleHolder.getInstance().create(str[i]);
                }
                if (types[i] == ValBoolean.class){
                    values[i] = ValBoolean.getInstance().create(str[i]);
                }
                if (types[i] == FloatHolder.class){
                    values[i] = FloatHolder.getInstance().create(str[i]);
                }
                if (types[i] == StringHolder.class){
                    values[i] = StringHolder.getInstance().create(str[i]);
                }
                if (types[i] == DateTimeHolder.class){
                    values[i] = DateTimeHolder.getInstance().create(str[i]);
                }*/
                values[i] = builders[i].build(str[i]);

            }
            add(values.clone());
        }

        br.close();

    }

    public DataFrame toDense() throws Exception{
        DataFrame dataFrame = new DataFrame(columns,types);
        Value[] tab = new Value[columns.length];
        for (int i=0; i<size; ++i){
            int index =0;
            for (Column column:dataframe){
                /*for (Value object:column.getArrayList()){
                    if (((COOValue)object).getIndex()==i){
                        tab[index] = ((COOValue) object).getValue();
                        break;
                    }
                    tab[index]=hideval;

                }
                index++;*/
                tab[index++] = column.getCOOElement(i,hideval);
            }
            dataFrame.add(tab.clone());
        }
        return dataFrame;
    }

    public void addElement(Column column, int index, Value value){
        column.getArrayList().add(new COOValue(value,index));
    }

    public void add(Value[] values){
        if (values.length!=dataframe.size()){
            throw new RuntimeException("Invalid length of input!!!");
        }
        int iterator = 0;
        for (Column col:dataframe){
            if(!hideval.eq(values[iterator]))
                addElement(col,size,values[iterator]);

            iterator++;
        }
        size++;
    }

    public int size() {
        return size;
    }

    public void print() {

        for (Column column:dataframe){
            System.out.println(column.getName());
            for (Value cooValue:column.getArrayList()){
                System.out.println(cooValue.toString());
            }
            System.out.println("-----------");
        }
    }
}
