package reactive.client.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.shared.communication.PushMode;
import com.vaadin.flow.shared.ui.Transport;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactive.client.dto.service.TranslationResponse;
import reactive.client.service.client.RestClient;
import reactive.client.ui.component.InputField;
import reactive.client.ui.component.OutputField;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;

@Slf4j
@Getter
@UIScope
@Route("")
@Component
@Push(value = PushMode.MANUAL, transport = Transport.LONG_POLLING)
@RequiredArgsConstructor
@PWA(name = "Reactive translator", shortName = "reactive-translate")
public class UiFrame extends VerticalLayout {

    private final OutputField field;
    private final InputField inputField;
    private final ComboBox<String> langComboBox = new ComboBox<>();
    private final HorizontalLayout fieldLayout = new HorizontalLayout();
    private final HorizontalLayout langSelectLayout = new HorizontalLayout();
    private final VerticalLayout verticalLayout = new VerticalLayout();
    private final RestClient client;

    private final ObjectMapper mapper;

    @PostConstruct
    public void init() {
        fieldLayout.add(inputField.getInputField(), field.getField());
        langSelectLayout.add(
                inputField.getLangBoxIn(),
                inputField.getExchangeButton(),
                inputField.getLangBoxOut(),
                inputField.getEmptyDiv());
        add(langSelectLayout, fieldLayout);
        configureLangComboBox();
    }

    private void configureLangComboBox() {
        try {
            inputField.setLangs(client.requestLanguageList());
        } catch (IOException e) {
            log.error("Error occurred while getting lang list");
        }
    }

    private TranslationResponse last = new TranslationResponse(200, "", Collections.singletonList(""), LocalDateTime.now());
}
