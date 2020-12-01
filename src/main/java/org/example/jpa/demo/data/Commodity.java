package org.example.jpa.demo.data;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;

@Data
@ToString
@Document(indexName = "commodity")
public class Commodity implements Serializable {

    private String skuId;

    private String name;

    private String category;

    private Integer price;

    private String brand;

    private Integer stock;

}