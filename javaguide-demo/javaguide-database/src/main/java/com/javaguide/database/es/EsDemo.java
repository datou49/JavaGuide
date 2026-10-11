package com.javaguide.database.es;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Elasticsearch - 检索 DSL、match/match_phrase/multi_match/bool/filter/term/聚合
 * <p>不依赖真实 ES，用「构建 JSON DSL + 内存检索模拟」演示核心查询语义。
 * 对应 JavaGuide 搜索引擎部分与谷粒商城 ES 检索。</p>
 */
public class EsDemo {

    public static void main(String[] args) {
        List<Map<String, Object>> docs = new ArrayList<>();
        docs.add(Map.of("title", "Java 并发编程实战", "category", "java", "price", 99, "tags", List.of("并发", "线程")));
        docs.add(Map.of("title", "Java 核心卷", "category", "java", "price", 128, "tags", List.of("基础", "集合")));
        docs.add(Map.of("title", "深入理解计算机系统", "category", "cs", "price", 139, "tags", List.of("系统")));
        docs.add(Map.of("title", "Effective Java", "category", "java", "price", 89, "tags", List.of("最佳实践")));

        System.out.println("=== ES Query DSL 演示 ===");
        System.out.println("1) match 全文检索（分词后匹配任意词）");
        System.out.println(buildMatch("title", "java 并发"));
        System.out.println("2) match_phrase 短语匹配（整体顺序）");
        System.out.println(buildMatchPhrase("title", "并发编程"));
        System.out.println("3) multi_match 多字段匹配");
        System.out.println(buildMultiMatch("并发", List.of("title", "tags")));
        System.out.println("4) bool 复合查询 + filter 过滤");
        System.out.println(buildBoolFilter("java", "cs", 100));
        System.out.println("5) term 精确匹配");
        System.out.println(buildTerm("category", "java"));
        System.out.println("6) aggregations 聚合（按 category 分组计数）");
        System.out.println(buildAggs("category"));

        System.out.println();
        System.out.println("=== 模拟检索结果 ===");
        long count = docs.stream().filter(d -> ((String) d.get("category")).equals("java")).count();
        System.out.println("category=java 命中 " + count + " 条: " + docs.stream()
                .filter(d -> ((String) d.get("category")).equals("java"))
                .map(d -> (String) d.get("title")).toList());
    }

    static String buildMatch(String field, String text) {
        return "{\"query\":{\"match\":{\"" + field + "\":\"" + text + "\"}}}";
    }

    static String buildMatchPhrase(String field, String text) {
        return "{\"query\":{\"match_phrase\":{\"" + field + "\":\"" + text + "\"}}}";
    }

    static String buildMultiMatch(String text, List<String> fields) {
        return "{\"query\":{\"multi_match\":{\"query\":\"" + text + "\",\"fields\":[\"" + String.join("\",\"", fields) + "\"]}}}";
    }

    public static String buildBoolFilter(String must, String mustNot, int rangeMax) {
        return "{\"query\":{\"bool\":{\"must\":[{\"term\":{\"category\":\"" + must + "\"}}],"
                + "\"must_not\":[{\"term\":{\"category\":\"" + mustNot + "\"}}],"
                + "\"filter\":[{\"range\":{\"price\":{\"lte\":" + rangeMax + "}}}]}}}";
    }

    static String buildTerm(String field, String value) {
        return "{\"query\":{\"term\":{\"" + field + "\":\"" + value + "\"}}}";
    }

    static String buildAggs(String field) {
        return "{\"aggs\":{\"group_by\":{\"terms\":{\"field\":\"" + field + "\"}}}}";
    }
}
