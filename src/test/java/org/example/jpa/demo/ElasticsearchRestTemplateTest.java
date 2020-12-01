package org.example.jpa.demo;

import org.example.jpa.demo.data.Commodity;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.querydsl.QPageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.matchQuery;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ElasticsearchRestTemplateTest {

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Test
    public void testInsert() {
        Commodity commodity = new Commodity();
        commodity.setSkuId("1501009005");
        commodity.setName("葡萄吐司面包（10片装）");
        commodity.setCategory("101");
        commodity.setPrice(160);
        commodity.setBrand("良品铺子");

        IndexQuery indexQuery = new IndexQueryBuilder().withObject(commodity).build();

        String result = elasticsearchRestTemplate.index(indexQuery, IndexCoordinates.of("commodity"));
        System.out.println(result);
    }

    @Test
    public void testQueryById() {
        Commodity commodity = elasticsearchRestTemplate.get("1501009005", Commodity.class);
        Assert.assertNotNull(commodity);
        Assert.assertEquals(commodity.getName(), "葡萄吐司面包（10片装）");
    }

    @Test
    public void testDeleteById() {
        String deleteId = elasticsearchRestTemplate.delete("1501009005", Commodity.class);
        System.out.println(deleteId);
    }

    @Test
    public void testQuery() {
        Query searchQuery = new NativeSearchQueryBuilder().withQuery(matchQuery("skuId", "1501009005")).build();
        SearchHits<Commodity> commoditys =
                elasticsearchRestTemplate.search(searchQuery, Commodity.class, IndexCoordinates.of("commodity"));
        Commodity commodity = commoditys.getSearchHit(0).getContent();
        Assert.assertNotNull(commodity);
        System.out.println(commodity);
    }

    @Test
    public void testQueryByPage() {
        QPageRequest p = QPageRequest.of(0, 10);
        Query query = new NativeSearchQueryBuilder()
                .withPageable(p)
                .build();
        SearchHits<Commodity> search = elasticsearchRestTemplate.search(query, Commodity.class);
        List<SearchHit<Commodity>> searchHits = search.toList();
        SearchHit<Commodity> objectSearchHit = searchHits.get(0);
        Commodity content = objectSearchHit.getContent();

        System.out.println(content);
    }
}
