package com.ecnu.rai.counsel.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ecnu.rai.counsel.entity.Arrange;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArrangeMapper extends BaseMapper<Arrange> {

    @Select("SELECT * FROM arrange WHERE id = #{id}")
    Arrange findById(@Param("id") Long id);

    @Update("UPDATE arrange SET create_time = #{arrange.create_time} , creator = #{arrange.creator} , last_update_time = #{arrange.last_update_time} , " +
            "last_updater = #{arrange.last_updater} , year = #{arrange.year} , month = #{arrange.month} , day = #{arrange.day} , " +
            "start_time = #{arrange.start_time} , end_time = #{arrange.end_time}  , user = #{arrange.user} , role = #{arrange.role} , " +
            "weekday = #{arrange.weekday} , local_date = #{arrange.local_date} " +
            "WHERE id = #{arrange.id}")
    void updateArrange(@Param("arrange") Arrange arrange);

    @Select("SELECT * FROM arrange WHERE user = #{user}")
    List<Arrange> findByUser(@Param("user") Long user);

}
