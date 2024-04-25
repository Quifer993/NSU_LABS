package zolotorevskii.risLab.worker.models.jsonBodies;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

import static zolotorevskii.risLab.worker.utils.Constants.IN_PROGRESS;

@Getter
@Setter
@AllArgsConstructor
public class ResultBody {
    String status;
    List<String> data;

    public ResultBody(){
        status = IN_PROGRESS;
        data = null;
    }
}
