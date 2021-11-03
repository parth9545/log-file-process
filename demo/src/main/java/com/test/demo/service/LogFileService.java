package com.test.demo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.demo.beans.EventBean;
import com.test.demo.beans.FileBean;
import com.test.demo.commons.FileConstants;
import com.test.demo.commons.StatusEnum;
import com.test.demo.dao.EventBeanDao;
import com.test.demo.dao.FileBeanDao;
import com.test.demo.util.InValidFilePathException;
import com.test.demo.util.Utility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.logging.Logger;
import java.util.stream.Stream;

@Slf4j
@Service
public class LogFileService {

    @Autowired
    private FileBeanDao fileBeanDao;

    @Autowired
    private EventBeanDao eventBeanDao;

    @Autowired
    private ObjectMapper objectMapper;

    public boolean processLogFile(String[] args) {
        FileBean fileBean = new FileBean();
        try {
            Stream<String> logFileStream = getFilePathFromArgs(args, fileBean);

            Map<String, EventBean> beanMap = new HashMap<>();

            logFileStream.forEach(processFileDataStream(beanMap));

            eventBeanDao.saveAll(beanMap.values());

            fileBean.setStatus(FileConstants.COMPLETE_STATUS);

        } catch (InValidFilePathException | IOException e) {
            log.error(e.getMessage());
        } finally {
            fileBeanDao.save(fileBean);
        }
        return true;
    }

    private Consumer<String> processFileDataStream(Map<String, EventBean> beanMap) {
        return bean -> {
            try {
                Map<String, String> map = objectMapper.readValue(bean, Map.class);
                EventBean eventBean = beanMap.get(map.get(FileConstants.ID));

                if (null == eventBean) {
                    eventBean = new EventBean();
                    eventBean.setEventId(map.get(FileConstants.ID));
                    beanMap.put(eventBean.getEventId(), eventBean);
                }

                if (StatusEnum.STARTED.name().equals(map.get(FileConstants.STATUS))) {
                    eventBean.setStartTime(Long.valueOf(map.get(FileConstants.TIMESTAMP)));
                } else if (StatusEnum.FINISHED.getValue().equals(map.get(FileConstants.STATUS))) {
                    eventBean.setEndTime(Long.valueOf(map.get(FileConstants.TIMESTAMP)));
                } else {
                    log.error("In valid status : " + map.get(FileConstants.STATUS));
                }

                if (eventBean.getStartTime() != null && eventBean.getEndTime() != null) {
                    long duration = eventBean.getEndTime() - eventBean.getStartTime();
                    eventBean.setAlert(duration > 4 ? true : false);
                    eventBean.setDuration(duration);
                }

                eventBean.setHost(map.get(FileConstants.HOST));
                eventBean.setType(map.get(FileConstants.TYPE));

            } catch (JsonProcessingException e) {
                log.error(e.getMessage());
            }
        };
    }

    private Stream<String> getFilePathFromArgs(String[] args, FileBean fileBean) throws InValidFilePathException, IOException {
        if (null != args && args.length > 0) {
            Path path = Paths.get(args[0]);
            if (Files.exists(path)) {
                setFileBean(fileBean, path);
                return Files.lines(path);
            }
        }
        throw new InValidFilePathException("File path not found");
    }

    private void setFileBean(FileBean fileBean, Path path) {
        fileBean.setFileName(path.toFile().getName());
        fileBean.setProcessedDate(new Date());
        fileBean.setStatus(FileConstants.PROCESSING_STATUS);
        fileBean.setProcessedDate(new Date());
    }

    public void getAll() {
        fileBeanDao.findAll().forEach(v -> log.info(v.toString()));
        eventBeanDao.findAll().forEach(v -> log.info(v.toString()));
    }
}
