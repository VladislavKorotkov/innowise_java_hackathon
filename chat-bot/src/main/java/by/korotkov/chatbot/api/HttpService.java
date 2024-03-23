package by.korotkov.chatbot.api;

import by.korotkov.chatbot.models.api.CurrencyValue;
import by.korotkov.chatbot.utils.CurrenciesMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.*;

@Service
@Slf4j
public class HttpService {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String apiUrl;

    public HttpService(@Value("${api.url}") String apiUrl){
        this.apiUrl = apiUrl;
    }

    public Map<String, CurrencyValue> fetchData() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(apiUrl, String.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            String responseBody = response.getBody();
            try {
                CurrencyValue[] currencies = objectMapper.readValue(responseBody, CurrencyValue[].class);
                return CurrenciesMapper.convertArrayToMap(currencies);
            } catch (IOException e) {
                log.error(Arrays.toString(e.getStackTrace()));
            }
        }
        return Collections.emptyMap();
    }


}
