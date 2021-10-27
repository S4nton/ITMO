package markup;

public class Text implements ForParagraph {
    public String text;

    Text(String newText) {
        this.text = newText;
    }

    public void toTex(StringBuilder str) {
        str.append(text);
    }
}
