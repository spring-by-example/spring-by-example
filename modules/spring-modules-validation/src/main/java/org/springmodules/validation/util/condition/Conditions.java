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

package org.springmodules.validation.util.condition;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import org.joda.time.ReadableInstant;
import org.springmodules.validation.util.condition.bean.EqualPropertiesBeanCondition;
import org.springmodules.validation.util.condition.bean.PropertyBeanCondition;
import org.springmodules.validation.util.condition.collection.IsEmptyCollectionCondition;
import org.springmodules.validation.util.condition.collection.MaxSizeCollectionCondition;
import org.springmodules.validation.util.condition.collection.MinSizeCollectionCondition;
import org.springmodules.validation.util.condition.collection.SizeRangeCollectionCondition;
import org.springmodules.validation.util.condition.common.*;
import org.springmodules.validation.util.condition.date.IsAfterDateCondition;
import org.springmodules.validation.util.condition.date.IsBeforeDateCondition;
import org.springmodules.validation.util.condition.date.IsInTheFutureDateCondition;
import org.springmodules.validation.util.condition.date.IsInThePastDateCondition;
import org.springmodules.validation.util.condition.date.jodatime.IsAfterInstantCondition;
import org.springmodules.validation.util.condition.date.jodatime.IsBeforeInstantCondition;
import org.springmodules.validation.util.condition.date.jodatime.IsInTheFutureInstantCondition;
import org.springmodules.validation.util.condition.date.jodatime.IsInThePastInstantCondition;
import org.springmodules.validation.util.condition.range.*;
import org.springmodules.validation.util.condition.string.*;

/**
 * A static factory class to help with creating the various conditions.
 *
 * @author Uri Boness
 */
public final class Conditions {

    // should never be instantiated
    private Conditions() {
    }

    // common conditions

    public static Condition not(Condition condition) {
        return new NotCondition(condition);
    }

    public static Condition and(Condition c1, Condition c2) {
        return and(new Condition[]{c1, c2});
    }

    public static Condition and(Condition[] conditions) {
        return new AndCondition(conditions);
    }

    public static Condition and(Collection conditions) {
        return new AndCondition(conditions);
    }

    public static Condition or(Condition c1, Condition c2) {
        return or(new Condition[]{c1, c2});
    }

    public static Condition or(Condition[] conditions) {
        return new OrCondition(conditions);
    }

    public static Condition or(Collection conditions) {
        return new OrCondition(conditions);
    }

    public static Condition isTrue() {
        return new IsTrueCondition();
    }

    public static Condition isFalse() {
        return not(isTrue());
    }

    public static Condition instanceOf(Class clazz) {
        return new InstanceOfCondition(clazz);
    }

    public static Condition isNull() {
        return new IsNullCondition();
    }

    public static Condition notNull() {
        return not(isNull());
    }

    // bean conditions

    public static Condition propertiesMatch(String[] propertyNames) {
        return new EqualPropertiesBeanCondition(propertyNames);
    }

    // date conditions

    public static Condition isDateInTheFuture() {
        return new IsInTheFutureDateCondition();
    }

    public static Condition isDateInThePast() {
        return new IsInThePastDateCondition();
    }

    public static Condition isInstantInTheFuture() {
        return new IsInTheFutureInstantCondition();
    }

    public static Condition isInstantInThePast() {
        return new IsInThePastInstantCondition();
    }

    public static Condition isBefore(Date date) {
        return new IsBeforeDateCondition(date);
    }

    public static Condition isBefore(ReadableInstant instant) {
        return new IsBeforeInstantCondition(instant);
    }

    public static Condition isBefore(Calendar calendar) {
        return new IsBeforeDateCondition(calendar);
    }

    public static Condition isAfter(Date date) {
        return new IsAfterDateCondition(date);
    }

    public static Condition isAfter(ReadableInstant instant) {
        return new IsAfterInstantCondition(instant);
    }

    public static Condition isAfter(Calendar calendar) {
        return new IsAfterDateCondition(calendar);
    }

    // string conditions

    public static Condition contains(String text) {
        return new ContainsSubstringStringCondition(text);
    }

    public static Condition equalsIgnoreCase(String text) {
        return new EqualsIgnoreCaseStringCondition(text);
    }

    public static Condition isEmptyString() {
        return new IsEmptyStringCondition();
    }

    public static Condition notBlank() {
        return not(isEmptyString());
    }

    public static Condition regexp(String regexp) {
        return new RegExpStringCondition(regexp);
    }

    public static Condition minLength(int minLength) {
        return new MinLengthStringCondition(minLength);
    }

    public static Condition maxLength(int maxLength) {
        return new MaxLengthStringCondition(maxLength);
    }

    public static Condition lengthBetween(int minLength, int maxLength) {
        return minLength(minLength).and(maxLength(maxLength));
    }

    // collection & array conditions

    public static Condition isEmpty() {
        return new IsEmptyCollectionCondition();
    }

    public static Condition notEmpty() {
        return not(isEmpty());
    }

    public static Condition minSize(int minSize) {
        return new MinSizeCollectionCondition(minSize);
    }

    public static Condition minSize(String propertyName, int minSize) {
        return property(propertyName, minSize(minSize));
    }

    public static Condition maxSize(int maxSize) {
        return new MaxSizeCollectionCondition(maxSize);
    }

    public static Condition maxSize(String propertyName, int maxSize) {
        return property(propertyName, maxSize(maxSize));
    }

    public static Condition sizeRange(int minSize, int maxSize) {
        return new SizeRangeCollectionCondition(minSize, maxSize);
    }

    public static Condition sizeRange(String propertyName, int minSize, int maxSize) {
        return property(propertyName, sizeRange(minSize, maxSize));
    }

    // range conditions

    public static Condition isGt(Comparable min) {
        return new GreaterThanCondition(min);
    }

    public static Condition isGt(String propertyName, Comparable min) {
        return property(propertyName, isGt(min));
    }

    public static Condition isGte(Comparable min) {
        return new GreaterThanOrEqualsCondition(min);
    }

    public static Condition isGte(String propertyName, Comparable min) {
        return property(propertyName, isGte(min));
    }

    public static Condition isLt(Comparable max) {
        return new LessThanCondition(max);
    }

    public static Condition isLte(Comparable max) {
        return new LessThanOrEqualsCondition(max);
    }

    public static Condition isLte(String propertyName, Comparable max) {
        return property(propertyName, isLte(max));
    }

    public static Condition isBetween(Comparable min, Comparable max) {
        return new BetweenCondition(min, max);
    }

    public static Condition isBetweenIncluding(Comparable min, Comparable max) {
        return new BetweenIncludingCondition(min, max);
    }

    public static Condition isBetweenIncludingMin(Comparable min, Comparable max) {
        return new BetweenIncludingLowerBoundCondition(min, max);
    }

    public static Condition isBetweenIncludingMax(Comparable min, Comparable max) {
        return new BetweenIncludingUpperBoundCondition(min, max);
    }

    // property based conditions

    public static Condition property(String propertyName, Condition condition) {
        return new PropertyBeanCondition(propertyName, condition);
    }

    public static Condition isTrue(String propertyName) {
        return property(propertyName, isTrue());
    }

    public static Condition isFalse(String propertyName) {
        return not(isTrue(propertyName));
    }

    public static Condition isNull(String propertyName) {
        return property(propertyName, isNull());
    }

    public static Condition notNull(String propertyName) {
        return not(isNull(propertyName));
    }

    public static Condition instanceOf(String propertyName, Class clazz) {
        return property(propertyName, instanceOf(clazz));
    }

    public static Condition isEmptyString(String propertyName) {
        return property(propertyName, isEmptyString());
    }

    public static Condition notEmptyString(String propertyName) {
        return not(isEmptyString(propertyName));
    }

    public static Condition maxLength(String propertyName, int maxLength) {
        return property(propertyName, maxLength(maxLength));
    }

    public static Condition minLength(String propertyName, int minLength) {
        return property(propertyName, minLength(minLength));
    }

    public static Condition isEmpty(String propertyName) {
        return property(propertyName, isEmpty());
    }

    public static Condition notEmpty(String propertyName) {
        return not(isEmpty(propertyName));
    }

    public static Condition isBetween(String propertyName, Comparable min, Comparable max) {
        return property(propertyName, isBetween(min, max));
    }

    public static Condition notInBetween(String propertyName, Comparable min, Comparable max) {
        return not(isBetween(propertyName, min, max));
    }

}
