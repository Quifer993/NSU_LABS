package zolotorevskii.risLab.worker.models.jsonBodies;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CrackHashManagerRequest {
    private String guid;
    private Integer partNumber;
    private Integer partCount;
    private String hash;
    private Integer maxLength;
    private String alphabet;

}
