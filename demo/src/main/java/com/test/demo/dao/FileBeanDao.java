package com.test.demo.dao;

import com.test.demo.beans.FileBean;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileBeanDao extends PagingAndSortingRepository<FileBean, Long> {
}
