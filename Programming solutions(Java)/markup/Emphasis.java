package markup;

import java.util.List;

public class Emphasis extends WithEnvironment implements ForParagraph {
    Emphasis(List<ForParagraph> items) {
        super(items);
        openTag = "\\emph{";
        closeTag = "}";
    }
}
