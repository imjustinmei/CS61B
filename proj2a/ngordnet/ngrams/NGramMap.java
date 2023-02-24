package ngordnet.ngrams;

import java.util.HashMap;
import java.util.Collection;
import edu.princeton.cs.algs4.In;

/** An object that provides utility methods for making queries on the
 *  Google NGrams dataset (or a subset thereof).
 *
 *  An NGramMap stores pertinent data from a "words file" and a "counts
 *  file". It is not a map in the strict sense, but it does provide additional
 *  functionality.
 *
 *  @author Josh Hug
 */
public class NGramMap {
    private HashMap<String, TimeSeries> history;
    private TimeSeries countHistory;

    /** Constructs an NGramMap from WORDSFILENAME and COUNTSFILENAME. */
    public NGramMap(String wordsFilename, String countsFilename) {
        history = new HashMap<>();
        countHistory = new TimeSeries();
        In words = new In(wordsFilename);
        In counts = new In(countsFilename);

        while (!words.isEmpty()) {
            String word = words.readString();
            int year = words.readInt();
            double occurrences = words.readDouble();
            words.readInt();

            if (!history.containsKey(word)) {
                history.put(word, new TimeSeries());
            }
            history.get(word).put(year, occurrences);
        }
        words.close();

        while (!counts.isEmpty()) {
            String[] data = counts.readLine().split(",");
            countHistory.put(Integer.parseInt(data[0]), Double.parseDouble(data[1]));
        }
        counts.close();
    }

    /** Provides the history of WORD. The returned TimeSeries should be a copy,
     *  not a link to this NGramMap's TimeSeries. In other words, changes made
     *  to the object returned by this function should not also affect the
     *  NGramMap. This is also known as a "defensive copy". */
    public TimeSeries countHistory(String word) {
        TimeSeries ts = new TimeSeries();
        ts.putAll(history.get(word));
        return ts;
    }

    /** Provides the history of WORD between STARTYEAR and ENDYEAR, inclusive of both ends. The
     *  returned TimeSeries should be a copy, not a link to this NGramMap's TimeSeries. In other words,
     *  changes made to the object returned by this function should not also affect the
     *  NGramMap. This is also known as a "defensive copy". */
    public TimeSeries countHistory(String word, int startYear, int endYear) {
        TimeSeries ts = new TimeSeries(history.get(word), startYear, endYear);
        return ts;
    }

    /** Returns a defensive copy of the total number of words recorded per year in all volumes. */
    public TimeSeries totalCountHistory() {
        TimeSeries ts = new TimeSeries();
        ts.putAll(countHistory);
        return ts;
    }

    /** Provides a TimeSeries containing the relative frequency per year of WORD compared to
     *  all words recorded in that year. */
    public TimeSeries weightHistory(String word) {
        TimeSeries ts = history.get(word);
        return ts.dividedBy(countHistory);
    }

    /** Provides a TimeSeries containing the relative frequency per year of WORD between STARTYEAR
     *  and ENDYEAR, inclusive of both ends. */
    public TimeSeries weightHistory(String word, int startYear, int endYear) {
        TimeSeries ts = new TimeSeries(history.get(word), startYear, endYear);
        return ts.dividedBy(countHistory);
    }

    /** Returns the summed relative frequency per year of all words in WORDS. */
    public TimeSeries summedWeightHistory(Collection<String> words) {
        TimeSeries ts = new TimeSeries();
        for (String word : words) ts = ts.plus(weightHistory(word));
        return ts;
    }

    /** Provides the summed relative frequency per year of all words in WORDS
     *  between STARTYEAR and ENDYEAR, inclusive of both ends. If a word does not exist in
     *  this time frame, ignore it rather than throwing an exception. */
    public TimeSeries summedWeightHistory(Collection<String> words, int startYear, int endYear) {
        TimeSeries ts = new TimeSeries();
        for (String word : words) ts = ts.plus(weightHistory(word, startYear, endYear));
        return ts;
    }
}
