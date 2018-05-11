package com.hxl.handroid.base;

import java.io.Serializable;
import java.util.List;

/**
 * 文章列表
 * Created by Administrator
 * on 2018/5/9 星期三.
 */
public class ArticleData {
    public int curPage;
    public List<Article> datas;
    public int offset;
    public boolean over;
    public int pageCount;
    public int size;
    public int total;

    public class Article implements Serializable {
        public String apkLink;
        public String author;
        public int chapterId;
        public String chapterName;
        public boolean collect;
        public int courseId;
        public String desc;
        public String envelopePic;
        public int id;
        public String link;
        public String niceDate;
        public String origin;
        public int originId;
        public String projectLink;
        public int superChapterId;
        public String superChapterName;
        public long publishTime;
        public String title;
        public int visible;
        public int zan;
    }
}
