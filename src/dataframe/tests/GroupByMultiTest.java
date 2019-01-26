package dataframe.tests;


import dataframe.DataFrame;
import dataframe.DateTimeHolder;
import dataframe.DoubleHolder;
import dataframe.StringHolder;

public class GroupByMultiTest {
    public static void main(String[] args) throws Exception{
        DataFrame dataFrame = new DataFrame("/home/pawelgalka/IdeaProjects/java/src/dataframe/groubymulti.csv",new Class[]{StringHolder.class, DateTimeHolder.class, DoubleHolder.class, DoubleHolder.class});

        DataFrame groupbymax = dataFrame.groupby(new String[]{"date"}).max();
        groupbymax.print();
        System.out.println();
        System.out.println();
        System.out.println();
        DataFrame groupbymin = dataFrame.groupby(new String[]{"id","date"}).min();
        groupbymin.print();
        System.out.println();
        System.out.println();
        System.out.println();
        DataFrame groupbysum = dataFrame.groupby(new String[]{"id","date"}).sum();
        groupbysum.print();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        DataFrame groupbymean = dataFrame.groupby(new String[]{"id","date"}).mean();
        groupbymean.print();
        System.out.println();
        System.out.println();
        System.out.println();
        DataFrame groupbystd = dataFrame.groupby(new String[]{"id","date"}).std();
        groupbystd.print();
        System.out.println();
        System.out.println();
        System.out.println();
        DataFrame groupbyvar = dataFrame.groupby(new String[]{"id","date"}).var();
        groupbyvar.print();


        System.out.println();
    }

}
