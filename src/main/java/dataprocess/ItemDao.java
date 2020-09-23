package dataprocess;

import model.ItemModel;
import org.apache.ibatis.session.SqlSession;

/**
 * @author sunshuangcheng
 * @version V-1.0
 * @description
 * @create 2020-09-15 11:02 上午
 */


public class ItemDao {
    SqlSession session = MybatisSessionUtil.getSession();


    public int insert(ItemModel model){
        int rint = session.insert("ItemModel.insertObject",model); // 第一个参数是mapper xml里的namespace+MappedStatement对应的id
        session.commit();// 不要忘记提交
        return rint;
    }
}
