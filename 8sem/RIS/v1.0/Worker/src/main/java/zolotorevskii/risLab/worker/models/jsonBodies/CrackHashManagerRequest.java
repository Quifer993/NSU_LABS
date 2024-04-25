package zolotorevskii.risLab.worker.models.jsonBodies;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CrackHashManagerRequest {
    private String guid;
    private Integer partNumber;
    private Integer partCount;
    private String hash;
    private Integer maxLength;
    private String alphabet;

}
