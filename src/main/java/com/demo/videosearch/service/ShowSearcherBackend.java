package com.demo.videosearch.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.demo.videosearch.db.HashTableSortedSets;
import com.demo.videosearch.db.IHashTableSortedSets;
import com.demo.videosearch.model.IShow;
import com.demo.videosearch.service.IShowSearcherBackend;
import com.demo.videosearch.util.RedisQuery;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ShowSearcher Backend implements IShowSearcherBackend interface
 * it is used to search and retrieve the database of shows within the ShowSearcher app.
 */
public class ShowSearcherBackend implements IShowSearcherBackend {
    // Hashtable class for searching and retrieve the database by mapping words and Show objects
    protected final IHashTableSortedSets<String, IShow> titleHash;
    // Hashtable class for searching and retrieve the database by mapping years and Show objects
    protected final IHashTableSortedSets<Integer, IShow> yearHash;
    // store filters
    private ArrayList<String> filter;

    private int shownumber;

    public ShowSearcherBackend() {
        this.titleHash = new HashTableSortedSets<>();
        this.yearHash = new HashTableSortedSets<>();
        this.filter = new ArrayList<>();
        this.shownumber = 0;
    }

    /**
     * add shows to titleHash and yearHash
     *
     * @param show show object being added
     */
    public void addShow(IShow show) {
        this.shownumber++;
        ArrayList<String> words = new ArrayList<>();
        Pattern pattern = Pattern.compile("[\\w%.']+");
        Matcher matcher = pattern.matcher(show.getTitle());
        while (matcher.find()) {
            words.add(matcher.group().toLowerCase());
        }

        ArrayList<String> visited = new ArrayList<>();
        for (String word : words) {
            if (!visited.contains(word)) {
                visited.add(word);
                titleHash.add(word, show);
            }
        }
        titleHash.add(show.getTitle().toLowerCase(), show);
        yearHash.add(show.getYear(), show);
    }

    /**
     * get the number of shows in backend database
     *
     * @return the number of shows
     */
    @Override
    public int getNumberOfShows() {
        return this.shownumber;
    }

    /**
     * set providers in the filter or out of filter
     *
     * @param provider the provider being in the filter or out of filter
     * @param filter   true if the provider should be filtered, false if the provider should be out of filter
     */
    @Override
    public void setProviderFilter(String provider, boolean filter) {
        if (filter) {
            this.filter.add(provider);
        } else {
            this.filter.remove(provider);
        }
    }

    /**
     * judge whether the provider should be filtered or not
     *
     * @param provider the provider being judged
     * @return true if the provider should be filtered, false if the provider should be out of filter
     */
    @Override
    public boolean getProviderFilter(String provider) {
        if (!this.filter.contains(provider)) {
            return false;
        }
        return true;
    }

    /**
     * toggle the provider
     * Call setProviderFilter method and getProviderFilter to achieve functionality
     *
     * @param provider provider being toggled
     */
    @Override
    public void toggleProviderFilter(String provider) {
        boolean filter;
        if (!this.filter.contains(provider)) {
            filter = true;
        } else {
            filter = false;
        }
        setProviderFilter(provider, filter);
    }

    /**
     * search and retrieve the database of shows by selected word
     *
     * @param keyword being used to search for shows
     * @return list of selected shows by this word
     */
    @Override
    public List<IShow> searchByTitleWord(String keyword) {
        // 先从缓存中查找
        List<IShow> result = getCachedSearchResult(keyword);
        if (result != null) {
            // 将查询关键字的计数器加1
            RedisQuery.incrementScore("hotKeywords", keyword, 1);
        }
        // 缓存中不存在，从 kv map 中查找
        result = searchFromHashMap(keyword);
        if (result != null) {
            // 将结果放入缓存
            cacheSearchResult(keyword, result);
            // 将查询关键字的计数器加1
            RedisQuery.incrementScore("hotKeywords", keyword, 1);
            // 将查询关键字放入历史查询记录中
            RedisQuery.leftPush("historyKeywords", keyword);
        }
        return result;
    }

    /**
     * 从 HashMap 中查找查询结果
     *
     * @param word
     * @return
     */
    private ArrayList<IShow> searchFromHashMap(String word) {
        if (!titleHash.containsKey(word.toLowerCase())) {
            return null;
        }
        ArrayList<IShow> result = (ArrayList<IShow>) titleHash.get(word.toLowerCase());

        for (String provider : this.filter) {
            for (int i = 0; i < result.size(); i++) {
                if (!result.get(i).isAvailableOn(provider)) {
                    result.remove(i);
                }
            }
        }
        this.filter.clear();
        return result;
    }

    /**
     * 将查询结果放入缓存
     *
     * @param keyword
     * @param result
     */
    public void cacheSearchResult(String keyword, List<IShow> result) {
        try {
            String resultString = JSON.toJSONString(result);
            RedisQuery.set(keyword, resultString);
        } catch (JedisConnectionException e) {
            System.err.println("Failed to connect to Redis, proceeding without caching.");
        }
    }

    /**
     * 从缓存中获取查询结果
     *
     * @param keyword
     * @return
     */
    public List<IShow> getCachedSearchResult(String keyword) {
        try {
            String resultString = RedisQuery.getByKey(keyword);
            if (resultString != null) {
                return JSON.parseObject(resultString, new TypeReference<List<IShow>>() {
                });
            }
        } catch (JedisConnectionException e) {
            System.err.println("Failed to connect to Redis, proceeding with normal query");
        }
        return searchFromHashMap(keyword);
    }

//    public List<IShow> getCachedSearchResult(String keyword) {
//        String resultString = null;
//        try {
////            resultString = redisTemplate.opsForValue().get(keyword);
//            resultString = RedisQuery.getByKey(keyword);
//        } catch (NullPointerException e) {
//            return null;
//        }
//        if (resultString != null) {
//            // 缓存中存在结果，反序列化为List<String>
//            return JSON.parseObject(resultString, new TypeReference<List<IShow>>() {
//            });
//        }
//        // 缓存中不存在结果
//        return null;
//    }

    /**
     * search and retrieve the database of shows by their publication years
     *
     * @param year year being used to search for shows
     * @return list of selected shows by this year
     */
    @Override
    public List<IShow> searchByYear(int year) {
        if (!yearHash.containsKey(year)) {
            return null;
        }
        List<IShow> result = yearHash.get(year);
        ArrayList<IShow> finals = new ArrayList<>();
        for (IShow show : result) {
            finals.add(show);
        }
        for (String provider : this.filter) {
            for (int i = 0; i < finals.size(); i++) {
                if (finals.get(i).isAvailableOn(provider)) {
                    finals.remove(i);
                    i = -1;
                }
            }
        }
        this.filter.clear();
        return finals;
    }

    // 获取热点关键字排行榜
    public List<String> getHotKeywords() {
        try {
            Set<String> hotKeywords = RedisQuery.reverseRange("hotKeywords", 0, 9);
            return new ArrayList<>(hotKeywords);
        } catch (JedisConnectionException e) {
            System.err.println("Failed to connect to Redis, unable to fetch hot keywords.");
        }
        return new ArrayList<>();
    }
}
