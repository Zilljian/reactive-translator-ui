package reactive.client.dto.operation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@ToString
@AllArgsConstructor
public class UIEvent {
    private String language;
    private String text;
    private LocalDateTime emitted;
}
