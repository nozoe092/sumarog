package com.smalog.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.smalog.dto.MakerDTO;

@Mapper
public interface MakerMapper {

    /**
     * メーカーの情報を取得する
     * @param kaishaKubunCode
     * @param hanbaiten
     * @param makerCode
     * @param name
     * @param category
     * @return
     */
    List<MakerDTO> getMakerList(@Param("kaishaKubunCode") String kaishaKubunCode,
                                @Param("hanbaiten") String hanbaiten,
                                @Param("makerCode") String makerCode,
                                @Param("name") String name,
                                @Param("category") String category);
}