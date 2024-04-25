package manager.models.JsonBodies;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ResultBody {
    String status;
    String[] data;
}
