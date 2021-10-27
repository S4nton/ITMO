package markup;

import java.util.List;

public class Paragraph extends WithEnvironment implements ForListItem{
    Paragraph(List<ForParagraph> items) {
        super(items);
        openTag = "";
        closeTag = "";
    }
}
