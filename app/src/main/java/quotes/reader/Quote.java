package quotes.reader;

import java.util.ArrayList;

public class Quote {
    public String author;
    public String likes;
    public int id;
    public ArrayList<String> tags;
    public String body;

    public Quote(String author, String text, String likes, ArrayList<String> tags, int id) {
        this.author = author;
        this.tags = tags;
        this.id = id;
    }

    @Override
    public String toString() {
        return "Quote{" +
                "author='" + author + '\'' +
                ", body='" + body + '\'' +
                ", likes='" + likes + '\'' +
                ", tags=" + tags +
                '}';
    }

    public String prettyPrint() {
        return String.format("%s -%s", body, author);
    }
}
