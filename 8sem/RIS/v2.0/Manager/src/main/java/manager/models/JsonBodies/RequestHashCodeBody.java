package manager.models.JsonBodies;

import lombok.Getter;

@Getter
public class RequestHashCodeBody {
    String hash;
    Integer maxLength;

    public RequestHashCodeBody(String hash, Integer maxLength){
        this.hash = hash;
        this.maxLength = maxLength;
    }
}
