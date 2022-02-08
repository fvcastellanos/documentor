package net.cavitos.documentor.web.model.response;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class ResourceResponse<T> {
    
    private T content;
    private LinkResponse links;

    public ResourceResponse(T object, String self) {

        final var links = new LinkResponse();
        links.setSelf(self);

        this.content = object;
        this.links = links;
    }
}
