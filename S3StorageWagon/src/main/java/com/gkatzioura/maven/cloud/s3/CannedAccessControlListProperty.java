/*
 * Copyright 2018 Emmanouil Gkatziouras
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gkatzioura.maven.cloud.s3;

import java.util.logging.Logger;

import com.amazonaws.services.s3.model.CannedAccessControlList;

public class CannedAccessControlListProperty {

    private static final Logger LOGGER = Logger.getLogger(CannedAccessControlListProperty.class.getName());

    private static final String CANNED_ACL_PROP_TAG = "cannedAcl";
    private static final String CANNED_ACL_ENV_TAG = "CANNED_ACL";

    private static final CannedAccessControlList DEFAULT_CANNED_ACL = CannedAccessControlList.Private;

    private final CannedAccessControlList cannedAccessControlList;
    @Deprecated
    private final PublicReadProperty publicReadProperty;

    public CannedAccessControlListProperty() {
        this((CannedAccessControlList) null, new PublicReadProperty(null));
    }

    /**
     * @param cannedAccessControlList may be null
     * @param publicReadProperty      may not be null
     */
    public CannedAccessControlListProperty(String cannedAccessControlList, PublicReadProperty publicReadProperty) {
        this(lenientValueOf(cannedAccessControlList), publicReadProperty);
    }

    private CannedAccessControlListProperty(CannedAccessControlList cannedAccessControlList, PublicReadProperty publicReadProperty) {
        this.cannedAccessControlList = cannedAccessControlList;
        this.publicReadProperty = publicReadProperty;
    }

    /**
     * return the cannedAccessControlList set in the constructor or the cannedAcl set using the system property or environment variable, else the now deprecated {@link PublicReadProperty}, or finally the default value
     */
    public CannedAccessControlList get() {
        if (this.cannedAccessControlList != null) {
            return this.cannedAccessControlList;
        }

        String cannedAccessControlListProp = System.getProperty(CANNED_ACL_PROP_TAG);
        if (cannedAccessControlListProp != null) {
            return lenientValueOf(cannedAccessControlListProp);
        }

        String cannedAccessControlListEnv = System.getenv(CANNED_ACL_ENV_TAG);
        if (cannedAccessControlListEnv != null) {
            return lenientValueOf(cannedAccessControlListEnv);
        }

        if (publicReadProperty.get()) {
            LOGGER.warning("Public read was set to true, so setting cannedAcl to PublicRead. This feature will be removed in future in favour of the cannedAcl property");
            return CannedAccessControlList.PublicRead;
        }

        return DEFAULT_CANNED_ACL;
    }

    private static CannedAccessControlList lenientValueOf(String stringValue) {
        if (stringValue == null) {
            return null;
        } else {
            for (CannedAccessControlList cannedAcl : CannedAccessControlList.values()) {
                if (cannedAcl.name().equalsIgnoreCase(stringValue) || cannedAcl.toString().equalsIgnoreCase(stringValue)) {
                    return cannedAcl;
                }
            }
        }
        LOGGER.warning("Could not find a cannedAcl value with name: " + stringValue + " so returning the default value of: " + DEFAULT_CANNED_ACL);
        return DEFAULT_CANNED_ACL;
    }
}
