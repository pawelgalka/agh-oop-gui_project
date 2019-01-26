package dataframe.tests;

import dataframe.*;

public class MultiplyTest {
    public static void main(String[] args) {
        DataFrame data1Frame = new DataFrame(new String[]{"a"},new Class[]{Integer.class});
        DataFrame dataFrame = new DataFrame("/home/pawelgalka/IdeaProjects/java/src/dataframe/dane.csv",new Class[]{StringHolder.class, IntHolder.class, DoubleHolder.class});
        try{
            dataFrame.add(new StringHolder("%"),"a","b");
            dataFrame.print();
            dataFrame.mul(new IntHolder(5),"a","b");
            dataFrame.print();
            dataFrame.div(new DoubleHolder(4.5),"a","b");
            dataFrame.print();
            dataFrame.addColumn(dataFrame.get("a"),"a");
            dataFrame.print();
            Column n = new Column("abc", Value.class);
            n.addElement(new IntHolder(2));
            n.addElement(new IntHolder(4));
            n.addElement(new StringHolder("b"));
            n.addElement(new IntHolder(2));
//            n.addElement(new IntHolder(4));
            dataFrame.addColumn(n,"b");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
