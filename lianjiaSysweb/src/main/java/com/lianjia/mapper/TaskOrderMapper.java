package com.lianjia.mapper;

import com.lianjia.entity.TaskOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TaskOrderMapper {

    TaskOrder selectById(@Param("id") Long id);

    TaskOrder selectByTaskNo(@Param("taskNo") String taskNo);

    TaskOrder selectByOrderId(@Param("orderId") Long orderId);

    List<TaskOrder> selectByWorkerId(@Param("workerId") Long workerId);

    List<TaskOrder> selectByWorkerIdAndStatus(@Param("workerId") Long workerId, @Param("status") String status);

    List<TaskOrder> selectList(TaskOrder task);

    List<TaskOrder> selectPage(@Param("task") TaskOrder task,
                                @Param("offset") int offset,
                                @Param("pageSize") int pageSize);

    long selectCount(TaskOrder task);

    int insert(TaskOrder task);

    int update(TaskOrder task);

    int updateStatus(@Param("id") Long id, @Param("status") String status);

    int deleteById(@Param("id") Long id);
}
