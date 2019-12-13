package hu.oe.bakonyi.bkk.bkkbackupservice.service;

import hu.oe.bakonyi.bkk.bkkbackupservice.documents.BackupRepository;
import hu.oe.bakonyi.bkk.bkkbackupservice.documents.model.MDBBkkBackup;
import hu.oe.bakonyi.bkk.bkkbackupservice.documents.model.MDBBkkBackupData;
import hu.oe.bakonyi.bkk.bkkbackupservice.documents.model.MDBBkkBackupIndex;
import hu.oe.bakonyi.bkk.bkkbackupservice.documents.model.Time;
import hu.oe.bakonyi.bkk.bkkbackupservice.model.ConditionalPossibilityResponse;
import hu.oe.bakonyi.bkk.bkkbackupservice.model.ConditionalQueryingRequest;
import hu.oe.bakonyi.bkk.bkkbackupservice.model.P;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class BackupDataAnalyzer {

    @Autowired
    BackupRepository backupRepository;

    public ConditionalPossibilityResponse conditionalProbability(ConditionalQueryingRequest request) {
        /*
         * P(A|B) = P(A metszet B)/P(A)
         * */
        if (request == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        if(!request.getCommissionerEvent().getRoute().contains("BKK") || !request.getQuestionableEvent().getRoute().contains("BKK")){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        if (request.getQuestionableEvent() == null || request.getQuestionableEvent().getConditions().isEmpty())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        if (request.getCommissionerEvent() == null || request.getCommissionerEvent().getConditions().isEmpty())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        //A kérdéses esemény
        P pA = calculateP(request.getQuestionableEvent().getRoute(), request.getQuestionableEvent().getFrom(), request.getQuestionableEvent().getTo(), request.getQuestionableEvent().getConditions());
        //A biztos esemény
        P pB = calculateP(request.getCommissionerEvent().getRoute(), request.getCommissionerEvent().getFrom(), request.getCommissionerEvent().getTo(), request.getCommissionerEvent().getConditions());
        //A két eseménytér metszete
        List<MDBBkkBackupData> intersectedData = new ArrayList<>(CollectionUtils.intersection(pA.getData(), pB.getData()));

        P intersectedP = P.builder().data(intersectedData).fullEvent(pA.getPositiveCases() + pB.getPositiveCases())
                .positiveCases(intersectedData.size()).possibility((double) intersectedData.size() / (pA.getPositiveCases() + pB.getPositiveCases())).build();

        if (intersectedData.isEmpty()) {
            return ConditionalPossibilityResponse.builder().route(request.getCommissionerEvent().getRoute())
                    .now(Instant.now())
                    .possibility(0)
                    .responseValue(ConditionalPossibilityResponse.ResponseValue.OK_NOSUCHDATA)
                    .message("Nincs az ön által leírt adathoz megfelelő megfigyelés").build();
        }
        return ConditionalPossibilityResponse.builder().route(request.getCommissionerEvent().getRoute())
                .now(Instant.now())
                .possibility(intersectedP.getPossibility() / pB.getPossibility())
                .responseValue(ConditionalPossibilityResponse.ResponseValue.OK_CALCULATED)
                .message("Az ön által leírt szcenárió valószínűsége: " + intersectedP.getPossibility() / pB.getPossibility()).build();
    }

    P calculateP(String route, Time from, Time to, List<ConditionBuilder> conditions) {
        List<MDBBkkBackupData> positiveCases = new ArrayList<>();
        List<MDBBkkBackupData> data = calculateDataTimes(from, to, route);

        for (MDBBkkBackupData x : data) {
            if (buildCondition(x, conditions)) {
                positiveCases.add(x);
            }
        }
        return P.builder().data(positiveCases).fullEvent(data.size()).positiveCases(positiveCases.size()).possibility((double) positiveCases.size() / data.size()).build();
    }

    List<MDBBkkBackupData> calculateDataTimes(Time from, Time to, String route) {
        List<MDBBkkBackupData> data = new ArrayList<>();

        for (int month = from.getMonth(); month <= to.getMonth(); month++) {
            for (int day = from.getDayOfWeek(); day <= to.getDayOfWeek(); day++) {
                for (int hour = from.getHour(); hour <= to.getHour(); hour++) {
                    MDBBkkBackupIndex index = MDBBkkBackupIndex.builder().time(
                            Time.builder().month(month).dayOfWeek(day).hour(hour).build()
                    ).routeId(Double.parseDouble(route.split("_")[1])).build();
                    MDBBkkBackup routeData = backupRepository.findById(index).get();
                    data.addAll(routeData.getDatas());
                }
            }
        }
        return data;
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
            case PRECIP:
                return x.getWeather().getRain() + x.getWeather().getSnow();
            default:
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Ilyen lekérdezésre nincs lehetősége");
        }
    }

    boolean executeConditioning(ConditionBuilder condition, double x) {
        switch (condition.operator) {
            case ISLOWER:
                return x > condition.value;
            case ISGREATER:
                return x < condition.value;
            case EQUALS:
                return x == condition.value;
            case INDELTA:
                return x < condition.value + condition.delta && x > condition.value - condition.delta;
            default:
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Ilyen lekérdezésre nincs lehetősége");
        }
    }

}
