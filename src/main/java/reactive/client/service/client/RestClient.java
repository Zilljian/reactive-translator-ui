package reactive.client.service.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import reactive.client.dto.operation.Credentials;
import reactive.client.dto.service.LanguageList;

import static java.util.Objects.isNull;

@Slf4j
@Component
@RequiredArgsConstructor
public class RestClient {
    private final Credentials credentials;
    private final RestTemplate restTemplate = new RestTemplate();

    @Bean
    public LanguageList requestLanguageList() {
        LanguageList response = restTemplate.getForObject(
                credentials.getUrl()
                        + credentials.getApiKey()
                        + "&ui=" + credentials.getUi(),
                LanguageList.class);

        if (isNull(response) || isNull(response.getLangs())) {
            log.error("Exception occurred while getting language list");
        }
        return response;
    }
}
