package kg.attractor.job_search.service;

public interface ResponseService {
    Long applyVacancy(Long vacancyId, Long resumeId, Long applicantId);
}
