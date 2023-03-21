package algo.trade.mongo.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection ="candles")
public class Candle {
    @Id
     private Long epoch;
    private double open;
    private double close;
    private double high;
    private double low;
}
