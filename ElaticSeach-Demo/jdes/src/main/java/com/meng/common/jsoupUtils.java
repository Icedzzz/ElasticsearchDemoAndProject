package com.meng.common;

import com.meng.bean.goods;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zwt
 * @version 1.0
 * @date 2020/4/25 22:57
 */
public class jsoupUtils {

    public static List<goods> getTargetGoods(String keywords) throws IOException {
        String url="https://search.jd.com/Search?keyword="+keywords;
        Document document = Jsoup.parse(new URL(url), 3000);
        Element list = document.getElementById("J_goodsList");
        //System.out.println(list.html());
        Elements li = list.getElementsByTag("li");
        List<goods> goodsArrayList = new ArrayList<>();
        for (Element element : li) {
            String img = element.getElementsByTag("img").eq(0).attr("source-data-lazy-img");
            String name = element.getElementsByClass("p-name").eq(0).text();
            String price = element.getElementsByClass("p-price").eq(0).text();
            goods goods = new goods();
            goods.setImg(img);
            goods.setName(name);
            goods.setPrice(price);
            goodsArrayList.add(goods);
        }
        return goodsArrayList;
    }

    public static void main(String[] args) throws IOException {
        List<goods> goods = jsoupUtils.getTargetGoods("python");
        System.out.println(goods.get(1).getName());
    }

}
