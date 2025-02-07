package io.github.reinershir.boot.utils;

public class FieldUtils {

	/**
	 * 将驼峰命名转化成下划线
	 * @param para
	 * @return
	 */
	public static String camelToUnderline(String para){
	    int length = 3;
        if(para.length()<length){
        	return para.toLowerCase(); 
        }
        StringBuilder sb=new StringBuilder(para);
        //定位
        int temp=0;
        //从第三个字符开始 避免命名不规范 
        for(int i=2;i<para.length();i++){
            if(Character.isUpperCase(para.charAt(i))){
                sb.insert(i+temp, "_");
                temp+=1;
            }
        }
        return sb.toString().toLowerCase(); 
	}
}
