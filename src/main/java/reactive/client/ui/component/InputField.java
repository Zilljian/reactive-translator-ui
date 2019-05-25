package reactive.client.ui.component;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactive.client.dto.operation.UIEvent;
import reactive.client.dto.service.LanguageList;
import reactive.client.service.client.KafkaClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.scheduler.Schedulers;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.Map;

import static java.util.Objects.nonNull;

@Getter
@UIScope
@Component
@RequiredArgsConstructor
public class InputField {

    private final TextArea inputField = new TextArea();
    private final ComboBox<String> langBoxOut = new ComboBox<>();
    private final ComboBox<String> langBoxIn = new ComboBox<>();
    private final Button exchangeButton = new Button(new Icon(VaadinIcon.REFRESH));
    private final Div emptyDiv = new Div();
    private final KafkaClient kafkaClient;

    private Map<String, String> langs;

    @PostConstruct
    public void init() {
        inputField.setValueChangeMode(ValueChangeMode.EAGER);
        inputField.setLabel("Enter text here");
        inputField.setWidth("49vw");
        inputField.setHeight("35vh");

        langBoxOut.setWidth("16vw");
        langBoxOut.setPlaceholder("Select destination language");
        langBoxIn.setWidth("16vw");
        langBoxIn.setPlaceholder("Select source language");

        configureExchangeButton();
        emptyDiv.setWidth("20vw");

        configureFieldListener();
        configureExchangeButton();
        configureComboBoxListener();
    }

    private void configureFieldListener() {
        inputField.addValueChangeListener(
                event -> Flux.<UIEvent>create(this::onEvent)
                        .onErrorReturn(new UIEvent("", "", LocalDateTime.now()))
                        .filter(uiEvent -> !uiEvent.getText().isEmpty()
                                && !uiEvent.getLanguage().isEmpty())
                        .log("in method")
                        .subscribeOn(Schedulers.parallel())
                        .subscribe(kafkaClient::sendEvent)
        );
    }

    private void configureComboBoxListener() {
        langBoxOut.addValueChangeListener(
                event -> Flux.<UIEvent>create(this::onEvent)
                        .onErrorReturn(new UIEvent("", "", LocalDateTime.now()))
                        .filter(uiEvent -> !uiEvent.getText().isEmpty()
                                && !uiEvent.getLanguage().isEmpty())
                        .log("in method")
                        .subscribeOn(Schedulers.parallel())
                        .subscribe(kafkaClient::sendEvent)
        );
    }

    private void onEvent(FluxSink<UIEvent> fluxSink) {
        String source = langs.entrySet().stream()
                .filter(entry -> nonNull(langBoxIn.getValue()) && langBoxIn.getValue().equals(entry.getValue()))
                .map(Map.Entry::getKey).findFirst().orElse("");
        String dest = langs.entrySet().stream()
                .filter(entry -> nonNull(langBoxOut.getValue()) && langBoxOut.getValue().equals(entry.getValue()))
                .map(Map.Entry::getKey).findFirst().orElse("");

        String lang = source.isEmpty() ? dest : source + "-" + dest;
        fluxSink.next(new UIEvent(lang, inputField.getValue(), LocalDateTime.now()));
    }

    public void setLangs(LanguageList list) {
        langs = list.getLangs();
        langBoxOut.setItems(langs.values());
        langBoxIn.setItems(langs.values());
    }

    private void configureExchangeButton() {
        exchangeButton.addClickListener(event -> {
            if (nonNull(langBoxIn.getValue()) && nonNull(langBoxOut.getValue())) {
                String temp = langBoxIn.getValue();
                langBoxIn.setValue(langBoxOut.getValue());
                langBoxOut.setValue(temp);
            }
        });
    }
}
