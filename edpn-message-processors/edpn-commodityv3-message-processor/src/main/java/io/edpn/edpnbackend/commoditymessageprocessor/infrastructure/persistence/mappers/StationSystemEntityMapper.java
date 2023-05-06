package io.edpn.edpnbackend.commoditymessageprocessor.infrastructure.persistence.mappers;

import io.edpn.edpnbackend.commoditymessageprocessor.application.dto.persistence.StationSystemEntity;
import io.edpn.backend.messageprocessor.infrastructure.persistence.util.UuidTypeHandler;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Mapper
public interface StationSystemEntityMapper {

    @Results(id = "StationSystemEntityResult", value = {
            @Result(property = "stationId", column = "station_id", javaType = UUID.class, typeHandler = UuidTypeHandler.class),
            @Result(property = "systemId", column = "system_id", javaType = UUID.class, typeHandler = UuidTypeHandler.class)
    })
    @Select("SELECT station_id, system_id FROM station_systems")
    List<StationSystemEntity> findAll();

    @ResultMap("StationSystemEntityResult")
    @Select("SELECT station_id, system_id FROM station_systems WHERE station_id = #{stationId}")
    Optional<StationSystemEntity> findById(@Param("stationId") UUID stationId);

    @Insert("INSERT INTO station_systems (station_id, system_id) VALUES (#{stationId}, #{systemId})")
    int insert(StationSystemEntity entity);

    @Update("UPDATE station_systems SET system_id = #{systemId} WHERE station_id = #{stationId}")
    int update(StationSystemEntity entity);

    @Delete("DELETE FROM station_systems WHERE station_id = #{stationId}")
    int delete(@Param("stationId") UUID stationId);
}
