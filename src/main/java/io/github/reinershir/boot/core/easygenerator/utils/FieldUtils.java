package io.github.reinershir.boot.core.easygenerator.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FieldUtils {
	static Pattern p=Pattern.compile("([a-z])([A-Z])");
	/**
	 * @date:   2019年6月14日 下午3:47:00   
	 * @author ReinerShir 
	 * @Description: 陀峰转下划线
	 * @param: @param str
	 * @param: @return      
	 * @return: String      
	 * @throws
	 */
	public static String escapeToMyBatisName(String str){
		Matcher matcher=p.matcher(str);
		return matcher.replaceAll("$1_$2");
	}
	/**
	 * @date:   2019年6月14日 下午3:47:14   
	 * @author ReinerShir 
	 * @Description: 首字母大写
	 * @param: @param s
	 * @param: @return      
	 * @return: String      
	 * @throws
	 */
	public static String toLowerCaseFirstOne(String s){
	    if(Character.isLowerCase(s.charAt(0)))
	      return s;
	    else
	      return (new StringBuilder()).append(Character.toLowerCase(s.charAt(0))).append(s.substring(1)).toString();
	}
	
	/**
	 * @date:   2019年6月14日 下午3:46:12   
	 * @author ReinerShir 
	 * @Description: 下划线转陀峰
	 * @param: @param name
	 * @param: @return      
	 * @return: String      
	 * @throws
	 */
	public static String camelName(String name) {
        StringBuilder result = new StringBuilder();
        // 快速检查
        if (name == null || name.isEmpty()) {
            // 没必要转换
            return "";
        } else if (!name.contains("_")) {
            // 不含下划线，仅将首字母小写
            //return name.substring(0, 1).toLowerCase() + name.substring(1);
        	//不含下划线，转换为小写
        	return name.toLowerCase();
        }
        // 用下划线将原始字符串分割
        String camels[] = name.split("_");
        for (String camel :  camels) {
            // 跳过原始字符串中开头、结尾的下换线或双重下划线
            if (camel.isEmpty()) {
                continue;
            }
            // 处理真正的驼峰片段
            if (result.length() == 0) {
                // 第一个驼峰片段，全部字母都小写
                result.append(camel.toLowerCase());
            } else {
                // 其他的驼峰片段，首字母大写
                result.append(camel.substring(0, 1).toUpperCase());
                result.append(camel.substring(1).toLowerCase());
            }
        }
        return result.toString();
    }
	
}
