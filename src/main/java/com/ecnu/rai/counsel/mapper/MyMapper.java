package com.ecnu.rai.counsel.mapper;

import com.ecnu.rai.counsel.entity.Visitor;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MyMapper {

    @Select("SELECT * FROM user")
    List<Visitor> findAllUser();
}
