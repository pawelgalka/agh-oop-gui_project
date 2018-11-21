package dataframe.tests;

import dataframe.DataFrame;
import dataframe.ValDateTime;
import dataframe.ValDouble;
import dataframe.ValString;

public class GroupBySingleTest {
    public static void main(String[] args) throws Exception {
        DataFrame dataFrame = new DataFrame("/home/pawelgalka/IdeaProjects/java/src/dataframe/dane.csv",new Class[]{ValString.class, ValDateTime.class, ValDouble.class, ValDouble.class});
        /*HashMap<List<Value>,DataFrame> map = dataFrame.groupby("id");
        for (var obj:map.values()){
            obj.print();
        }*/
        /*DataFrame groupby = dataFrame.groupby(new String[]{"date"}).max();
        DataFrame groupby1 = dataFrame.groupby(new String[]{"id"}).min();
        DataFrame groupby2 = dataFrame.groupby(new String[]{"id"}).mean();
        DataFrame groupby3 = dataFrame.groupby(new String[]{"id"}).sum();*/
        DataFrame.GroupByDataFrame groupby4 = dataFrame.groupby(new String[]{"date"});
        DataFrame groupby5 = dataFrame.groupby(new String[]{"date"}).var();

    /*    System.out.println("-----max-----");
        groupby.print();
        System.out.println("-----min-----");
        groupby1.print();
        System.out.println("-----mean-----");
        groupby2.print();
        System.out.println("-----sum-----");
        groupby3.print();*/
        System.out.println("------std----");
        for (DataFrame df:groupby4.getGroupDataFrameList()){
            df.print();
        }
/*
        System.out.println("------var----");
        groupby5.print();
*/

    }
}
