package me.femrek.viewcounter.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import me.femrek.viewcounter.model.AppRequest;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@Builder
public class RequestDTO {
    private String userAgent;
    private String ipAddress;
    private Timestamp timestamp;

    public RequestDTO(AppRequest request) {
        this.userAgent = request.getUserAgent();
        this.ipAddress = request.getIpAddress();
        this.timestamp = request.getTimestamp();
    }
}
