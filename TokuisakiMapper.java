package com.smalog.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.smalog.dto.TokuisakiDTO;
import com.smalog.dto.common.SelectBoxDTO;

@Mapper
public interface TokuisakiMapper {
	List<TokuisakiDTO> getHanbaitenList(
            @Param("kaishaKubunCode") String kaishaKubunCode, 
            @Param("slUseFlag") String slUseFlag, 
            @Param("tokuisakiCode") String tokuisakiCode, 
            @Param("tokuisakiName") String tokuisakiName,
            @Param("tokuisakiNameKana") String tokuisakiNameKana);
            

        List<TokuisakiDTO> getHanbaitenListByHanbaiten(
                @Param("kaishaKubunCode") String kaishaKubunCode,
                @Param("slUseFlag") String slUseFlag,
                @Param("hanbaiten") String hanbaiten,
                @Param("tokuisakiCode") String tokuisakiCode,
                @Param("tokuisakiName") String tokuisakiName);

        List<SelectBoxDTO> getSelectBoxDataList(
                @Param("kaishaKubunCode") String kaishaKubunCode, 
                @Param("slUseFlag") String slUseFlag, 
                @Param("tokuisakiCode") String tokuisakiCode);

        TokuisakiDTO findTokuisaki(
            @Param("kaishaKubunCode") String kaishaKubunCode, 
            @Param("tokuisakiCode") String tokuisakiCode,
            @Param("slUseFlag") String slUseFlag);

        TokuisakiDTO findTokuisakiByCode(
            @Param("tokuisakiCode") String tokuisakiCode);
}
