package com.stackroute.datamunger.query.parser;

/*There are total 4 DataMungerTest file:
 *
 * 1)DataMungerTestTask1.java file is for testing following 4 methods
 * a)getBaseQuery()  b)getFileName()  c)getOrderByClause()  d)getGroupByFields()
 *
 * Once you implement the above 4 methods,run DataMungerTestTask1.java
 *
 * 2)DataMungerTestTask2.java file is for testing following 2 methods
 * a)getFields() b) getAggregateFunctions()
 *
 * Once you implement the above 2 methods,run DataMungerTestTask2.java
 *
 * 3)DataMungerTestTask3.java file is for testing following 2 methods
 * a)getRestrictions()  b)getLogicalOperators()
 *
 * Once you implement the above 2 methods,run DataMungerTestTask3.java
 *
 * Once you implement all the methods run DataMungerTest.java.This test case consist of all
 * the test cases together.
 */

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QueryParser {
    private QueryParameter queryParameter = new QueryParameter();

    /*
     * This method will parse the queryString and will return the object of
     * QueryParameter class
     */


    static final String where = "where";
    static final String city = "city ";

    public QueryParameter parseQuery(String queryString) {

        queryParameter.setQueryString(queryString);
        queryParameter.setFileName(getFileName(queryString));
        queryParameter.setBaseQuery(getBaseQuery(queryString));

        String[] orderBy = getOrderByFields(queryString);
        if (orderBy != null) {
            List<String> str = new ArrayList<>(Arrays.asList(orderBy));
            queryParameter.setOrderByFields(str);
        }

        String[] str = getGroupByFields(queryString);
        if (str == null) {
            queryParameter.setGroupByFields(null);
        }
        if (str != null) {
            queryParameter.setGroupByFields(Arrays.asList(str));
        }


        String[] fields = getFields(queryString);
        if (fields != null) {
            List<String> str2 = new ArrayList<>();
            Collections.addAll(str2, fields);
            queryParameter.setFields(str2);
        }

        String[] aggregateFunctions = getAggregateFunctions(queryString);
        if (aggregateFunctions != null) {
            List<AggregateFunction> aggregateFunctionList = new ArrayList<>();
            for (String aggregateFunction : aggregateFunctions) {
                int index1 = aggregateFunction.indexOf("(");
                int index2 = aggregateFunction.indexOf(")");
                aggregateFunctionList.add(new AggregateFunction(aggregateFunction.substring(index1 + 1, index2), aggregateFunction.substring(0, index1)));
            }
            queryParameter.setAggregateFunctions(aggregateFunctionList);
        }


        queryParameter.setRestrictions(getConditions(queryString));

        String[] logicalOperators = getLogicalOperators(queryString);
        ArrayList<String> list = new ArrayList<>();
        if (logicalOperators == null) {
            queryParameter.setLogicalOperators(null);
        }
        if (logicalOperators != null) {
            Collections.addAll(list, logicalOperators);
            queryParameter.setLogicalOperators(list);
        }

        return queryParameter;
    }

    /*
     * Extract the name of the file from the query. File name can be found after the
     * "from" clause.
     */

    public String getFileName(String queryString) {
        int fromIndex = queryString.indexOf("from ");
        int indexFileName = fromIndex + 5;
        String subString = queryString.substring(indexFileName);
        String[] fileName = subString.split(" ");
        return fileName[0];
    }

    /*
     *
     * Extract the baseQuery from the query.This method is used to extract the
     * baseQuery from the query string. BaseQuery contains from the beginning of the
     * query till the where clause
     */

    public String getBaseQuery(String queryString) {
        int whereLocation = queryString.indexOf(where);
        int groupLocation = queryString.indexOf("group");
        if (whereLocation > 0) {
            return queryString.substring(0, whereLocation - 1);
        } else if (groupLocation > 0) {
            return queryString.toLowerCase().substring(0, groupLocation - 1);
        } else {
            return queryString;
        }
    }

    /*
     * extract the order by fields from the query string. Please note that we will
     * need to extract the field(s) after "order by" clause in the query, if at all
     * the order by clause exists. For eg: select city,winner,team1,team2 from
     * data/ipl.csv order by city from the query mentioned above, we need to extract
     * "city". Please note that we can have more than one order by fields.
     */

    public String[] getOrderByFields(String queryString) {
        int orderBy = queryString.indexOf("order by");
        if (orderBy > 0) {
            String newString = queryString.substring(orderBy + 9);
            String[] value = new String[1];
            value[0] = newString;
            return value;
        }
        return null;
    }

    /*
     * Extract the group by fields from the query string. Please note that we will
     * need to extract the field(s) after "group by" clause in the query, if at all
     * the group by clause exists. For eg: select city,max(win_by_runs) from
     * data/ipl.csv group by city from the query mentioned above, we need to extract
     * "city". Please note that we can have more than one group by fields.
     */

    public String[] getGroupByFields(String queryString) {
        int indexOfGroup = queryString.indexOf("group by");
        int indexOfOrder = queryString.indexOf("order by");
        if (indexOfGroup > 0 && (indexOfOrder < 0)) {
            String newString = queryString.substring(indexOfGroup + 9);
            String[] value = new String[1];
            value[0] = newString;
            return value;
        } else if (indexOfGroup > 0 && (indexOfOrder > 0)) {
            String newString = queryString.substring(indexOfGroup + 9, indexOfOrder - 1);
            String[] value = new String[1];
            value[0] = newString;
            return value;
        }
        return null;
    }

    /*
     * Extract the selected fields from the query string. Please note that we will
     * need to extract the field(s) after "select" clause followed by a space from
     * the query string. For eg: select city,win_by_runs from data/ipl.csv from the
     * query mentioned above, we need to extract "city" and "win_by_runs". Please
     * note that we might have a field containing name "from_date" or "from_hrs".
     * Hence, consider this while parsing.
     */


    public String[] getFields(String queryString) {
        if (!queryString.contains("*")) {
            String substring = queryString.substring(queryString.indexOf("select ") + 7, queryString.indexOf("from "));
            String[] items = substring.split(",");
            String[] fields = new String[items.length];
            for (int i = 0; i < fields.length; i++) {
                fields[i] = items[i].trim();
            }
            return fields;
        } else {
            return new String[]{"*"};
        }
    }


    /*
     * Extract the conditions from the query string(if exists). for each condition,
     * we need to capture the following: 1. Name of field 2. condition 3. value
     *
     * For eg: select city,winner,team1,team2,player_of_match from data/ipl.csv
     * where season >= 2008 or toss_decision != bat
     *
     * here, for the first condition, "season>=2008" we need to capture: 1. Name of
     * field: season 2. condition: >= 3. value: 2008
     *
     * the query might contain multiple conditions separated by OR/AND operators.
     * Please consider this while parsing the conditions.
     *
     */

    public String getConditionsPartQuery(String queryString) {
        int whereLocation = queryString.indexOf(where);
        int orderLocation = queryString.indexOf("order by city");
        int groupLocation = queryString.indexOf("group by");
        if (whereLocation > 0 && orderLocation > 0) {
            return queryString.toLowerCase().substring(whereLocation + 6, orderLocation - 1);
        } else if (whereLocation > 0 && groupLocation > 0) {
            return queryString.toLowerCase().substring(whereLocation + 6, groupLocation - 1);
        } else if (whereLocation > 0) {
            return queryString.toLowerCase().substring(whereLocation + 6, queryString.length());
        }
        return null;
    }

    /*
     * Extract the logical operators(AND/OR) from the query, if at all it is
     * present. For eg: select city,winner,team1,team2,player_of_match from
     * data/ipl.csv where season >= 2008 or toss_decision != bat and city =
     * bangalore
     *
     * The query mentioned above in the example should return a List of Strings
     * containing [or,and]
     */

    public List<Restriction> getConditions(String queryString) {

        String trimmed = queryString.trim();
        String[] tokens = trimmed.trim().split(where);

        if (tokens.length == 1) {
            return null;
        }

        String[] conditions = tokens[1].trim().split("order by|group by");
        String[] operations = conditions[0].trim().split(" and | or ");
        List<Restriction> restrictionList = new LinkedList<>();
        for (String string : operations) {
            String condition = "";
            if (string.contains(">=")) {
                condition = ">=";
            } else if (string.contains("<=")) {
                condition = "<=";
            } else if (string.contains("!=")) {
                condition = "!=";
            } else if (string.contains(">")) {
                condition = ">";
            } else if (string.contains("<")) {
                condition = "<";
            } else if (string.contains("=")) {
                condition = "=";
            }
            String name = string.split(condition)[0].trim();
            String value = string.split(condition)[1].trim().replaceAll("'", "");
            Restriction restrictionInstance = new Restriction(name, value, condition);
            restrictionList.add(restrictionInstance);
        }
        return restrictionList;
    }

    public String[] getLogicalOperators(String queryString) {
        int whereLocation = queryString.indexOf(where);
        StringBuilder operators = new StringBuilder();
        String[] output = null;
        if (whereLocation > 0) {
            Pattern pattern = Pattern.compile(" and | or ");
            Matcher matcher = pattern.matcher(queryString);
            while (matcher.find()) {
                operators.append(matcher.group().trim()).append("dummy");
            }
            if (operators.length() != 0) {
                output = operators.toString().split("dummy");
            }
        }
        return output;
    }

    /*
     * Extract the aggregate functions from the query. The presence of the aggregate
     * functions can determined if we have either "min" or "max" or "sum" or "count"
     * or "avg" followed by opening braces"(" after "select" clause in the query
     * string. in case it is present, then we will have to extract the same. For
     * each aggregate functions, we need to know the following: 1. type of aggregate
     * function(min/max/count/sum/avg) 2. field on which the aggregate function is
     * being applied.
     *
     * Please note that more than one aggregate function can be present in a query.
     *
     *
     */

    public String[] getAggregateFunctions(String queryString) {
        if (queryString.contains("(")) {
            int indexOfFrom = queryString.indexOf("from ");
            String newString = queryString.substring(0, indexOfFrom);
            String[] strings = newString.toLowerCase().split("\\s");
            String longest = null;
            int maxSubstringLength = 0;
            for (String string : strings) {
                if (string.length() > maxSubstringLength) {
                    maxSubstringLength = string.length();
                    longest = string;
                }
            }
            assert longest != null;
            int lastIndexOfBracket = longest.lastIndexOf(")");
            String splitIntoAggregates = longest.substring(0, lastIndexOfBracket + 1);
            return splitIntoAggregates.split(",");
        }
        return null;
    }

}