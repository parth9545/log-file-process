package com.test.demo.dao;

import com.test.demo.beans.EventBean;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventBeanDao extends PagingAndSortingRepository<EventBean, Long> {
}
