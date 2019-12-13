package hu.oe.bakonyi.bkk.bkkbackupservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import hu.oe.bakonyi.bkk.bkkbackupservice.documents.BackupRepository;
import hu.oe.bakonyi.bkk.bkkbackupservice.documents.model.MDBBkkBackup;
import hu.oe.bakonyi.bkk.bkkbackupservice.documents.model.MDBBkkBackupData;
import hu.oe.bakonyi.bkk.bkkbackupservice.documents.model.Time;
import hu.oe.bakonyi.bkk.bkkbackupservice.model.ConditionalQueryingRequest;
import hu.oe.bakonyi.bkk.bkkbackupservice.model.QueryingCase;
import org.junit.jupiter.api.Assertions;
import  org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.ResourceUtils;

import java.io.IOException;
import java.time.Instant;
import java.util.Arrays;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class BackupAnalyzerTest {

    @MockBean
    BackupRepository repository;

    @Test
    public void executeConditioning_runOK(){
        ConditionBuilder condition = ConditionBuilder.builder()
                .value(1)
                .operator(ConditionBuilder.ConditionalOperator.ISGREATER)
                .variable(ConditionBuilder.ConditionalVariable.TEMPERATURE)
                .build();
        BackupDataAnalyzer analyzer = new BackupDataAnalyzer();
        Assertions.assertEquals(analyzer.executeConditioning(condition, 0), true);
        Assertions.assertEquals(analyzer.executeConditioning(condition, 2), false);

        condition.setOperator(ConditionBuilder.ConditionalOperator.ISLOWER);
        Assertions.assertEquals(analyzer.executeConditioning(condition, 0), false);
        Assertions.assertEquals(analyzer.executeConditioning(condition, 2), true);

        condition.setOperator(ConditionBuilder.ConditionalOperator.INDELTA);
        condition.setDelta(1);
        Assertions.assertEquals(analyzer.executeConditioning(condition, 1), true);
        Assertions.assertEquals(analyzer.executeConditioning(condition, 3), false);
    }

    @Test
    public void getConditionalVariable_runOk() throws IOException {
        BackupDataAnalyzer analyzer = new BackupDataAnalyzer();
        MDBBkkBackupData model = loadData("classpath:mdbdocumentdata.json");

        ConditionBuilder conditionWithTemperature = ConditionBuilder.builder()
                .value(1)
                .operator(ConditionBuilder.ConditionalOperator.ISGREATER)
                .variable(ConditionBuilder.ConditionalVariable.TEMPERATURE)
                .build();
        Assertions.assertEquals(analyzer.getConditionalVariable(conditionWithTemperature, model),3.11);
        Assertions.assertNotEquals(analyzer.getConditionalVariable(conditionWithTemperature, model),1.0);

        ConditionBuilder conditionWithVisibility = ConditionBuilder.builder()
                .value(1)
                .operator(ConditionBuilder.ConditionalOperator.ISGREATER)
                .variable(ConditionBuilder.ConditionalVariable.VISIBILITY)
                .build();
        Assertions.assertEquals(analyzer.getConditionalVariable(conditionWithVisibility, model),2.0);
        Assertions.assertNotEquals(analyzer.getConditionalVariable(conditionWithVisibility, model),1.0);

        ConditionBuilder conditionWithHumidity = ConditionBuilder.builder()
                .value(1)
                .operator(ConditionBuilder.ConditionalOperator.ISGREATER)
                .variable(ConditionBuilder.ConditionalVariable.HUMIDITY)
                .build();
        Assertions.assertEquals(analyzer.getConditionalVariable(conditionWithHumidity, model),100.0);
        Assertions.assertNotEquals(analyzer.getConditionalVariable(conditionWithHumidity, model),1.0);

        ConditionBuilder conditionWithSnow = ConditionBuilder.builder()
                .value(1)
                .operator(ConditionBuilder.ConditionalOperator.ISGREATER)
                .variable(ConditionBuilder.ConditionalVariable.SNOW)
                .build();
        Assertions.assertEquals(analyzer.getConditionalVariable(conditionWithSnow, model),0.0);
        Assertions.assertNotEquals(analyzer.getConditionalVariable(conditionWithSnow, model),1.0);
        ConditionBuilder conditionWithPrecip = ConditionBuilder.builder()
                .value(1)
                .operator(ConditionBuilder.ConditionalOperator.ISGREATER)
                .variable(ConditionBuilder.ConditionalVariable.PRECIP)
                .build();
        Assertions.assertEquals(analyzer.getConditionalVariable(conditionWithPrecip, model),0.0);
        Assertions.assertNotEquals(analyzer.getConditionalVariable(conditionWithPrecip, model),1.0);
        ConditionBuilder conditionWithValue = ConditionBuilder.builder()
                .value(1)
                .operator(ConditionBuilder.ConditionalOperator.ISGREATER)
                .variable(ConditionBuilder.ConditionalVariable.LATENESS)
                .build();
        Assertions.assertEquals(analyzer.getConditionalVariable(conditionWithValue, model),0.0);
        Assertions.assertNotEquals(analyzer.getConditionalVariable(conditionWithValue, model),1.0);
    }

    @Test
    public void buildCondition_isOk(){
        BackupDataAnalyzer analyzer = new BackupDataAnalyzer();
        MDBBkkBackupData model = loadData("classpath:mdbdocumentdata.json");

        ConditionBuilder conditionWithTemperature = ConditionBuilder.builder()
                .value(5)
                .operator(ConditionBuilder.ConditionalOperator.ISGREATER)
                .variable(ConditionBuilder.ConditionalVariable.TEMPERATURE)
                .build();

        ConditionBuilder conditionWithVisibility = ConditionBuilder.builder()
                .value(1)
                .operator(ConditionBuilder.ConditionalOperator.ISLOWER)
                .variable(ConditionBuilder.ConditionalVariable.VISIBILITY)
                .build();

        Assertions.assertEquals(analyzer.buildCondition(model, Arrays.asList(conditionWithTemperature, conditionWithVisibility)), true);

        conditionWithTemperature.setOperator(ConditionBuilder.ConditionalOperator.EQUALS);
        Assertions.assertEquals(analyzer.buildCondition(model, Arrays.asList(conditionWithTemperature, conditionWithVisibility)), false);
    }

    @Test
    public void asd(){

        MDBBkkBackup datas = loadBulkData("classpath:bulkmdbdocumentdata.json");
        Mockito.when(repository.findById(Mockito.any())).thenReturn(Optional.ofNullable(datas));
        ConditionBuilder a = ConditionBuilder.builder()
                .value(3)
                .operator(ConditionBuilder.ConditionalOperator.ISGREATER)
                .variable(ConditionBuilder.ConditionalVariable.TEMPERATURE)
                .build();

        ConditionBuilder b = ConditionBuilder.builder()
                .value(0)
                .operator(ConditionBuilder.ConditionalOperator.EQUALS)
                .variable(ConditionBuilder.ConditionalVariable.RAIN)
                .build();

        ConditionBuilder c = ConditionBuilder.builder()
                .value(3.0)
                .operator(ConditionBuilder.ConditionalOperator.EQUALS)
                .variable(ConditionBuilder.ConditionalVariable.VISIBILITY)
                .build();


        Time time = Time.builder().month(12).hour(23).dayOfWeek(4).build();

        ConditionalQueryingRequest request = ConditionalQueryingRequest.builder()
                .commissionerEvent(QueryingCase.builder().conditions(Arrays.asList(a,b)).route("BKK_3040").from(time).to(time).build())
                .questionableEvent(QueryingCase.builder().conditions(Arrays.asList(b,c)).route("BKK_3040").from(time).to(time).build())
                .build();

        BackupDataAnalyzer analyzer = new BackupDataAnalyzer();
        analyzer.backupRepository = repository;

        System.out.println("Valószínűség: "+analyzer.conditionalProbability(request));
        Assertions.assertNotEquals(analyzer.conditionalProbability(request),0.0);
    }

    public MDBBkkBackupData loadData(String path){
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        try {
            return mapper.readValue(ResourceUtils.getFile(path), MDBBkkBackupData.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public MDBBkkBackup loadBulkData(String path){
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        try {
            return mapper.readValue(ResourceUtils.getFile(path), MDBBkkBackup.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


}
