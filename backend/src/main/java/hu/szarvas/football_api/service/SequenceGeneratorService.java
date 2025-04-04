package hu.szarvas.football_api.service;

import hu.szarvas.football_api.model.DatabaseSequence;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

/**
 * Service for generating sequential IDs for MongoDB documents.
 * Uses a separate collection to maintain sequence counters.
 */
@Service
@RequiredArgsConstructor
public class SequenceGeneratorService {
    private final MongoOperations mongoOperations;

    /**
     * Generates the next sequence number for a given sequence name.
     * Atomically increments and returns the sequence value.
     *
     * @param sequenceName The name of the sequence to increment
     * @return The next sequence number
     */
    public int generateSequence(String sequenceName) {
        DatabaseSequence counter = mongoOperations.findAndModify(
                Query.query(Criteria.where("_id").is(sequenceName)),
                new Update().inc("sequence", 1),
                FindAndModifyOptions.options().returnNew(true).upsert(true),
                DatabaseSequence.class);
        return counter != null ? counter.getSequence() : 1;
    }
}
