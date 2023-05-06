package io.edpn.edpnbackend.infrastructure.persistence.mappers;

import io.edpn.edpnbackend.application.dto.persistence.SchemaLatestTimestampEntity;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Optional;

@Mapper
public interface SchemaLatestTimestampEntityMapper {

    @Results(id = "SchemaLatestTimestampEntityResult", value = {
            @Result(property = "schema", column = "schema"),
            @Result(property = "timestamp", column = "timestamp")
    })
    @Select("SELECT schema, timestamp FROM schema_latest_timestamps")
    List<SchemaLatestTimestampEntity> findAll();

    @ResultMap("SchemaLatestTimestampEntityResult")
    @Select("SELECT schema, timestamp FROM schema_latest_timestamps WHERE schema = #{schema}")
    Optional<SchemaLatestTimestampEntity> findBySchema(@Param("schema") String schema);

    @Insert("INSERT INTO schema_latest_timestamps (schema, timestamp) VALUES (#{schema}, #{timestamp})")
    int insert(SchemaLatestTimestampEntity schemaLatestTimestampEntity);

    @Update("UPDATE schema_latest_timestamps SET timestamp = #{timestamp} WHERE schema = #{schema}")
    int update(SchemaLatestTimestampEntity schemaLatestTimestampEntity);
}
