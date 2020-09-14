package digital.vix.vertxdemo.utils;

public class ApplicationUtils {

	/**
	 * If an array contains any of the following values return true
	 * @param values values to search through
	 * @param searchValues to search with
	 * @return true if any of the search values are found in the values param
	 */
	public static boolean arrayContains(long[] values, long... searchValues) {
		for (long value : values) {
			for (long searchValue : searchValues)
				if (searchValue == value)
					return true;
		}
		return false;
	}
}
