package markup;

import java.util.List;

public class ListItem extends WithEnvironment{
    ListItem(List<ForListItem> items) {
        super(items);
        openTag = "\\item ";
        closeTag = "";
    }
}
