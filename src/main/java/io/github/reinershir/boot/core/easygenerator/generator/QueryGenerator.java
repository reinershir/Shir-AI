package io.github.reinershir.boot.core.easygenerator.generator;

public class QueryGenerator{

	public String generateAnnotation(String opreation) {
		System.out.println(opreation);
		StringBuffer anno = new StringBuffer("@QueryRule(QueryRuleEnum.");
		switch(opreation) {
		case ">":
			anno.append("GT");
			break;
		case "<":
			anno.append("LT");
			break;
		case "=":
			anno.append("EQ");
			break;
		case ">=":
			anno.append("GE");
			break;
		case "in":
			anno.append("IN");
			break;
		case "left_like":
			anno.append("LEFT_LIKE");
			break;
		case "right_like":
			anno.append("RIGHT_LIKE");
			break;
		case "like":
			anno.append("LIKE");
			break;
		case "is_null":
			anno.append("EMPTY");
			break;
		case "not_empty":
			anno.append("NOT_EMPTY");
			break;
		case "!=":
			anno.append("NE");
			break;
		case "not_in":
			anno.append("NOT_IN");
			break;
		case "hidden":
			anno.append("HIDDEN");
			break;
		}
		anno.append(")");
		return anno.toString();
	}
}
