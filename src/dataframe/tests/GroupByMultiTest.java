package dataframe.tests;


import dataframe.DataFrame;
import dataframe.ValDateTime;
import dataframe.ValDouble;
import dataframe.ValString;

public class GroupByMultiTest {
    public static void main(String[] args) throws Exception{
        DataFrame dataFrame = new DataFrame("/home/pawelgalka/IdeaProjects/java/src/dataframe/groubymulti.csv",new Class[]{ValString.class, ValDateTime.class, ValDouble.class, ValDouble.class});

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
