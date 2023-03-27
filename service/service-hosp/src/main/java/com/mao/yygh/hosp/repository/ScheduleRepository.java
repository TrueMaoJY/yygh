package com.mao.yygh.hosp.repository;

import com.mao.yygh.model.hosp.Schedule;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @InterfaceName ScheduleRepository
 * @Description TODO
 * @Author MAOjy
 * @DATE 2023/3/15 10:09
 * @Version 1.0
 */
@Repository
public interface ScheduleRepository extends MongoRepository<Schedule,String> {
    Schedule findScheduleByHoscodeAndHosScheduleId(String hoscode, String hosScheduleId);

    Schedule getScheduleByHoscodeAndDepcode(String hoscode, String depcode);

    List<Schedule> findScheduleByHoscodeAndDepcodeAndWorkDate(String hoscode, String depcode, Date workDate);
}
