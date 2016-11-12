/*
 *  Copyright 2016 Netsmart Technologies, Inc. All rights reserved.
 *  NETSMART PROPRIETARY/CONFIDENTIAL.
 */
package com.cagst.swkroa;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Defines the attributes to load for a given entity.  This is useful for lazily loaded attributes that would cause
 * additional hit to the database to retrieve the information.
 *
 * @author Craig Gaskill
 */
public class LoadingPolicy {
    public static final String LOAD_ALL = "LOAD_ALL";

    public static final LoadingPolicy ALL = new LoadingPolicy(Collections.singleton(LOAD_ALL));

    private final Set<String> attributes;

    /**
     * Default Constructor used to create an instance of <i>LoadingPolicy</i>.
     */
    public LoadingPolicy() {
        attributes = new HashSet<>();
    }

    /**
     * Private Constructor used to create an instance of <i>LoadingPolicy</i> with the default set of attributes.
     *
     * @param attributes
     *      The default {@link Set} of attributes to load.
     */
    private LoadingPolicy(Set<String> attributes) {
        Objects.requireNonNull(attributes, "Assertion Failure: argument [attributes] cannot be null");

        this.attributes = attributes;
    }

    /**
     * @return An unmodifiable {@link Set} of Attributes.
     */
    public Set<String> getAttributes() {
        return Collections.unmodifiableSet(attributes);
    }

    /**
     * Add the specified attribute to the set of attributes to load.
     *
     * @param attribute
     *      A {@link String} that identifies the attribute to add.
     */
    public void addAttribute(String attribute) {
        attributes.add(attribute);
    }

    /**
     * Removes the specified attribute from the set of attributes to load.
     *
     * @param attribute
     *      A {@link String} that identifies the attribute to remove.
     */
    public void removeAttribute(String attribute) {
        attributes.remove(attribute);
    }

    /**
     * Clears (removes) all attributes from the set of attributes to load.
     */
    public void clearAttributes() {
        attributes.clear();
    }

    /**
     * Checks whether the specified attribute is defined within the set of attributes to load.
     *
     * @param attribute
     *      A {@link String} that identifies the attribute to check.
     *
     * @return {@code true} if the attribute has been set as an attribute to load, {@code false} otherwise.
     */
    public boolean containsAttribute(String attribute) {
        return (attributes.contains(LOAD_ALL) || attributes.contains(attribute));
    }
}
