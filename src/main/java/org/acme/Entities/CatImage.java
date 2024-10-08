package org.acme.Entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CatImage implements Serializable {
    private String id;
    private String url;
    private int width;
    private int height;

    @Override
    public String toString() {
        return "CatImage{id='" + id + "', url='" + url + "', width=" + width + ", height=" + height + "}";
    }

}
