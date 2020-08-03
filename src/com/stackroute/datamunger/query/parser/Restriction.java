package com.stackroute.datamunger.query.parser;

/*
 * This class is used for storing name of field, condition and value for
 * each conditions
 * */
public class Restriction {

	private String name;
	private String value;
	private String condition;

	// Write logic for constructor
	public Restriction(String name, String value, String condition) {
		this.condition = condition;
		this.name = name;
		this.value = value;
	}

	public String getPropertyName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPropertyValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	@Override
	public String toString() {
		return "variable : " + name + "\noperator : " + condition + "\nvalue    : " + value;
	}
}
