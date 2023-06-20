package com.ecnu.rai.counsel.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ecnu.rai.counsel.entity.Usersig;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface UserSigMapper extends BaseMapper<Usersig> {
    @Insert("INSERT INTO usersig (imid, usersig, name, role) " +
        "VALUES (#{usersig1.imid}, #{usersig1.usersig}, #{usersig1.name}, #{usersig1.role})")
    void insertUserSig(@Param("usersig1") Usersig usersig1);

    //get name by imid
    @Select("SELECT name FROM usersig WHERE imid = #{imid}")
    String getNameByImid(@Param("imid") String imid);

    //get UserSig by name
    @Select("SELECT * FROM usersig WHERE name = #{name}")
    Usersig getUserSigByName(@Param("name") String name);

    //get imid by name
    @Select("SELECT imid FROM usersig WHERE name = #{name}")
    String getImidByName(@Param("name") String name);

}
