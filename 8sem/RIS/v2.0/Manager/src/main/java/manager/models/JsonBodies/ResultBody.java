package manager.models.JsonBodies;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultBody {
    @Id
    String uuid;
    String status;
    List<String> data;
    private Integer partNumber;
}
