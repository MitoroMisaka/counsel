package com.ecnu.rai.counsel.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ecnu.rai.counsel.entity.Usersig;
import org.apache.ibatis.annotations.Insert;
import org.springframework.stereotype.Repository;

@Repository
public interface UserSigMapper extends BaseMapper<Usersig> {
    @Insert("INSERT INTO usersig (userid, usersig, name, role) " +
        "VALUES (#{usersig.userid}, #{usersig.usersig}, #{usersig.name}, #{usersig.role})")
    void insertUserSig(Usersig usersig);
}
