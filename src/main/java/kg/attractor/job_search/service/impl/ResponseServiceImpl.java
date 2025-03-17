package kg.attractor.job_search.service.impl;

import kg.attractor.job_search.dto.UserDto;
import kg.attractor.job_search.service.ResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ResponseServiceImpl implements ResponseService {
    @Override
    public List<UserDto> getApplicationsByVacancyId(Long vacancyId, Long employerId) {
        //TODO получение соискателей на вакансию по id вакансии, проверка на работодателя по id
        return List.of();
    }

    @Override
    public HttpStatus applyVacancy(Long vacancyId, Long applicantId) {
        //TODO отклик на вакансию по id соискателю, проверка на соикателя по id
        return HttpStatus.OK;
    }
}
