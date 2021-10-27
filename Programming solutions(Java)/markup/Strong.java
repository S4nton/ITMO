package markup;

import java.util.List;

public class Strong extends WithEnvironment implements ForParagraph{
    Strong(List<ForParagraph> items) {
        super(items);
        openTag = "\\textbf{";
        closeTag = "}";
    }
}
