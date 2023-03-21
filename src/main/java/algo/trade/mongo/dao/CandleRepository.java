package algo.trade.mongo.dao;

import algo.trade.mongo.model.Candle;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CandleRepository extends MongoRepository<Candle,Long> {
}
