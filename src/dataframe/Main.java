package dataframe;

public class Main {
    public static void main(String[] args) throws Exception{
        DataFrame dataFrame = new DataFrame("/home/pawelgalka/IdeaProjects/java/src/dataframe/dane.csv",new Class[]{StringHolder.class, DoubleHolder.class, DoubleHolder.class});
       /* DataFrame gr = dataFrame.groupby(new String[]{"id"}).apply(new Mediana());
        gr.print();*/

        /*Class[] arg = new Class[2];
        arg[0]=IntHolder.class;
*/      /*DoubleHolder hide = new DoubleHolder(0.0);
        System.out.println(new DoubleHolder(0.0).eq(hide));*/
       // System.out.println(Integer.valueOf("0.2"));
        //System.out.println(Math.pow((-0.4524634484707701-0.000313),2));
       /* long time = System.currentTimeMillis();
//        DataFrame dataFrame = new DataFrame("/home/pawelgalka/IdeaProjects/java/src/dataframe/data.csv",new Class[]{DoubleHolder.class,DoubleHolder.class,DoubleHolder.class});
        System.out.println(System.currentTimeMillis()-time);
//        System.out.println(dataFrame.size());


        /*DataFrame.GroupByDataFrame df = dataFrame.groupby(new String[]{"id","date"});
        df.groupDataFrameList.get(0).print();*/
     /*ArrayList<Value> values = new ArrayList<>();
        Value a = DateTimeHolder.getInstance().create("2258-11-01");
        Value b = DateTimeHolder.getInstance().create("2258-10-19");
        Value c = DateTimeHolder.getInstance().create("2258-10-31");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = dateFormat.parse("2258-11-01");
        Date date1 =  dateFormat.parse("2258-10-31");
        System.out.println(date.after(date1));*/
        /*System.out.println(((DateTimeHolder) a).getValue().before(((DateTimeHolder) c).getValue()));
        values.add(a);
        values.add(b);
        values.add(c);
        System.out.println(Collections.max(values));
        //long startTime = System.currentTimeMillis();
        SparseDataFrame dataFrame = new SparseDataFrame("/home/pawelgalka/IdeaProjects/java/src/dataframe/sparse.csv",new Class[]{DoubleHolder.class,DoubleHolder.class,DoubleHolder.class},new DoubleHolder(0.0));
        long s1 = System.currentTimeMillis();
        dataFrame.print();
        dataFrame.toDense().print();
        long stopTime = System.currentTimeMillis();
        System.out.println(s1-startTime);
        System.out.println(stopTime - startTime);
       /* DataFrame dataFrame2 = sparseDataFrame.toDense();
        dataFrame2.print();*/
      //  System.out.println(.getClass().isAssignableFrom(Value.class));
        /*IntHolder.class type = dataFrame.dataframe.get(1).getType();
        IntHolder valInteger = Class.forName(type.getName());*/
        //dataFrame.add(new Value[]{new IntHolder(1), new IntHolder(11)});
        //DataFrame dataFrame = new DataFrame()
    }
}
