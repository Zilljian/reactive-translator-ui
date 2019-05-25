package reactive.client.dto.operation;

import lombok.Data;

@Data
public class KafkaProperties {
    private String url;
    private String groupId;
}
