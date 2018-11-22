package dataframe;

import java.io.*;
import java.security.InvalidParameterException;
import java.util.*;
import java.util.stream.Collectors;

public class DataFrame {

    ArrayList<Column> dataframe;
    String[] columns;
    Class<? extends Value>[] types;

    public ArrayList<Column> getDataframe() {
        return dataframe;
    }

    public String[] getColumns() {
        return columns;
    }

    public Class<? extends Value>[] getTypes() {
        return types;
    }

    public DataFrame(String[] namesOfColumns, Class<? extends Value>[] typesOfColumns){
        if (namesOfColumns.length!=typesOfColumns.length){
            throw new InvalidParameterException("Invalid length");
        }
        columns = namesOfColumns;
        types = typesOfColumns;
        dataframe = new ArrayList<>();
        for (int i=0; i<namesOfColumns.length; ++i){
            try{
                if (Value.class.isAssignableFrom(types[i])) {
                    dataframe.add(new Column(namesOfColumns[i], typesOfColumns[i]));
                }
                else throw new InvalidClassException("Class unassignable from Value class");
            }
            catch (InvalidClassException e){e.printStackTrace();};
        }
    }

    public DataFrame(Column[] kolumny){
        dataframe = new ArrayList<>();
        columns=new String[kolumny.length];
        types = new Class[kolumny.length];
        for (int i=0; i<kolumny.length; ++i) {
            columns[i]=kolumny[i].getName();
            types[i]=kolumny[i].getType();
            dataframe.add(kolumny[i]);
            if (dataframe.get(i).getArrayList().size()!=dataframe.get(0).getArrayList().size())
                throw new InvalidParameterException("Invalid length of column");
        }
    }
    public DataFrame(String filename, Class<? extends Value>[] typesOfColumns, String[] colnames) {
        try {
            FileInputStream fstream = new FileInputStream(filename);
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

            dataframe = new ArrayList<>();
            columns = new String[typesOfColumns.length];
            types = typesOfColumns;
            for (int i = 0; i < columns.length; ++i) {
                columns[i] = colnames[i];
            }
            if (columns.length != types.length)
                throw new InvalidParameterException("Passed invalid data: columns number of file differ from passed column types");
            for (int i = 0; i < typesOfColumns.length; ++i) {
                dataframe.add(new Column(columns[i], types[i]));
            }

            String strLine;
            Value[] values = new Value[dataframe.size()];
            Value.ValueBuilder[] builders = new Value.ValueBuilder[dataframe.size()];
            for (int i = 0; i < builders.length; i++) {
                builders[i] = Value.builder(types[i]);
            }

            br.readLine();
            while ((strLine = br.readLine()) != null) {
                String[] str = strLine.split(",");
                for (int i = 0; i < str.length; i++) {
                    System.out.println(str[i]);
                    if (types[i] == ValInteger.class){
                        values[i] = ValInteger.getInstance().create(str[i]);
                    }
                    if (types[i] == ValDouble.class){
                        values[i] = ValDouble.getInstance().create(str[i]);
                    }
                    if (types[i] == ValBoolean.class){
                        values[i] = ValBoolean.getInstance().create(str[i]);
                    }
                    if (types[i] == ValFloat.class){
                        values[i] = ValFloat.getInstance().create(str[i]);
                    }
                    if (types[i] == ValString.class){
                        values[i] = ValString.getInstance().create(str[i]);
                    }
                    if (types[i] == ValDateTime.class){
                        values[i] = ValDateTime.getInstance().create(str[i]);
                    }
                }
                add(values.clone());
            }

            br.close();
            fstream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    public DataFrame(String filename, Class<? extends Value>[] typesOfColumns){
        this(filename,typesOfColumns,true);
    }

    public DataFrame(String filename, Class<? extends Value>[] typesOfColumns, boolean header) {
        try{
            FileInputStream fstream = new FileInputStream(filename);
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

            dataframe = new ArrayList<>();
            columns = new String[typesOfColumns.length];
            types = typesOfColumns;
            if (header) {
                columns = br.readLine().split(",");
            } else {
                System.out.println("Enter names of columns");
                Scanner scanner = new Scanner(System.in);
                for (int i = 0; i < columns.length; ++i) {
                    columns[i] = scanner.next();
                }
            }
            if (columns.length!=types.length) throw new InvalidParameterException("Passed invalid data: columns number of file differ from passed column types");
            for (int i = 0; i < typesOfColumns.length; ++i) {
                dataframe.add(new Column(columns[i], types[i]));
            }

            String strLine;
            Value[] values = new Value[dataframe.size()];
            Value.ValueBuilder[] builders = new Value.ValueBuilder[dataframe.size()];
            for (int i = 0; i < builders.length; i++) {
                builders[i] = Value.builder(types[i]);
            }


            while ((strLine = br.readLine()) != null) {
                String[] str = strLine.split(",");
                for (int i = 0; i < str.length; i++) {
                    values[i] = builders[i].build(str[i]);
                }
                add(values.clone());
            }

            br.close();
            fstream.close();
        } catch (IOException e){e.printStackTrace();}


    }

    public void add(Value[] values){
        if (values.length!=dataframe.size()){
            throw new RuntimeException("Invalid length of input!!!");
        }
        int iterator = 0;
        for (Column col:dataframe){
            col.addElement(values[iterator++]);
        }
    }

    public void print(){
        for (String string:columns){
            System.out.print(string+" ");
        }
        System.out.println();
        for (int i=0; i<size(); i++){
            for (Column col:dataframe){
                System.out.print(col.getArrayList().get(i)+" ");
            }
            System.out.println();
        }
    }

    public int size(){
        return this.dataframe.get(0).size();
    }

    public Column get(String colname){
        /*for (Column col:dataframe){
            if (Objects.equals(col.getName(),colname)){
                return col;
            }
        }
        throw new RuntimeException("Column not found!");*/
        return dataframe.stream().filter(c -> c.getName().equals(colname)).findFirst().orElse(null);
    }

    public Value[] getRecord(int index){
        Value[] tab = new Value[this.dataframe.size()];
        int it=0;
        for (Column column:dataframe){
            tab[it++]=column.getArrayList().get(index);
        }
        return tab;
    }

    public DataFrame get(String [] cols, boolean copy){
        Column[] tab = new Column[cols.length];
        for (int i=0; i<tab.length; ++i){
            if (!copy){ //shallow copy
                tab[i] = this.get(cols[i]);
            }
            else{ //deep copy
                tab[i] = new Column(get(cols[i]));
            }
        }
        //for (Column column:tab) System.out.println(column.getName()+column.getType());
        DataFrame dataFrame = new DataFrame(tab);
        return dataFrame;

    }

    public DataFrame iloc(int i) throws Exception{
        return iloc(i,i);

    }

    public DataFrame iloc(int from, int to) throws Exception{
        if(from<0 || from>=size())
            throw new IndexOutOfBoundsException("No such index: "+from);

        else if(to<0 || to>=size())
            throw new IndexOutOfBoundsException("No such index: "+to);

        else if(to<from)
            throw new IndexOutOfBoundsException("unable to create range from "+from+" to "+to);

        DataFrame nowydf = new DataFrame(columns,types);
        Value[] tab = new Value[dataframe.size()];
        for (int i=from; i<=to; ++i){
            for (int j=0; j<tab.length; j++){
                tab[j] = dataframe.get(j).getArrayList().get(i);
            }
            nowydf.add(tab);

        }
        return nowydf;
    }

    /*public DataFrame multiplyByValue(Value factor, String colid) throws Exception{
        boolean found = false;
        int colIndex = 0;
        for (int i=0; i<columns.length; ++i){
            if (Objects.equals(colid,columns[i])) {found = true; colIndex = i;}
        }
        if (!found) throw new Exception("Invalid column name");

        for (int i=0; i<dataframe.get(colIndex).getArrayList().size(); ++i){
            Value tmp = dataframe.get(colIndex).getArrayList().get(i).mul(factor);
            dataframe.get(colIndex).getArrayList().remove(i);
            dataframe.get(colIndex).getArrayList().add(i,tmp);
        }
//        System.out.println(dataframe.get(colIndex).getArrayList().get(0));
        return this;
    }*/

    public GroupByDataFrame groupby(String... colnames) throws Exception{
        HashMap<List<Value>, DataFrame> map = new HashMap<>(colnames.length);
        List<Column> columns1 = Arrays.stream(colnames).map(this::get).collect(Collectors.toList());
        ArrayList<Integer> indexes = GetIndexesOfColumns(colnames);

        //for(var col:columns1) System.out.println(col.getName());
        for (int i = 0; i < size(); i++) {
            List<Value> values = new ArrayList<>(columns1.size());

            for (Column column: columns1) {
                values.add(column.getArrayList().get(i));
            }

            if(!map.containsKey(values)) {
                map.put(values, iloc(i));
            } else {
                map.get(values).add(getRecord(i));
            }
        }
        return new GroupByDataFrame(new LinkedList<DataFrame>(map.values()),this.columns,types,indexes);
    }


    public void add(Value value, String ... columns) throws CustomException {
        ArrayList<Integer> indexes= GetIndexesOfColumns(columns);
        for (int index:indexes){
            try {
                dataframe.get(index).add(value);
            }
            catch (CustomException e){
                e.printStackTrace();
            }
        }
    }

    public void mul(Value value, String ... columns) throws CustomException {
        ArrayList<Integer> indexes= GetIndexesOfColumns(columns);
        for (int index:indexes){
            try {
                dataframe.get(index).mul(value);
            }
            catch (CustomException e){
                e.printStackTrace();
            }
        }
    }

    public void div(Value value, String ... columns) throws CustomException {
        ArrayList<Integer> indexes= GetIndexesOfColumns(columns);
        for (int index:indexes){
            try {
                dataframe.get(index).div(value);
            }
            catch (CustomException e){
                e.printStackTrace();
            }
        }
    }

    public void addColumn(Column column, String... whereToAdd){
        try {
            ArrayList<Integer> indexes= GetIndexesOfColumns(whereToAdd);
            for (Integer v:indexes) System.out.println(v);
            for (int index:indexes){

                dataframe.get(index).add(column);
            }
        } catch (InvalidColumnSizeException | CustomException e) {
            e.printStackTrace();
        }
    }

    public void mulColumn(Column column, String... whereToAdd){
        try {
            ArrayList<Integer> indexes= GetIndexesOfColumns(whereToAdd);
            for (int index:indexes){
                dataframe.get(index).mul(column);
            }
        } catch (InvalidColumnSizeException | CustomException e) {
            e.printStackTrace();
        }
    }

    public void divColumn(Column column, String... whereToAdd){
        try {
            ArrayList<Integer> indexes= GetIndexesOfColumns(whereToAdd);
            for (int index:indexes){
                dataframe.get(index).div(column);
            }
        } catch (InvalidColumnSizeException | CustomException e) {
            e.printStackTrace();
        }
    }
    private ArrayList<Integer> GetIndexesOfColumns(String... colnames) throws CustomException {
       // for (var v:columns) System.out.println(v);
        ArrayList<Integer> indexes = new ArrayList<>();
        int index=0;
        for (String str: colnames){
            boolean found = false;
            for (int i=0; i<columns.length; ++i){
//               System.out.println(str+colnames[i]);
                if (Objects.equals(str,columns[i])) {indexes.add(index++,i); found = true;}
            }
            if (!found) throw new CustomException("Invalid column name "+str);
        }
//        for(int i:indexes) System.out.println(i);;

        return indexes;
    }
    /*/**
     *
     * @param colnames - cilumns that we group by
     * @return GroupbyDataFrame linekd list of grouped dataframes
     * @throws Exception //TODO: rethink exceptions
     */
    /*public GroupByDataFrame groupby(String[] colnames) throws Exception{
        if (colnames.length>columns.length) throw new Exception();
        LinkedList<DataFrame> dataFrameLinkedList = new LinkedList<>();
        dataFrameLinkedList.add(this);
        //int[] indexes = new int[colnames.length];
        ArrayList<Integer> indexes = new ArrayList<>();

        //for(var col:columns1) System.out.println(col.getName());
        int index =0;
        for (String str: colnames){
            boolean found = false;
            for (int i=0; i<columns.length; ++i){
                if (Objects.equals(str,columns[i])) {indexes.add(index++,i); found = true;}
            }
            if (!found) throw new Exception("Invalid column name");
        }
        for (int j:indexes){
            LinkedList<DataFrame>result = new LinkedList<>();
            for (DataFrame dataFrame: dataFrameLinkedList){
                Set<Value> UniqueValues = new HashSet<>(dataFrame.dataframe.get(j).getArrayList());
                List<Value> values = new ArrayList<>(UniqueValues);
                Collections.sort(values);
                //for (Value value:listOfUniqueValues) System.out.println(value);
                for (Value value:values){
                    DataFrame current = getDataFrameOfCertainValue(dataFrame,value,j);
                    if (current.size()!=0) result.add(current);
                }

                dataFrameLinkedList = new LinkedList<>(result);
            }
        }
        //for (int i:indexes) System.out.println(i);
        return new GroupByDataFrame(dataFrameLinkedList,columns,types,indexes);

    }*/


    private DataFrame getDataFrameOfCertainValue(DataFrame df, Value v, int indexOfColumn) throws Exception{
        DataFrame dataFrame= new DataFrame(columns,types);
        int indexOfRow=0;
        for (Value value:df.dataframe.get(indexOfColumn).getArrayList()){
            if (value.equals(v)){
                dataFrame.add(df.getRecord(indexOfRow));
            }
            indexOfRow++;
        }
        return dataFrame;
    }

    public class GroupByDataFrame implements GroupBy {
        LinkedList<DataFrame> groupDataFrameList;
        String[] columns;
        Class<? extends Value>[] types;
        ArrayList<Integer> groupedCols;
        ArrayList<String> groupedColsNames = new ArrayList<>();

        public LinkedList<DataFrame> getGroupDataFrameList() {
            return groupDataFrameList;
        }

        public String[] getColumns() {
            return columns;
        }

        public Class<? extends Value>[] getTypes() {
            return types;
        }

        public ArrayList<Integer> getGroupedCols() {
            return groupedCols;
        }

        public ArrayList<String> getGroupedColsNames() {
            return groupedColsNames;
        }

        GroupByDataFrame(LinkedList<DataFrame> linkedList, String[] colnames, Class<? extends Value>[] coltypes, ArrayList<Integer> groupedCols){
            this.groupDataFrameList=linkedList;
            this.columns = colnames;
            this.types = coltypes;
            this.groupedCols = groupedCols;
            for (int i:groupedCols) groupedColsNames.add(columns[i]);
        }

        @Override
        public DataFrame max(){
            DataFrame output = new DataFrame(columns,types);

            for (DataFrame dataFrame: groupDataFrameList){
                int index=0;
                int currentCol =0;

                Value[] currentRow = new Value[columns.length];
                for (Column column:dataFrame.dataframe){
                    if (groupedCols.contains(currentCol++)){
                        currentRow[index++] = column.getArrayList().get(0);
                    }
                    else{
                        currentRow[index++] = Collections.max(column.getArrayList());
                    }
                }
                output.add(currentRow.clone());

            }
            return output;
        }

        @Override
        public DataFrame min(){
            DataFrame output = new DataFrame(columns,types);
            for (DataFrame dataFrame: groupDataFrameList){
                int currentCol =0;
                int index=0;
                Value[] currentRow = new Value[columns.length];
                for (Column column:dataFrame.dataframe){
                    if (groupedCols.contains(currentCol++)){
                        currentRow[index++] = column.getArrayList().get(0);
                    }
                    else{
                        currentRow[index++] = Collections.min(column.getArrayList());
                    }
                }
                output.add(currentRow.clone());
            }
            return output;
        }

        @Override
        public DataFrame mean() throws CustomException {
            DataFrame output = CreateDataFrameOfSpecifiedIndexes();

            Value.ValueBuilder[] builders = new Value.ValueBuilder[output.dataframe.size()];
            for (int i = 0; i < builders.length; i++) builders[i] = Value.builder(output.types[i]);
            for (DataFrame dataFrame: groupDataFrameList){
                int index=0;
                int currentCol =0;

                Value[] currentRow = new Value[output.dataframe.size()];

                for (Column column:dataFrame.dataframe){
                    if (groupedCols.contains(currentCol++)) currentRow[index++] = column.getArrayList().get(0);
                    else if (column.getType()== ValString.class || column.getType() == ValDateTime.class)
                        continue;
                    else {
                        Value sum = column.getArrayList().get(0);
                        for (int i=1 ; i<column.size(); ++i){
                            sum = sum.add(column.getArrayList().get(i));
                        }
                        Value mean = sum.div(new ValInteger(column.size()));
                        currentRow[index++] = mean;
                    }
                }
                output.add(currentRow.clone());
            }

            return output;
        }

        @Override
        public DataFrame sum() throws CustomException {
            DataFrame output = CreateDataFrameOfSpecifiedIndexes();

            Value.ValueBuilder[] builders = new Value.ValueBuilder[output.dataframe.size()];
            for (int i = 0; i < builders.length; i++) builders[i] = Value.builder(output.types[i]);

            for (DataFrame dataFrame: groupDataFrameList){
                int index=0;
                int currentCol =0;

                Value[] currentRow = new Value[output.dataframe.size()];
                for (Column column:dataFrame.dataframe){
                    if (groupedCols.contains(currentCol++)) currentRow[index++] = column.getArrayList().get(0);
                    else if(column.getType()== ValString.class || column.getType() == ValDateTime.class) continue;
                    else {
                        Value sum = column.getArrayList().get(0);
                        for (int i=1 ; i<column.size(); ++i){
                            sum = sum.add(column.getArrayList().get(i));
                        }
                        currentRow[index++] = sum;
                    }
                }
                output.add(currentRow.clone());
            }
            return output;
        }

        @Override
        public DataFrame std() throws CustomException {
            DataFrame output = CreateDataFrameOfSpecifiedIndexes();
            DataFrame mean = mean();
//            mean.print();
//            output.print();
            Value.ValueBuilder[] builders = new Value.ValueBuilder[output.dataframe.size()];
            for (int i = 0; i < builders.length; i++) builders[i] = Value.builder(output.types[i]);

            int currentDf=0;
            ValInteger exp = new ValInteger(2);
            for (DataFrame dataFrame: groupDataFrameList){
                int index=0;
                int currentCol =0;
//                System.out.println(groupedCols.get(0));
                Value[] currentRow = new Value[output.dataframe.size()];
                for (Column column:dataFrame.dataframe){
                    if (groupedCols.contains(currentCol)) {currentRow[index++] = column.getArrayList().get(0);}
                    else if(column.getType()== ValString.class || column.getType() == ValDateTime.class) {currentCol++;continue;
                        }
                    else {
//                        try {
                            Value currentmean = mean.getRecord(currentDf)[index];
//                            System.out.println(currentDf+" "+currentmean+"--------");
                            /*if (currentmean.getClass()==ValDateTime.class || currentmean.getClass()==ValString.class || currentmean.getClass()==ValBoolean.class){
                                currentRow[index++]=null;
                                continue;
                            }*/
                            Value sum = (column.getArrayList().get(0).sub(currentmean)).pow(exp);
                            for (int i = 1; i < column.size(); ++i) {
                                sum = sum.add((column.getArrayList().get(i).sub(currentmean)).pow(exp));
                            }

                            currentRow[index] = builders[index++].build(Double.toString(Math.sqrt((double) sum.div(new ValInteger(column.size() - 1)).getValue())));
//                        } catch (CustomException custom){custom.printStackTrace();
                    }
                    currentCol++;
                }
                output.add(currentRow.clone());
                currentDf++;
            }
            return output;
        }


        @Override
        public DataFrame var() throws CustomException {
            DataFrame output = CreateDataFrameOfSpecifiedIndexes();
            DataFrame std = std();

            Value.ValueBuilder[] builders = new Value.ValueBuilder[output.dataframe.size()];
            for (int i = 0; i < builders.length; i++) builders[i] = Value.builder(output.types[i]);

            ValInteger exp = new ValInteger(2);
            int currentDf=0;
            for (DataFrame dataFrame: groupDataFrameList){
                int index=0;
                int currentCol =0;
                Value[] currentRow = new Value[output.dataframe.size()];
                for (Column column:dataFrame.dataframe){
                    if (groupedCols.contains(currentCol)) currentRow[index++] = column.getArrayList().get(0);
                    else if(column.getType()== ValString.class || column.getType() == ValDateTime.class) {currentCol++;continue;}
                    else {
                        Value currentstd = std.getRecord(currentDf)[index];
                        currentRow[index++] = currentstd.pow(exp);
                    }
                    currentCol++;
                }
//                for (Value v:currentRow) System.out.println(v);
                output.add(currentRow.clone());
                currentDf++;
            }
            return output;
        }

        @Override
        public DataFrame apply(Applyable a) throws CustomException {
            DataFrame output = new DataFrame(columns,types);
            for (DataFrame dataFrame: groupDataFrameList){
                DataFrame current = a.apply(dataFrame);
                for (int i=0; i<current.size(); ++i)
                    output.add(current.getRecord(i));
            }
            return output;
        }

 /*       public DataFrame operation(enum oper){

        }*/
        /**
         *
         * @return dataframe of columns that were being grouped and columns that are not string and datetime
         */
        private DataFrame CreateDataFrameOfSpecifiedIndexes(){
            ArrayList<Integer> validIndexesOfColumns = new ArrayList<>(groupedCols); //valid columns
            int currentIndexOfCol = 0;
            for (Class<? extends Value> type:types){
                if (type != ValString.class && type != ValDateTime.class) validIndexesOfColumns.add(currentIndexOfCol);
                currentIndexOfCol++;
            }
            String[] cols = new String[validIndexesOfColumns.size()];
            Class<? extends Value>[] typs = new Class[validIndexesOfColumns.size()];
            for (int i=0; i<validIndexesOfColumns.size(); i++){
                cols[i] = columns[validIndexesOfColumns.get(i)];
                typs[i] = types[validIndexesOfColumns.get(i)];
            }
            return new DataFrame(cols,typs);

        }
    }
}
