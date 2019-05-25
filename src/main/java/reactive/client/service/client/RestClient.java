package reactive.client.service.client;

import com.vaadin.flow.spring.annotation.UIScope;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import reactive.client.dto.operation.Credentials;
import reactive.client.dto.service.LanguageList;

import java.io.IOException;

import static java.util.Objects.isNull;

@Slf4j
@UIScope
@Component
@RequiredArgsConstructor
public class RestClient {
    private final Credentials credentials;
    private final RestTemplate restTemplate = new RestTemplate();

    public LanguageList requestLanguageList() throws IOException {
        LanguageList response = restTemplate.getForObject(
                credentials.getUrl()
                        + credentials.getApiKey()
                        + "&ui=" + credentials.getUi(),
                LanguageList.class);

        if (isNull(response) || isNull(response.getLangs())) {
            throw new IOException();
        }
        return response;
    }
}
