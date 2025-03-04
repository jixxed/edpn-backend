package io.edpn.backend.messageprocessor.commodityv3.infrastructure.persistence.mappers;

import io.edpn.backend.messageprocessor.commodityv3.application.dto.persistence.SystemEntity;
import io.edpn.backend.messageprocessor.infrastructure.persistence.util.UuidTypeHandler;
import org.apache.ibatis.annotations.*;

import java.util.Optional;
import java.util.UUID;

@Mapper
public interface SystemEntityMapper {

    @Results(id = "SystemEntityResult", value = {
            @Result(property = "id", column = "id", javaType = UUID.class, typeHandler = UuidTypeHandler.class),
            @Result(property = "name", column = "name")
    })
    @Select("SELECT id, name FROM systems WHERE id = #{id}")
    Optional<SystemEntity> findById(@Param("id") UUID id);

    @Insert("INSERT INTO systems (id, name) VALUES (#{id}, #{name})")
    int insert(SystemEntity systemEntity);

    @ResultMap("SystemEntityResult")
    @Select("SELECT id, name FROM systems WHERE name = #{name}")
    Optional<SystemEntity> findByName(@Param("name") String name);
}
