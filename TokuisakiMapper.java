package com.smalog.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.smalog.dto.TokuisakiDTO;

@Mapper
public interface TokuisakiMapper {
	List<TokuisakiDTO> getHanbaitenList(
            @Param("kaishaKubunCode") String kaishaKubunCode, 
            @Param("tokuisakiCode") String tokuisakiCode, 
            @Param("tokuisakiName") String tokuisakiName,
            @Param("tokuisakiNameKana") String tokuisakiNameKana);

    TokuisakiDTO findTokuisaki(
            @Param("kaishaKubunCode") String kaishaKubunCode, 
            @Param("tokuisakiCode") String tokuisakiCode);
}
