package com.example.wechat;
/**
 * 聊天内容类
 */

public class TalkData {
    public static final int TYPE_RECEIVED = 0;
    public static final int TYPE_SENT = 1;
    private int type;

    private int imageView;
    private String talkContent;
    private String talkFriendName;
    private int hintNum;

    /**
     * 用来创建聊天内容
     * @param imageView  头像
     * @param talkContent   聊天内容
     * @param type  收发类型
     */
    public TalkData(int imageView, String talkContent, int type){
        this.imageView = imageView;
        this.type = type;
        this.talkContent = talkContent;
    }

    /**
     *  创建聊天listView item
     * @param imageView     头像
     * @param talkFriendName    名字
     * @param talkContent   聊天内容
     * @param hintNum       接受信息提示
     */
    public TalkData(int imageView, String talkFriendName, String talkContent, int hintNum){
        this.imageView = imageView;
        this.talkFriendName = talkFriendName;
        this.talkContent = talkContent;
        this.hintNum = hintNum;
    }
    public TalkData(int imageView, String talkFriendName){
        this.talkFriendName = talkFriendName;
        this.imageView = imageView;
    }

    public String getTalkContent() {
        return talkContent;
    }

    public int getImageView() {
        return imageView;
    }

    public String getTalkFriendName() {
        return talkFriendName;
    }

    public int getType(){
        return type;
    }

    public int getHintNum() {
        return hintNum;
    }
}
