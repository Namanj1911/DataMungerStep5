package com.stackroute.datamunger.query.parser;

import java.util.ArrayList;
import java.util.List;

/*
 * This class will contain the elements of the parsed Query String such as conditions,
 * logical operators,aggregate functions, file name, fields group by fields, order by
 * fields, Query Type
 * */
public class QueryParameter {

    private String queryString;
    private String fileName;
    private String baseQuery;
    private List<String> fields = new ArrayList<>();
    private String QUERY_TYPE;
    private List<Restriction> restrictions = new ArrayList<Restriction>();
    private List<String> logicalOperators = new ArrayList<>();
    private List<AggregateFunction> aggregateFunctions = new ArrayList<>();
    private List<String> orderByFields = new ArrayList<>();
    private List<String> groupByFields = new ArrayList<>();

    public void setBaseQuery(String baseQuery) {
        this.baseQuery = baseQuery;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    public void setFileName(String file) {
        this.fileName = file;
    }

    public void setFields(List<String> fields) {
        this.fields = fields;
    }

    public void setQUERY_TYPE(String QUERY_TYPE) {
        this.QUERY_TYPE = QUERY_TYPE;
    }

    public void setRestrictions(List<Restriction> restrictions) {
        this.restrictions = restrictions;
    }

    public void setLogicalOperators(List<String> logicalOperators) {
        this.logicalOperators = logicalOperators;
    }

    public void setAggregateFunctions(List<AggregateFunction> aggregateFunctions) {
        this.aggregateFunctions = aggregateFunctions;
    }

    public void setOrderByFields(List<String> orderByFields) {
        this.orderByFields = orderByFields;
    }

    public void setGroupByFields(List<String> groupByFields) {
        this.groupByFields = groupByFields;
    }

    public String getFileName() {
        return this.fileName;
    }

    public String getQUERY_TYPE() {
        return this.QUERY_TYPE;
    }

    public String getBaseQuery() {
        return this.baseQuery;
    }

    public List<Restriction> getRestrictions() {
        return this.restrictions;
    }

    public List<String> getLogicalOperators() {
        return this.logicalOperators;
    }

    public List<String> getFields() {
        return this.fields;
    }

    public String getQueryString() {
        return this.queryString;
    }

    public List<AggregateFunction> getAggregateFunctions() {
        return this.aggregateFunctions;
    }

    public List<String> getGroupByFields() {
        return this.groupByFields;
    }

    public List<String> getOrderByFields() {
        return this.orderByFields;
    }
}
