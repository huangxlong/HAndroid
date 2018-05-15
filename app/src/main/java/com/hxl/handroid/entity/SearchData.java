package com.hxl.handroid.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator
 * on 2018/5/14 星期一.
 */
public class SearchData implements Serializable {
    public int curpage;
    public int offset;
    public Boolean over;
    public int pageCount;
    public int size;
    public int total;
    public List<SearchResultData> datas;


    public class SearchResultData implements Serializable {
        public String apkLink;
        public String author;
        public String chapterName;
        public String desc;
        public String envelopePic;
        public String link;
        public String niceDate;
        public String origin;
        public String projectLink;
        public long publishTime;
        public String superChapterName;
        public String tags;
        public String title;
        public int chapterId;
        public int courseId;
        public int id;
        public int superChapterId;
        public int type;
        public int userId;
        public int visible;
        public int zan;
        public Boolean collect;
        public Boolean fresh;
    }
}
