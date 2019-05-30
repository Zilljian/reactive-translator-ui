package reactive.client.ui.component;

import com.google.common.collect.HashBiMap;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactive.client.dto.operation.UIEvent;
import reactor.core.publisher.UnicastProcessor;

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
    private final UnicastProcessor<UIEvent> event;

    private Map<String, String> langs;
    private Map<String, String> keys;

    public void setLangs(Map<String, String> list) {
        langs = list;
        keys = HashBiMap.create(list).inverse();
        langBoxOut.setItems(list.values());
        langBoxIn.setItems(list.values());
    }

    @PostConstruct
    public void init() {
        configureExchangeButton();
        configureLangComboBox();
        configureInputField();
    }

    private UIEvent in() {
        return new UIEvent(
                nonNull(langBoxIn.getValue())
                        ? keys.get(langBoxIn.getValue())
                        .concat("-")
                        .concat(keys.get(langBoxOut.getValue()))
                        : keys.get(langBoxOut.getValue()),
                inputField.getValue(),
                LocalDateTime.now());
    }

    private void configureExchangeButton() {
        exchangeButton.addClickListener(
                event -> {
                    if (nonNull(langBoxIn.getValue()) && nonNull(langBoxOut.getValue())) {
                        String temp = langBoxIn.getValue();
                        langBoxIn.setValue(langBoxOut.getValue());
                        langBoxOut.setValue(temp);
                    }
                });
    }

    private void configureLangComboBox() {
        langBoxOut.setWidth("16vw");
        langBoxOut.setPlaceholder("Select destination language");
        langBoxIn.setWidth("16vw");
        langBoxIn.setPlaceholder("Select source language");

        getLangBoxOut().addValueChangeListener(
                e -> {
                    if (nonNull(e.getValue())) {
                        event.onNext(in());
                    }
                });
    }

    private void configureInputField() {
        inputField.setValueChangeMode(ValueChangeMode.EAGER);
        inputField.setLabel("Enter text here");
        inputField.setWidth("49vw");
        inputField.setHeight("35vh");

        inputField.addValueChangeListener(
                e -> {
                    if (nonNull(getLangBoxOut().getValue())) {
                        event.onNext(in());
                    }
                });
    }
}
