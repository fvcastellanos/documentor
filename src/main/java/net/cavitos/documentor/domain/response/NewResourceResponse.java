package net.cavitos.documentor.domain.response;

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
