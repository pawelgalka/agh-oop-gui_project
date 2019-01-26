package dataframe;

import java.io.*;
import java.security.InvalidParameterException;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class DataFrame implements Serializable{

    public ArrayList<Column> dataframe;
    public String[] columns;
    public Class<? extends Value>[] types;

    public ArrayList<Column> getDataframe() {
        return dataframe;
    }

    public String[] getColumns() {
        return columns;
    }

    public Class<? extends Value>[] getTypes() {
        return types;
    }

    public DataFrame() {
    }

    /* void initiateCols(Class<? extends Value>[] types) throws CustomException{
         for (int i=0; i<columns.length; i++){
             if (!Value.class.isAssignableFrom(types[i])) throw new CustomException("Invalid type of column");
             if (types[i] == IntHolder.class){
                 dataframe.add(new Column(columns[i], types[i], new ArrayList<IntHolder>()));
             }
             if (types[i] == DoubleHolder.class){
                 dataframe.add(new Column(columns[i], types[i], new ArrayList<DoubleHolder>()));
             }
             if (types[i] == FloatHolder.class){
                 dataframe.add(new Column(columns[i], types[i], new ArrayList<FloatHolder>()));
             }
             if (types[i] == StringHolder.class){
                 dataframe.add(new Column(columns[i], types[i], new ArrayList<StringHolder>()));
             }
             if (types[i] == ValBoolean.class){
                 dataframe.add(new Column(columns[i], types[i], new ArrayList<ValBoolean>()));
             }
             if (types[i] == DateTimeHolder.class){
                 dataframe.add(new Column(columns[i], types[i], new ArrayList<DateTimeHolder>()));
             }
         }
     }*/
    public DataFrame(String[] namesOfColumns, Class<? extends Value>[] typesOfColumns) {
        if (namesOfColumns.length != typesOfColumns.length) {
            throw new InvalidParameterException("Invalid length");
        }
        columns = namesOfColumns;
        types = typesOfColumns;
        dataframe = new ArrayList<>();
        for (int i = 0; i < namesOfColumns.length; ++i) {
            try {
                if (Value.class.isAssignableFrom(types[i])) {
                    dataframe.add(new Column(namesOfColumns[i], typesOfColumns[i]));
                } else throw new InvalidClassException("Class unassignable from Value class");
            } catch (InvalidClassException e) {
                e.printStackTrace();
            }
            ;
        }
        /*try {
            initiateCols(types);
        }
        catch (CustomException e){e.printStackTrace();}*/
    }

    public DataFrame(Column[] kolumny) {
        dataframe = new ArrayList<>();
        columns = new String[kolumny.length];
        types = new Class[kolumny.length];
        for (int i = 0; i < kolumny.length; ++i) {
            columns[i] = kolumny[i].getName();
            types[i] = kolumny[i].getType();
            dataframe.add(kolumny[i]);
            if (dataframe.get(i).getArrayList().size() != dataframe.get(0).getArrayList().size())
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
                add(str);
            }

            br.close();
            fstream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public DataFrame(String filename, Class<? extends Value>[] typesOfColumns) {
        this(filename, typesOfColumns, true);
    }

    void add(String [] content) throws NumberFormatException, CustomException{
        ArrayList <Value> values = new ArrayList<>();
        if(content.length > columns.length){
            throw new CustomException("Too many arguments to add");
        }
        if(content.length < columns.length){
            throw new CustomException("Too few arguments to add");
        }
        try {
            for (int columnIterator = 0; columnIterator < columns.length; columnIterator++) {
                values.add(Value.builder(types[columnIterator]).build(content[columnIterator]));
            }
        }
        catch(Exception e){
            e.printStackTrace();
            values.add(null);
        }

        add(values);
    }

    public void add(List<Value> values){
        if (values.size() != dataframe.size()) {
            throw new RuntimeException("Invalid length of input!!!");
        }
        int iterator = 0;
        for (Column col : dataframe) {
            col.addElement(values.get(iterator++));
        }
    }
    public DataFrame(String filename, Class<? extends Value>[] typesOfColumns, boolean header) {
        try {
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
            if (columns.length != types.length)
                throw new InvalidParameterException("Passed invalid data: columns number of file differ from passed column types");
            for (int i = 0; i < typesOfColumns.length; ++i) {
                dataframe.add(new Column(columns[i], types[i]));
            }

            String strLine;
            Value[] values = new Value[dataframe.size()];
            Value.ValueBuilder[] builders = new Value.ValueBuilder[dataframe.size()];


            while ((strLine = br.readLine()) != null) {
                String[] str = strLine.split(",");
                add(str);
            }

            br.close();
            fstream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void add(Value[] values) {
        if (values.length != dataframe.size()) {
            throw new RuntimeException("Invalid length of input!!!");
        }
        int iterator = 0;
        for (Column col : dataframe) {
            col.addElement(values[iterator++]);
        }
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (String string : columns) {
            stringBuilder.append(string + " ");
        }
        stringBuilder.append("\n");
        for (int i = 0; i < size(); i++) {
            for (Column col : dataframe) {
                stringBuilder.append(col.getArrayList().get(i) + " ");
            }
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

    public void print() {
        for (String string : columns) {
            System.out.print(string + " ");
        }
        System.out.println();
        for (int i = 0; i < size(); i++) {
            for (Column col : dataframe) {
                System.out.print(col.getArrayList().get(i) + " ");
            }
            System.out.println();
        }
    }

    public int size() {
        return this.dataframe.get(0).size();
    }

    public Column get(String colname) {
        return dataframe.stream().filter(c -> c.getName().equals(colname)).findFirst().orElse(null);
    }

    public Value[] getRecord(int index) {
        Value[] tab = new Value[this.dataframe.size()];
        int it = 0;
        for (Column column : dataframe) {
            tab[it++] = column.getArrayList().get(index);
        }
        return tab;
    }

    public DataFrame get(String[] cols, boolean copy) {
        Column[] tab = new Column[cols.length];
        for (int i = 0; i < tab.length; ++i) {
            if (!copy) { //shallow copy
                tab[i] = this.get(cols[i]);
            } else { //deep copy
                tab[i] = new Column(get(cols[i]));
            }
        }
        //for (Column column:tab) System.out.println(column.getName()+column.getType());
        DataFrame dataFrame = new DataFrame(tab);
        return dataFrame;

    }

    public DataFrame iloc(int i) throws Exception {
        return iloc(i, i);

    }

    public DataFrame iloc(int from, int to) throws Exception {
        if (from < 0 || from >= size())
            throw new IndexOutOfBoundsException("No such index: " + from);

        else if (to < 0)
            throw new IndexOutOfBoundsException("No such index: " + to);

        else if (to>=size()) to = size()-1;
        else if (to < from)
            throw new IndexOutOfBoundsException("unable to create range from " + from + " to " + to);

        DataFrame nowydf = new DataFrame(columns, types);
        Value[] tab = new Value[dataframe.size()];
        for (int i = from; i <= to; ++i) {
            for (int j = 0; j < tab.length; j++) {
                tab[j] = dataframe.get(j).getArrayList().get(i);
            }
            nowydf.add(tab);

        }
        return nowydf;
    }

    private class GroupByThread implements Runnable{
        List<Column> columns;
        int from,to;
        Map<List<Value>, DataFrame> map;
        GroupByThread(List<Column> columns_, int from_, int to_, Map<List<Value>, DataFrame> map_){
            columns = columns_;
            from = from_;
            to = to_;
            map = map_;
        }
        @Override
        public void run() {
            for (int i=from; i<=to; i++){

                List<Value> values = new ArrayList<>(columns.size());

                for (Column column : columns) {
                    values.add(column.getArrayList().get(i));
                }

                if (!map.containsKey(values)) {
                    try{
                        synchronized (map){
                            map.put(values, iloc(i));
                        }
                    } catch (Exception e ) {
                        e.printStackTrace();
                    }

                } else {
                    synchronized (map){
                        map.get(values).add(getRecord(i));}
                }
            }
        }
    }

    public GroupByDataFrame groupbyparallel(String ... colnames) throws Exception{
//        HashMap<List<Value>, DataFrame> map = new HashMap<>(colnames.length);
        Map<List<Value>, DataFrame> map = new ConcurrentHashMap<>(colnames.length);

        List<Column> columns1 = Arrays.stream(colnames).map(this::get).collect(Collectors.toList());
        ArrayList<Integer> indexes = GetIndexesOfColumns(colnames);
        Thread[] arrayOfThreads = new Thread[n];
        ExecutorService executorService = Executors.newFixedThreadPool(n);
        int counter = 0;
        for (int i=0; i<size(); i=i+size()/n+1){
//            System.out.println(i+size()/10);
//            arrayOfThreads[counter] = new Thread(new GroupByThread(columns1,i,(i+size()/10>size()) ? size()-1 : i+size()/10,map));
//            arrayOfThreads[counter++].start();
            executorService.execute(new Thread(new GroupByThread(columns1,i,(i+size()/n>size()) ? size()-1 : i+size()/n,map)));
        }
        /*for (Thread t:arrayOfThreads){
            t.join();
        }*/
        awaitTerminationAfterShutdown(executorService);
//        executorService.shutdownNow();
        return new GroupByDataFrame(new LinkedList<DataFrame>(map.values()), this.columns, types, indexes);
    }

    public GroupByDataFrame groupby(String... colnames) throws Exception {
        HashMap<List<Value>, DataFrame> map = new HashMap<>(colnames.length);
        List<Column> columns1 = Arrays.stream(colnames).map(this::get).collect(Collectors.toList());
        ArrayList<Integer> indexes = GetIndexesOfColumns(colnames);
        for (int i = 0; i < size(); i++) {
            List<Value> values = new ArrayList<>(columns1.size());
            for (Column column : columns1) {
                values.add(column.getArrayList().get(i));
            }

            if (!map.containsKey(values)) {
                map.put(values, iloc(i));
            } else {
                map.get(values).add(getRecord(i));
            }
        }
        return new GroupByDataFrame(new LinkedList<DataFrame>(map.values()), this.columns, types, indexes);
    }
    public void awaitTerminationAfterShutdown(ExecutorService threadPool) {
        threadPool.shutdown();
        try {
            if (!threadPool.awaitTermination(60, TimeUnit.SECONDS)) {
                threadPool.shutdownNow();
            }
        } catch (InterruptedException ex) {
            threadPool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    public void add(Value value, String... columns) throws CustomException {
        ArrayList<Integer> indexes = GetIndexesOfColumns(columns);
        for (int index : indexes) {
            try {
                dataframe.get(index).add(value);
            } catch (CustomException e) {
                e.printStackTrace();
            }
        }
    }

    public void mul(Value value, String... columns) throws CustomException {
        ArrayList<Integer> indexes = GetIndexesOfColumns(columns);
        for (int index : indexes) {
            try {
                dataframe.get(index).mul(value);
            } catch (CustomException e) {
                e.printStackTrace();
            }
        }
    }

    public void div(Value value, String... columns) throws CustomException {
        ArrayList<Integer> indexes = GetIndexesOfColumns(columns);
        for (int index : indexes) {
            try {
                dataframe.get(index).div(value);
            } catch (CustomException e) {
                e.printStackTrace();
            }
        }
    }

    public void addColumn(Column column, String... whereToAdd) {
        try {
            ArrayList<Integer> indexes = GetIndexesOfColumns(whereToAdd);
            for (Integer v : indexes) System.out.println(v);
            for (int index : indexes) {

                dataframe.get(index).add(column);
            }
        } catch (InvalidColumnSizeException | CustomException e) {
            e.printStackTrace();
        }
    }

    public void mulColumn(Column column, String... whereToAdd) {
        try {
            ArrayList<Integer> indexes = GetIndexesOfColumns(whereToAdd);
            for (int index : indexes) {
                dataframe.get(index).mul(column);
            }
        } catch (InvalidColumnSizeException | CustomException e) {
            e.printStackTrace();
        }
    }

    public void divColumn(Column column, String... whereToAdd) {
        try {
            ArrayList<Integer> indexes = GetIndexesOfColumns(whereToAdd);
            for (int index : indexes) {
                dataframe.get(index).div(column);
            }
        } catch (InvalidColumnSizeException | CustomException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Integer> GetIndexesOfColumns(String... colnames) throws CustomException {
        ArrayList<Integer> indexes = new ArrayList<>();
        int index = 0;
        for (String str : colnames) {
            boolean found = false;
            for (int i = 0; i < columns.length; ++i) {
                if (Objects.equals(str, columns[i])) {
                    indexes.add(index++, i);
                    found = true;
                }
            }
            if (!found) throw new CustomException("Invalid column name");
        }
        return indexes;
    }


    private DataFrame getDataFrameOfCertainValue(DataFrame df, Value v, int indexOfColumn) throws Exception {
        DataFrame dataFrame = new DataFrame(columns, types);
        int indexOfRow = 0;
        for (Value value : df.dataframe.get(indexOfColumn).getArrayList()) {
            if (value.equals(v)) {
                dataFrame.add(df.getRecord(indexOfRow));
            }
            indexOfRow++;
        }
        return dataFrame;
    }
    int n = 32;

    public class GroupByDataFrame implements GroupBy,Serializable{
        LinkedList<DataFrame> groupDataFrameList;
        String[] columns;
        Class<? extends Value>[] types;
        ArrayList<Integer> groupedCols;
        ArrayList<String> groupedColsNames = new ArrayList<>();
        ExecutorService threads = Executors.newFixedThreadPool(n);
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

        public GroupByDataFrame(){}
        public GroupByDataFrame(LinkedList<DataFrame> linkedList, String[] colnames, Class<? extends Value>[] coltypes, ArrayList<Integer> groupedCols) {
            this.groupDataFrameList = linkedList;
            this.columns = colnames;
            this.types = coltypes;
            this.groupedCols = groupedCols;
            for (int i : groupedCols) groupedColsNames.add(columns[i]);
        }

        public GroupByDataFrame(String[] cols, Class[] types, ArrayList<Integer> groupedCols) {
            groupDataFrameList = new LinkedList<>();
            columns = cols;
            this.types = types;
            this.groupedCols = groupedCols;

        }

        public void addDF(DataFrame df) {
            groupDataFrameList.add(df);
        }

        class ThreadDF{
            DataFrame workable, out;

            ThreadDF(DataFrame current, DataFrame output) {
                workable = current;
                out = output;
            }
        }
        class MaxThread extends ThreadDF implements Runnable {
            MaxThread(DataFrame current, DataFrame out){super(current,out);};
            @Override
            public void run() {
                int index = 0;
                int currentCol = 0;

                Value[] currentRow = new Value[columns.length];
                for (Column column : workable.dataframe) {
                    if (groupedCols.contains(currentCol++)) {
                        currentRow[index++] = column.getArrayList().get(0);
                    } else {
                        currentRow[index++] = Collections.max(column.getArrayList());
                    }
                }
                synchronized (out){
                    out.add(currentRow.clone());
                }
            }
        }

        class MinThread extends ThreadDF implements Runnable {
            MinThread(DataFrame current, DataFrame out){super(current,out);};
            @Override
            public void run() {
//                System.out.println("wejscie do wątku "+workable.getDataframe().get(1).getArrayList().get(0));
                int index = 0;
                int currentCol = 0;

                Value[] currentRow = new Value[columns.length];
                for (Column column : workable.dataframe) {
                    if (groupedCols.contains(currentCol++)) {
                        currentRow[index++] = column.getArrayList().get(0);
                    } else {
                        currentRow[index++] = Collections.min(column.getArrayList());
                    }
                }
                synchronized (out){
                    out.add(currentRow.clone());
                }
//                System.out.println("wyjscie do wątku "+workable.getDataframe().get(1).getArrayList().get(0));
            }
        }

        class MeanThread extends ThreadDF implements Runnable {
            MeanThread(DataFrame current, DataFrame out){super(current,out);};
            MeanThread(DataFrame current, DataFrame out, Value[][] a,int i){super(current,out); currentDf=i;}
            int currentDf;
            @Override
            public void run() {
                try{
//                    System.out.println("mean1");
                    int index = 0;
                    int currentCol = 0;

                    Value[] currentRow = new Value[out.dataframe.size()];

                    for (Column column : workable.dataframe) {
                        if (groupedCols.contains(currentCol++)) currentRow[index++] = column.getArrayList().get(0);
                        else if (column.getType() == StringHolder.class || column.getType() == DateTimeHolder.class)
                            continue;
                        else {
                            Value sum = column.getArrayList().get(0);
//                            System.out.println(sum+ " "+column.getArrayList().get(0));

                            for (int i = 1; i < column.size(); ++i) {
//                                synchronized (sum){
                                sum = sum.add(column.getArrayList().get(i));
//                                System.out.println(sum+ " "+column.getArrayList().get(i));
                            }
                            Value mean = sum.div(new IntHolder(column.size()));
//                            System.out.println(sum);
                            currentRow[index++] = mean;
                        }
                    }

                    synchronized (out){
                        out.add(currentRow.clone());
                    }

                }
                catch (CustomException e){e.printStackTrace();}
//                System.out.println("mean2");
            }
        }

        class SumThread extends ThreadDF implements Runnable {
            SumThread(DataFrame current, DataFrame out,Value.ValueBuilder[] valueBuilders){super(current,out);builders = valueBuilders.clone();};
            Value.ValueBuilder[] builders;
            @Override
            public void run() {
                try{
                    int index = 0;
                    int currentCol = 0;

                    Value[] currentRow = new Value[out.dataframe.size()];
                    for (Column column : workable.dataframe) {
                        if (groupedCols.contains(currentCol++)) currentRow[index++] = column.getArrayList().get(0);
                        else if (column.getType() == StringHolder.class || column.getType() == DateTimeHolder.class) continue;
                        else {
                            Value sum = column.getArrayList().get(0);

                            for (int i = 1; i < column.size(); ++i) {
                                sum = sum.add(column.getArrayList().get(i));
                            }
                            currentRow[index++] = sum;
                        }
                    }
                    synchronized (out){
                        out.add(currentRow.clone());}
                }
                catch (CustomException e){e.printStackTrace();}
            }
        }

        class StdThread extends ThreadDF implements Runnable {
            int currentDf ;
            Value.ValueBuilder[] builders;
            IntHolder exp;
            DataFrame mean;
            StdThread(DataFrame current, DataFrame out, int currentDf, Value.ValueBuilder[] builders, IntHolder exp, DataFrame mean){super(current,out); this.currentDf = currentDf; this.builders = builders.clone(); this.exp = exp; this.mean = mean;};
            @Override
            public void run() {
                try{
//                    System.out.println(currentDf);
//                    System.out.println("wejscie do wątku "+workable.getDataframe().get(1).getArrayList().get(0));

                    int index = 0;
                    int currentCol = 0;
                    Value[] currentRow = new Value[out.dataframe.size()];
                    for (Column column : workable.dataframe) {
                        if (groupedCols.contains(currentCol)) {
                            currentRow[index++] = column.getArrayList().get(0);
                        } else if (column.getType() == StringHolder.class || column.getType() == DateTimeHolder.class) {
                            currentCol++;
                            continue;
                        } else {
//                        try {

                            Value currentmean = mean.getRecord(currentDf)[index];

                            Value sum = builders[index].build((column.getArrayList().get(0).sub(currentmean)).pow(exp).toString());
                            for (int i = 1; i < column.size(); ++i) {
                                sum = sum.add((column.getArrayList().get(i).sub(currentmean)).pow(exp));
                            }
                            currentRow[index] = builders[index++].build(sum.div(new IntHolder(column.size())).pow(new DoubleHolder(0.5)).toString());

//                        } catch (CustomException custom){custom.printStackTrace();
                        }
                        currentCol++;
                    }
                    //System.out.println(currentRow);
                    synchronized (out){
                        out.add(currentRow.clone());
//                   out.print();
                        //System.out.println(out.size());
                    }}
                catch (CustomException e) {e.printStackTrace();}
//                System.out.println("wyjscie z wątku "+workable.getDataframe().get(1).getArrayList().get(0));

            }
        }

        class VarThread extends ThreadDF implements Runnable {
            int currentDf ;
            IntHolder exp;
            DataFrame std;
            VarThread(DataFrame current, DataFrame out, int currentDf, IntHolder exp, DataFrame mean){super(current,out); this.currentDf = currentDf; this.exp = exp; this.std = mean;};
            @Override
            public void run() {
                try{
                    int index = 0;
                    int currentCol = 0;
                    Value[] currentRow = new Value[out.dataframe.size()];
                    for (Column column : workable.dataframe) {
                        if (groupedCols.contains(currentCol)) currentRow[index++] = column.getArrayList().get(0);
                        else if (column.getType() == StringHolder.class || column.getType() == DateTimeHolder.class) {
                            currentCol++;
                            continue;
                        } else {
                            Value currentstd = std.getRecord(currentDf)[index];
                            currentRow[index++] = currentstd.pow(exp);
                        }
                        currentCol++;
                    }
                    synchronized (out){
                        out.add(currentRow.clone());
                    }}
                catch (CustomException e) {e.printStackTrace();}
            }
        }


        public DataFrame maxParallel2() {
            DataFrame output = new DataFrame(columns, types);;
            List<Callable<List<Value>>> callables = new ArrayList<>();
            /*for (DataFrame df : groupDataFrameList) {
                new Thread(new MaxThread(df,output)).start();
            }*/
            Thread[] array = new Thread[groupDataFrameList.size()];
            int currentDf = 0;
            for (DataFrame df: groupDataFrameList){
                callables.add(()->{
                    List<Value> toAdd = new ArrayList<>();
//                    int index = 0;
                    int currentCol = 0;

                    for (Column column : df.dataframe) {
                        if (groupedCols.contains(currentCol++)) {
                            toAdd.add(column.getArrayList().get(0));
                        } else {
                            Value max = column.getArrayList().get(0);
                            for (Iterator<Value> iterator =  column.getArrayList().iterator(); iterator.hasNext();){
                                Value current = iterator.next();
                                if (current.gte(max))
                                    max = current;
                            }
                            toAdd.add(max);
//                            toAdd.add(Collections.max(column.getArrayList()));
                        }

                    }
                    return toAdd;
                });
            }
            List<List<Value>> aggregateDataFrameValues = new ArrayList<>();
            List<Future<List<Value>>> futureValues;
            try {
                futureValues = threads.invokeAll(callables);
                for (Future<List<Value>> value : futureValues) {
                    aggregateDataFrameValues.add(value.get());
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            threads.shutdown();
            for (List<Value> result : aggregateDataFrameValues) {
                output.add(result);
            }
           /* for (DataFrame df : groupDataFrameList) {
                // f(df,output,currentDf,builders,exp,mean);
                array[currentDf++] = new Thread(new MaxThread(df,output));
                array[currentDf-1].start();
            }
            for(Thread t:array){
                try {
                    t.join();
                }
                catch (InterruptedException e ){e.printStackTrace();}
            }
            /*for (DataFrame df : groupDataFrameList) {
                new Thread(new MeanThread(df,output)).start();}*/
            /*for (DataFrame df: groupDataFrameList){
                threads.execute(new Thread(new MaxThread(df,output)));
            }
            threads.shutdownNow();*/
            return output;
            //return output;
        }
        public DataFrame maxParallel() {
            DataFrame output = new DataFrame(columns, types);;
            /*for (DataFrame df : groupDataFrameList) {
                new Thread(new MaxThread(df,output)).start();
            }*/
            Thread[] array = new Thread[groupDataFrameList.size()];
            int currentDf = 0;
           /* for (DataFrame df : groupDataFrameList) {
                // f(df,output,currentDf,builders,exp,mean);
                array[currentDf++] = new Thread(new MaxThread(df,output));
                array[currentDf-1].start();
            }
            for(Thread t:array){
                try {
                    t.join();
                }
                catch (InterruptedException e ){e.printStackTrace();}
            }
            /*for (DataFrame df : groupDataFrameList) {
                new Thread(new MeanThread(df,output)).start();}*/
            for (DataFrame df: groupDataFrameList){
                threads.execute(new Thread(new MaxThread(df,output)));
            }
            awaitTerminationAfterShutdown(threads);
//           threads.shutdownNow();
            return output;
            //return output;
        }
        public DataFrame maxParallel1() {
            DataFrame output = new DataFrame(columns, types);;
            Thread[] array = new Thread[groupDataFrameList.size()];
            int currentDf = 0;
            for (DataFrame df : groupDataFrameList) {
                array[currentDf++] = new Thread(new MaxThread(df,output));
                array[currentDf-1].start();
            }
            for(Thread t:array){
                try {
                    t.join();
                }
                catch (InterruptedException e ){e.printStackTrace();}
            }
            return output;
            //return output;
        }

        public DataFrame minParallel() {
            DataFrame output = new DataFrame(columns, types);;
            /*for (DataFrame df : groupDataFrameList) {
                new Thread(new MinThread(df,output)).start();
            }*/
            Thread[] array = new Thread[groupDataFrameList.size()];
            int currentDf = 0;
            for (DataFrame df : groupDataFrameList) {
                // f(df,output,currentDf,builders,exp,mean);
                threads.execute(new Thread(new MinThread(df, output)));
            }
//            threads.shutdownNow();
            awaitTerminationAfterShutdown(threads);
            return output;
            //return output;
        }
        public DataFrame minParallel1() {
            DataFrame output = new DataFrame(columns, types);;
            /*for (DataFrame df : groupDataFrameList) {
                new Thread(new MinThread(df,output)).start();
            }*/
            Thread[] array = new Thread[groupDataFrameList.size()];
            int currentDf = 0;
            for (DataFrame df : groupDataFrameList) {
                // f(df,output,currentDf,builders,exp,mean);
                array[currentDf++] = new Thread(new MinThread(df,output));
                array[currentDf-1].start();
            }
            for(Thread t:array){
                try {
                    t.join();
                }
                catch (InterruptedException e ){e.printStackTrace();}
            }
            /*for (DataFrame df : groupDataFrameList) {
                new Thread(new MeanThread(df,output)).start();}*/
            return output;
            //return output;
        }
        public DataFrame meanParallel1() {
            DataFrame output = CreateDataFrameOfSpecifiedIndexes();
            Thread[] array = new Thread[groupDataFrameList.size()];
            int currentDf = 0;
            Value[][] array1 = new Value[groupDataFrameList.size()][];
            for (DataFrame df : groupDataFrameList) {
                // f(df,output,currentDf,builders,exp,mean);
                array[currentDf] = new Thread(new MeanThread(df,output,array1,currentDf));
                array[currentDf++].start();

            }

            for (int i=0; i<array.length; i++){
                try {
                    array[i].join();

                } catch (Exception e){}
//                output.add(array1[i]);
            }
            return output;
        }
        public DataFrame meanParallel() {
            DataFrame output = CreateDataFrameOfSpecifiedIndexes();
            Thread[] array = new Thread[groupDataFrameList.size()];
            int currentDf = 0;
            Value[][] array1 = new Value[groupDataFrameList.size()][];
//            List<Callable<>> taskList = new ArrayList<>();
            for (DataFrame df : groupDataFrameList) {
                threads.execute(new Thread(new MeanThread(df, output, array1, currentDf)));
            }
//            threads.shutdownNow();
            awaitTerminationAfterShutdown(threads);
           /* try {

                while (!threads.awaitTermination(24L, TimeUnit.HOURS)) {
                    System.out.println("Not yet. Still waiting for termination");
                }
            } catch (InterruptedException e){e.printStackTrace();}*/

            return output;
        }

        public DataFrame sumParallel() {
            DataFrame output = CreateDataFrameOfSpecifiedIndexes();
            threads = Executors.newCachedThreadPool();
            Value.ValueBuilder[] builders = new Value.ValueBuilder[output.dataframe.size()];
            for (int i = 0; i < builders.length; i++) builders[i] = Value.builder(output.types[i]);
            for (DataFrame df : groupDataFrameList) {
                threads.execute(new Thread(new SumThread(df,output,builders)));
            }
//            threads.shutdownNow();
            awaitTerminationAfterShutdown(threads);
            return output;
            //return output;
        }
        public  DataFrame sumParallel1() {
            DataFrame output = CreateDataFrameOfSpecifiedIndexes();
            Value.ValueBuilder[] builders = new Value.ValueBuilder[output.dataframe.size()];
            for (int i = 0; i < builders.length; i++) builders[i] = Value.builder(output.types[i]);
            Thread[] array = new Thread[groupDataFrameList.size()];
            int currentDf = 0;
            for (DataFrame df : groupDataFrameList) {
                // f(df,output,currentDf,builders,exp,mean);
                array[currentDf++] = new Thread(new SumThread(df,output,builders));
                array[currentDf-1].start();
            }
            for(Thread t:array){
                try {
                    t.join();
                }
                catch (InterruptedException e ){e.printStackTrace();}
            }
            return output;

        }

        public  DataFrame stdParallel() {
            DataFrame output = CreateDataFrameOfSpecifiedIndexes();
            DataFrame mean = meanParallel();
            threads = Executors.newFixedThreadPool(n*2);
            Value.ValueBuilder[] builders = new Value.ValueBuilder[output.dataframe.size()];
            for (int i = 0; i < builders.length; i++) builders[i] = Value.builder(output.types[i]);
            int currentDf = 0;
            IntHolder exp = new IntHolder(2);
            for (DataFrame df : groupDataFrameList) {
                threads.execute(new Thread(new StdThread(df,output,currentDf++,builders,exp,mean)) );
            }
            awaitTerminationAfterShutdown(threads);
            return output;
        }

        public  DataFrame stdParallel1() {
            DataFrame output = CreateDataFrameOfSpecifiedIndexes();

            DataFrame mean = meanParallel1();

            Value.ValueBuilder[] builders = new Value.ValueBuilder[output.dataframe.size()];
            for (int i = 0; i < builders.length; i++) builders[i] = Value.builder(output.types[i]);
            Thread[] array = new Thread[groupDataFrameList.size()];
            int currentDf = 0;
            IntHolder exp = new IntHolder(2);
            for (DataFrame df : groupDataFrameList) {
                // f(df,output,currentDf,builders,exp,mean);
                array[currentDf] = new Thread(new StdThread(df,output,currentDf++,builders,exp,mean));
                array[currentDf-1].start();
            }
            for(Thread t:array){
                try {
                    t.join();
                }
                catch (InterruptedException e ){e.printStackTrace();}
            }
            return output;
        }
        public DataFrame varParallel(){
            DataFrame output = CreateDataFrameOfSpecifiedIndexes();
            DataFrame std = stdParallel();
            threads = Executors.newFixedThreadPool(n);
            IntHolder exp = new IntHolder(2);
            int currentDf = 0;
            for (DataFrame df : groupDataFrameList) {
                threads.execute(new Thread(new VarThread(df,output,currentDf++,exp,std)));
            }
            threads.shutdownNow();
            return output;
        }

        public DataFrame varParallel1(){
            DataFrame output = CreateDataFrameOfSpecifiedIndexes();
            DataFrame std = stdParallel1();
            IntHolder exp = new IntHolder(2);
            int currentDf = 0;
            /*for (DataFrame df : groupDataFrameList) {
                new Thread(new VarThread(df,output,currentDf++,exp,std)).start();
            }*/
            Thread[] array = new Thread[groupDataFrameList.size()];

            for (DataFrame df : groupDataFrameList) {
                // f(df,output,currentDf,builders,exp,mean);
                array[currentDf] = new Thread(new VarThread(df,output,currentDf++,exp,std));
                array[currentDf-1].start();
            }
            for(Thread t:array){
                try {
                    t.join();
                }
                catch (InterruptedException e ){e.printStackTrace();}
            }
            return output;
        }
        /* no threads */
        @Override
        public DataFrame max() {
            DataFrame output = new DataFrame(columns, types);

            for (DataFrame dataFrame : groupDataFrameList) {
                int index = 0;
                int currentCol = 0;

                Value[] currentRow = new Value[columns.length];
                for (Column column : dataFrame.dataframe) {
                    Column column1 = column;
                    if (groupedCols.contains(currentCol++)) {
                        currentRow[index++] = column.getArrayList().get(0);
                    } else {
                        currentRow[index++] = Collections.max(column1.getArrayList());
                    }
                }
                output.add(currentRow.clone());

            }
            return output;
        }

        @Override
        public DataFrame min() {
            DataFrame output = new DataFrame(columns, types);
            for (DataFrame dataFrame : groupDataFrameList) {
                int currentCol = 0;
                int index = 0;
                Value[] currentRow = new Value[columns.length];
                for (Column column : dataFrame.dataframe) {
                    if (groupedCols.contains(currentCol++)) {
                        currentRow[index++] = column.getArrayList().get(0);
                    } else {
                        currentRow[index++] = Collections.min(column.getArrayList());
                    }
                }
                output.add(currentRow.clone());
            }
            return output;
        }

        @Override
        public DataFrame mean() {
            DataFrame output = CreateDataFrameOfSpecifiedIndexes();

            Value.ValueBuilder[] builders = new Value.ValueBuilder[output.dataframe.size()];
            for (int i = 0; i < builders.length; i++) builders[i] = Value.builder(output.types[i]);
            for (DataFrame dataFrame : groupDataFrameList) {
                try {

                    int index = 0;
                    int currentCol = 0;

                    Value[] currentRow = new Value[output.dataframe.size()];

                    for (Column column : dataFrame.dataframe) {
                        if (groupedCols.contains(currentCol++)) currentRow[index++] = column.getArrayList().get(0);
                        else if (column.getType() == StringHolder.class || column.getType() == DateTimeHolder.class)
                            continue;
                        else {
                            Value sum = column.getArrayList().get(0);
//                            System.out.println(sum+ " "+column.getArrayList().get(0));
                            for (int i = 1; i < column.size(); ++i) {
                                sum = sum.add(column.getArrayList().get(i));
                            }
                            Value mean = sum.div(new IntHolder(column.size()));
//                        System.out.println(sum);
                            currentRow[index++] = mean;
                        }
                    }
                    output.add(currentRow.clone());
                }catch (CustomException e){e.printStackTrace();}
            }

            return output;
        }

        @Override
        public DataFrame sum() throws CustomException {
            DataFrame output = CreateDataFrameOfSpecifiedIndexes();

            /// Value.ValueBuilder[] builders = new Value.ValueBuilder[output.dataframe.size()];
            // for (int i = 0; i < builders.length; i++) builders[i] = Value.builder(output.types[i]);

            for (DataFrame dataFrame : groupDataFrameList) {
                int index = 0;
                int currentCol = 0;

                Value[] currentRow = new Value[output.dataframe.size()];
                for (Column column : dataFrame.dataframe) {
                    if (groupedCols.contains(currentCol++)) currentRow[index++] = column.getArrayList().get(0);
                    else if (column.getType() == StringHolder.class || column.getType() == DateTimeHolder.class) continue;
                    else {
                        Value sum = column.getArrayList().get(0);

                        for (int i = 1; i < column.size(); ++i) {
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
            Value.ValueBuilder[] builders = new Value.ValueBuilder[output.dataframe.size()];
            for (int i = 0; i < builders.length; i++) builders[i] = Value.builder(output.types[i]);

            int currentDf = 0;
            IntHolder exp = new IntHolder(2);
            for (DataFrame dataFrame : groupDataFrameList) {
//                System.out.println("wejscie do wątku "+dataFrame.getDataframe().get(1).getArrayList().get(0));

                int index = 0;
                int currentCol = 0;
                Value[] currentRow = new Value[output.dataframe.size()];
                for (Column column : dataFrame.dataframe) {
                    if (groupedCols.contains(currentCol)) {
                        currentRow[index++] = column.getArrayList().get(0);
                    } else if (column.getType() == StringHolder.class || column.getType() == DateTimeHolder.class) {
                        currentCol++;
                        continue;
                    } else {

                        Value currentmean = mean.getRecord(currentDf)[index];
                        Value sum = builders[index].build((column.getArrayList().get(0).sub(currentmean)).pow(exp).toString());
                        for (int i = 1; i < column.size(); ++i) {
//                            System.out.println(dataFrame.get("id").getArrayList().get(i)+ " " + sum +" "+(column.getArrayList().get(i).sub(currentmean)).pow(exp));
                            sum = sum.add((column.getArrayList().get(i).sub(currentmean)).pow(exp));
                        }
//                        System.out.println(sum);
                        currentRow[index] = builders[index++].build(sum.div(new IntHolder(column.size())).pow(new DoubleHolder(0.5)).toString());

                    }
                    currentCol++;
                }
                output.add(currentRow.clone());
                currentDf++;
//                System.out.println("wyjscie z wątku "+dataFrame.getDataframe().get(1).getArrayList().get(0));

            }
            return output;
        }


        @Override
        public DataFrame var() throws CustomException {
            DataFrame output = CreateDataFrameOfSpecifiedIndexes();
            DataFrame std = std();
            IntHolder exp = new IntHolder(2);
            int currentDf = 0;
            for (DataFrame dataFrame : groupDataFrameList) {
                int index = 0;
                int currentCol = 0;
                Value[] currentRow = new Value[output.dataframe.size()];
                for (Column column : dataFrame.dataframe) {
                    if (groupedCols.contains(currentCol)) currentRow[index++] = column.getArrayList().get(0);
                    else if (column.getType() == StringHolder.class || column.getType() == DateTimeHolder.class) {
                        currentCol++;
                        continue;
                    } else {
                        Value currentstd = std.getRecord(currentDf)[index];
                        currentRow[index++] = currentstd.pow(exp);
                    }
                    currentCol++;
                }
                output.add(currentRow.clone());
                currentDf++;
            }
            return output;
        }

        @Override
        public DataFrame apply(Applyable a) throws CustomException {
            DataFrame output = new DataFrame(columns, types);
            for (DataFrame dataFrame : groupDataFrameList) {
                DataFrame current = a.apply(dataFrame);
                for (int i = 0; i < current.size(); ++i)
                    output.add(current.getRecord(i));
            }
            return output;
        }


        /**
         * @return dataframe of columns that were being grouped and columns that are not string and datetime
         */
        private DataFrame CreateDataFrameOfSpecifiedIndexes() {
            ArrayList<Integer> validIndexesOfColumns = new ArrayList<>(groupedCols); //valid columns
            int currentIndexOfCol = 0;
            for (Class<? extends Value> type : types) {
                if (type != StringHolder.class && type != DateTimeHolder.class) validIndexesOfColumns.add(currentIndexOfCol);
                currentIndexOfCol++;
            }
            Collections.sort(validIndexesOfColumns);
            String[] cols = new String[validIndexesOfColumns.size()];
            Class<? extends Value>[] typs = new Class[validIndexesOfColumns.size()];
            for (int i = 0; i < validIndexesOfColumns.size(); i++) {
                cols[i] = columns[validIndexesOfColumns.get(i)];
                typs[i] = types[validIndexesOfColumns.get(i)];
            }

            return new DataFrame(cols, typs);
        }
    }

}
