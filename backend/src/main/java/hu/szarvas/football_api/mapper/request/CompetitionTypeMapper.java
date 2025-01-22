package hu.szarvas.football_api.mapper.request;

import hu.szarvas.football_api.model.CompetitionType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CompetitionTypeMapper {
    public static CompetitionType toCompetitionType(String type) {
        try {
            return CompetitionType.valueOf(type);
        } catch (IllegalArgumentException e) {
            log.warn("Unknown competition type: {}", type);
            return CompetitionType.CUP;
        }
    }
}
