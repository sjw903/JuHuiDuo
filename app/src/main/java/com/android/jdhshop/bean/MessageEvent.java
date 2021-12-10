package com.android.jdhshop.bean;

public class MessageEvent{
    private String message;
    private int position;
    public  MessageEvent(String message){
        this.message=message;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
