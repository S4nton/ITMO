package markup;

import java.util.List;

public class WithEnvironment implements Tex {
    public List<? extends Tex> items;
    String openTag, closeTag;

    WithEnvironment(List<? extends Tex> newItems) {
        this.items = newItems;
    }

    public void toTex(StringBuilder str) {
        str.append(openTag);
        for (Tex item : items) {
            item.toTex(str);
        }
        str.append(closeTag);
    }
}
