package dataframe;

public interface GroupBy {
    DataFrame max();
    DataFrame min();
    DataFrame mean() throws CustomException;
    DataFrame std() throws CustomException;
    DataFrame sum() throws CustomException;
    DataFrame var() throws CustomException;
    DataFrame apply(Applyable a) throws CustomException;

}
