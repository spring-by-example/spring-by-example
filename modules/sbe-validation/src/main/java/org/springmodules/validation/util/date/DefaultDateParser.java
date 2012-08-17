/*
 * Copyright 2004-2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springmodules.validation.util.date;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections.Predicate;

/**
 * <p>DefaultDateParser parses many date formats to a string.
 * <p/>
 * <p>The supported date formats are:
 * <p/>
 * <ul>
 * <li>yyyy-MM-dd (^\\d{4}\\-\\d{2}\\-\\d{2}$)
 * <li>yyyyMMdd (^\\d{8}$)
 * <li>yyyy-MM-dd HH:mm:ss (^\\d{4}\\-\\d{2}\\-\\d{2}\\s+\\d{2}:\\d{2}:\\d{2}$)
 * <li>yyyyMMdd HHmmss (^\\d{8}\\s+\\d{6}$)
 * <li>yyyyMMdd HH:mm:ss (^\\d{8}\\s+\\d{2}:\\d{2}:\\d{2}$)
 * <li>yyyy-MM-dd HHmmss (^\\d{4}\\-\\d{2}\\-\\d{2}\\s+\\d{6}$)
 * <li>T (^T$)
 * </ul>
 * <p/>
 * <p>Date formats can be added using DefaultDateParser#register(String, String).
 * <p/>
 * <p>These modifiers are supported:
 * <p/>
 * <ul>
 * <li>T+?S (add ? milliseconds to T, T can be any valid date format with modifiers)
 * <li>T-?S (subtract ? milliseconds from T, idem)
 * <li>T+?s (add ? seconds to T, idem)
 * <li>T-?s (subtract ? seconds from T, idem)
 * <li>T+?m (add ? minutes to T, idem)
 * <li>T-?m (subtract ? minutes from T, idem)
 * <li>T+?H (add ? hours to T, idem)
 * <li>T-?H (subtract ? hours from T, idem)
 * <li>T+?d (add ? hours to T, idem)
 * <li>T-?d (subtract ? hours from T, idem)
 * <li>T+?w (add ? weeks to T, idem)
 * <li>T-?w (subtract ? weeks from T, idem)
 * <li>T+?M (add ? months to T, idem)
 * <li>T-?M (subtract ? months from T, idem)
 * <li>T+?y (add ? years to T, idem)
 * <li>T-?y (subtract ? years from T, idem)
 * <li>T&lt;s (shift T to start of current second, idem)
 * <li>T&gt;s (shift T to start of next second, idem)
 * <li>T&lt;m (shift T to start of current minute, idem)
 * <li>T&gt;m (shift T to start of next minute, idem)
 * <li>T&lt;H (shift T to start of current hour, idem)
 * <li>T&gt;H (shift T to start of next hour, idem)
 * <li>T&lt;d (shift T to start of current day, idem)
 * <li>T&gt;d (shift T to start of next day, idem)
 * <li>T&lt;w (shift T to start of current week, idem)
 * <li>T&gt;w (shift T to start of next week, idem)
 * <li>T&lt;y (shift T to start of current year, idem)
 * <li>T&gt;y (shift T to start of next year, idem)
 * </ul>
 * <p/>
 * <p>Modifiers can be added using DefaultDateParser#register(String, DateModifier).
 * <p/>
 * <p>Modifiers can be combined and are parsed from left to right, for example:
 * <p/>
 * <p><code>2005-04-09 23:30:00&gt;M+10d+8H</code> results in <code>2005-05-11 08:00:00</code>.
 *
 * @author Steven Devijver
 * @since 25-04-2005
 */
public class DefaultDateParser implements DateParser {

    private static DefaultDateParser instance = new DefaultDateParser();

    private Map registrations = new HashMap();

    public static DefaultDateParser getInstance() {
        return instance;
    }

    public DefaultDateParser() {
        super();
        register("^\\d{8}$", "yyyyMMdd");
        register("^\\d{4}\\-\\d{2}\\-\\d{2}$", "yyyy-MM-dd");
        register("^\\d{4}\\-\\d{2}\\-\\d{2}\\s+\\d{2}:\\d{2}:\\d{2}$", "yyyy-MM-dd HH:mm:ss");
        register("^\\d{8}\\s+\\d{6}$", "yyyyMMdd HHmmss");
        register("^\\d{8}\\s+\\d{2}:\\d{2}:\\d{2}$", "yyyyMMdd HH:mm:ss");
        register("^\\d{4}\\-\\d{2}\\-\\d{2}\\s+\\d{6}$", "yyyy-MM-dd HHmmss");

        register("^T$", new DateModifier() {
            public void modify(Calendar calendar, String value) {
            }
        });

        register("^T\\+(\\d+)S$", new DateModifier() {
            public void modify(Calendar calendar, String value) {
                calendar.add(Calendar.MILLISECOND, Integer.parseInt(value));
            }
        });

        register("^T\\-(\\d+)S$", new DateModifier() {
            public void modify(Calendar calendar, String value) {
                calendar.add(Calendar.MILLISECOND, Integer.parseInt(value) * -1);
            }
        });

        register("^T>s$", new DateModifier() {
            public void modify(Calendar calendar, String value) {
                calendar.add(Calendar.SECOND, 1);
                calendar.add(Calendar.MILLISECOND, calendar.get(Calendar.MILLISECOND) + 1 * -1);
            }
        });

        register("^T<s$", new DateModifier() {
            public void modify(Calendar calendar, String value) {
                calendar.add(Calendar.MILLISECOND, calendar.get(Calendar.MILLISECOND) * -1);
            }
        });

        register("^T\\+(\\d+)s$", new DateModifier() {
            public void modify(Calendar calendar, String value) {
                calendar.add(Calendar.SECOND, Integer.parseInt(value));
            }
        });

        register("^T\\-(\\d+)s$", new DateModifier() {
            public void modify(Calendar calendar, String value) {
                calendar.add(Calendar.SECOND, Integer.parseInt(value) * -1);
            }
        });

        register("^T>m$", new DateModifier() {
            public void modify(Calendar calendar, String value) {
                calendar.add(Calendar.MINUTE, 1);
                calendar.add(Calendar.SECOND, calendar.get(Calendar.SECOND) * -1);
                calendar.add(Calendar.MILLISECOND, calendar.get(Calendar.MILLISECOND) + 1 * -1);
            }
        });

        register("^T<m$", new DateModifier() {
            public void modify(Calendar calendar, String value) {
                calendar.add(Calendar.SECOND, calendar.get(Calendar.SECOND) * -1);
                calendar.add(Calendar.MILLISECOND, calendar.get(Calendar.MILLISECOND) * -1);
            }
        });

        register("^T\\+(\\d+)m$", new DateModifier() {
            public void modify(Calendar calendar, String value) {
                calendar.add(Calendar.MINUTE, Integer.parseInt(value));
            }
        });

        register("^T\\-(\\d+)m$", new DateModifier() {
            public void modify(Calendar calendar, String value) {
                calendar.add(Calendar.MINUTE, Integer.parseInt(value) * -1);
            }
        });

        register("^T>H$", new DateModifier() {
            public void modify(Calendar calendar, String value) {
                calendar.add(Calendar.HOUR_OF_DAY, 1);
                calendar.add(Calendar.MINUTE, calendar.get(Calendar.MINUTE) * -1);
                calendar.add(Calendar.SECOND, calendar.get(Calendar.SECOND) * -1);
                calendar.add(Calendar.MILLISECOND, calendar.get(Calendar.MILLISECOND) + 1 * -1);
            }
        });

        register("^T<H$", new DateModifier() {
            public void modify(Calendar calendar, String value) {
                calendar.add(Calendar.MINUTE, calendar.get(Calendar.MINUTE) * -1);
                calendar.add(Calendar.SECOND, calendar.get(Calendar.SECOND) * -1);
                calendar.add(Calendar.MILLISECOND, calendar.get(Calendar.MILLISECOND) * -1);
            }
        });

        register("^T\\+(\\d+)H$", new DateModifier() {
            public void modify(Calendar calendar, String value) {
                calendar.add(Calendar.HOUR_OF_DAY, Integer.parseInt(value));
            }
        });

        register("^T\\-(\\d+)H$", new DateModifier() {
            public void modify(Calendar calendar, String value) {
                calendar.add(Calendar.HOUR_OF_DAY, Integer.parseInt(value) * -1);
            }
        });

        register("^T>d$", new DateModifier() {
            public void modify(Calendar calendar, String value) {
                calendar.add(Calendar.DAY_OF_YEAR, 1);
                calendar.add(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) * -1);
                calendar.add(Calendar.MINUTE, calendar.get(Calendar.MINUTE) * -1);
                calendar.add(Calendar.SECOND, calendar.get(Calendar.SECOND) * -1);
                calendar.add(Calendar.MILLISECOND, calendar.get(Calendar.MILLISECOND) + 1 * -1);
            }
        });

        register("^T<d$", new DateModifier() {
            public void modify(Calendar calendar, String value) {
                calendar.add(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) * -1);
                calendar.add(Calendar.MINUTE, calendar.get(Calendar.MINUTE) * -1);
                calendar.add(Calendar.SECOND, calendar.get(Calendar.SECOND) * -1);
                calendar.add(Calendar.MILLISECOND, calendar.get(Calendar.MILLISECOND) * -1);
            }
        });

        register("^T\\+(\\d+)d$", new DateModifier() {
            public void modify(Calendar calendar, String value) {
                calendar.add(Calendar.DAY_OF_YEAR, Integer.parseInt(value));
            }
        });

        register("^T\\-(\\d+)d$", new DateModifier() {
            public void modify(Calendar calendar, String value) {
                calendar.add(Calendar.DAY_OF_YEAR, Integer.parseInt(value) * -1);
            }
        });

        register("^T>w$", new DateModifier() {
            public void modify(Calendar calendar, String value) {
                int thisWeek = calendar.get(Calendar.WEEK_OF_YEAR);
                calendar.add(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) * -1);
                calendar.add(Calendar.MINUTE, calendar.get(Calendar.MINUTE) * -1);
                calendar.add(Calendar.SECOND, calendar.get(Calendar.SECOND) * -1);
                calendar.add(Calendar.MILLISECOND, calendar.get(Calendar.MILLISECOND) * -1);

                while (thisWeek == calendar.get(Calendar.WEEK_OF_YEAR)) {
                    calendar.add(Calendar.DAY_OF_YEAR, 1);
                }

                calendar.add(Calendar.MILLISECOND, -1);
            }
        });

        register("^T<w$", new DateModifier() {
            public void modify(Calendar calendar, String value) {
                int thisWeek = calendar.get(Calendar.WEEK_OF_YEAR);
                calendar.add(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) * -1);
                calendar.add(Calendar.MINUTE, calendar.get(Calendar.MINUTE) * -1);
                calendar.add(Calendar.SECOND, calendar.get(Calendar.SECOND) * -1);
                calendar.add(Calendar.MILLISECOND, calendar.get(Calendar.MILLISECOND) * -1);

                while (thisWeek == calendar.get(Calendar.WEEK_OF_YEAR)) {
                    calendar.add(Calendar.DAY_OF_YEAR, -1);
                }
                calendar.add(Calendar.DAY_OF_YEAR, 1);
            }
        });

        register("^T\\+(\\d+)w$", new DateModifier() {
            public void modify(Calendar calendar, String value) {
                calendar.add(Calendar.WEEK_OF_YEAR, Integer.parseInt(value));
            }
        });

        register("^T\\-(\\d+)w$", new DateModifier() {
            public void modify(Calendar calendar, String value) {
                calendar.add(Calendar.WEEK_OF_YEAR, Integer.parseInt(value) * -1);
            }
        });

        register("^T>M$", new DateModifier() {
            public void modify(Calendar calendar, String value) {
                int thisMonth = calendar.get(Calendar.MONTH);
                calendar.add(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) * -1);
                calendar.add(Calendar.MINUTE, calendar.get(Calendar.MINUTE) * -1);
                calendar.add(Calendar.SECOND, calendar.get(Calendar.SECOND) * -1);
                calendar.add(Calendar.MILLISECOND, calendar.get(Calendar.MILLISECOND) * -1);

                while (thisMonth == calendar.get(Calendar.MONTH)) {
                    calendar.add(Calendar.DAY_OF_YEAR, 1);
                }

                calendar.add(Calendar.MILLISECOND, -1);
            }
        });

        register("^T<M$", new DateModifier() {
            public void modify(Calendar calendar, String value) {
                int thisMonth = calendar.get(Calendar.MONTH);
                calendar.add(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) * -1);
                calendar.add(Calendar.MINUTE, calendar.get(Calendar.MINUTE) * -1);
                calendar.add(Calendar.SECOND, calendar.get(Calendar.SECOND) * -1);
                calendar.add(Calendar.MILLISECOND, calendar.get(Calendar.MILLISECOND) * -1);

                while (thisMonth == calendar.get(Calendar.MONTH)) {
                    calendar.add(Calendar.DAY_OF_YEAR, -1);
                }
                calendar.add(Calendar.DAY_OF_YEAR, 1);
            }
        });

        register("^T\\+(\\d+)M$", new DateModifier() {
            public void modify(Calendar calendar, String value) {
                calendar.add(Calendar.MONTH, Integer.parseInt(value));
            }
        });

        register("^T\\-(\\d+)M$", new DateModifier() {
            public void modify(Calendar calendar, String value) {
                calendar.add(Calendar.MONTH, Integer.parseInt(value) * -1);
            }
        });

        register("^T>y$", new DateModifier() {
            public void modify(Calendar calendar, String value) {
                int thisYear = calendar.get(Calendar.YEAR);
                calendar.add(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) * -1);
                calendar.add(Calendar.MINUTE, calendar.get(Calendar.MINUTE) * -1);
                calendar.add(Calendar.SECOND, calendar.get(Calendar.SECOND) * -1);
                calendar.add(Calendar.MILLISECOND, calendar.get(Calendar.MILLISECOND) * -1);

                while (thisYear == calendar.get(Calendar.YEAR)) {
                    calendar.add(Calendar.DAY_OF_YEAR, 1);
                }

                calendar.add(Calendar.MILLISECOND, -1);
            }
        });

        register("^T<y$", new DateModifier() {
            public void modify(Calendar calendar, String value) {
                int thisYear = calendar.get(Calendar.YEAR);
                calendar.add(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) * -1);
                calendar.add(Calendar.MINUTE, calendar.get(Calendar.MINUTE) * -1);
                calendar.add(Calendar.SECOND, calendar.get(Calendar.SECOND) * -1);
                calendar.add(Calendar.MILLISECOND, calendar.get(Calendar.MILLISECOND) * -1);

                while (thisYear == calendar.get(Calendar.YEAR)) {
                    calendar.add(Calendar.DAY_OF_YEAR, -1);
                }
                calendar.add(Calendar.DAY_OF_YEAR, 1);
            }
        });

        register("^T\\+(\\d+)y$", new DateModifier() {
            public void modify(Calendar calendar, String value) {
                calendar.add(Calendar.YEAR, Integer.parseInt(value));
            }
        });

        register("^T\\-(\\d+)y$", new DateModifier() {
            public void modify(Calendar calendar, String value) {
                calendar.add(Calendar.YEAR, Integer.parseInt(value) * -1);
            }
        });
    }

    public Date parse(String str) throws DateParseException {
        Date t = null;
        String tmpStr = null;
        int parsedSoFar = 0;
        boolean firstPass = true;

        if (str == null || str.length() == 0) {
            throw new IllegalArgumentException("Date string should not be null or blank!");
        }

        tmpStr = str;

        while (tmpStr.length() > 0) {
            Date tmpT = null;
            tmpT = simpleParse(tmpStr, t);
            if (tmpT != null) {
                t = tmpT;
                if (firstPass) {
                    parsedSoFar = tmpStr.length();
                } else {
                    parsedSoFar += tmpStr.length() - 1;
                }
                tmpStr = "T" + str.substring(parsedSoFar);
                firstPass = false;
            } else {
                tmpStr = tmpStr.substring(0, tmpStr.length() - 1);
            }
            if (tmpStr.equals("T")) {
                if (parsedSoFar == str.length()) {
                    break;
                } else {
                    throw new DateParseException("Could not parse date string [" + str + "]!");
                }
            }
        }

        if (t == null) {
            throw new DateParseException("Could not parse date string [" + str + "]!");
        } else {
            return t;
        }
    }

    private Date simpleParse(String str, Date t) throws DateParseException {
        for (Iterator iter = this.registrations.keySet().iterator(); iter.hasNext();) {
            RegexpPredicate predicate = (RegexpPredicate) iter.next();
            if (predicate.evaluate(str)) {
                Object dateParser = this.registrations.get(predicate);
                if (dateParser instanceof DateParser) {
                    return ((DateParser) dateParser).parse(str);
                } else if (dateParser instanceof DateModifier) {
                    Calendar calendar = new GregorianCalendar();
                    calendar.setFirstDayOfWeek(Calendar.MONDAY);
                    if (t == null) {
                        calendar.setTime(new Date());
                    } else {
                        calendar.setTime(t);
                    }
                    ((DateModifier) dateParser).modify(calendar, predicate.getGroup1(str));
                    return calendar.getTime();
                }
            }
        }

        return null;
    }

    /**
     * <p>Register a date format for a given regular expression.
     *
     * @param regexp the regular expression
     * @param format the date format
     */
    public void register(String regexp, String format) {
        this.registrations.put(new RegexpPredicate(regexp), new BasicDateParser(format));
    }

    /**
     * <p>Register your own date parser for a given regular expression.
     *
     * @param regexp the regular expression
     * @param dateParser the date parser
     */
    public void register(String regexp, DateModifier dateParser) {
        this.registrations.put(new RegexpPredicate(regexp), dateParser);
    }

    public interface DateModifier {

        public void modify(Calendar calendar, String value);
    }

    private class RegexpPredicate implements Predicate {

        private Pattern pattern = null;

        public RegexpPredicate(String regexp) {
            super();
            if (regexp == null || regexp.length() == 0) {
                throw new IllegalArgumentException("Regular expression parameter should not be null or blank!");
            }
            this.pattern = Pattern.compile(regexp);
        }

        public boolean evaluate(Object o) {
            return this.pattern.matcher((String) o).matches();
        }

        public String getGroup1(Object o) {
            Matcher matcher = this.pattern.matcher((String) o);
            if (matcher.matches() && matcher.groupCount() > 0) {
                return matcher.group(1);
            } else {
                return null;
            }
        }
    }

    private class BasicDateParser implements DateParser {

        private DateFormat dateFormat = null;

        public BasicDateParser(String format) {
            super();
            if (format == null || format.length() == 0) {
                throw new IllegalArgumentException("Format parameter should not be null or blank!");
            }
            this.dateFormat = new SimpleDateFormat(format);
        }

        public Date parse(String s) throws DateParseException {
            try {
                return this.dateFormat.parse(s);
            } catch (ParseException e) {
                return null;
            }
        }
    }
}