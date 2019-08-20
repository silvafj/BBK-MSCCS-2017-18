package visitor;


import java.util.List;

public abstract class HtmlTag implements Element {
    private String tagName;
    private String startTag;
    private String endTag;

    public HtmlTag(String tagName) {
        this.tagName = tagName;
    }

    public String getTagName() {
        return tagName;
    }

    public String getStartTag() {
        return this.startTag;
    }

    public void setStartTag(String tag) {
        this.startTag = tag;
    }

    public String getEndTag() {
        return this.endTag;
    }

    public void setEndTag(String tag) {
        this.endTag = tag;
    }

    public void setTagBody(String tagBody) {
        throw new UnsupportedOperationException("Current operation is not support for this object");
    }

    public void addChildTag(HtmlTag htmlTag) {
        throw new UnsupportedOperationException("Current operation is not support for this object");
    }

    public void removeChildTag(HtmlTag htmlTag) {
        throw new UnsupportedOperationException("Current operation is not support for this object");
    }

    public List<HtmlTag> getChildren() {
        throw new UnsupportedOperationException("Current operation is not support for this object");
    }

    public abstract void generateHtml();
}
