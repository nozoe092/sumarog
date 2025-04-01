package com.smalog.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.smalog.dto.MakerDetailDTO;

@Mapper
public interface MakerDetailMapper {
        MakerDetailDTO findById(
                @Param("id") Integer id
        );

        MakerDetailDTO findByTokuisakiCode(
                @Param("tokuisakiCode") String tokuisakiCode
        );

        void insert(
                MakerDetailDTO MakerDetailDTO
        );

        int update(
                MakerDetailDTO MakerDetailDTO
        );
}
