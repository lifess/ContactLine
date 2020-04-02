package com.ss.example.contactline;

/**
 * @Description: 连线的原数据
 * @Author wangjianzhou@qding.me
 * @Date 2020/3/22 10:41 PM
 * @Version
 */
public class LinkDataBean {

    /**
     * col : 0
     * content : apple
     * q_num : 0
     * row : 0
     * type : 0
     */

    private int col;
    private String content;
    private int q_num;
    private int row;
    private String type;

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getQ_num() {
        return q_num;
    }

    public void setQ_num(int q_num) {
        this.q_num = q_num;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "LinkDataBean{" +
                "col=" + col +
                ", content='" + content + '\'' +
                ", q_num=" + q_num +
                ", row=" + row +
                ", type='" + type + '\'' +
                '}';
    }
}
