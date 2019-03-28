package cn.itxsl.kernel.utils;

import cn.itxsl.kernel.model.dto.Result;

import java.util.List;

/**
 * @author ：itxsl
 * @date ：Created in 2019/1/13 12:55
 * @description：返回工具类
 */
public class ResultUtils {

    public static Result post(Object data){
        Result result = new Result();
        if (data==null){
            result.setCode(201);
            result.setMessage("添加失败！");
        }else {
            result.setCode(200);
            result.setMessage("添加成功！");
        }
        return result;
    }

    public static Result get(Object data){
        Result result = new Result();
        if (data==null){
            result.setCode(201);
            result.setMessage("数据不存在！");
        }else {
            result.setCode(200);
            result.setMessage("获取成功！");
            result.setData(data);
        }
        return result;
    }

    public static Result put(Object data){
        Result result = new Result();
        if (data==null){
            result.setCode(201);
            result.setMessage("更新失败！");
        }else {
            result.setCode(200);
            result.setMessage("更新成功！");
        }
        return result;
    }

    public static Result del(Integer size){
        Result result = new Result();
        if (size==null||size<1){
            result.setCode(201);
            result.setMessage("删除失败！");
        }else {
            result.setCode(200);
            result.setMessage("删除成功！");
        }
        return result;
    }



    public static Result gets(List<?> list){
        Result result = new Result();
        if (list==null||list.size()==0){
            result.setCode(200);
            result.setMessage("暂无数据！");
        }else {
            result.setCode(200);
            result.setData(list);
            result.setMessage("获取成功！");
        }
        return result;
    }

    public static Result gets(List<?> list,Integer totalSize){
        Result result = new Result();
        if (list==null||list.size()==0||totalSize<1){
            result.setCode(200);
            result.setMessage("暂无数据！");
            result.setTotalSize(totalSize);
        }else {
            result.setCode(200);
            result.setData(list);
            result.setMessage("获取成功！");
            result.setTotalSize(totalSize);
        }
        return result;
    }


}