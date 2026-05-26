package com.czh.leave.mapper;

import com.czh.leave.entity.ApprovalStep;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ApprovalStepMapper {
    @Select("SELECT * FROM approval_steps WHERE flow_id = #{flowId} AND step_order = #{stepOrder}")
    ApprovalStep selectByFlowAndOrder(Long flowId, Integer stepOrder);

    @Select("SELECT * FROM approval_steps WHERE flow_id = #{flowId} AND step_order = #{stepOrder}")
    ApprovalStep selectNextStep(Long flowId, Integer stepOrder);
}
