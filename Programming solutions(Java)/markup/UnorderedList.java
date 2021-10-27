package markup;

import java.util.List;

public class UnorderedList extends WithEnvironment implements ForListItem{
    UnorderedList(List<ListItem> items) {
        super(items);
        openTag = "\\begin{itemize}";
        closeTag = "\\end{itemize}";
    }
}
