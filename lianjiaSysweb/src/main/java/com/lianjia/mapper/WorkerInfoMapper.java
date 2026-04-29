package com.lianjia.mapper;

import com.lianjia.entity.WorkerInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface WorkerInfoMapper {

    WorkerInfo selectById(@Param("id") Long id);

    WorkerInfo selectByPhone(@Param("phone") String phone);

    WorkerInfo selectByUserId(@Param("userId") Long userId);

    List<WorkerInfo> selectList(WorkerInfo worker);

    List<WorkerInfo> selectPage(@Param("worker") WorkerInfo worker,
                                 @Param("offset") int offset,
                                 @Param("pageSize") int pageSize);

    long selectCount(WorkerInfo worker);

    int insert(WorkerInfo worker);

    int update(WorkerInfo worker);

    int deleteById(@Param("id") Long id);

    int updateStatus(@Param("id") Long id, @Param("status") Integer status);
}
