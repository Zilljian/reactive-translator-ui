package reactive.client.dto.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TranslationResponse {
    private int code;
    private String lang;
    private List<String> text;
    private LocalDateTime created;
}
