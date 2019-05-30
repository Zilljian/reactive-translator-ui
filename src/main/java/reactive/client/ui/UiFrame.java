package reactive.client.ui;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.shared.communication.PushMode;
import com.vaadin.flow.shared.ui.Transport;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactive.client.dto.service.LanguageList;
import reactive.client.dto.service.TranslationResponse;
import reactive.client.ui.component.InputField;
import reactive.client.ui.component.OutputField;
import reactor.core.publisher.Flux;

import javax.annotation.PostConstruct;

@UIScope
@Route("")
@Component
@Push(value = PushMode.MANUAL, transport = Transport.LONG_POLLING)
@RequiredArgsConstructor
@PWA(name = "Reactive translator", shortName = "reactive-translate")
public class UiFrame extends VerticalLayout {

    private final OutputField field;
    private final InputField inputComponents;
    private final HorizontalLayout fieldLayout = new HorizontalLayout();
    private final HorizontalLayout langSelectLayout = new HorizontalLayout();

    private final Flux<TranslationResponse> response;
    private final LanguageList langs;

    @PostConstruct
    public void init() {
        fieldLayout.add(inputComponents.getInputField(), field.getField());
        langSelectLayout.add(
                inputComponents.getLangBoxIn(),
                inputComponents.getExchangeButton(),
                inputComponents.getLangBoxOut());
        add(langSelectLayout, fieldLayout);
        configureLangComboBox();
    }

    private void configureLangComboBox() {
        inputComponents.setLangs(langs.getLangs());

        response.subscribe(
                e -> getUI().ifPresent(
                        ui -> ui.access(
                                () -> {
                                    field.setValue(String.join(" ", e.getText()));
                                    ui.push();
                                })));
    }
}
