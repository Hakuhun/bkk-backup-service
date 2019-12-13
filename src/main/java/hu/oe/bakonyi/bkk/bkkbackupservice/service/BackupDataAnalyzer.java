package hu.oe.bakonyi.bkk.bkkbackupservice.service;

import hu.oe.bakonyi.bkk.bkkbackupservice.documents.BackupRepository;
import hu.oe.bakonyi.bkk.bkkbackupservice.documents.model.MDBBkkBackup;
import hu.oe.bakonyi.bkk.bkkbackupservice.documents.model.MDBBkkBackupData;
import hu.oe.bakonyi.bkk.bkkbackupservice.documents.model.MDBBkkBackupIndex;
import hu.oe.bakonyi.bkk.bkkbackupservice.documents.model.Time;
import hu.oe.bakonyi.bkk.bkkbackupservice.model.AnalyzerRequestData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Service
public class BackupDataAnalyzer {

    @Autowired
    BackupRepository backupRepository;

    public double conditionalProbability(AnalyzerRequestData data){
        return conditionalProbability(data.getRealTime(), data.getRoute(),data.getA(), data.getB());
    }

    double conditionalProbability(Instant realTime, String route, List<ConditionBuilder> a, List<ConditionBuilder> b) {
        /*
         * P(A|B) = P(A metszet B)/P(A)
         * */

        int hour = realTime.atZone(ZoneId.of("Europe/Budapest")).getHour();
        int month = realTime.atZone(ZoneId.of("Europe/Budapest")).getMonthValue();
        int day = realTime.atZone(ZoneId.of("Europe/Budapest")).getDayOfWeek().getValue();
        Time time = Time.builder().dayOfWeek(day).hour(hour).month(month).build();

        double pA = p(route,time,a);
        double pB = p(route, time, b);
        List<ConditionBuilder> intercept = new ArrayList<>();
        intercept.addAll(a); intercept.addAll(b);
        double pAinterceptB = p(route, time, intercept);
        return pAinterceptB/pB;
    }

    double p(String route, Time time, List<ConditionBuilder> conditions) {
        MDBBkkBackupIndex index = MDBBkkBackupIndex.builder().time(time)
                .routeId(Double.parseDouble(route.split("_")[1])).build();

        MDBBkkBackup routeData = backupRepository.findById(index).get();
        double total = routeData.getDatas().size();
        double conditionalSum = 0;

        for (MDBBkkBackupData x : routeData.getDatas()) {
            //if (x.getValue() > 5 && x.getWeather().getRain() == 0){
            if (buildCondition(x, conditions)) {
                conditionalSum++;
            }
        }
        return conditionalSum / total;
    }

    boolean buildCondition(MDBBkkBackupData x, List<ConditionBuilder> conditions) {
        List<Boolean> logicalValues = new ArrayList<>();
        for (ConditionBuilder condition : conditions) {
            double conditionVariableValue = getConditionalVariable(condition, x);
            logicalValues.add(executeConditioning(condition, conditionVariableValue));
        }
        return logicalValues.stream().noneMatch(logic -> !logic);
    }

    double getConditionalVariable(ConditionBuilder condition, MDBBkkBackupData x) {
        switch (condition.variable) {
            case RAIN:
                return x.getWeather().getRain();
            case SNOW:
                return x.getWeather().getSnow();
            case TEMPERATURE:
                return x.getWeather().getTemperature();
            case HUMIDITY:
                return x.getWeather().getHumidity();
            case VISIBILITY:
                return x.getWeather().getVisibility();
            case LATENESS:
                return x.getValue();
            case PRECIP: return x.getWeather().getRain() + x.getWeather().getSnow();
            default:
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Ilyen lekérdezésre nincs lehetősége");
        }
    }

    boolean executeConditioning(ConditionBuilder condition, double x){
        switch (condition.operator) {
            case ISLOWER: return x > condition.value;
            case ISGREATER: return x < condition.value;
            case EQUALS: return x == condition.value;
            case INDELTA: return x < condition.value + condition.delta && x > condition.value - condition.delta;
            default: throw new ResponseStatusException(HttpStatus.CONFLICT, "Ilyen lekérdezésre nincs lehetősége");
        }
    }

}
