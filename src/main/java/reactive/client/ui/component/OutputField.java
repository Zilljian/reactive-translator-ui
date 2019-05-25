package reactive.client.ui.component;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Getter
@UIScope
@Component
@RequiredArgsConstructor
public class OutputField {

    private final Div div = new Div();
    private final TextArea field = new TextArea();

    @PostConstruct
    private void init() {
        field.setValueChangeMode(ValueChangeMode.EAGER);
        field.setLabel("Translated");
        field.setWidth("48vw");
        field.setHeight("35vh");
        field.setReadOnly(true);

        div.setId("Translated text");
        div.setVisible(true);
    }
}
