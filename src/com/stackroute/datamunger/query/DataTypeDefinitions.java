package com.stackroute.datamunger.query;

import java.util.Date;

/*
 * Implementation of DataTypeDefinitions class. This class contains a method getDataTypes()
 * which will contain the logic for getting the datatype for a given field value. This
 * method will be called from QueryProcessors.
 * In this assignment, we are going to use Regular Expression to find the
 * appropriate data type of a field.
 * Integers: should contain only digits without decimal point
 * Double: should contain digits as well as decimal point
 * Date: Dates can be written in many formats in the CSV file.
 * However, in this assignment,we will test for the following date formats('dd/mm/yyyy',
 * 'mm/dd/yyyy','dd-mon-yy','dd-mon-yyyy','dd-month-yy','dd-month-yyyy','yyyy-mm-dd')
 */
public class DataTypeDefinitions {

    //method stub
    public static Object getDataTypes(String input) {

        if (input.matches("^[0-9]+$")) {
            return Integer.class.toString().split(" ")[1];
        }
        if (input.matches("[a-zA-Z_ ]+")) {
            return String.class.toString().split(" ")[1];
        }
        if (input.matches("[0-9]+\\.[0-9]+")) {
            return Double.class.toString().split(" ")[1];
        }
        if (input.matches("\\d{4}-\\d\\d-\\d\\d")) {
            return Date.class.toString().split(" ")[1];
        }
        if (input.matches("((\\d\\d/\\d\\d/\\d{4}) | (\\d\\d-[A-Za-z]{3}-\\d\\d) | (\\d\\d-[A-Za-z]{3}-\\d{4}) | (\\d{4}-\\d\\d-\\d\\d) | (\\d\\d-([A-Za-z]+)-\\d\\d) | (\\d\\d-([A-Za-z]+)-\\d{4}))")) {
            return Date.class.toString().split(" ")[1];
        }
        return null;
    }
}

