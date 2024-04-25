package manager.models.JsonBodies;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class Response {
    private List<String> answer;
    private boolean isTimeout;
}
