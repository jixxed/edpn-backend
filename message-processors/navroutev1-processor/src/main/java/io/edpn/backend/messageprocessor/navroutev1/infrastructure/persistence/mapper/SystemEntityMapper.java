package io.edpn.backend.messageprocessor.navroutev1.infrastructure.persistence.mapper;

import io.edpn.backend.messageprocessor.navroutev1.application.dto.persistence.SystemEntity;
import io.edpn.backend.messageprocessor.infrastructure.persistence.util.UuidTypeHandler;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Mapper
public interface SystemEntityMapper {

    @Results(id = "SystemEntityResult", value = {
            @Result(property = "id", column = "id", javaType = UUID.class, typeHandler = UuidTypeHandler.class),
            @Result(property = "name", column = "name"),
            @Result(property = "eliteId", column = "elite_id"),
            @Result(property = "xCoordinate", column = "x_coordinate"),
            @Result(property = "yCoordinate", column = "y_coordinate"),
            @Result(property = "zCoordinate", column = "z_coordinate"),
            @Result(property = "starClass", column = "star_class"),
    })
    @Select("SELECT id, name, elite_id, x_coordinate, y_coordinate, z_coordinate, star_class FROM systems WHERE id = #{id}")
    Optional<SystemEntity> findById(@Param("id") UUID id);

    @Insert("INSERT INTO systems (id, name, elite_id, x_coordinate, y_coordinate, z_coordinate, star_class) VALUES (#{id}, #{name}, #{eliteId}, #{xCoordinate}, #{yCoordinate}, #{zCoordinate}, #{starClass})")
    int insert(SystemEntity systemEntity);

    @Update("UPDATE systems SET name = #{name}, elite_id = #{eliteId}, x_coordinate = #{xCoordinate}, y_coordinate = #{yCoordinate}, z_coordinate = #{zCoordinate}, star_class = #{starClass} WHERE id = #{id}")
    int update(SystemEntity systemEntity);

    @ResultMap("SystemEntityResult")
    @Select("SELECT id, name, elite_id, x_coordinate, y_coordinate, z_coordinate, star_class FROM systems WHERE name = #{name}")
    Optional<SystemEntity> findByName(@Param("name") String name);
}
