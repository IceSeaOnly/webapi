package site.binghai.biz.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.binghai.biz.entity.RespEntity;

import java.util.List;

/**
 * @author huaishuo
 * @date 2018/09/29
 */
@Service
public class RespEntityService  {
    @Autowired
    private RespEntityDao dao;

    public Object findById(Integer id) {
        return dao.findById(id);
    }


    public List<RespEntity> findAll(){
        return dao.findAll();
    }
}
