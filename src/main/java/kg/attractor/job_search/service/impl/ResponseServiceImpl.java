package kg.attractor.job_search.service.impl;

import kg.attractor.job_search.service.ResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ResponseServiceImpl implements ResponseService {
    @Override
    public HttpStatus applyVacancy(Long vacancyId) {
        //TODO отклик на вакансию по id соискателю, проверка на соикателя по id
        return HttpStatus.OK;
    }
}
