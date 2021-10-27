package markup;

import java.util.List;

public class OrderedList extends WithEnvironment implements ForListItem{
    OrderedList(List<ListItem> items) {
        super(items);
        openTag = "\\begin{enumerate}";
        closeTag = "\\end{enumerate}";
    }
}
