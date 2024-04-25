package manager.models.JsonBodies;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Deprecated
@AllArgsConstructor
@Getter
public class Response {
    private List<String> answer;
    private boolean isTimeout;
}
