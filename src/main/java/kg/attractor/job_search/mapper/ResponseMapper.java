package kg.attractor.job_search.mapper;

import kg.attractor.job_search.dto.ResponseDto;
import kg.attractor.job_search.entity.Response;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ResponseMapper {
    @Mapping(target = "vacancy.responses", ignore = true)
    ResponseDto toDto(Response response);

    @Mapping(target = "vacancy.responses", ignore = true)
    Response toEntity(ResponseDto dto);
}
