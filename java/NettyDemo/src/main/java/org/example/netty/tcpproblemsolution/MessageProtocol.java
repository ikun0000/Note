package org.example.netty.tcpproblemsolution;

// 自定义协议
public class MessageProtocol {
    private int len;
    private byte[] content;

    public MessageProtocol() {
    }

    public MessageProtocol(int len, byte[] content) {
        this.len = len;
        this.content = content;
    }

    public void setLen(int len) {
        this.len = len;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public int getLen() {
        return len;
    }

    public byte[] getContent() {
        return content;
    }
}
