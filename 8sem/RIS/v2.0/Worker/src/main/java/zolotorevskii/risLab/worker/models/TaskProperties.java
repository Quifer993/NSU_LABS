package zolotorevskii.risLab.worker.models;

import com.rabbitmq.client.Channel;
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
    private Channel channel;
    private long tag;
    private LocalDateTime startTime;
    private ArrayList<String> answers;
    private String hash;
    private CrackHashManagerRequest inputBody;

    private Integer partNumber;
    private Integer partCount;
}
