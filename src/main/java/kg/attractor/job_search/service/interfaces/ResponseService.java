package kg.attractor.job_search.service.interfaces;

import kg.attractor.job_search.dto.ResponseDto;
import org.springframework.data.domain.Page;

public interface ResponseService {
    Long applyVacancy(Long vacancyId, Long resumeId, Long applicantId);

    Page<ResponseDto> getResponsesByVacancyId(Long vacancyId, Integer page, Integer size);

    Page<ResponseDto> getResponsesByApplicantId(Long applicantId, Integer page, Integer size);

    Integer getResponseCountByApplicantId(Long applicantId, boolean isConfirmed);

    Integer getResponseCountByEmployerId(Long employerId, boolean isConfirmed);
}
