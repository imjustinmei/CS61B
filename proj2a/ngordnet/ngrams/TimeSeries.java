package ngordnet.ngrams;

import java.util.*;

/** An object for mapping a year number (e.g. 1996) to numerical data. Provides
 *  utility methods useful for data analysis.
 *  @author Josh Hug
 */
public class TimeSeries extends TreeMap<Integer, Double> {
    /** Constructs a new empty TimeSeries. */
    public TimeSeries() {
        super();
    }

    /** Creates a copy of TS, but only between STARTYEAR and ENDYEAR,
     *  inclusive of both end points. */
    public TimeSeries(TimeSeries ts, int startYear, int endYear) {
        SortedMap sub =  ts.subMap(startYear, endYear + 1);
        putAll(sub);
    }

    /** Returns all years for this TimeSeries (in any order). */
    public List<Integer> years() {
        return new ArrayList<>(keySet());
    }

    /** Returns all data for this TimeSeries (in any order).
     *  Must be in the same order as years(). */
    public List<Double> data() {
        return new ArrayList<>(values());
    }

    /** Returns the yearwise sum of this TimeSeries with the given TS. In other words, for
     *  each year, sum the data from this TimeSeries with the data from TS. Should return a
     *  new TimeSeries (does not modify this TimeSeries). */
    public TimeSeries plus(TimeSeries ts) {
        TimeSeries sumTS = new TimeSeries();
        sumTS.putAll(ts);
        for (int key : keySet()) {
            if (sumTS.containsKey(key)) sumTS.put(key, sumTS.get(key) + get(key));
            else sumTS.put(key, get(key));
        }
        return sumTS;
    }

     /** Returns the quotient of the value for each year this TimeSeries divided by the
      *  value for the same year in TS. If TS is missing a year that exists in this TimeSeries,
      *  throw an IllegalArgumentException. If TS has a year that is not in this TimeSeries, ignore it.
      *  Should return a new TimeSeries (does not modify this TimeSeries). */
     public TimeSeries dividedBy(TimeSeries ts) throws IllegalArgumentException {
         TimeSeries quotientTS = new TimeSeries();
         for (int key : keySet()) {
             if (ts.get(key) == null) throw new IllegalArgumentException();
             quotientTS.put(key, get(key) / ts.get(key));
         }
         return quotientTS;
    }
}
