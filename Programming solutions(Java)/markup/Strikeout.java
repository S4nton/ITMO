package markup;

import java.util.List;

public class Strikeout extends WithEnvironment implements ForParagraph {
    Strikeout(List<ForParagraph> items) {
        super(items);
        openTag = "\\textst{";
        closeTag = "}";
    }
}
