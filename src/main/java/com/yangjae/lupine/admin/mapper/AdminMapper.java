package com.yangjae.lupine.admin.mapper;

import com.yangjae.lupine.model.dto.AdminDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface AdminMapper {
    Map<String, Object> selectAdminByIdx(Integer adminIdx);

}
