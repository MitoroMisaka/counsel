package com.ecnu.rai.counsel.mapper;

import com.ecnu.rai.counsel.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MyMapper {

    @Select("SELECT * FROM user")
    List<User> findAllUser();
}
