package provider.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ChatMessage {

    private String streamingSite;
    private String userName;
    private String message;
    private LocalDateTime timeCreated;

}
