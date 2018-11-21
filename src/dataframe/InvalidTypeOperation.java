package dataframe;

public class InvalidTypeOperation extends CustomException {
    InvalidTypeOperation(String msg, int row, String colname){
        super(msg+" column: "+colname+" row: "+row);
    }
}
