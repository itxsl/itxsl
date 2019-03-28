package cn.itxsl.kernel.repository.base;

import cn.itxsl.kernel.model.BaseMapped;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.pager.Pager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.ParameterizedType;
import java.util.Date;
import java.util.List;

import static cn.itxsl.kernel.utils.RunUtils.getAuthentication;
import static cn.itxsl.kernel.utils.RunUtils.getCurrentUser;

public class Repository<T> {

    public Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public Dao dao;

    public Class<T> aClass =(Class <T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];

    /**
     * 添加
     * @param t 添加对象
     * @return
     */
    public T post(T t){
        try{
            Date date = new Date();
            BaseMapped baseMapped = (BaseMapped) t;
            baseMapped.setCreateTime(date);
            baseMapped.setUpdateTime(date);
            if (getAuthentication().isAuthenticated()){
                baseMapped.setCreateUsername(getCurrentUser().getUsername());
                baseMapped.setUpdateUsername(getCurrentUser().getUsername());
            }
            t = aClass.cast(baseMapped);
        }catch (Exception e){
            return null;
        }
        return dao.insert(t);
    }


    /**
     * 删除映射关系
     * @param t
     * @param s
     * @return
     */
    public T delRelation(T t,String s){
       return dao.clearLinks(t,s);
    }

    /**
     * 添加映射关系
     * @param t
     * @param s
     * @return
     */
    public T postRelation(T t,String s){
       return dao.insertRelation(t,s);
    }

    /**
     * 修改
     * @param t 修改对象
     * @return
     */
    public T put(T t){
        try {
            Date date = new Date();
            BaseMapped baseMapped = (BaseMapped) t;
            baseMapped.setUpdateTime(date);
            baseMapped.setCreateUsername(getCurrentUser().getUsername());
            baseMapped.setUpdateUsername(getCurrentUser().getUsername());
            t = aClass.cast(baseMapped);
        }catch (Exception e){
            return null;
        }
        int i = dao.updateIgnoreNull(t);
        if (i>0){
            return dao.fetch(t);
        }
        return null;
    }

    /**
     * 更新
     * @param t
     * @return
     */
    public Integer put(List<T> t){
        return dao.update(t);
    }


    /**
     * 删除
     * @param id 对象id
     * @return
     */
    public Integer del(Long id){
        return dao.delete(aClass,id);
    }


    /**
     * 删除
     * @return
     */
    public Integer del(Cnd cnd){

        return dao.clear(aClass,cnd);
    }


    /**
     * 删除
     * @return
     */
    public Integer del(List<T> t){
        return dao.delete(t);
    }


    /**
     * 删除
     * @param cnd 条件
     * @return
     */
    public Integer clear(Cnd cnd){
        return dao.clear(aClass,cnd);
    }

    /**
     * 根据id删除
     * @param id 对象id
     * @return
     */
    public Integer del(Integer id){

        return dao.delete(aClass,id);
    }

    /**
     * 根据id查询
     * @param id 对象id
     * @return
     */
    public  T get(Integer id){
        return dao.fetch(aClass, id);
    }

    /**
     * 根据id查询
     * @param id
     * @return
     */
    public  T get(Long id){
        return dao.fetch(aClass, id);
    }


    /**
     * 根据条件查询
     * @param cnd
     * @return
     */
    public  T get(Cnd cnd){
        return   dao.fetch(aClass, cnd);
    }

    /**
     * 根据条件获取全部
     * @param cnd
     * @return
     */
    public List<T> gets(Cnd cnd ){
        return dao.query(aClass,cnd);
    }


    /**
     * 根据条件获取全部，带分页
     * @param cnd
     * @param pager
     * @return
     */
    public List<T> gets(Cnd cnd , Pager pager){
        return dao.query(aClass,cnd,pager);
    }
    public List<T> getsByJoin(String join,Cnd cnd , Pager pager){
        return dao.queryByJoin(aClass,join,cnd,pager);
    }

    /**
     * 根据条件统计数量
     * @param cnd
     * @return
     */
    public Integer count(Cnd cnd){
        return dao.count(aClass,cnd);
    }

    /**
     * 根据id获取对象和关联对象
     * @param id
     * @param join
     * @return
     */
    public T getjoin(Long id,String join) {
        return dao.fetchLinks(dao.fetch(aClass,id),join);
    }
}
