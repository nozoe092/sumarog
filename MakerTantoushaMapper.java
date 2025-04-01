package com.smalog.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.smalog.dto.MakerTantoushaDTO;

@Mapper
public interface MakerTantoushaMapper {
        MakerTantoushaDTO findById(
                @Param("id") Integer id
        );

        List<MakerTantoushaDTO> findByIdList(
                @Param("idList") List<Integer> idList
        );

        List<MakerTantoushaDTO> findByMakerDetailId(
                @Param("makerDetailId") Integer makerDetailId
        );

        List<MakerTantoushaDTO> getNotIdList(
                @Param("makerDetailId") Integer makerDetailId,
                @Param("idList") List<Integer> idList
        );

        void insertList(
                @Param("list") List<MakerTantoushaDTO> makerDetailDTOList
        );

        int update(
                MakerTantoushaDTO makerDetailDTO
        );

        int deleteList(
                @Param("list") List<MakerTantoushaDTO> makerDetailDTOList
        );
}
