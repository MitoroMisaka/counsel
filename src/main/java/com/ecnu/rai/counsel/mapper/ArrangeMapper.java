package com.ecnu.rai.counsel.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ecnu.rai.counsel.dao.CounselorBasicInfo;
import com.ecnu.rai.counsel.entity.Arrange;
import com.ecnu.rai.counsel.response.DayNum;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
public interface ArrangeMapper extends BaseMapper<Arrange> {

    @Insert("INSERT INTO arrange (create_time, creator, last_update_time, last_updater, year, month, day, start_time, end_time, user, role, weekday, local_date) " +
            "VALUES (#{arrange.createTime}, #{arrange.creator}, #{arrange.lastUpdateTime}, #{arrange.lastUpdater}, #{arrange.year}, #{arrange.month}, #{arrange.day}, #{arrange.startTime}, #{arrange.endTime}, #{arrange.user}, #{arrange.role}, #{arrange.weekday}, #{arrange.localDate})")
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
    List<DayNum> findCounselorInfoByMonth(@Param("year") Integer year,
                                                        @Param("month") Integer month);

    @Select("SELECT day, COUNT(DISTINCT(user)) AS num FROM arrange WHERE " +
            "year = #{year} AND " +
            "month = #{month} AND " +
            "role = 'supervisor' " +
            "GROUP BY day")
    List<DayNum> findSupervisorInfoByMonth(@Param("year") Integer year,
                                                @Param("month") Integer month);

    @Select("SELECT DISTINCT(user) FROM arrange WHERE " +
            "year = #{year} AND " +
            "month = #{month} AND " +
            "day = #{day} AND " +
            "role = 'counselor' ")
    List<Long> findCounselorIDListByDay(@Param("year") Integer year,
                                                    @Param("month") Integer month,
                                                    @Param("day") Integer day);

    @Select("SELECT DISTINCT(user) FROM arrange WHERE " +
            "year = #{year} AND " +
            "month = #{month} AND " +
            "day = #{day} AND " +
            "role = 'supervisor' ")
    List<Long> findSupervisorIDListByDay(@Param("year") Integer year,
                                        @Param("month") Integer month,
                                        @Param("day") Integer day);

    @Select("SELECT user FROM arrange WHERE NOW()>start_time AND NOW()<end_time AND role='counselor' ORDER BY ${order}")
    List<Long> findCounselorByCurrentTime(@Param("order") String order);

    @Select("SELECT user FROM arrange WHERE NOW()>start_time AND NOW()<end_time AND role='supervisor' ORDER BY ${order}")
    List<Long> findSupervisorByCurrentTime(@Param("order") String order);

    @Select("SELECT day FROM arrange WHERE user = #{user} and year = YEAR(CURRENT_DATE()) and month = MONTH(CURRENT_DATE())")
    List<Integer> findDayArrange(@Param("user") Long user);

    @Select("SELECT DISTINCT `user` FROM arrange WHERE `role` = 'SUPERVISOR' AND `user` NOT IN " +
            "(SELECT DISTINCT `user` FROM arrange " +
            "WHERE `year` = YEAR(CURRENT_DATE()) " +
            "AND `month` = MONTH(CURRENT_DATE()) " +
            "AND `day` = DAY(CURRENT_DATE()))")
    List<Long> findFreeSupervisor();

    @Select("SELECT DISTINCT `user` FROM arrange " +
            "WHERE `role` = 'SUPERVISOR' " +
            "AND `year` = YEAR(CURRENT_DATE()) " +
            "AND `month` = MONTH(CURRENT_DATE()) " +
            "AND `day` = DAY(CURRENT_DATE())")
    List<Long> findWorkingSupervisor();

    @Select("SELECT DISTINCT `user` FROM arrange WHERE `role` = 'COUNSELOR' AND `user` NOT IN " +
            "(SELECT DISTINCT `user` FROM arrange " +
            "WHERE `year` = YEAR(CURRENT_DATE()) " +
            "AND `month` = MONTH(CURRENT_DATE()) " +
            "AND `day` = DAY(CURRENT_DATE()))")
    List<Long> findFreeCounselor();

    @Select("SELECT DISTINCT `user` FROM arrange " +
            "WHERE `role` = 'COUNSELOR' " +
            "AND `year` = YEAR(CURRENT_DATE()) " +
            "AND `month` = MONTH(CURRENT_DATE()) " +
            "AND `day` = DAY(CURRENT_DATE())")
    List<Long> findWorkingCounselor();

}
