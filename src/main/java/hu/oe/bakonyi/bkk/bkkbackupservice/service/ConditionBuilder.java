package hu.oe.bakonyi.bkk.bkkbackupservice.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConditionBuilder {

    ConditionalVariable variable;
    ConditionalOperator operator;
    double delta;
    double value;

    public enum ConditionalOperator {
        ISLOWER("<"), ISGREATER(">"), EQUALS("="), INDELTA("delta");

        String operator;

        ConditionalOperator(String value) {
            this.operator = value;
        }
    }

    public enum ConditionalVariable {
        RAIN("rain"), SNOW("snow"), TEMPERATURE("temperature"), HUMIDITY("humidity"), LATENESS("lateness"), VISIBILITY("visibility"), PRECIP("precip");

        String value;

        ConditionalVariable(String value) {
            this.value = value;
        }
    }

}
