package com.smalog.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.smalog.dto.HanbaitenTantoushaDTO;

@Mapper
public interface HanbaitenTantoushaMapper {
        HanbaitenTantoushaDTO findById(
                @Param("id") Integer id
        );

        List<HanbaitenTantoushaDTO> findByIdList(
                @Param("idList") List<Integer> idList
        );

        List<HanbaitenTantoushaDTO> findByHanbaitenDetailId(
                @Param("hanbaitenDetailId") Integer hanbaitenDetailId
        );

        List<HanbaitenTantoushaDTO> getNotIdList(
                @Param("hanbaitenDetailId") Integer hanbaitenDetailId,
                @Param("idList") List<Integer> idList
        );

        void insertList(
                @Param("list") List<HanbaitenTantoushaDTO> hanbaitenDetailDTOList
        );

        int update(
                HanbaitenTantoushaDTO hanbaitenDetailDTO
        );

        int deleteList(
                @Param("list") List<HanbaitenTantoushaDTO> hanbaitenDetailDTOList
        );
}
