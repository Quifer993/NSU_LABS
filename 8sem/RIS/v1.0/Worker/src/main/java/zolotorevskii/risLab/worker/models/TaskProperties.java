package zolotorevskii.risLab.worker.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import zolotorevskii.risLab.worker.models.jsonBodies.CrackHashManagerRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;

@AllArgsConstructor
@Getter
@Setter
public class TaskProperties {
    private LocalDateTime startTime;
    private ArrayList<String> answers;
    private String hash;
    private CrackHashManagerRequest inputBody;
    private volatile boolean isError;
    private volatile boolean isDone;
}
