package io.edpn.backend.messageprocessor.commodityv3.infrastructure.persistence.mappers;

import io.edpn.backend.messageprocessor.commodityv3.application.dto.persistence.ApproachsettlementEntity;
import io.edpn.backend.messageprocessor.infrastructure.persistence.util.UuidTypeHandler;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Mapper
public interface ApproachsettlementEntityMapper {

    @Results(id = "ApproachsettlementEntityResult", value = {
            @Result(property = "id", column = "id", javaType = UUID.class, typeHandler = UuidTypeHandler.class),
            @Result(property = "name", column = "name")
    })
    @Select("SELECT id, name FROM Approachsettlements")
    List<ApproachsettlementEntity> findAll();

    @ResultMap("ApproachsettlementEntityResult")
    @Select("SELECT id, name FROM approachsettlements WHERE id = #{id}")
    Optional<ApproachsettlementEntity> findById(@Param("id") UUID id);

    @Insert("INSERT INTO approachsettlements (id, name) VALUES (#{id}, #{name})")
    int insert(ApproachsettlementEntity approachsettlementEntity);

    @Update("UPDATE approachsettlements SET name = #{name} WHERE id = #{id}")
    int update(ApproachsettlementEntity approachsettlementEntity);

    @Delete("DELETE FROM approachsettlements WHERE id = #{id}")
    int delete(@Param("id") UUID id);

    @ResultMap("ApproachsettlementEntityResult")
    @Select("SELECT id, name FROM approachsettlements WHERE name = #{name}")
    Optional<ApproachsettlementEntity> findByName(@Param("name") String name);
}
