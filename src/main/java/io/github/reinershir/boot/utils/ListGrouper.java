package io.github.reinershir.boot.utils;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class ListGrouper{

    /**
     * 将集合按指定数量分组
     * @param list 数据集合
     * @param quantity 分组数量
     * @return 分组结果
     */
    public static <T> List<List<T>> groupListByQuantity(List<T> list, int quantity) {
        if (list == null || list.size() == 0) {
            return null;
        }

        if (quantity <= 0) {
            new IllegalArgumentException("Wrong quantity.");
        }

        List<List<T>> wrapList = new ArrayList<List<T>>();
        int count = 0;
        while (count < list.size()) {
            wrapList.add(new ArrayList<T>(list.subList(count, (count + quantity) > list.size() ? list.size() : count + quantity)));
            count += quantity;
        }

        return wrapList;
    }


    /**
     * 将List<String>集合 转化为String
     * 如{"aaa","bbb"} To 'aaa','bbb'
     */
    public static String convertListToString(List<String> strlist){
        StringBuffer sb = new StringBuffer();
        if(CollectionUtils.isNotEmpty(strlist)){
            for (int i=0;i<strlist.size();i++) {
                if(i==0){
                    sb.append("'").append(strlist.get(i)).append("'");
                }else{
                    sb.append(",").append("'").append(strlist.get(i)).append("'");
                }
            }
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        List<Integer> list = new ArrayList<Integer>();
        for (int i = 1; i <= 1011; i++) {
            list.add(i);
        }

        List<List<Integer>> resultList = ListGrouper.groupListByQuantity(list, 50);
        for (List<Integer> l : resultList) {
            System.out.println(l);
        }
        System.out.println(resultList.size());
    }

}

