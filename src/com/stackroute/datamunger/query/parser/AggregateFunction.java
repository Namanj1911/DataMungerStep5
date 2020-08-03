package com.stackroute.datamunger.query.parser;

/* This class is used for storing name of field, aggregate function for
 * each aggregate function
 * */
public class AggregateFunction {

    private String field;
    private String function;

    // Write logic for constructor
    public AggregateFunction(String field, String function) {
        this.field = field;
        this.function = function;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    @Override
    public String toString() {
        return "Aggregate Name  : " + function + "\nAggregate Field : " + field;
    }
}
