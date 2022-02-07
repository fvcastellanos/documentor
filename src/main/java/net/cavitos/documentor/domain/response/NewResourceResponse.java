package net.cavitos.documentor.domain.response;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class NewResourceResponse<T> {
    
    private T content;
    private LinkResponse links;

    public NewResourceResponse(T object, String self) {

        final var links = new LinkResponse();
        links.setSelf(self);

        this.content = object;
        this.links = links;
    }
}
