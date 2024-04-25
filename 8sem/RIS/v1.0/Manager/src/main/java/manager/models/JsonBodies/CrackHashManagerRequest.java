package manager.models.JsonBodies;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
