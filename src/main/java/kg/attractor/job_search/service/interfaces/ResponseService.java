package kg.attractor.job_search.service.interfaces;

import kg.attractor.job_search.dto.ResponseDto;
import kg.attractor.job_search.entity.Response;
import org.springframework.data.domain.Page;


public interface ResponseService {
    boolean hasAccessToResponse(Long userId, Long responseId);

    Response getEntityById(Long id);

    ResponseDto getResponseById(Long id);

    Long applyVacancy(Long vacancyId, Long resumeId, Long applicantId);

    Page<ResponseDto> getResponsesByVacancyId(Long vacancyId, Integer page, Integer size);

    Page<ResponseDto> getResponsesByResumeId(Long resumeId, Integer page, Integer size);

    Page<ResponseDto> getResponsesByApplicantId(Long applicantId, Integer page, Integer size);

    Integer getResponseCountByApplicantId(Long applicantId, boolean isConfirmed);

    Integer getResponseCountByEmployerId(Long employerId, boolean isConfirmed);

    Boolean isApplicantApplied(Long vacancyId, Long applicantId);

    ResponseDto getResponseByVacancyIdAndUserId(Long vacancyId, Long authId);

    Long getInterlocutorId(ResponseDto response, Long currentUserId);
}
