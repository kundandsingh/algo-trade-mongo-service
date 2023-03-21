package algo.trade.mongo.controller;

import algo.trade.mongo.dao.CandleRepository;
import algo.trade.mongo.model.Candle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/data-retention")
public class DataRetentionController {
    @Autowired
    private CandleRepository candleRepository;


    @PostMapping(value = "/saveCandle")
    public Candle saveCandle(@RequestBody Candle candle)  {
        return candleRepository.save(candle);
    }

    @GetMapping(value = "/getCandles")
    public List<Candle> getCandles()  {
        return candleRepository.findAll();
    }

    @GetMapping(value = "/getCandleById/{id}")
    public Candle getBooks(@PathVariable("id") Long candleId) throws Exception {
        Optional<Candle> optionalCandle = candleRepository.findById(candleId);
        if (!optionalCandle.isPresent()) {
            throw new Exception("Candle not found with id : " + candleId);
        }
        Candle candle = optionalCandle.get();
        return candle;

    }

}
