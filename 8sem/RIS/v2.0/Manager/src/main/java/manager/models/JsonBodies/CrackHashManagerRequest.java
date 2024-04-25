package manager.models.JsonBodies;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CrackHashManagerRequest {
    @Id
    private String guid;
    private Integer partNumber;
    private Integer partCount;
    private String hash;
    private Integer maxLength;
    private String alphabet;

}
