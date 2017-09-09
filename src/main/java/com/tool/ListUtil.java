package com.tool;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/9.
 */
public class ListUtil {

    public static <T> List<List<T>> averageAssign(List<T> source, int n){
        List<List<T>> result = new ArrayList<List<T>>();
        int remaider = source.size()%n;  //(先计算出余数)
        int number = source.size()/n;  //然后是商
        int offset=0;//偏移量
        for(int i=0;i<n;i++){
            List<T> value = null;
            if(remaider>0){
                value = source.subList(i*number+offset, (i+1)*number+offset+1);
                remaider--;
                offset++;
            }else{
                value = source.subList(i*number+offset, (i+1)*number+offset);
            }
            result.add(value);
        }
        return result;
    }

    public static <T> List<List<T>> pageList(List<T> source, int n){
        List<List<T>> result = new ArrayList<List<T>>();
        List<T> value = new ArrayList<T>();
        for (int i=0; i< source.size(); i++) {
            if (i%n==0) {
                break;
            } else {
                value.add(source.get(i));
            }
        }

        return result;
    }

    public static <T> List<T> page(int pageNo,int pageSize,List<T> list) throws Exception{
        List<T> result = new ArrayList<T>();
        if(list != null && list.size() > 0){
            int allCount = list.size();
            int pageCount = (allCount + pageSize-1) / pageSize;
            if(pageNo > pageCount){
                return null;
            } else {
                int start = (pageNo-1) * pageSize;
                int end = pageNo * pageSize;
                if(end >= allCount){
                    end = allCount;
                }
                for(int i = start; i < end; i ++){
                    result.add(list.get(i));
                }
            }
        }
        return (result != null && result.size() > 0) ? result : null;
    }


    public static void main(String[] args) throws Exception {
        List<Object> list = new ArrayList<Object>();
        list.add("a");
        list.add("b");
        list.add("c");
        list.add("d");
        list.add("e");
        List<Object> result = page(3,2,list);
        System.out.print(result);
    }
}
