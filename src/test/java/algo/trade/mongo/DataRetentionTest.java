package algo.trade.mongo;

import algo.trade.mongo.controller.DataRetentionController;
import algo.trade.mongo.dao.CandleRepository;
import algo.trade.mongo.model.Candle;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
public class DataRetentionTest {

    @InjectMocks
    private  DataRetentionController dataRetentionController;
    @Mock
    private CandleRepository candleRepository;

     private ObjectMapper objectMapper;
     private ObjectWriter objectWriter;

     Candle candle1 = new Candle(1L,100,150,180,90);
     Candle candle2 = new Candle(2L,110,160,190,100);
     Candle candle3 = new Candle(3L,120,170,200,110);

    private MockMvc mockMvc;
    @BeforeAll
    public void setUp(){
        objectMapper =  new ObjectMapper();
        objectWriter = objectMapper.writer();
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(dataRetentionController).build();
    }

    @Test
    public void givenListOfCandles_whenGetAllCandles_thenReturnCandlesList() throws Exception{
        List<Candle> candlesList = new ArrayList<>();
        candlesList.add(candle1);
        candlesList.add(candle2);
        candlesList.add(candle3);
        Mockito.when(candleRepository.findAll()).thenReturn(candlesList);
        mockMvc.perform(MockMvcRequestBuilders
                .get("/data-retention/getCandles")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$",hasSize(3)))
                .andExpect(jsonPath("$[2].low",is(110.0)));
         }

   @Test
   public void giveCandleData_whenGetCandleData_thenSaveCandleData() throws Exception {
       Candle candle = buildCandle();
       Mockito.when(candleRepository.save(candle)).thenReturn(candle);
       MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/data-retention/saveCandle")
               .contentType(MediaType.APPLICATION_JSON)
               .accept(MediaType.APPLICATION_JSON)
               .content(objectWriter.writeValueAsString(candle));

       mockMvc.perform(mockRequest)
               .andExpect(status().isOk())
               .andExpect(jsonPath("$",notNullValue()))
               .andExpect(jsonPath("$.low",is(100.0)));

   }

   @Test
   public void givenIdOfCandle_whenGetIdCandle_thenReturnCandle() throws Exception {
        Mockito.when(candleRepository.findById(candle1.getEpoch())).thenReturn(Optional.ofNullable(candle1));
       mockMvc.perform(MockMvcRequestBuilders
                       .get("/data-retention/getCandleById/1")
                       .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
              // .andExpect(MockMvcResultMatchers.jsonPath("$",hasSize(3)))
               .andExpect(jsonPath("$.high",is(180.0)));

   }

    Candle buildCandle(){
        return new Candle().builder().epoch(4L)
                .low(100)
                .high(200)
                .open(120)
                .close(180).build();
    }
}
