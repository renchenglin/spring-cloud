package com.example.demo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import com.example.demo.model.User;
import com.example.demo.model.UserExample;

@Repository
public interface UserMapper {
    @SelectProvider(type=UserSqlProvider.class, method="countByExample")
    long countByExample(UserExample example);

    @DeleteProvider(type=UserSqlProvider.class, method="deleteByExample")
    int deleteByExample(UserExample example);

    @Delete({
        "delete from t_user",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(Long id);

    @Insert({
        "insert into t_user (userId, userName, ",
        "age, address)",
        "values (#{userId,jdbcType=VARCHAR}, #{userName,jdbcType=VARCHAR}, ",
        "#{age,jdbcType=INTEGER}, #{address,jdbcType=VARCHAR})"
    })
    @Options(useGeneratedKeys=true,keyProperty="id")
    int insert(User record);

    @InsertProvider(type=UserSqlProvider.class, method="insertSelective")
    @Options(useGeneratedKeys=true,keyProperty="id")
    int insertSelective(User record);

    @SelectProvider(type=UserSqlProvider.class, method="selectByExample")
    @Results({
        @Result(column="t_user_id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="t_user_userId", property="userId", jdbcType=JdbcType.VARCHAR),
        @Result(column="t_user_userName", property="userName", jdbcType=JdbcType.VARCHAR),
        @Result(column="t_user_age", property="age", jdbcType=JdbcType.INTEGER),
        @Result(column="t_user_address", property="address", jdbcType=JdbcType.VARCHAR)
    })
    List<User> selectByExample(UserExample example);

    @Select({
        "select",
        "t_user.id as t_user_id, t_user.userId as t_user_userId, t_user.userName as t_user_userName, ",
        "t_user.age as t_user_age, t_user.address as t_user_address",
        "from t_user t_user",
        "where t_user.id = #{id,jdbcType=BIGINT}"
    })
    @Results({
        @Result(column="t_user_id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="t_user_userId", property="userId", jdbcType=JdbcType.VARCHAR),
        @Result(column="t_user_userName", property="userName", jdbcType=JdbcType.VARCHAR),
        @Result(column="t_user_age", property="age", jdbcType=JdbcType.INTEGER),
        @Result(column="t_user_address", property="address", jdbcType=JdbcType.VARCHAR)
    })
    User selectByPrimaryKey(Long id);

    @UpdateProvider(type=UserSqlProvider.class, method="updateByExampleSelective")
    int updateByExampleSelective(@Param("record") User record, @Param("example") UserExample example);

    @UpdateProvider(type=UserSqlProvider.class, method="updateByExample")
    int updateByExample(@Param("record") User record, @Param("example") UserExample example);

    @UpdateProvider(type=UserSqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(User record);

    @Update({
        "update t_user",
        "set userId = #{userId,jdbcType=VARCHAR},",
          "userName = #{userName,jdbcType=VARCHAR},",
          "age = #{age,jdbcType=INTEGER},",
          "address = #{address,jdbcType=VARCHAR}",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(User record);
}