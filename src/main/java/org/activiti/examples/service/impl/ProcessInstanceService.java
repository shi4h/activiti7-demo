package org.activiti.examples.service.impl;

import org.activiti.api.process.model.builders.ProcessPayloadBuilder;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.api.runtime.shared.query.Pageable;
import org.activiti.examples.resp.SuccessResp;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author wangkai
 * @since JDK8
 */
@Service
public class ProcessInstanceService {

    @Resource
    private ProcessRuntime processRuntime;

    public Object startProcess(String processDefinitionKey) {
        processRuntime.start(ProcessPayloadBuilder
                .start()
                .withProcessDefinitionKey(processDefinitionKey)
//                .withVariable("fileContent", content)
                .build());
        return new SuccessResp();
    }

    public Object getProcessInstances() {
        return processRuntime.processInstances(Pageable.of(0, 100));
    }

    public Object deleteProcess(String processInstanceId) {
        processRuntime.delete(ProcessPayloadBuilder.delete(processInstanceId));
        return new SuccessResp();
    }

}
