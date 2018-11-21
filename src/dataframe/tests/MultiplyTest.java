package dataframe.tests;

import dataframe.*;

public class MultiplyTest {
    public static void main(String[] args) {
        DataFrame data1Frame = new DataFrame(new String[]{"a"},new Class[]{Integer.class});
        DataFrame dataFrame = new DataFrame("/home/pawelgalka/IdeaProjects/java/src/dataframe/dane.csv",new Class[]{ValString.class, ValInteger.class, ValDouble.class});
        try{
            dataFrame.add(new ValString("%"),"a","b");
            dataFrame.print();
            dataFrame.mul(new ValInteger(5),"a","b");
            dataFrame.print();
            dataFrame.div(new ValDouble(4.5),"a","b");
            dataFrame.print();
            dataFrame.addColumn(dataFrame.get("a"),"a");
            dataFrame.print();
            Column n = new Column("abc", Value.class);
            n.addElement(new ValInteger(2));
            n.addElement(new ValInteger(4));
            n.addElement(new ValString("b"));
            n.addElement(new ValInteger(2));
//            n.addElement(new ValInteger(4));
            dataFrame.addColumn(n,"b");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
