package com.plf.diary.activiti;

import org.activiti.engine.*;
import org.activiti.engine.impl.persistence.entity.VariableInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.jupiter.api.Test;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author panlf
 * @date 2023/7/11
 */
public class ActivitiCommonTest {

    //activiti 初始化
    @Test
    public void test01(){
        //通过 getDefaultProcessEngine() 方法获取流程引擎对象 会加载resources 目录下的activiti.cfg.xml
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        System.out.println(defaultProcessEngine);
    }

    @Test
    public void test02(){
        ProcessEngine processEngine = ProcessEngineConfiguration
                .createStandaloneProcessEngineConfiguration()
                .setJdbcDriver("")
                .setJdbcUrl("")
                .setJdbcUsername("")
                .setJdbcPassword("")
                .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE)
                .buildProcessEngine();
        System.out.println(processEngine);
    }

    // 流程部署操作
    /**
     * act_re_deployment 流程部署表
     * act_re_procdef 流程定义表
     */
    @Test
    public void test03(){
        // 1. 获取ProcessEngine对象
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        // 2. 完成流程的部署操作 需要通过RepositoryService来完成
        RepositoryService repositoryService = defaultProcessEngine.getRepositoryService();

        Deployment deploy = repositoryService.createDeployment()
                .addClasspathResource("flow/test3.bpmn20.xml")
                .name("请假流程-监听器")
                .deploy(); //是一个流程部署的行为，可以部署多个流程定义
        System.out.println(deploy.getId());
        System.out.println(deploy.getName());
    }

    //查询当前部署的流程
    @Test
    public void test04(){
        // 1. 获取ProcessEngine对象
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        // 2. 完成流程的部署操作 需要通过RepositoryService来完成
        RepositoryService repositoryService = defaultProcessEngine.getRepositoryService();

        //repositoryService.createDeploymentQuery() 查询流程部署的相关信息

        //repositoryService.createProcessDefinitionQuery()  查询部署的流程的相关定义

        List<Deployment> deploymentList = repositoryService.createDeploymentQuery().list();
        if(!CollectionUtils.isEmpty(deploymentList)){
            deploymentList.forEach(x->{
                System.out.println(x.getId());
                System.out.println(x.getName());
            });
        }

        List<ProcessDefinition> processDefinitionList = repositoryService.createProcessDefinitionQuery().list();

        if(!CollectionUtils.isEmpty(processDefinitionList)){
            processDefinitionList.forEach(x->{
                //test1:1:3
                System.out.println(x.getId());
                System.out.println(x.getName());
                System.out.println(x.getDescription());
                System.out.println(x.getKey());
            });
        }
    }

    // 发起流程
    // 发起流程成功后，在对应的act_ru_task中就有一条对应的待办记录
    @Test
    public void test05(){
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        // 发起流程  需要通过 runtimeService 来实现
        RuntimeService runtimeService = defaultProcessEngine.getRuntimeService();
        // 通过流程定义ID来启动流程 返回的是流程实例对象
        ProcessInstance processInstance = runtimeService.startProcessInstanceById("test3:1:20003");

        // 2501
        System.out.println("id : "+processInstance.getId());
        System.out.println("deploymentId : "+processInstance.getDeploymentId());
        System.out.println("description : "+processInstance.getDescription());
    }

    // 发起流程 - 值表达式
    @Test
    public void test05_1(){
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        // 发起流程  需要通过 runtimeService 来实现
        RuntimeService runtimeService = defaultProcessEngine.getRuntimeService();

        Map<String,Object> param = new HashMap<>();
        param.put("assign1","张三");
        param.put("assign2","李四"); // 在审批之前审批也可以

        // 通过流程定义ID来启动流程 返回的是流程实例对象
        ProcessInstance processInstance = runtimeService.startProcessInstanceById("test2:1:10003",param);

        System.out.println("id : "+processInstance.getId());
        System.out.println("deploymentId : "+processInstance.getDeploymentId());
        System.out.println("description : "+processInstance.getDescription());
    }

    /**
     * 方法表达式
     *
     * 方法表达式 Method expression ： 调用一个方法，可以带或不带参数。
     *  当调用不带参数的方法时，要确保在方法名后添加空括号（以避免与值表达式混肴）。
     *  传递的参数可以是字面值（literal value），也可以是表达式，他们会被自动解析。
     *
     *  ${printer.print()}
     *  ${myBean.getAssignee()}
     *  ${myBean.addNewOrder('orderName')}
     *  ${myBean.doSomething(myVar,execution)}
     *  myBean 是Spring容器中的Bean对象，表示调用的是bean的addNewOrder方法
     */


    //待办流程
    @Test
    public void test06(){
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        // 待办查询 执行中的任务处理通过 TaskService 来实现
        TaskService taskService = defaultProcessEngine.getTaskService();
        // Task 对象对应的其实就是 act_ru_task 这张表的记录
        List<Task> taskList = taskService.createTaskQuery().taskAssignee("张三").list();

        if(!CollectionUtils.isEmpty(taskList)){
            taskList.forEach(x->{
                //2505
                System.out.println(x.getId());

                System.out.println(x.getName());

                System.out.println(x.getAssignee());
            });
        }

    }

    //任务审批
    @Test
    public void test07(){
        /**
         * 审批涉及的表
         *  act_re_deployment   部署流程的记录表：一次部署行为会产生一张表
         *  act_re_procdef  流程定义表：一张流程图对应的表
         *  act_hi_procinst 流程实例表：发起一个流程。就会创建对应的一张表
         *  act_ru_task 流程待办表：当前需要审批的记录表，节点审批后就会被删除
         *  act_hi_actinst  历史记录：流程审批节点的记录信息
         */

        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        //做任务申请 也需要通过 TaskService 来实现
        TaskService taskService = defaultProcessEngine.getTaskService();

        //根据当前登录用户查询出对应的待办信息
        List<Task> taskList = taskService.createTaskQuery().taskAssignee("lisi").list();

        if(!CollectionUtils.isEmpty(taskList)){
            taskList.forEach(x->{
               taskService.complete(x.getId());

               //任务交接
               //taskService.setAssignee(x.getId(),"zhangsan");
            });
        }
    }

    /**
     * act_re_* re 代表 repository 。 带有这个前缀的表包含静态信息，例如流程定义与流程资源（图片、规则等）。
     * act_ru_* ru 代表 runtime 。这些表存储运行时信息，例如流程实例（process instance）、用户任务（user task）、
     *                          变量（variable）、作业（job）等。Activiti只在实例运行中保存运行时数据，并在流程结束时删除记录，
     *                          这样保证运行时表小和块。
     * act_id_*  id 代表 identity。这些表包含身份信息，例如用户、组等
     * act_hi_*  hi 代表 history。 这些表存储历史数据，例如已完成的流程实例、变量、任务等。
     * act_ge_*  通用数据。用于不同场景下。
     */


    /**
     * 流程变量
     *   流程变量可以将数据添加到流程的运行时状态中，或者更具体说，变量作用域中。改变实体的各种API可以用来更新这些附加的变量。
     *
     * 运行时变量
     *   流程实例运行时的变量，存入act_ru_variable表中。在流程实例运行结束时，此实例的变量在表中删除。在流程实例创建及启动时，
     *   可设置流程变量。所有startProcessInstanceXXX方法都有一个可选参数用于设置变量。
     *
     * 注意：由于流程实例结束时，对应在运行时表的数据跟着被删除。所以查询一个已经完结流程实例的变量，只能在历史变量表中查找。
     *
     * 当然运行时变量我们也可以根据对应的作用域把他分为 全局变量 和 局部变量
     *
     *
     * 全局变量
     *     流程变量的默认作用域是流程实例。当一个流程变量的作用域为流程实例时，可以称为global变量
     *     注意：如：Global变量：userId（变量名） zhangsan（变量值）
     *
     *     global变量中变量名不允许重复、设置相同名称的变量，后设置的值会覆盖前设置的变量值
     *
     *
     * 局部变量
     *
     *     任务和执行实例仅仅是针对一个任务和一个执行实例范围，范围没有流程实例大，称为local变量
     *
     *     local变量由于在不同的任务或不同的执行实例中，作用域互不影响，变量名可以相同没有影响。
     *     local变量名也可以和global变量名相同，没有影响。
     *
     *     我们通过RuntimeService设置的local变量绑定的是executionId。在该流程中有效。
     */


    /**
     * 查询当前的流程变量信息
     */
    @Test
    public void test08(){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RuntimeService runtimeService = processEngine.getRuntimeService();
        //获取流程变量信息 获取某个流程实例的变量信息
        Map<String, VariableInstance> variableInstances = runtimeService.getVariableInstances("");

        variableInstances.forEach((k,v)->{
            System.out.println(k+" = "+v);
        });
    }

    @Test
    public void test09(){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RuntimeService runtimeService = processEngine.getRuntimeService();
        //设置本地局部变量
        runtimeService.setVariableLocal("60007","aaa","111");
        //executionId
        Map<String, Object> variablesLocal = runtimeService.getVariablesLocal("60007");
        System.out.println(variablesLocal);

        TaskService taskService = processEngine.getTaskService();
        // taskId 通过taskService来绑定本地流程变量。需要指定对应的taskId
        Map<String, Object> serviceVariablesLocal = taskService.getVariablesLocal("60004");
        System.out.println(serviceVariablesLocal);

        // TaskService绑定的Local变量的作用域只是在当前的Task有效。而通过RuntimeService绑定的Local变量作用的访问是executionId
    }


    /*
    * 历史变量
    *   存入act_hi_varinst表中。在流程启动时，流程变量会同时存入历史变量中；
    *   在流程结束时，历史表中的变量仍然存在，可理解为“永久代”的流程变量
    *
    * 获取已完成的、id为XXX的流程中，所有HistoricVariableInstances（历史变量实例），并以变量名排序
    *
    * */
    @Test
    public void test10(){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        processEngine.getHistoryService().createHistoricVariableInstanceQuery()
                .processInstanceId("XXX")
                .orderByVariableName().desc()
                .list();
    }


    /**
     * 候选人 待办任务查询
     *  如果这个任务被其他人拾取，那其他候选人就不能查询到任务信息了
     *  归还后就能重新查找到该任务
     */
    @Test
    public void test11(){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();

        List<Task> tasks = taskService.createTaskQuery().taskCandidateUser("张三").list();

        if(!CollectionUtils.isEmpty(tasks)){
            tasks.forEach(x->{
                System.out.println(x.getId());
            });
        }
    }

    /**
     * 候选人 待办任务 拾取 操作
     *  从候选人 --> 处理人
     */
    @Test
    public void test12(){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();

        List<Task> tasks = taskService.createTaskQuery().taskCandidateUser("李四").list();

        if(!CollectionUtils.isEmpty(tasks)){
            tasks.forEach(x->{
                //李四 拾取了 这个任务的审批权限 --> 变成了这个任务的审批人
                taskService.claim(x.getId(),"李四");
            });

        }
    }


    /**
     * 候选人 待办任务 归还 操作，就放弃了审批人操作
     *   拾取的用户 不审批了
     *
     *   其他候选人可以重新拾取人了
     */
    @Test
    public void test13(){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();

        List<Task> tasks = taskService.createTaskQuery()
                .taskCandidateOrAssigned("李四") //根据 审批人 或者 候选人 来查询待办任务
                .list();

        if(!CollectionUtils.isEmpty(tasks)){
            tasks.forEach(x->{
                 // 归还任务 归还操作的本质就是设置审批人为空
                 taskService.unclaim(x.getId());
            });

        }
    }


    /**
     * 候选人组
     *  当候选人很多的情况下，我们可以分组来处理，先创建组，然后把用户分配到这个组中
     *
     */
    @Test
    public void test14(){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        String group = "人事部";
        List<Task> tasks = taskService.createTaskQuery()
                .taskCandidateGroup(group)
                .list();

        if(!CollectionUtils.isEmpty(tasks)){
            tasks.forEach(x->{
                System.out.println(x.getId());
            });
        }
    }
}

