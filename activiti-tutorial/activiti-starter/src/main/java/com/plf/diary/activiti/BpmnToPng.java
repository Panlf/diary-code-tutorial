package com.plf.diary.activiti;

import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.impl.util.io.InputStreamSource;
import org.activiti.image.ProcessDiagramGenerator;
import org.activiti.image.impl.DefaultProcessDiagramGenerator;
import org.apache.batik.transcoder.keys.LengthKey;
import org.junit.Test;

import java.io.*;
import java.util.Collections;


/**
 * @author panlf
 * @date 2023/7/27
 */
public class BpmnToPng {

    @Test
    public void test04() throws Exception {
        /*convertBpmnToImage("C:\\Users\\Breeze\\Downloads\\holiday.bpmn",
                "C:\\Users\\Breeze\\Downloads\\copy.svg");*/

        System.out.println(new LengthKey().isCompatibleValue(400.5f));
    }

    public static void convertBpmnToImage(String bpmnFilePath, String outputImagePath) throws Exception{
        BpmnModel bpmnModel = new BpmnXMLConverter()
                .convertToBpmnModel(new InputStreamSource(new FileInputStream(bpmnFilePath)),
                        true, false);
        ProcessDiagramGenerator diagramGenerator = new DefaultProcessDiagramGenerator();
        OutputStream outputStream = new FileOutputStream(outputImagePath);
        InputStream inputStream = diagramGenerator.generateDiagram(bpmnModel,Collections.emptyList());
        try {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer))!=-1){
                outputStream.write(buffer,0,bytesRead);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            inputStream.close();
            outputStream.close();
        }
    }
}
