package com.socialhub.mapper;

import org.mapstruct.*;
import com.socialhub.dto.*;
import com.socialhub.model.Product;
import java.util.List;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProductMapper {
    ProductBasicDTO toProductBasicDTO(Product product);
    ProductDetailDTO toProductDetailDTO(Product product);

    List<ProductBasicDTO> toProductBasicDTOs(List<Product> products);
    List<ProductDetailDTO> toProductDetailDTOs(List<Product> products);

    // Add your custom mapping methods here
}