/**
 * Copyright © 2010-2011 Nokia
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
package com.github.restdriver.serverdriver.matchers;

import java.text.ParseException;

import org.codehaus.jackson.JsonNode;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import com.jayway.jsonpath.JsonPath;

/**
 * Matcher to enable assertions on JSON objects using JSONpath.
 *
 * @param <T> The type of the matcher.
 */
public final class HasJsonPath<T> extends TypeSafeMatcher<JsonNode> {

    private final String jsonPath;
    private final Matcher<T> matcher;

    /**
     * Constructor.
     *
     * @param jsonPath The JSONpath to use.
     */
    public HasJsonPath(String jsonPath) {
        this(jsonPath, null);
    }

    /**
     * Constructor.
     *
     * @param jsonPath The JSONpath to use.
     * @param matcher  The matcher to apply to the result of the JSONpath.
     */
    public HasJsonPath(String jsonPath, Matcher<T> matcher) {
        this.jsonPath = jsonPath;
        this.matcher = matcher;
    }

    @Override
    public boolean matchesSafely(JsonNode jsonNode) {

        Object jsonPathResult = null;

        try {

            jsonPathResult = JsonPath.read(jsonNode.toString(), jsonPath);

            if (matcher == null) {
                return jsonPathResult != null;
            }

            boolean initialMatchResult = matcher.matches(jsonPathResult);

            // if matcher is for integers and jsonPath returns a Long, do our best
            if (!initialMatchResult && jsonPathResult instanceof Long) {
                return matcher.matches(longToInt(jsonPathResult));
            }

            return initialMatchResult;

        } catch (ParseException pe) {
            // we weren't passed valid JSON, which can only happen if jsonNode.toString() produces bad JSON...
            return false;

        } catch (ClassCastException cce) {

            if (matcher.matches(longToInt(jsonPathResult))) {
                return true;

            } else {
                throw new RuntimeJsonTypeMismatchException("JSONpath returned a type unsuitable for matching with the given matcher: " + cce.getMessage(), cce);

            }

        }

    }

    private int longToInt(Object o) {

        long l;

        try {
            l = (Long) o;
        } catch (ClassCastException cce) {
            throw new RuntimeJsonTypeMismatchException("JSONpath returned a type unsuitable for matching with the given matcher: " + cce.getMessage(), cce);
        }

        if (l > Integer.MAX_VALUE || l < Integer.MIN_VALUE) {
            throw new RuntimeJsonTypeMismatchException("JSONpath returned a Long value which does not match the given Matcher."
                    + "  You should use a Long in your matcher.");
        }

        return (int) l;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("a JSON object matching JSONpath \"" + jsonPath + "\"");

        if (matcher != null) {
            description.appendText(" with ");
            matcher.describeTo(description);
        }

    }
}
