package com.elk.demo;

import com.alibaba.fastjson.JSON;
import com.elk.domain.user;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class DemoApplicationTests {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    //索引的创建
    @Test
    void testCreateIndex() throws IOException {
        // 1.创建索引请求
        CreateIndexRequest request = new CreateIndexRequest("meng_index");
        //2.客户端执行请求
        CreateIndexResponse createIndexResponse = restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);
        System.out.println(createIndexResponse);
    }
    //获取索引
    @Test
    void testExistIndex() throws IOException {
        GetIndexRequest request = new GetIndexRequest("meng_index");
        boolean exists = restHighLevelClient.indices().exists(request, RequestOptions.DEFAULT);
        System.out.println(exists);
    }

    //删除索引
    @Test
    void testDeleteIndex() throws IOException{
        DeleteIndexRequest request = new DeleteIndexRequest("meng_index");
        //删除
        AcknowledgedResponse delete = restHighLevelClient.indices().delete(request, RequestOptions.DEFAULT);
        System.out.println(delete.isAcknowledged());
    }
    // 测试添加文档
    @Test
    void testAddDocument() throws IOException {
        user user = new user("萌神说", 3);
        //创建请求
        IndexRequest request = new IndexRequest("meng_index");
        //规则 put /index/_doc/1
        request.id("1");
        request.timeout(TimeValue.timeValueSeconds(1));
        request.timeout("1s");

        //将我们的数据放入请求
        request.source(JSON.toJSONString(user), XContentType.JSON);
        //客户端发送请求，获取响应结果
        IndexResponse indexResponse = restHighLevelClient.index(request, RequestOptions.DEFAULT);
        System.out.println(indexResponse.toString());
        System.out.println(indexResponse.status());
        //对应我们命令返回的状态

    }
    // 获取文档，判断是否存在 get /index/doc/1
    @Test
    void testIsExist() throws IOException {
        GetRequest request = new GetRequest("meng_index", "1");
        // 不获取返回的 _source 的上下文了
        request.fetchSourceContext(new FetchSourceContext(false));
        request.storedFields("_none_");
        boolean exists = restHighLevelClient.exists(request, RequestOptions.DEFAULT);
        System.out.println(exists);
    }

    //获取文档信息
    @Test
    void testGetDocument() throws IOException {
        GetRequest getRequest = new GetRequest("meng_index", "1");
        GetResponse getResponse = restHighLevelClient.get(getRequest, RequestOptions.DEFAULT);
        System.out.println(getResponse.getSourceAsString());
        System.out.println(getResponse);
    }

    //更新文档的信息
    @Test
    void testUpdateDocument() throws IOException{
        UpdateRequest request = new UpdateRequest("meng_index", "1");
        request.timeout("1s");
        user user = new user("狂神说java", 18);
        request.doc(JSON.toJSONString(user), XContentType.JSON);
        UpdateResponse updateResponse = restHighLevelClient.update(request, RequestOptions.DEFAULT);
        System.out.println(updateResponse.status());
    }
    //删除文档的信息
    @Test
    void testDeleteRequest() throws IOException{
        DeleteRequest request = new DeleteRequest("meng_index", "1");
        request.timeout("1s");

        DeleteResponse response= restHighLevelClient.delete(request, RequestOptions.DEFAULT);
        System.out.println(response.status());
    }
    // 特殊的，真的项目一般都会批量插入数据！
    @Test
    void testBulkRequest() throws IOException{
        BulkRequest bulkRequest = new BulkRequest();
         bulkRequest.timeout("10s");
        ArrayList<user> userList = new ArrayList<>();
        userList.add(new user("mengshen1",1));
        userList.add(new user("mengshen2",2));
        userList.add(new user("mengshen3",3));
        userList.add(new user("mengshen4",5));
        userList.add(new user("mengshen5",6));
        userList.add(new user("mengshen7",7));
        for (int i = 0; i < userList.size(); i++) {
            //批量跟新删除
            bulkRequest.add(
                    new IndexRequest("meng_index")
                    .id(""+(i+1)) .source(JSON.toJSONString(userList.get(i)),XContentType.JSON));
        }
        BulkResponse bulkResponse = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
        System.out.println(bulkResponse.hasFailures());
    }
    /**查询
     *  SearchRequest 搜索请求
     *  SearchSourceBuilder 条件构造
     *  HighlightBuilder 构建高亮
     *  TermQueryBuilder 精确查询
     *  MatchAllQueryBuilder匹配所有
     *   QueryBuilder 对应我们刚才看到的命令！
     *   查询条件，我们可以使用 QueryBuilders 工具来实现
     *      QueryBuilders.termQuery 精确
     *      QueryBuilders.matchAllQuery() 匹配所有
     */
    @Test
    void testSearch() throws IOException {
        SearchRequest searchRequest = new SearchRequest("meng_index");
        //构建搜索条件
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.highlighter();

        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("name", "mengshen1");

        searchSourceBuilder.query(termQueryBuilder);
        searchSourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));

        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        System.out.println(JSON.toJSONString(searchResponse.getHits()));
        System.out.println("=================================");
        for (SearchHit hit : searchResponse.getHits().getHits()) {
            System.out.println(hit.getSourceAsMap());
        }
    }
}
