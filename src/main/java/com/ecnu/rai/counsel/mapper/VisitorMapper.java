package com.ecnu.rai.counsel.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ecnu.rai.counsel.entity.User;
import com.ecnu.rai.counsel.entity.WXUser;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

public interface VisitorMapper extends BaseMapper<WXUser> {
    @Update("UPDATE visitor SET openid = #{visitor.id}")
    void update(@Param("user") User user);
}
