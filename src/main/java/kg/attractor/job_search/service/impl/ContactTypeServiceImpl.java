package kg.attractor.job_search.service.impl;

import kg.attractor.job_search.dao.ContactTypeDao;
import kg.attractor.job_search.exception.ContactTypeNotFoundException;
import kg.attractor.job_search.service.ContactTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ContactTypeServiceImpl implements ContactTypeService {
    private  final ContactTypeDao contactTypeDao;

    @Override
    public Long getContactTypeIdIfPresent(Long id) {
        try {
            return contactTypeDao.getContactTypeById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ContactTypeNotFoundException("Не существует типа контакта с таким id!");
        }
    }
}
