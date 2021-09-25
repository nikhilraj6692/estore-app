package com.estore.demo.product.domain;

import com.estore.demo.constants.ApplicationConstants;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.util.Objects;

/*
POJO to hold image id and their description
 */
@Document(collection = "ProductImage")
public class ProductImage {
    private static int count = 0;
    @NotNull
    private String imageId;
    private String imageDescription;

    public ProductImage() {
    }

    public ProductImage(String imageDescription) {
        this.imageId = ApplicationConstants.IMAGE_PREFIX + ++count;
        this.imageDescription = imageDescription;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getImageDescription() {
        return imageDescription;
    }

    public void setImageDescription(String imageDescription) {
        this.imageDescription = imageDescription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductImage that = (ProductImage) o;
        return imageId == that.imageId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(imageId);
    }
}
