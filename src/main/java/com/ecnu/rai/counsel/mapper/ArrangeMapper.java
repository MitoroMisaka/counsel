package com.ecnu.rai.counsel.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ecnu.rai.counsel.entity.Arrange;
import com.ecnu.rai.counsel.response.DayNum;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArrangeMapper extends BaseMapper<Arrange> {

    @Insert("INSERT INTO arrange (create_time, creator, last_update_time, last_updater, year, month, day, start_time, end_time, user, role, weekday, local_date) " +
            "VALUES (#{arrange.createTime}, #{arrange.creator}, #{arrange.lastUpdate_time}, #{arrange.lastUpdater}, #{arrange.year}, #{arrange.month}, #{arrange.day}, #{arrange.startTime}, #{arrange.endTime}, #{arrange.user}, #{arrange.role}, #{arrange.weekday}, #{arrange.localDate})")
    void insertArrange(@Param("arrange") Arrange arrange);

    @Select("SELECT * FROM arrange WHERE id = #{id}")
    Arrange findById(@Param("id") Long id);

    @Update("UPDATE arrange SET " +
            "create_time = #{arrange.createTime}, " +
            "creator = #{arrange.creator}, " +
            "last_update_time = #{arrange.lastUpdate_time}, " +
            "last_updater = #{arrange.lastUpdater}, " +
            "year = #{arrange.year}, " +
            "month = #{arrange.month}, " +
            "day = #{arrange.day}, " +
            "start_time = #{arrange.startTime}, " +
            "end_time = #{arrange.endTime}, " +
            "user = #{arrange.user}, " +
            "role = #{arrange.role}, " +
            "weekday = #{arrange.weekday}, " +
            "local_date = #{arrange.localDate} " +
            "WHERE id = #{arrange.id}")
    void updateArrange(@Param("arrange") Arrange arrange);



    @Select("SELECT * FROM arrange WHERE user = #{user}")
    List<Arrange> findByUser(@Param("user") Long user);

    @Select("SELECT * FROM arrange WHERE " +
            "user = #{user} AND " +
            "year = #{year} AND " +
            "month = #{month}")
    List<Arrange> findByUserYearMonth(@Param("user") Long user,
                                      @Param("year") Integer year,
                                      @Param("month") Integer month);

    @Select("SELECT day, COUNT(DISTINCT(user)) AS num FROM arrange WHERE " +
            "year = #{year} AND " +
            "month = #{month} AND " +
            "role = 'counselor' " +
            "GROUP BY day")
    List<DayNum> findArrangeCounselorInfoByYearMonthDay(@Param("year") Integer year,
                                                        @Param("month") Integer month);

    @Select("SELECT day, COUNT(DISTINCT(user)) AS num FROM arrange WHERE " +
            "year = #{year} AND " +
            "month = #{month} AND " +
            "role = 'supervisor' " +
            "GROUP BY day")
    List<DayNum> findArrangeSupervisorInfoByYearMonthDay(@Param("year") Integer year,
                                                @Param("month") Integer month);

    @Select("SELECT DISTINCT(user) FROM arrange WHERE NOW()>start_time AND NOW()<end_time AND role='counselor' ")
    List<Long> findCounselorByCurrentTime();

    @Select("SELECT DISTINCT(user) FROM arrange WHERE NOW()>start_time AND NOW()<end_time AND role='supervisor' ")
    List<Long> findSupervisorByCurrentTime();

}
