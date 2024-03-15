package com.aminurdev.category.domain.excepation;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundExcepation extends RuntimeException{

    public ResourceNotFoundExcepation(String message)
    {
        super(message);
    }
}
