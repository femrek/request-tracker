package me.femrek.viewcounter.dto;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
public class RequestDTO {
    private String userAgent;
    private String ipAddress;
}
