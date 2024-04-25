package zolotorevskii.risLab.worker.models.jsonBodies;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

import static zolotorevskii.risLab.worker.utils.Constants.IN_PROGRESS;

@Data
@AllArgsConstructor
public class ResultBody {
    String uuid;
    String status;
    List<String> data;
    private Integer partNumber;

    public ResultBody(){
        status = IN_PROGRESS;
        data = null;
    }
}
