package kg.attractor.job_search.service.impl;

import kg.attractor.job_search.dto.ResponseDto;
import kg.attractor.job_search.entity.Response;
import kg.attractor.job_search.entity.Resume;
import kg.attractor.job_search.entity.Vacancy;
import kg.attractor.job_search.exception.AlreadyRespondedException;
import kg.attractor.job_search.exception.iae.IncorrectCategoryException;
import kg.attractor.job_search.exception.nsee.ResponseNotFoundException;
import kg.attractor.job_search.mapper.ResponseMapper;
import kg.attractor.job_search.repository.ResponseRepository;
import kg.attractor.job_search.service.interfaces.ResponseService;
import kg.attractor.job_search.service.interfaces.ResumeService;
import kg.attractor.job_search.service.interfaces.VacancyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.function.Supplier;

@Service
@Slf4j
@RequiredArgsConstructor
public class ResponseServiceImpl implements ResponseService {
    private final VacancyService vacancyService;
    private final ResumeService resumeService;
    private final ResponseRepository responseRepository;
    private final MessageSource messageSource;
    private final ResponseMapper responseMapper;

    @Override
    public boolean hasAccessToResponse(Long userId, Long responseId) {
        ResponseDto responseDto = getResponseById(responseId);
        Long applicantId = responseDto.getResume().getApplicant().getId();
        Long employerId = responseDto.getVacancy().getEmployer().getId();
        return userId.equals(applicantId) || userId.equals(employerId);
    }

    @Override
    public Response getEntityById(Long id) {
        return responseRepository.findById(id)
                .orElseThrow(() -> new ResponseNotFoundException("Отклик с id " + id + " не найден"));
    }

    @Override
    public ResponseDto getResponseById(Long id) {
        Response response = getEntityById(id);
        log.info("Получен отклик с id {}", id);
        return responseMapper.toDto(response);
    }

    @Override
    public Long applyVacancy(Long vacancyId, Long resumeId, Long applicantId) {
        Vacancy vacancy = vacancyService.getVacancyById(vacancyId);
        Resume resume = resumeService.getResumeById(resumeId, applicantId);

        Locale locale = LocaleContextHolder.getLocale();

        if (responseRepository.existsByResumeIdAndVacancyId(resumeId, vacancyId)) {
            throw new AlreadyRespondedException(messageSource.getMessage("response.already.error", null, locale));
        }

        if (!vacancy.getCategory().getId().equals(resume.getCategory().getId())) {
            throw new IncorrectCategoryException(messageSource.getMessage("response.category.error", null, locale));
        }

        Response respondedApplicant = Response.builder()
                .vacancy(vacancy)
                .resume(resume)
                .isConfirmed(false)
                .build();

        responseRepository.save(respondedApplicant);
        return respondedApplicant.getId();
    }

    @Override
    public Page<ResponseDto> getResponsesByVacancyId(Long vacancyId, Integer page, Integer size) {
        Pageable pageable = getPageable(page, size);
        return getResponsePage(() -> responseRepository.findAllByVacancy_Id(vacancyId, pageable));
    }

    @Override
    public Page<ResponseDto> getResponsesByResumeId(Long resumeId, Integer page, Integer size) {
        Pageable pageable = getPageable(page, size);
        return getResponsePage(() -> responseRepository.findAllByResumeId(resumeId, pageable));

    }

    @Override
    public Page<ResponseDto> getResponsesByApplicantId(Long applicantId, Integer page, Integer size) {
        Pageable pageable = getPageable(page, size);
        return getResponsePage(() -> responseRepository.findAllByResume_Applicant_Id(applicantId, pageable));
    }

    @Override
    public Integer getResponseCountByApplicantId(Long applicantId, boolean isConfirmed) {
        if (isConfirmed) {
            return responseRepository.countAllByIsConfirmedTrueAndResume_Applicant_Id(applicantId);
        }
        return responseRepository.countAllByResume_Applicant_Id(applicantId);
    }

    @Override
    public Integer getResponseCountByEmployerId(Long employerId, boolean isConfirmed) {
        if (isConfirmed) {
            return responseRepository.countAllByIsConfirmedTrueAndVacancy_Employer_Id(employerId);
        }
        return responseRepository.countAllByVacancy_Employer_Id(employerId);
    }

    @Override
    public Boolean isApplicantApplied(Long vacancyId, Long applicantId) {
        return responseRepository.existsByVacancy_IdAndResume_Applicant_Id(vacancyId, applicantId);
    }

    @Override
    public ResponseDto getResponseByVacancyIdAndUserId(Long vacancyId, Long authId) {
        Response response = responseRepository.findByVacancyIdAndUserId(vacancyId, authId)
                .orElseThrow(() -> new ResponseNotFoundException("Отклик не найден"));
        log.info("Получен отклик по вакансии {} и пользователю {}", vacancyId, authId);
        return responseMapper.toDto(response);
    }

    @Override
    public Long getInterlocutorId(ResponseDto response, Long currentUserId) {
        Long applicantId = response.getResume().getApplicant().getId();
        Long employerId = response.getVacancy().getEmployer().getId();

        return currentUserId.equals(applicantId) ? employerId : applicantId;
    }

    private Pageable getPageable(Integer page, Integer size) {
        return PageRequest.of(page - 1, size);
    }

    private Page<ResponseDto> getResponsePage(Supplier<Page<Response>> supplier) {
        Page<ResponseDto> responses = supplier.get()
                .map(responseMapper::toDto);

        if (responses.isEmpty() || !responses.hasContent()) {
            throw new ResponseNotFoundException("Нет откликов на вакансии");
        }
        log.info("Получено откликов на вакансии: {}", responses.getSize());
        return responses;
    }
}
