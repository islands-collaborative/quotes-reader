package quotes.reader;

import com.google.gson.Gson;

import java.io.*;
import java.util.*;

public class QuotesReader {
    String localCachePath;
    BufferedReader br;
    Gson gson = new Gson();
    Random rand = new Random();
    QuotesAPI api = new QuotesAPI();
    Map<Integer, Quote> quotesMap = new HashMap<>();

    public QuotesReader(String localCachePath) throws IOException {
        this.localCachePath = localCachePath;
        File localCacheFile = new File(localCachePath);
        if (localCacheFile.exists()) {
            br = new BufferedReader(new FileReader(localCachePath));
            // load file into quotesArray
            System.out.println("Quotes cache exist.");
            Quote[] quotesArray = gson.fromJson(br, Quote[].class);
            // insert quotes into our hashArray
            for (Quote q : quotesArray) {
                System.out.println("Adding quote to quotes map");
                quotesMap.put(q.id, q);
            }
        } else {
            System.out.println("File does not exist.");
            // Create a file initialized as an empty JSON array
            BufferedWriter bw = new BufferedWriter(new FileWriter(localCacheFile));
            bw.append("[\n]");
            bw.close();
        }
    }

    public Quote getRandomQuotationByAuthor(String author) {
        List<Quote> quotes;
        try {
            quotes = api.getQuotationsByAuthor(author);
        } catch (Exception e) {
            quotes = getCachedQuotationsByAuthor(author);
        }
        int idx = rand.nextInt(quotes.size());
        return quotes.get(idx);
    }

    public Quote getRandomQuotationByTag(String tag) {
        List<Quote> quotes;
        try {
            quotes = api.getQuotationsByTag(author);
        } catch (Exception e) {
            quotes = getCachedQuotationsByTag(author);
        }
        int idx = rand.nextInt(quotes.size());
        return quotes.get(idx);
    }

    public Quote getRandomQuotationByContains(String author) {
        List<Quote> quotes;
        try {
            quotes = api.getQuotationsByContains(author);
        } catch (Exception e) {
            quotes = getCachedQuotationsByContains(author);
        }
        int idx = rand.nextInt(quotes.size());
        return quotes.get(idx);
    }

    public void cacheLocal(List<Quote> quotes) throws IOException {
        boolean hasEntries = false;
        // Delete the last two characters (a newline and a square bracket)
        try (RandomAccessFile f = new RandomAccessFile(localCachePath, "rw")) {
            if (f.length() > 4) hasEntries = true;
            f.setLength(f.length() - 2);
        }

        try (FileWriter writer = new FileWriter(localCachePath, true)) {
            for (Quote q: quotes) {
                // Check to see if our quote exists in our localCache list
                // If it doesn't then save the quote to our localCachePath file
                if (!quotesMap.containsKey(q.id)) {
                    quotesMap.put(q.id, q);
                    if (hasEntries) writer.append(",\n");
                    else writer.append("\n");
                    gson.toJson(q, Quote.class, writer);
                }
            }
            writer.append("\n]");
        }
    }

    public static void main(String[] args) throws IOException {
        String path = "src/main/resources/quotescache.json";
        QuotesReader qr = new QuotesReader(path);
        Quote q = new Quote("Santa claus", "Ho ho ho!", "", null, 5);
        ArrayList<Quote> quotes = new ArrayList<>();
        quotes.add(q);

        qr.cacheLocal(quotes);
    }
}