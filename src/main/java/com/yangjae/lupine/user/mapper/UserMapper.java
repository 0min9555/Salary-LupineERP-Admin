package com.yangjae.lupine.user.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    boolean isAccessToday(Integer userIdx);
}
