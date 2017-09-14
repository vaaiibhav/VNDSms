package me.vaaiibhav.www.vndsms;

/**
 * Created by Vaaiibhav on 09-Sep-17.
 */

public class DataHolderClass {
    private static final DataHolderClass ourInstance = new DataHolderClass();
    String SerialNo;
    public static synchronized DataHolderClass getInstance() {
        return ourInstance;
    }

    public String getSerialNo() {
        return SerialNo;
    }

    public void setSerialNo(String serialNo) {
        SerialNo = serialNo;
    }

    DataHolderClass() {
    }
}
