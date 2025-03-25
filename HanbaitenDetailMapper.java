package com.smalog.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.smalog.dto.HanbaitenDetailDTO;

@Mapper
public interface HanbaitenDetailMapper {
        HanbaitenDetailDTO findById(
                @Param("id") int id
        );

        HanbaitenDetailDTO findByTokuisakiCode(
                @Param("tokuisakiCode") String tokuisakiCode
        );

        void insert(
                HanbaitenDetailDTO hanbaitenDetailDTO
        );

        int update(
                HanbaitenDetailDTO hanbaitenDetailDTO
        );
}
