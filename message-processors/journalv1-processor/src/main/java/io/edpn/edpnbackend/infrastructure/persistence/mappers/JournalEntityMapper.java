package io.edpn.edpnbackend.infrastructure.persistence.mappers;

import io.edpn.backend.messageprocessor.infrastructure.persistence.util.UuidTypeHandler;
import io.edpn.edpnbackend.application.dto.persistence.JournalEntity;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Mapper
public interface JournalEntityMapper {

    @Results(id = "JournalEntityResult", value = {
            @Result(property = "id", column = "id", javaType = UUID.class, typeHandler = UuidTypeHandler.class),
            @Result(property = "name", column = "name")
    })
    @Select("SELECT id, name FROM journals")
    List<JournalEntity> findAll();

    @ResultMap("JournalEntityResult")
    @Select("SELECT id, name FROM journals WHERE id = #{id}")
    Optional<JournalEntity> findById(@Param("id") UUID id);

    @Insert("INSERT INTO journals (id, name) VALUES (#{id}, #{name})")
    int insert(JournalEntity journalEntity);

    @Update("UPDATE journals SET name = #{name} WHERE id = #{id}")
    int update(JournalEntity journalEntity);

    @Delete("DELETE FROM journals WHERE id = #{id}")
    int delete(@Param("id") UUID id);

    @ResultMap("journalEntityResult")
    @Select("SELECT id, name FROM journals WHERE name = #{name}")
    Optional<JournalEntity> findByName(@Param("name") String name);
}
