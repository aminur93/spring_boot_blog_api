package com.aminurdev.category.service;

import com.aminurdev.category.domain.entity.Replay;
import com.aminurdev.category.domain.model.ReplayRequest;
import com.aminurdev.category.response.pagination.PaginatedResponse;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface ReplayService {

    PaginatedResponse<Replay> index(Sort.Direction direction, int page, int perPage);

    List<Replay> allReplays();

    Replay store(ReplayRequest replayRequest);

    void delete(Integer replayId);
}
